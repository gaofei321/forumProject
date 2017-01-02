package com.kaishengit.web.manage;


import com.kaishengit.entity.Topic;
import com.kaishengit.exception.ServiceException;
import com.kaishengit.service.TopicService;
import com.kaishengit.util.Page;
import com.kaishengit.util.StringUtils;
import com.kaishengit.web.BaseServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/admin/topicManage")
public class ManageTopicServlet extends BaseServlet{

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        TopicService topicService=new TopicService();

        String p=req.getParameter("p");
        Integer pageNo= StringUtils.isNumeric(p)?Integer.valueOf(p):1;

        //分页查询所有的帖子
        Page<Topic> page=topicService.findAllTopic(pageNo);
        req.setAttribute("page",page);


        forward("admin/managetopic.jsp",req,resp);

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String topicid=req.getParameter("topicid");
        TopicService topicService=new TopicService();

        try {
            topicService.deltopicById(topicid);
            renderText("success",resp);
        }catch (ServiceException e){
            renderText(e.getMessage(),resp);
        }

    }
}
