package com.kaishengit.web.manage;


import com.kaishengit.dto.JsonResult;
import com.kaishengit.entity.Node;
import com.kaishengit.exception.ServiceException;
import com.kaishengit.service.NodeService;
import com.kaishengit.web.BaseServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@WebServlet("/admin/newnode")
public class ManageAddNewNode extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        forward("admin/newnode.jsp",req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String newNode=req.getParameter("newnode");
        System.out.println(newNode);
        NodeService nodeService=new NodeService();
        JsonResult jsonResult=new JsonResult();
        try {
            Node node = nodeService.findNodeByName(newNode);
            jsonResult.setState(JsonResult.SUCCESS);
        }catch(ServiceException e){
            jsonResult.setMessage(e.getMessage());
        }
        renderJSON(jsonResult,resp);


    }
}
