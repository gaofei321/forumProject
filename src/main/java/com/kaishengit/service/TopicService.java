package com.kaishengit.service;

import com.kaishengit.dao.NodeDao;
import com.kaishengit.dao.TopicDao;
import com.kaishengit.entity.Node;
import com.kaishengit.entity.Topic;

import java.util.List;

public class TopicService {
    TopicDao topicDao=new TopicDao();
    NodeDao nodeDao=new NodeDao();

    public List<Node> findAllNode() {
        List<Node> nodeList=nodeDao.findAllNode();
        return  nodeList;
    }


    public Topic addNewTopic(String title, String content, Integer nodeid, Integer userid) {

        Topic topic=new Topic();
        topic.setTitle(title);
        topic.setContent(content);
        topic.setNodeid(nodeid);
        topic.setUserid(userid);

        Integer topicId=topicDao.save(topic);
        topic.setId(topicId);
        return topic;

    }

    public Topic findTopicById(String topicid) {
        Topic topic=topicDao.findTopicById(topicid);
        return  topic;
    }
}
