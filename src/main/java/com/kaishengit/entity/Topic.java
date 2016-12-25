package com.kaishengit.entity;


import org.joda.time.DateTime;

import java.io.Serializable;
import java.sql.Timestamp;

public class Topic implements Serializable{

    private Integer id;
    private String title;
    private String content;
    private Timestamp createtime;
    private Integer clicknum;
    private Integer favnum;
    private Integer thankyounum;
    private Integer replynum;
    private Timestamp lastreplytime;
    private Integer userid;
    private Integer nodeid;

    private User user;
    private Node node;



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Timestamp getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Timestamp createtime) {
        this.createtime = createtime;
    }

    public Integer getClicknum() {
        return clicknum;
    }

    public void setClicknum(Integer clicknum) {
        this.clicknum = clicknum;
    }

    public Integer getFavnum() {
        return favnum;
    }

    public void setFavnum(Integer favnum) {
        this.favnum = favnum;
    }

    public Integer getThankyounum() {
        return thankyounum;
    }

    public void setThankyounum(Integer thankyounum) {
        this.thankyounum = thankyounum;
    }

    public Integer getReplynum() {
        return replynum;
    }

    public void setReplynum(Integer replynum) {
        this.replynum = replynum;
    }

    public Timestamp getLastreplytime() {
        return lastreplytime;
    }

    public void setLastreplytime(Timestamp lastreplytime) {
        this.lastreplytime = lastreplytime;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public Integer getNodeid() {
        return nodeid;
    }

    public void setNodeid(Integer nodeid) {
        this.nodeid = nodeid;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }



    public boolean isEdit(){
        //获取帖子发布后5分钟内并且没有回复的时候返回一个true;

        DateTime dateTime=new DateTime(getCreatetime());
        if(dateTime.plusMinutes(500).isAfterNow()&&getReplynum()==0){
                return  true;
        }else{
                return  false;
        }

    };






}
