package com.kaishengit.dao;

import com.kaishengit.entity.LoginLog;
import com.kaishengit.util.DbHelp;

public class LoginLogDao {


    public void save(LoginLog loginLog) {
        String sql="insert into t_login_log(ip,userid)values(?,?)";
        DbHelp.update(sql,loginLog.getIp(),loginLog.getUserid());
    }
}
