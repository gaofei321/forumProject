package com.kaishengit.web.manage;

import com.kaishengit.dto.JsonResult;
import com.kaishengit.exception.ServiceException;
import com.kaishengit.service.UserService;
import com.kaishengit.util.Page;
import com.kaishengit.util.StringUtils;
import com.kaishengit.vo.VoUser;
import com.kaishengit.web.BaseServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


@WebServlet("/admin/user")
public class ManageUserServlet extends BaseServlet {


    private UserService userService=new UserService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String p=req.getParameter("p");
        Integer pageNo= StringUtils.isNumeric(p)?Integer.valueOf(p):1;

        Page<VoUser> page=userService.findVoUser(pageNo);

        req.setAttribute("page",page);
        forward("admin/manageuser.jsp",req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String id=req.getParameter("id");
        String state=req.getParameter("state");
        JsonResult jsonResult=new JsonResult();
        try {

            userService.updateStateById(id, state);

            if("1".equals(state)){
                state="2";
            }else {
                state="1";
            }

            jsonResult.setData(state);
            jsonResult.setState(JsonResult.SUCCESS);

        }catch (ServiceException e){
            jsonResult.setMessage(e.getMessage());
        }
        renderJSON(jsonResult,resp);
    }
}
