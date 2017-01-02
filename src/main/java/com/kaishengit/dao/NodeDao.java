package com.kaishengit.dao;

import com.kaishengit.entity.Node;
import com.kaishengit.util.DbHelp;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.util.List;

public class NodeDao {


    public List<Node> findAllNode() {

        String sql = "select * from t_node";
        return DbHelp.query(sql,new BeanListHandler<>(Node.class));

    }

    public Node findNodeById(Integer nodeid) {
        String sql="select*from t_node where id=?";
        return DbHelp.query(sql,new BeanHandler<>(Node.class),nodeid);
    }

    public void update(Node node) {
        String sql="update t_node set nodename=?,topicnum=? where id=?";
        DbHelp.update(sql,node.getNodename(),node.getTopicnum(),node.getId());
    }


    public Node findNodeByName(String nodename) {
        String sql="select * from t_node where nodename=?";
        return DbHelp.query(sql,new BeanHandler<Node>(Node.class),nodename);
    }


    public void delNodeByNodeId(String nodeid) {

        String sql="delete from t_node where id=?";
        DbHelp.update(sql,nodeid);
    }

    public void saveNode(Node node) {
        String sql="insert into t_node (nodename,topicnum)values(?,?)";
        DbHelp.update(sql,node.getNodename(),node.getTopicnum());
    }
}
