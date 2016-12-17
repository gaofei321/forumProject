package com.kaishengit.entity;


import java.io.Serializable;
import java.sql.Timestamp;

public class LoginLog implements Serializable{


    private Integer id;
    private Timestamp logintime;
    private String ip;
    private Integer userid;

    public Timestamp getLogintime() {
        return logintime;
    }

    public void setLogintime(Timestamp logintime) {
        this.logintime = logintime;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }



    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }




}
