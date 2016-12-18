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
import com.kaishengit.util.StringUtils;
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

    //防止用户操作频繁的TOKEN缓存
    private static Cache<String,String> activeCache=CacheBuilder.newBuilder()
            .expireAfterWrite(60,TimeUnit.SECONDS)
            .build();

    private static Cache<String,String> passwordCache=CacheBuilder.newBuilder()
            .expireAfterWrite(30,TimeUnit.MINUTES)
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


    public void foundPassword(String sessionID, String type, String value) {

        if(activeCache.getIfPresent(sessionID)==null){
            if(type=="phone"){
                //手机号码找回
            }else {
                User user=userDao.findByEmail(value);
                if(user!=null){
                    Thread thread=new Thread(new Runnable() {
                        @Override
                        public void run() {
                            String uuid=UUID.randomUUID().toString();


                            String url="http://localhost/foundpassword/newpassword?token="+uuid;
                            passwordCache.put(uuid,user.getUsername());
                            String html="Dear:"+user.getUsername()+"<br>请<a href='"+url+"'>点击该链接</a>进行密码找回,该链接在30分钟内有效";
                            EmailUtil.sendHtmlEmail(value,"密码找回邮件",html);
                        }
                    });
                        thread.start();
                }
            }
            activeCache.put(sessionID,"小样儿让你点那么频繁");
        }else{

        }


    }

    public User foundPasswordGetUserByToken(String token) {

        String username=passwordCache.getIfPresent(token);
        if(StringUtils.isEmpty(username)){
            throw new ServiceException("token过期或者错误");
        }else{
            User user=userDao.findUserName(username);
            if(user==null){
                throw new ServiceException("该用户不存在");
            }else {
                return user;
            }
        }
    }

    public void resetPassword(String id, String token, String password) {

        if(StringUtils.isNotEmpty(token)){
            String username=passwordCache.getIfPresent(token);
            User user=userDao.findUserName(username);
            if(user.getId().equals(Integer.valueOf(id))){
                user.setPassword(DigestUtils.md5Hex(password+Config.get("user.password.salt")));
                userDao.update(user);
                logger.info("{} 重置了密码",user.getUsername());
            }else{
                throw new ServiceException("没有找到对应的账号");
            }
        }else{
            throw new ServiceException("token过期或者不存在");
        }
//        if(passwordCache.getIfPresent(token) == null) {
//            throw new ServiceException("token过期或错误");
//        } else {
//            User user = userDao.findById(Integer.valueOf(id));
//            user.setPassword(DigestUtils.md5Hex(password+Config.get("user.password.salt")));
//            userDao.update(user);
//            logger.info("{} 重置了密码",user.getUsername());
//        }


    }
}
