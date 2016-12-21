package com.kaishengit.service;


import com.kaishengit.dao.NodeDao;
import com.kaishengit.entity.Node;
import com.kaishengit.entity.Topic;
import com.kaishengit.exception.ServiceException;

public class NodeService {

    NodeDao nodeDao=new NodeDao();

    public void setTopicNum(Integer topicid) {
        Node node=nodeDao.findNodeById(topicid);
        if(node!=null){
            node.setTopicnum(node.getTopicnum()+1);
            nodeDao.update(node);
        }else{
            throw new ServiceException("节点数量异常");
        }


    }
}
