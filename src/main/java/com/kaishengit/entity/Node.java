package com.kaishengit.entity;


import java.io.Serializable;

public class Node implements Serializable{

    private int id;
    private String nodename;
    private int topicnum;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNodename() {
        return nodename;
    }

    public void setNodename(String nodename) {
        this.nodename = nodename;
    }

    public int getTopicnum() {
        return topicnum;
    }

    public void setTopicnum(int topicnum) {
        this.topicnum = topicnum;
    }
}
