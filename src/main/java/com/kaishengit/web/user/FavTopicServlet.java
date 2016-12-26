package com.kaishengit.web.user;


import com.kaishengit.dto.JsonResult;
import com.kaishengit.entity.Topic;
import com.kaishengit.entity.User;
import com.kaishengit.service.TopicService;
import com.kaishengit.util.StringUtils;
import com.kaishengit.web.BaseServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/topicFav")
public class FavTopicServlet  extends BaseServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String topicId=req.getParameter("topicid");
        String action=req.getParameter("action");

        User user=getCurrentUser(req);
        TopicService topicService=new TopicService();
        JsonResult jsonResult=new JsonResult();
        if(StringUtils.isNotEmpty(action)&&StringUtils.isNumeric(topicId)){

            if(action.equals("fav")){
                topicService.favTopic(user,topicId);
                jsonResult.setState(JsonResult.SUCCESS);
            }else {
                topicService.unfavTopic(user,topicId);
                jsonResult.setState(JsonResult.SUCCESS);
            }
            Topic topic=topicService.findTopicById(topicId);
            jsonResult.setData(topic.getFavnum());
        }else {

            jsonResult.setMessage("参数异常");
        }
            renderJSON(jsonResult,resp);
    }
}
