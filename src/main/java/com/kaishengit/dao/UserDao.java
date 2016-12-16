package com.kaishengit.dao;

import com.kaishengit.entity.User;
import com.kaishengit.util.DbHelp;
import com.kaishengit.util.StringUtils;
import org.apache.commons.dbutils.handlers.BeanHandler;

/**
 * Created by gf on 2016/12/15.
 */
public class UserDao {


    public User findUserName(String username) {
        String sql="select*from t_user where username=?";
        return DbHelp.query(sql,new BeanHandler<>(User.class),username);
    }

    public static User findByEmail(String email) {
        String sql="select*from t_user where email=?";
        return DbHelp.query(sql,new BeanHandler<>(User.class),email);

    }


    public void save(User user) {
        String sql="insert into t_user(username,password,email,phone,state,avatar)values(?,?,?,?,?,?)";
        DbHelp.update(sql,user.getUsername(),user.getPassword(),user.getEmail(),user.getPhone(),user.getState(),user.getAvatar());
    }

    public void update(User user) {
        String sql="update t_user set password=?,email=?,phone=?,state=?,avatar=? where id=?";
        DbHelp.update(sql,user.getPassword(),user.getEmail(),user.getPhone(),user.getState(),user.getAvatar(),user.getId());
    }
}
