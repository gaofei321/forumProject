package com.kaishengit.service;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Lists;
import com.kaishengit.dao.LoginLogDao;
import com.kaishengit.dao.UserDao;
import com.kaishengit.entity.LoginLog;
import com.kaishengit.entity.User;
import com.kaishengit.exception.ServiceException;
import com.kaishengit.util.Config;
import com.kaishengit.util.EmailUtil;
import com.kaishengit.util.Page;
import com.kaishengit.util.StringUtils;
import com.kaishengit.vo.VoUser;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.util.Auth;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
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
    //重置密码的TOKEN缓存
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

        if (user != null && DigestUtils.md5Hex(password+Config.get("user.password.salt")).equals(user.getPassword())) {
            if (user.getState().equals(User.USERSTATE_ACTIVE)) {
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
        passwordCache.invalidate(token);
//        if(passwordCache.getIfPresent(token) == null) {
//            throw new ServiceException("token过期或错误");
//        } else {
//            User user = userDao.findById(Integer.valueOf(id));
//            user.setPassword(DigestUtils.md5Hex(password+Config.get("user.password.salt")));
//            userDao.update(user);
//            logger.info("{} 重置了密码",user.getUsername());
//        }


    }

    public void updateEmail(User user, String email) {
        user.setEmail(email);
        userDao.update(user);
    }

    public void updatePassword(String oldPassword, String newPassword,User user) {
        if((DigestUtils.md5Hex(oldPassword+Config.get("user.password.salt"))).equals(user.getPassword())){
            user.setPassword(DigestUtils.md5Hex(newPassword+Config.get("user.password.salt")));
            userDao.update(user);
        }else{
            throw new ServiceException("原始密码不正确，请重新输入");
        }


    }
        //qiniu删除文件
    public void updataAvatar(User user, String fileKey) {
        Auth auth=Auth.create(Config.get("qiniu.ak"),Config.get("qiniu.sk"));
        Zone z=Zone.zone0();
        Configuration c=new Configuration(z);
        BucketManager bucketManager=new BucketManager(auth,c);
        String bucket = "db22";
        String key = user.getAvatar();
        try{
            bucketManager.delete(bucket,key);
        }catch(QiniuException e){
            Response r=e.response;
            System.out.println(r.toString());
        }


        user.setAvatar(fileKey);
        userDao.update(user);
    }

    public Page<VoUser> findVoUser(Integer pageNo) {
        int count=0;
        count=userDao.findCount();
        Page<VoUser> userPage=new Page<>(count,pageNo);
        int start=userPage.getStart();
        int pageSize=userPage.getPageSize();

        List<User> userList=userDao.findAllUser(start,pageSize);

        List<VoUser> voUserList=new ArrayList<>();
        for(User user:userList){
            VoUser voUser=new VoUser();
            if(user.getState()!=User.USERSTATE_UNACTIVE){
                LoginLog loginLog = loginLogDao.findLoginByUserId(user.getId());
                //设置state
                voUser.setId(user.getId());
                voUser.setState(user.getState());
                voUser.setUsername(user.getUsername());
                voUser.setCreatetime(user.getCreatetime());
                voUser.setLogintime(loginLog.getLogintime());
                voUser.setIp(loginLog.getIp());
                voUserList.add(voUser);
            }
        }
        userPage.setItems(voUserList);
        return userPage;

    }

    //更改用户的状态
    public void updateStateById(String id, String state) {
        if(StringUtils.isNumeric(id)){
            User user=userDao.findById(Integer.valueOf(id));
            if(user!=null){
                if(user.getState()==Integer.valueOf(state)){
                    if("1".equals(state)){
                        user.setState(User.USERSTATE_DISABLED);
                        userDao.update(user);
                    }else {
                        user.setState(User.USERSTATE_ACTIVE);
                        userDao.update(user);
                    }
                }else {
                    throw new ServiceException("网络异常请稍后再试");
                }
            }else {
                throw new ServiceException("账户异常,请稍后再试");
            }
        }else {
            throw new ServiceException("该账户状态异常，请稍后再试");
        }
    }
}
