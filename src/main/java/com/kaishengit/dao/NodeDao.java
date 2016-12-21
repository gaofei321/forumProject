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

    public Node findNodeById(Integer topicid) {
        String sql="select*from t_node where id=?";
        return DbHelp.query(sql,new BeanHandler<>(Node.class),topicid);
    }

    public void update(Node node) {
        String sql="update t_node set nodename=?,topicnum=? where id=?";
        DbHelp.update(sql,node.getNodename(),node.getTopicnum(),node.getId());
    }
}
