package com.kaishengit.web;

import com.kaishengit.entity.Node;
import com.kaishengit.entity.Topic;
import com.kaishengit.service.TopicService;
import com.kaishengit.util.Config;
import com.kaishengit.util.Page;
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

        String p=req.getParameter("p");
        Integer pageNo=StringUtils.isNumeric(p)?Integer.valueOf(p):1;

        List<Node> nodeList=topicService.findAllNode();

        //分页查询所有的帖子
        if(nodeId==null||nodeId==""){
            Page<Topic> page=topicService.findAllTopic(pageNo);
            req.setAttribute("page",page);
        }else {
            if(StringUtils.isNumeric(nodeId)){
                Page<Topic> page=topicService.findAllTopic(pageNo,nodeId);
                req.setAttribute("page",page);
            }else{
                forward("home.jsp",req,resp);
            }
        }

        String qiniu= Config.get("qiniu.domain");
        req.setAttribute("nodeList",nodeList);
        req.setAttribute("qiniu",qiniu);

        forward("home.jsp",req,resp);
    }
}
