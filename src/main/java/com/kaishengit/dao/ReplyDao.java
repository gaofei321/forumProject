package com.kaishengit.dao;


import com.kaishengit.entity.Reply;
import com.kaishengit.entity.User;
import com.kaishengit.util.Config;
import com.kaishengit.util.DbHelp;
import org.apache.commons.dbutils.BasicRowProcessor;
import org.apache.commons.dbutils.handlers.AbstractListHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class ReplyDao {


    public void saveNewReply(Reply reply) {
        String sql="insert into t_reply(content,userid,topicid)values(?,?,?)";
        DbHelp.update(sql,reply.getContent(),reply.getUserid(),reply.getTopicid());
    }

    public List<Reply> findReplyListByTopicId(String topicid) {
        String sql="select tu.id,tu.username,tu.avatar,tr.* from t_reply tr, t_user tu where tr.userid=tu.id and topicid=?";
        return DbHelp.query(sql, new AbstractListHandler<Reply>() {
            @Override
            protected Reply handleRow(ResultSet resultSet) throws SQLException {
                Reply reply=new BasicRowProcessor().toBean(resultSet,Reply.class);
                User user=new User();
                user.setAvatar(Config.get("qiniu.domain")+resultSet.getString("avatar"));
                user.setUsername(resultSet.getString("username"));
                user.setId(resultSet.getInt("id"));
                reply.setUser(user);

                return reply;

            }
        },topicid);

    }

    public void delReplyByTopicid(String topicid) {

        String sql="delete from t_reply where topicid=?";
        DbHelp.update(sql,topicid);
    }

    public Reply findReply(String topicid) {

        String sql="select*from t_reply where topicid=?";
        return DbHelp.query(sql,new BeanHandler<Reply>(Reply.class),topicid);
    }
}
