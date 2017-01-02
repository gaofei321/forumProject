package com.kaishengit.web.manage;


import com.kaishengit.dto.JsonResult;
import com.kaishengit.entity.Admin;
import com.kaishengit.exception.ServiceException;
import com.kaishengit.service.AdminService;
import com.kaishengit.web.BaseServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;


@WebServlet("/admin/login")
public class ManageLoginServlet extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        HttpSession session=req.getSession();
        session.removeAttribute("curr_admin");
        forward("admin/login.jsp",req,resp);

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String adminName=req.getParameter("adminname");
        String password=req.getParameter("password");
        String requestUrl=req.getRequestURI();

        AdminService adminService=new AdminService();
        JsonResult jsonResult=new JsonResult();
       try {
           Admin curr_admin = adminService.findAdminByNameAndPassword(adminName, password);
           req.getSession().setAttribute("curr_admin",curr_admin);
           jsonResult.setState(JsonResult.SUCCESS);
       }catch (ServiceException e){
           jsonResult.setMessage(e.getMessage());
       }
        renderJSON(jsonResult,resp);


    }
}
