package com.kaishengit.web.manage;

import com.kaishengit.dto.JsonResult;
import com.kaishengit.exception.ServiceException;
import com.kaishengit.service.NodeService;
import com.kaishengit.web.BaseServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@WebServlet("/admin/delNode")
public class ManageDeleteNodeServlet extends BaseServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        NodeService nodeService=new NodeService();
        String nodeid=req.getParameter("nodeid");
        JsonResult jsonResult=new JsonResult();
        try {
            nodeService.delNodeByid(nodeid);
            jsonResult.setState(JsonResult.SUCCESS);
        }catch (ServiceException e){
            jsonResult.setMessage(e.getMessage());
        }
            renderJSON(jsonResult,resp);
    }
}
