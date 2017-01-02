package com.kaishengit.web.manage;
import com.kaishengit.service.TopicService;
import com.kaishengit.util.Page;
import com.kaishengit.util.StringUtils;
import com.kaishengit.vo.TopicReplyCount;
import com.kaishengit.web.BaseServlet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;



@WebServlet("/admin/home")
public class ManageHomeServlet extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        TopicService topicService=new TopicService();

        String p=req.getParameter("p");
        Integer pageNo= StringUtils.isNumeric(p)?Integer.valueOf(p):1;

        //分页查询所有的帖子
        Page<TopicReplyCount> page=topicService.findAllTopicEveryDay(pageNo);
        req.setAttribute("page",page);

        forward("admin/adminhome.jsp",req,resp);
    }
}
