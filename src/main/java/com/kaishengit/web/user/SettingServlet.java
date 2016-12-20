package com.kaishengit.web.user;

import com.google.common.collect.Maps;
import com.kaishengit.dto.JsonResult;
import com.kaishengit.entity.User;
import com.kaishengit.exception.ServiceException;
import com.kaishengit.service.UserService;
import com.kaishengit.util.Config;
import com.kaishengit.util.StringUtils;
import com.kaishengit.web.BaseServlet;
import com.qiniu.util.Auth;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@WebServlet("/setting")
public class SettingServlet extends BaseServlet{
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Auth auth=Auth.create(Config.get("qiniu.ak"),Config.get("qiniu.sk"));
        String token=auth.uploadToken(Config.get("qiniu.bucket"));

        req.setAttribute("token",token);
        forward("user/setting.jsp",req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action=req.getParameter("action");
        if(StringUtils.isNotEmpty(action)&&"profile".equals(action)){
            updateProfile(req, resp);
        }else if(StringUtils.isNotEmpty(action)&&"password".equals(action)){
            updatePassword(req,resp);
        }else if(StringUtils.isNotEmpty(action)&&"avatar".equals(action)){
            updateAvatar(req,resp);
        }
    }

    private void updateAvatar(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String fileKey=req.getParameter("fileKey");
        User user=getCurrentUser(req);

        UserService userService=new UserService();
        userService.updataAvatar(user,fileKey);

        JsonResult jsonResult=new JsonResult();
        jsonResult.setState(JsonResult.SUCCESS);
        renderJSON(jsonResult,resp);
    }

    private void updatePassword(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String oldPassword=req.getParameter("oldpassword");
        String newPassword=req.getParameter("newpassword");
        User user=getCurrentUser(req);
        UserService userService=new UserService();

        try{

            userService.updatePassword(oldPassword,newPassword,user);
            JsonResult result=new JsonResult();
            result.setState(JsonResult.SUCCESS);
            renderJSON(result,resp);

        }catch(ServiceException e){
            JsonResult result=new JsonResult(e.getMessage());
            renderJSON(result,resp);
        }

    }


    protected void updateProfile(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email=req.getParameter("email");
        User user=getCurrentUser(req);

        UserService userService=new UserService();
        userService.updateEmail(user,email);

        Map<String,Object> result= Maps.newHashMap();
        result.put("state","success");
        renderJSON(result,resp);
    }

}
