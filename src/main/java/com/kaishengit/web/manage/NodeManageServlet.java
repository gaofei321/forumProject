package com.kaishengit.web.manage;


import com.kaishengit.entity.Node;
import com.kaishengit.service.TopicService;
import com.kaishengit.web.BaseServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/admin/nodeManage")
public class NodeManageServlet extends BaseServlet{

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        TopicService topicService=new TopicService();
        List<Node> nodeList=topicService.findAllNode();
        req.setAttribute("nodeList",nodeList);

        forward("admin/nodeManage.jsp",req,resp);
    }


}
