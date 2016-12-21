package com.kaishengit.web.user;

import com.kaishengit.dto.JsonResult;
import com.kaishengit.entity.Node;
import com.kaishengit.entity.Topic;
import com.kaishengit.entity.User;
import com.kaishengit.exception.ServiceException;
import com.kaishengit.service.NodeService;
import com.kaishengit.service.TopicService;
import com.kaishengit.web.BaseServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/newpost")
public class NewPostServlet extends BaseServlet {
    TopicService topicService=new TopicService();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Node> nodeList=topicService.findAllNode();
        req.setAttribute("nodeList",nodeList);

        forward("user/newpost.jsp",req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String title=req.getParameter("post_title");
        String content=req.getParameter("editor");
        String nodeid=req.getParameter("nodeid");
        try {
            NodeService nodeService = new NodeService();
            nodeService.setTopicNum(Integer.valueOf(nodeid));
        }catch (ServiceException e){
            throw new ServiceException(e.getMessage());
        }


        User user=getCurrentUser(req);
        Topic topic=topicService.addNewTopic(title,content,Integer.valueOf(nodeid),user.getId());

        JsonResult jsonResult=new JsonResult(topic);
        renderJSON(jsonResult,resp);

    }
}

