package com.kaishengit.service;


import com.kaishengit.dao.NodeDao;
import com.kaishengit.entity.Node;
import com.kaishengit.entity.Topic;
import com.kaishengit.exception.ServiceException;

import java.util.List;

public class NodeService {

    NodeDao nodeDao=new NodeDao();

    public void setTopicNum(Integer nodeid) {
        Node node=nodeDao.findNodeById(nodeid);
        if(node!=null){
            node.setTopicnum(node.getTopicnum()+1);
            nodeDao.update(node);
        }else{
            throw new ServiceException("节点数量异常");
        }


    }

    public List<Node> findAllNode() {

       return nodeDao.findAllNode();

    }
}
