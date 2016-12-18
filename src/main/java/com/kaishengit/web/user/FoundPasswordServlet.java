package com.kaishengit.web.user;

import com.google.common.collect.Maps;
import com.kaishengit.exception.ServiceException;
import com.kaishengit.service.UserService;
import com.kaishengit.web.BaseServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@WebServlet("/foundpassword")
public class FoundPasswordServlet extends BaseServlet{

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        forward("/user/foundpassword.jsp",req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String type=req.getParameter("type");
        String value=req.getParameter("value");

        //获取当前客户端的sessionID
        String sessionID=req.getSession().getId();

        Map<String,Object> result= Maps.newHashMap();
        UserService userService=new UserService();
        try{
            userService.foundPassword(sessionID,type,value);
            result.put("state","success");
        }catch(ServiceException e){
            result.put("state","error");
            result.put("message",e.getMessage());
        }
            renderJSON(result,resp);
    }
}
