package com.kaishengit.web.manage;

import com.kaishengit.entity.Node;
import com.kaishengit.service.NodeService;
import com.kaishengit.web.BaseServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/admin/validateUpdateNode")
public class ValidateUpdateNodeServlet extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        NodeService nodeService=new NodeService();

        String nodename=req.getParameter("nodename");
        nodename=new String(nodename.getBytes("ISO8859-1"),"UTF-8");
        String nodeid=req.getParameter("nodeid");

        //判断nodename是否存在
        Node node=nodeService.findNodeByIdAndName(nodeid,nodename);
        if(node!=null){
            renderText("true",resp);
        }else {
            renderText("false",resp);
        }


    }
}
