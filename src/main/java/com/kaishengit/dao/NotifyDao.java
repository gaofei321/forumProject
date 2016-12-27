package com.kaishengit.dao;


import com.kaishengit.entity.Notify;
import com.kaishengit.util.DbHelp;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.util.List;

public class NotifyDao {


    public List<Notify> findNotifyByState(Integer id) {

        String sql="select*from t_notify where userid=? order by createtime desc,readtime asc";
        return DbHelp.query(sql,new BeanListHandler<Notify>(Notify.class),id);
    }

    public void saveNotify(Notify notify) {
        String sql="insert into t_notify (content,state,userid)values(?,?,?)";
        DbHelp.update(sql,notify.getContent(),notify.getState(),notify.getUserid());
    }

    public Notify findNotifyById(String id) {
        String sql="select*from t_notify where id=?";
        return DbHelp.query(sql,new BeanHandler<Notify>(Notify.class),Integer.valueOf(id));
    }

    public void updateNotifyState(Notify notify) {
        String sql = "update t_notify set state = ?,readtime = ? where id = ?";
        DbHelp.update(sql,notify.getState(),notify.getReadtime(),notify.getId());
    }
}
