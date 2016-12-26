package com.kaishengit.service;

import com.kaishengit.dao.*;
import com.kaishengit.entity.*;
import com.kaishengit.exception.ServiceException;
import com.kaishengit.util.StringUtils;

import java.sql.Timestamp;
import org.joda.time.DateTime;
import java.util.List;

public class TopicService {
    private TopicDao topicDao=new TopicDao();
    private NodeDao nodeDao=new NodeDao();
    private ReplyDao replyDao=new ReplyDao();
    private UserDao userDao=new UserDao();
    private FavDao favDao=new FavDao();
    private ThankDao thankDao=new ThankDao();

    public List<Node> findAllNode() {
        List<Node> nodeList=nodeDao.findAllNode();
        return  nodeList;
    }


    public Topic addNewTopic(String title, String content, Integer nodeid, Integer userid) {
        //封装新的topic对象
        Topic topic=new Topic();
        topic.setTitle(title);
        topic.setContent(content);
        topic.setNodeid(nodeid);
        topic.setUserid(userid);
        //暂时设置最后回复时间为当前时间
        topic.setLastreplytime(new Timestamp(new DateTime().getMillis()));


        Integer topicId=topicDao.save(topic);
        topic.setId(topicId);
        return topic;

    }

    public Topic findTopicById(String topicid) {
        if(topicid!=null) {
            Topic topic = topicDao.findTopicById(topicid);
            if(topic!=null){

                User user=userDao.findById(topic.getUserid());
                Node node=nodeDao.findNodeById(topic.getNodeid());
                topic.setUser(user);
                topic.setNode(node);

//                topic.setClicknum(topic.getClicknum()+1);
//                topicDao.update(topic);
                return topic;
            }else {
                throw new ServiceException("该帖子不存在,请稍后再试");
            }
        }else {
            throw new ServiceException("该帖子不存在，请稍后再试");
        }
    }

    public void saveNewReply(String content, String topicid, User user) {

        Reply reply=new Reply();
        reply.setContent(content);
        reply.setTopicid(Integer.valueOf(topicid));
        reply.setUserid(user.getId());
        replyDao.saveNewReply(reply);
       // reply.getCreatetime();


        Topic topic=topicDao.findTopicById(topicid);
        if(topic!=null){

            topic.setReplynum(topic.getReplynum()+1);
            topic.setLastreplytime(new Timestamp(new DateTime().getMillis()));
            topicDao.update(topic);
        }else {
            throw new ServiceException("回复数量异常,请稍后再试");
        }



    }

    public List<Reply> findReplyListByTopicId(String topicid) {
        if (StringUtils.isNotEmpty(topicid)){
            return replyDao.findReplyListByTopicId(topicid);

        }else {
                throw new ServiceException("你要浏览的帖子不存在，请稍后再试");
        }


    }

    public Topic findTopicByIdAndEdit(String topicid) {

       if (StringUtils.isNumeric(topicid)){
            Topic topic=topicDao.findTopicById(topicid);
            if(topic.isEdit()){
                return topic;
            }else {

                throw new ServiceException("该帖子已超过编辑时间或者已经有回复不能进行编辑");
            }
       }else{
           throw new ServiceException("你要编辑的帖子异常，请稍后再试");
       }






    }

    public void deltopicByOldId(String topicoldid) {

        topicDao.delTopicByOldId(topicoldid);

    }

    public void EditTopic(String topicid, String title, String content, String nodeid) {
        //重新编辑帖子的时候需要改变帖子的节点数
        if(StringUtils.isNumeric(topicid)){
            Topic topic=topicDao.findTopicById(topicid);

            if(topic.getNodeid()!=Integer.valueOf(nodeid)){
                Node oldNode=nodeDao.findNodeById(topic.getNodeid());
                oldNode.setTopicnum(oldNode.getTopicnum()-1);
                nodeDao.update(oldNode);
                Node newNode=nodeDao.findNodeById(Integer.valueOf(nodeid));
                newNode.setTopicnum(newNode.getTopicnum()+1);
                nodeDao.update(newNode);
            }

            if (topic.isEdit()){

                topic.setContent(content);
                topic.setTitle(title);
                topic.setNodeid(Integer.valueOf(nodeid));
                topicDao.update(topic);
            }else {
                throw new ServiceException("该帖子已超时或已有回复不能进行编辑,请稍后再试");
            }
        }else{
            throw new ServiceException("你要更改的帖子不存在，请稍后再试");
        }

    }


    public Fav findFavByUserIdAndTopicId(String topicId, User user) {
        return favDao.findByTopicIdAndUserId(user.getId(),Integer.valueOf(topicId));
    }

    public void favTopic(User user, String topicId) {
        Fav fav = new Fav();
        fav.setTopicid(Integer.valueOf(topicId));
        fav.setUserid(user.getId());
        favDao.addFav(fav);

        //topic表收藏字段 +1
        Topic topic = topicDao.findTopicById(topicId);
        topic.setFavnum(topic.getFavnum() + 1);
        topicDao.update(topic);
    }


    public void unfavTopic(User user, String topicId) {

        favDao.deleteFav(user.getId(),topicId);

        //topic表收藏字段 -1
        Topic topic = topicDao.findTopicById(topicId);
        topic.setFavnum(topic.getFavnum() - 1);
        topicDao.update(topic);
    }

    public void updateClicknum(String topicid) {
        Topic topic=topicDao.findTopicById(topicid);

        topic.setClicknum(topic.getClicknum()+1);
        topicDao.update(topic);

    }

    public void thankTopic(User user, String topicid) {

        Thank thank=new Thank();
        thank.setUserid(user.getId());
        thank.setTopicid(Integer.valueOf(topicid));
        thankDao.addTopicThank(thank);

        //topic感谢列+1
        Topic topic = topicDao.findTopicById(topicid);
        topic.setThankyounum(topic.getThankyounum() + 1);
        topicDao.update(topic);

    }


    public void unthankTopic(User user, String topicid) {

        thankDao.delThanknum(user.getId(),Integer.valueOf(topicid));

        Topic topic = topicDao.findTopicById(topicid);
        topic.setThankyounum(topic.getThankyounum() - 1);
        topicDao.update(topic);

    }

    public Thank findThankByUserIdAndTopicId(String topicid, User user) {

        return thankDao.findThankyounumByUserIdAndTopicid(user.getId(),Integer.valueOf(topicid));
    }

    public List<Topic> findAllTopic() {

        return topicDao.findAllTopicAndUser();
    }

    public List<Topic> findAllTopicByNodeId(String nodeId) {

        return TopicDao.findTopicByNodeId(Integer.valueOf(nodeId));
    }
}
