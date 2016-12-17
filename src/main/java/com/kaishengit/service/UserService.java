package com.kaishengit.service;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.kaishengit.dao.LoginLogDao;
import com.kaishengit.dao.UserDao;
import com.kaishengit.entity.LoginLog;
import com.kaishengit.entity.User;
import com.kaishengit.exception.ServiceException;
import com.kaishengit.util.Config;
import com.kaishengit.util.EmailUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;



public class UserService {

    private UserDao userDao=new UserDao();
    private LoginLogDao loginLogDao=new LoginLogDao();
    private Logger logger= LoggerFactory.getLogger(UserService.class);
    //发送激活邮件的TOKEN缓存
    private static Cache<String,String> cache= CacheBuilder.newBuilder()
            .expireAfterWrite(6, TimeUnit.HOURS)
            .build();


    //校验用户名是否被占用
    public boolean validateUserName(String username) {
        String name= Config.get("no.signup.username");
        List<String> userList= Arrays.asList(name.split(","));
        if(userList.contains(username)){
            return false;
        }
        return userDao.findUserName(username)==null;
    }


    public User findByEmail(String email) {
        return UserDao.findByEmail(email);

    }

    public void saveNewUser(String username, String password, String email, String phone) {

        User user=new User();
        user.setUsername(username);
        user.setPassword(DigestUtils.md5Hex(password+Config.get("user.password.salt")));
        user.setEmail(email);
        user.setPhone(phone);
        user.setState(User.USERSTATE_UNACTIVE);
        user.setAvatar(User.DEFAULT_AVATAR_NAME);

        System.out.println(User.USERSTATE_UNACTIVE);
        System.out.println(User.DEFAULT_AVATAR_NAME);
        System.out.println(DigestUtils.md5Hex(password+Config.get("user.password.salt")));

        userDao.save(user);

        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                String uuid= UUID.randomUUID().toString();
                String url="http://localhost/user/active?_="+uuid;
                cache.put(uuid,username);
                String html="<h3>Dear"+username+"</h3>请点击<a href='"+url+"'>该链接</a>去激活你的账号.<br> 创客联盟";
                EmailUtil.sendHtmlEmail(email,"用户激活邮件",html);
            }
        });
        thread.start();
    }

    public void activeUser(String token) {
        String username=cache.getIfPresent(token);

        if(username==null){
            throw new ServiceException("token无效或已过期");
        }else {
            User user=userDao.findUserName(username);
            if(user==null){
                throw new ServiceException("无法找到对应的账号");
            }else {
                user.setState(User.USERSTATE_ACTIVE);
                userDao.update(user);
                cache.invalidate(token);
            }
        }
    }

    public User login(String username, String password, String ip) {
        User user = userDao.findUserName(username);
        System.out.println("2222"+user.getUsername());
        System.out.println("数据里的密码"+user.getPassword());
        System.out.println(DigestUtils.md5Hex(password+Config.get("user.password.salt")));
        if (user != null && DigestUtils.md5Hex(password+Config.get("user.password.salt")).equals(user.getPassword())) {
            if (user.getState().equals(User.USERSTATE_ACTIVE)) {
                System.out.println(ip);
                LoginLog loginLog = new LoginLog();
                loginLog.setIp(ip);
                loginLog.setUserid(user.getId());

                loginLogDao.save(loginLog);

                logger.info("{}登录了系统，IP:{}", username, ip);

                return user;
            } else if (User.USERSTATE_UNACTIVE.equals(user.getState())) {
                throw new ServiceException("该账号未激活");
            } else {
                throw new ServiceException("该账号已被冻结，请联系客服进行处理");
            }
        } else {
            throw new ServiceException("账号或密码错误");
        }

    }





}
