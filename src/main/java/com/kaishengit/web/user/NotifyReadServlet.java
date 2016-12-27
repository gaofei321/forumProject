package com.kaishengit.web.user;

import com.kaishengit.service.TopicService;
import com.kaishengit.web.BaseServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/notifyReady")
public class NotifyReadServlet extends BaseServlet{

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String ids=req.getParameter("ids");

        TopicService topicService=new TopicService();
        topicService.updateNotifyStateById(ids);
        renderText("success",resp);

    }
}
