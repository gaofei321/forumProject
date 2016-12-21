package com.kaishengit.dao;
import com.kaishengit.entity.Topic;
import com.kaishengit.util.DbHelp;
import org.apache.commons.dbutils.handlers.BeanHandler;


public class TopicDao {


    public void saveAll(Topic topic) {
        String sql="insert into t_topic(title,content,clicknum,favnum,thankyounum,replynum,lastreplynum,userid,nodeid)values()";
        DbHelp.update(sql,topic.getTitle(),topic.getContent(),topic.getClicknum(),topic.getFavnum(),topic.getThankyounum(),topic.getReplynum(),topic.getLastreplytime(),topic.getUserid(),topic.getNodeid());
    }

    public Integer save(Topic topic) {
        String sql = "insert into t_topic (title,content,nodeid,userid) values(?,?,?,?)";
        return DbHelp.insert(sql,topic.getTitle(),topic.getContent(),topic.getNodeid(),topic.getUserid());
    }

    public Topic findTopicById(String topicid) {

        String sql="select*from t_topic where id=?";
        return DbHelp.query(sql,new BeanHandler<>(Topic.class),topicid);
    }
}
