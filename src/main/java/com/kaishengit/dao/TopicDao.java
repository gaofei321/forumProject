package com.kaishengit.dao;
import com.kaishengit.entity.Topic;
import com.kaishengit.entity.User;
import com.kaishengit.util.DbHelp;
import org.apache.commons.dbutils.BasicRowProcessor;
import org.apache.commons.dbutils.handlers.AbstractListHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


public class TopicDao {


    public void saveAll(Topic topic) {
        String sql="insert into t_topic(title,content,clicknum,favnum,thankyounum,replynum,lastreplynum,userid,nodeid)values(?,?,?,?,?,?,?,?,?)";
        DbHelp.update(sql,topic.getTitle(),topic.getContent(),topic.getClicknum(),topic.getFavnum(),topic.getThankyounum(),topic.getReplynum(),topic.getLastreplytime(),topic.getUserid(),topic.getNodeid());
    }

    public Integer save(Topic topic) {
        String sql = "insert into t_topic (title,content,nodeid,userid) values(?,?,?,?)";
        return DbHelp.insert(sql,topic.getTitle(),topic.getContent(),topic.getNodeid(),topic.getUserid());
    }

    public Topic findTopicById(String topicid) {

        String sql="select*from t_topic where id=?";
        return DbHelp.query(sql,new BeanHandler<>(Topic.class),Integer.valueOf(topicid));
    }

    public void update(Topic topic) {
        String sql ="update t_topic set title = ? ,content = ? ,clicknum = ?,favnum = ?,thankyounum = ?,replynum = ?,lastreplytime = ?, nodeid = ?,userid = ? where id = ?";
        DbHelp.update(sql,topic.getTitle(),topic.getContent(),topic.getClicknum(),topic.getFavnum(),topic.getThankyounum(),topic.getReplynum(),topic.getLastreplytime(),topic.getNodeid(),topic.getUserid(),topic.getId());

    }

    public void delTopicByOldId(String topicoldid) {
        String sql="delete from user where id = ?";
        DbHelp.update(sql,Integer.valueOf(topicoldid));
    }

    public List<Topic> findAllTopicAndUser(){
        String sql="select tu.username,tu.avatar,tt.* from t_user tu,t_topic tt where tu.id=tt.userid";
        return DbHelp.query(sql, new AbstractListHandler<Topic>() {
            @Override
            protected Topic handleRow(ResultSet rs) throws SQLException {
                //自动封装Topic
                Topic topic=new BasicRowProcessor().toBean(rs,Topic.class);

                User user=new User();
                user.setUsername(rs.getString("username"));
                user.setAvatar(rs.getString("avatar"));

                topic.setUser(user);
                return topic;
            }
        });


    }


    public static List<Topic> findTopicByNodeId(Integer nodeId) {
        String sql="select tu.username,tu.avatar,tt.* from t_user tu,t_topic tt where tu.id=tt.userid and nodeid=?";
        return DbHelp.query(sql, new AbstractListHandler<Topic>() {
            @Override
            protected Topic handleRow(ResultSet rs) throws SQLException {
                Topic topic=new BasicRowProcessor().toBean(rs,Topic.class);

                User user=new User();
                user.setAvatar(rs.getString("avatar"));
                user.setUsername(rs.getString("username"));
                topic.setUser(user);
                return topic;
            }
        },nodeId);
    }
}
