package com.kaishengit.web.user;


import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.kaishengit.dto.JsonResult;
import com.kaishengit.entity.Notify;
import com.kaishengit.entity.User;
import com.kaishengit.service.TopicService;
import com.kaishengit.util.StringUtils;
import com.kaishengit.web.BaseServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


@WebServlet("/notify")
public class NotifyServlet extends BaseServlet{

    TopicService topicService=new TopicService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


        //查询未读的信息
        User user=getCurrentUser(req);
        if (user!=null){
            //根据google 的guava进行筛选出已读的通知还是未读的通知
            List<Notify> notifyList=topicService.findnotifyList(user.getId());

            System.out.println(notifyList.size());

            List<Notify> unreadList= Lists.newArrayList(Collections2.filter(notifyList, new Predicate<Notify>() {
                @Override
                public boolean apply(Notify notify) {
                    return notify.getState()==0;
                }
            }));

            JsonResult jsonResult=new JsonResult(unreadList.size());
            renderJSON(jsonResult,resp);
        }

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        User user=getCurrentUser(req);

            if(user!=null){
                List<Notify> notifyList=topicService.findnotifyList(user.getId());
                req.setAttribute("notifyList",notifyList);
                forward("user/notify.jsp",req,resp);
            }

    }
}
