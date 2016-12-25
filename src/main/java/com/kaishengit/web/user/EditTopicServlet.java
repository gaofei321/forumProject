package com.kaishengit.web.user;

import com.kaishengit.dto.JsonResult;
import com.kaishengit.entity.Node;
import com.kaishengit.entity.Topic;
import com.kaishengit.exception.ServiceException;
import com.kaishengit.service.NodeService;
import com.kaishengit.service.TopicService;
import com.kaishengit.web.BaseServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/edit")
public class EditTopicServlet extends BaseServlet {

    private NodeService nodeService=new NodeService();
    private TopicService topicService=new TopicService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String topicid= req.getParameter("topicid");

        try{
            Topic topic=topicService.findTopicByIdAndEdit(topicid);
            List<Node> nodeList=nodeService.findAllNode();
            req.setAttribute("topic",topic);
            req.setAttribute("nodeList",nodeList);
            forward("user/edittopic.jsp",req,resp);
        }catch (ServiceException e){
            req.setAttribute("message",e.getMessage());
            forward("user/active_error.jsp",req,resp);
        }
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String topicid=req.getParameter("topicid");
        String title=req.getParameter("post_title");
        String content=req.getParameter("editor");
        String nodeid=req.getParameter("nodeid");

        try{
            topicService.EditTopic(topicid,title,content,nodeid);
            JsonResult jsonResult=new JsonResult();

            System.out.println(topicid);

            jsonResult.setState(JsonResult.SUCCESS);
            jsonResult.setData(topicid);
            renderJSON(jsonResult,resp);

        }catch (ServiceException e){
            JsonResult jsonResult=new JsonResult(e.getMessage());
            renderJSON(jsonResult,resp);
        }

    }
}
