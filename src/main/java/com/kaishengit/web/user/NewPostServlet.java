package com.kaishengit.web.user;

import com.kaishengit.dto.JsonResult;
import com.kaishengit.entity.Node;
import com.kaishengit.entity.Topic;
import com.kaishengit.entity.User;
import com.kaishengit.exception.ServiceException;
import com.kaishengit.service.NodeService;
import com.kaishengit.service.TopicService;
import com.kaishengit.util.Config;
import com.kaishengit.web.BaseServlet;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/newpost")
public class NewPostServlet extends BaseServlet {
    TopicService topicService=new TopicService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Auth auth=Auth.create(Config.get("qiniu.ak"),Config.get("qiniu.sk"));
        StringMap stringMap=new StringMap();
        stringMap.put("returnBody","{\"success\": true,\"file_path\": \""+Config.get("qiniu.domain")+"${key}\"}");

        String token=auth.uploadToken(Config.get("qiniu.bucket"),null,3600,stringMap);

        List<Node> nodeList=topicService.findAllNode();
        req.setAttribute("nodeList",nodeList);
        req.setAttribute("token",token);
        forward("user/newpost.jsp",req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String title=req.getParameter("post_title");
        String content=req.getParameter("editor");
        String nodeid=req.getParameter("nodeid");

        User user=getCurrentUser(req);
        Topic topic=topicService.addNewTopic(title,content,Integer.valueOf(nodeid),user.getId());


        NodeService nodeService = new NodeService();
        try {
            JsonResult jsonResult=new JsonResult(topic);
            nodeService.setTopicNum(Integer.valueOf(nodeid));
            renderJSON(jsonResult,resp);
        }catch (ServiceException e){
               // jsonResult.setMessage(e.getMessage());
              JsonResult jsonResult1=new JsonResult(e.getMessage());
            renderJSON(jsonResult1,resp);
        }


    }
}

