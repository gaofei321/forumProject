package com.kaishengit.service;


import com.kaishengit.dao.NodeDao;
import com.kaishengit.entity.Node;
import com.kaishengit.entity.Topic;
import com.kaishengit.exception.ServiceException;
import com.kaishengit.util.StringUtils;

import javax.xml.ws.Service;
import java.util.List;

public class NodeService {

    NodeDao nodeDao = new NodeDao();

    public void setTopicNum(Integer nodeid) {
        Node node = nodeDao.findNodeById(nodeid);
        if (node != null) {
            node.setTopicnum(node.getTopicnum() + 1);
            nodeDao.update(node);
        } else {
            throw new ServiceException("节点数量异常");
        }


    }

    public List<Node> findAllNode() {

        return nodeDao.findAllNode();

    }

    public Node findNodeByIdAndName(String nodeid, String nodename) {

        if (StringUtils.isNumeric(nodeid)) {

            Node node = nodeDao.findNodeById(Integer.valueOf(nodeid));
            if (node.getNodename().equals(nodename)) {
                return node;
            }else {

                Node node1=nodeDao.findNodeByName(nodename);
                if(node1==null){
                    return node;
                }else {
                    return null;
                }
            }
        }else {
            return null;
        }

    }

    public void updateNodeByNameAndId(String nodeName, String nodeId) {
        if(StringUtils.isNumeric(nodeId)){
            Node node=nodeDao.findNodeById(Integer.valueOf(nodeId));
            node.setNodename(nodeName);
            nodeDao.update(node);
        }else {
            throw new ServiceException("该节点异常，请稍后再试");
        }

    }

    public void delNodeByid(String nodeid) {

        if(StringUtils.isNumeric(nodeid)){

            Node node=nodeDao.findNodeById(Integer.valueOf(nodeid));
            if(node!=null&&node.getTopicnum()==0){
                nodeDao.delNodeByNodeId(nodeid);
            }else {
                throw new ServiceException("该节点下有帖子,不能删除");
            }
        }else {
            throw new ServiceException("该节点异常，请稍后再试");
        }

    }

    public Node findNodeByName(String newNode) {

        if(StringUtils.isNotEmpty(newNode)){
            Node node=nodeDao.findNodeByName(newNode);
            if(node==null){
                Node node1=new Node();
                node1.setNodename(newNode);
                nodeDao.saveNode(node1);
                return node1;
            }else {
                throw new ServiceException("该节点已存在");
            }
        }else {
            throw new ServiceException("节点不能为空");
        }
    }
}