package com.kaishengit.entity;


import java.io.Serializable;
import java.sql.Timestamp;

public class User implements Serializable{

    public static final String DEFAULT_AVATAR_NAME="default-avatar.jpg";

    public static final Integer USERSTATE_UNACTIVE=0;
    public static final Integer USERSTATE_ACTIVE=1;
    public static final Integer USERSTATE_DISABLED=2;

    private Integer id;
    private String username;
    private String password;
    private String email;
    private String phone;
    private Timestamp createtime;
    private Integer state;
    private String avatar;




    public Timestamp getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Timestamp createtime) {
        this.createtime = createtime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }



    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }




}
