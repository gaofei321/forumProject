package com.kaishengit.web.manage;

import com.kaishengit.dto.JsonResult;
import com.kaishengit.entity.Node;
import com.kaishengit.exception.ServiceException;
import com.kaishengit.service.NodeService;
import com.kaishengit.util.StringUtils;
import com.kaishengit.web.BaseServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@WebServlet("/admin/updatenode")
public class ManageUpdateServlet extends BaseServlet {

    private NodeService nodeService=new NodeService();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String nodeid=req.getParameter("nodeid");
        if(StringUtils.isNumeric(nodeid)){
            Node node=nodeService.findNodeById(nodeid);
            req.setAttribute("node",node);
            forward("admin/updatenode.jsp",req,resp);

        }else {
            resp.sendError(404,"要更改的节点不能为空");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


        String nodeName=req.getParameter("nodename");
        String nodeId=req.getParameter("nodeid");
        JsonResult jsonResult=new JsonResult();
        try {
            nodeService.updateNodeByNameAndId(nodeName, nodeId);
            jsonResult.setState(JsonResult.SUCCESS);
        }catch (ServiceException e){
            jsonResult.setMessage(e.getMessage());
        }
        renderJSON(jsonResult,resp);



    }
}
