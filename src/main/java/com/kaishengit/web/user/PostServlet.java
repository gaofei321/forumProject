package com.kaishengit.web.user;

import com.kaishengit.entity.*;
import com.kaishengit.exception.ServiceException;
import com.kaishengit.service.TopicService;
import com.kaishengit.util.Config;
import com.kaishengit.util.StringUtils;
import com.kaishengit.web.BaseServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


@WebServlet("/post")
public class PostServlet extends BaseServlet {
    TopicService topicService=new TopicService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String topicid=req.getParameter("topicid");
        //String topicoldid=req.getParameter("topicoldid");
        //根据编辑帖子时的id删掉要编辑的帖子然后再新建一个帖子告诉用户编辑成功了，
        // t_topic有外键约束,没有外键约束的时候可以用此方法
        //topicService.deltopicByOldId(topicoldid);
        try {
            //获取回复的列表项
            Topic topic = topicService.findTopicById(topicid);
            //更改帖子的点击次数
            topicService.updateClicknum(topicid);

            List<Reply> replyList=topicService.findReplyListByTopicId(topicid);

            //判断用户是否收藏了该帖子
            User user=getCurrentUser(req);
            if(StringUtils.isNumeric(topicid)&&user!=null){
                Fav fav=topicService.findFavByUserIdAndTopicId(topicid,user);
                req.setAttribute("fav",fav);
            }

            //判断用户是否感谢了该帖子

            if(StringUtils.isNumeric(topicid)&&user!=null){
                Thank th=topicService.findThankByUserIdAndTopicId(topicid,user);
                req.setAttribute("th",th);
            }


            req.setAttribute("qiniu.domain", Config.get("qiniu.domain"));
            req.setAttribute("replyList",replyList);
            req.setAttribute("topic", topic);
            forward("/user/post.jsp",req,resp);
        }catch (ServiceException e){
            resp.sendError(404,e.getMessage());
        }
    }
}
