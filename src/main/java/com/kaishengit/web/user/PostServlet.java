package com.kaishengit.web.user;

import com.kaishengit.entity.Topic;
import com.kaishengit.exception.ServiceException;
import com.kaishengit.service.TopicService;
import com.kaishengit.web.BaseServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@WebServlet("/post")
public class PostServlet extends BaseServlet {
    TopicService topicService=new TopicService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String topicid=req.getParameter("topicid");

        try {
            Topic topic = topicService.findTopicById(topicid);
            req.setAttribute("topic", topic);
            forward("user/post.jsp",req,resp);
        }catch (ServiceException e){
            resp.sendError(404);
        }
    }
}
