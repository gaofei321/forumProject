package com.kaishengit.web.user;


import com.kaishengit.entity.Topic;
import com.kaishengit.entity.User;
import com.kaishengit.exception.ServiceException;
import com.kaishengit.service.TopicService;
import com.kaishengit.util.StringUtils;
import com.kaishengit.web.BaseServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/newReply")
public class NewReplyServlet extends BaseServlet {

    TopicService topicService=new TopicService();
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String content=req.getParameter("content");
        //获取当前用户要访问的URL
        //System.out.println(req.getRequestURI());

        String topicid=req.getParameter("topicid");
        User user=getCurrentUser(req);
        if(StringUtils.isNotEmpty(topicid)){
            try {
                topicService.saveNewReply(content, topicid, user);
            }catch (ServiceException e){
                resp.sendError(404,e.getMessage());
            }
        }else {
            resp.sendError(404);
        }
        resp.sendRedirect("/post?topicid="+topicid);
    }
}
