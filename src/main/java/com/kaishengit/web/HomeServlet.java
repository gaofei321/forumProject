package com.kaishengit.web;

import com.kaishengit.entity.Node;
import com.kaishengit.entity.Topic;
import com.kaishengit.service.TopicService;
import com.kaishengit.util.Config;
import com.kaishengit.util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/home")
public class HomeServlet extends BaseServlet{




    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        TopicService topicService=new TopicService();
        String nodeId=req.getParameter("nodeid");
        System.out.println(nodeId);
        //根据帖子所属的节点nodeid查询该节点下的所有帖子

        List<Node> nodeList=topicService.findAllNode();

        if(nodeId==null){
            List<Topic> topicList=topicService.findAllTopic();
            req.setAttribute("topicList",topicList);
        }else {
            List<Topic> topics=topicService.findAllTopicByNodeId(nodeId);
            req.setAttribute("topicList",topics);
        }


        String qiniu= Config.get("qiniu.domain");
        req.setAttribute("nodeList",nodeList);
        req.setAttribute("qiniu",qiniu);

        forward("home.jsp",req,resp);
    }
}
