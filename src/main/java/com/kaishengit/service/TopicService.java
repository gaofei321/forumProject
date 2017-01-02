package com.kaishengit.service;

import com.kaishengit.dao.*;
import com.kaishengit.entity.*;
import com.kaishengit.exception.ServiceException;
import com.kaishengit.util.Page;
import com.kaishengit.util.StringUtils;

import java.sql.Timestamp;

import com.kaishengit.vo.TopicReplyCount;
import org.joda.time.DateTime;
import java.util.List;

public class TopicService {
    private TopicDao topicDao=new TopicDao();
    private NodeDao nodeDao=new NodeDao();
    private ReplyDao replyDao=new ReplyDao();
    private UserDao userDao=new UserDao();
    private FavDao favDao=new FavDao();
    private ThankDao thankDao=new ThankDao();
    private NotifyDao notifyDao=new NotifyDao();

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

        Topic topic=topicDao.findTopicById(topicid);

        //把回复内容放到通知表里面一份
        if(!user.getId().equals(topic.getUserid())){

            Notify notify=new Notify();
            String content1=user.getUsername()+"回复了你的主题贴子<a href=\"/post?topicid="+topic.getId()+" \">"+topic.getTitle()+"</a>";
            notify.setContent(content1);
            notify.setUserid(topic.getUserid());
            notify.setState(Notify.NOTIFY_STATE_UNREAD);

            notifyDao.saveNotify(notify);
        }



        Reply reply=new Reply();
        reply.setContent(content);
        reply.setTopicid(Integer.valueOf(topicid));
        reply.setUserid(user.getId());
        replyDao.saveNewReply(reply);
       // reply.getCreatetime();



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






    public List<Notify> findnotifyList(Integer id) {


        return notifyDao.findNotifyByState(id);
    }


    public void updateNotifyStateById(String ids) {

        String[] idArry=ids.split(",");
        for (int i=0;i<idArry.length;i++){
            System.out.println(idArry[i]);
            Notify notify=notifyDao.findNotifyById(idArry[i]);
            notify.setState(Notify.NOTIFY_STATE_READ);
            notify.setReadtime(new Timestamp(new DateTime().getMillis()));
            notifyDao.updateNotifyState(notify);
        }


    }


    public Page<Topic> findAllTopic(Integer pageNo) {
            int count=0;
            count=topicDao.count();
            //获取总页数,当前页起始行数，每页显示的数据量
            Page<Topic> totalPage=new Page<>(count,pageNo);
            int start=totalPage.getStart();
            int pageSize=totalPage.getPageSize();
            List<Topic> topicList=topicDao.findAllTopicAndUser(start,pageSize);
            totalPage.setItems(topicList);
            return totalPage;
        }

    public Page<Topic> findAllTopic(Integer pageNo,String nodeId) {

        Node node = nodeDao.findNodeById(Integer.valueOf(nodeId));
        if (node != null) {
            int count = 0;
            count = topicDao.count(Integer.valueOf(nodeId));

            //获取总页数,当前页起始行数，每页显示的数据量
            Page<Topic> totalPage = new Page<>(count, pageNo);
            Integer start = totalPage.getStart();
            Integer pageSize = totalPage.getPageSize();
            if (node.getTopicnum() != 0) {
                List<Topic> topicList = topicDao.findTopicByNodeId(start, pageSize, Integer.valueOf(nodeId));
                totalPage.setItems(topicList);
                return totalPage;

            } else {
                return totalPage;
            }
        } else {
            throw new ServiceException("该节点不存在");
        }

    }


    public void deltopicById(String topicid) {
        if(StringUtils.isNumeric(topicid)) {
            Topic topic = topicDao.findTopicById(topicid);
            if (topic != null) {
                if (topic.getReplynum() != 0) {
                    replyDao.delReplyByTopicid(topicid);
                    //把帖子的回复数改为0
                    Reply reply=replyDao.findReply(topicid);
                    if(reply==null){
                        topic.setReplynum(0);
                        topicDao.update(topic);
                    }else {
                        throw new ServiceException("该帖子已有回复，暂时不能删除");
                    }
                    topicDao.delTopicByOldId(topicid);
                } else {
                    topicDao.delTopicByOldId(topicid);
                }
            } else {
                throw new ServiceException("该帖子不存在，请稍后再试");
            }

        }else{
            throw new ServiceException("该帖子异常，请稍后再试");
        }
    }

    //管理者的首页查询
    public Page<TopicReplyCount> findAllTopicEveryDay(Integer pageNo) {
        int count = topicDao.countTopicByDay();
        Page<TopicReplyCount> page = new Page<>(count,pageNo);

        List<TopicReplyCount> countLit =  topicDao.getTopicAndReplyNumList(page.getStart(),page.getPageSize());
        page.setItems(countLit);
        return page;
    }
}
