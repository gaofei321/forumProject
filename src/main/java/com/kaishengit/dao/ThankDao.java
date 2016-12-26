package com.kaishengit.dao;


import com.kaishengit.entity.Thank;
import com.kaishengit.util.DbHelp;
import org.apache.commons.dbutils.handlers.BeanHandler;

public class ThankDao {


    public void addTopicThank(Thank thank) {
       String sql="insert into t_thank(userid,topicid)values(?,?)";
       DbHelp.update(sql,thank.getUserid(),thank.getTopicid());
    }

    public void delThanknum(Integer userid, Integer topicid) {

        String sql="delete from t_thank where userid=? and topicid=?";
        DbHelp.update(sql,userid,topicid);
    }

    public Thank findThankyounumByUserIdAndTopicid(Integer userid, Integer topicid) {
        String sql="select * from t_thank where userid=? and topicid=?";
        return DbHelp.query(sql,new BeanHandler<Thank>(Thank.class),userid,topicid);


    }
}
