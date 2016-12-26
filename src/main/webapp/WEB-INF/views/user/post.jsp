
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>主题页</title>
    <link href="http://cdn.bootcss.com/font-awesome/4.5.0/css/font-awesome.min.css" rel="stylesheet">
    <link href="http://cdn.bootcss.com/bootstrap/2.3.1/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="/static/css/style.css">
    <link rel="stylesheet" href="/static/js/editer/styles/simditor.css">
    <style>
        body{
            background-image: url(/static/img/bg.jpg);
        }
        .simditor .simditor-body {
            min-height: 100px;
        }
        .div1{
            width:100px;height: 50px;border: solid 1px;
        }

    </style>
</head>

<body>
<%@include file="../include/navbar.jsp" %>
<!--header-bar end-->
<div class="container">
    <div class="box">
        <ul class="breadcrumb" style="background-color: #fff;margin-bottom: 0px;">
            <li><a href="/home">首页</a> <span class="divider">/</span></li>
            <li class="active">${requestScope.topic.node.nodename}</li>
        </ul>
        <div class="topic-head">
            <c:if test="${not empty sessionScope.curr_user}">
                <img class="img-rounded avatar" src="http://oi2n24juy.bkt.clouddn.com/${sessionScope.curr_user.avatar}?imageView2/1/w/60/h/60" class="img-circle" alt="设置头像">
            </c:if>
                <h3 class="title">${requestScope.topic.title}</h3>
            <p class="topic-msg muted"><a href="">${requestScope.topic.user.username}</a> · <span id="#topictime">${requestScope.topic.createtime}</span></p>
        </div>
        <div class="topic-body">
            ${requestScope.topic.content}
        </div>
        <div class="topic-toolbar">
            <c:if test="${not empty sessionScope.curr_user}">
                <ul class="unstyled inline pull-left">
                    <c:choose>
                        <c:when test="${not empty fav}">
                            <li><a href="javascript:;" id="favtopic">取消收藏</a></li>
                        </c:when>
                        <c:otherwise>
                            <li><a href="javascript:;" id="favtopic">加入收藏</a></li>
                        </c:otherwise>
                    </c:choose>

                    <c:choose>
                        <c:when test="${not empty th}">
                            <li><a href="javascript:;" id="tktopic">感谢</a></li>
                        </c:when>
                        <c:otherwise>
                            <li><a href="javascript:;" id="tktopic">感谢</a></li>
                        </c:otherwise>
                    </c:choose>


                    <c:if test="${sessionScope.curr_user.id==topic.userid and topic.edit}">
                        <li><a href="/edit?topicid=${topic.id}" id="edit1">编辑</a></li>
                    </c:if>
                    <li><a href name="#reply"></a></li>
                </ul>
            </c:if>
            <ul class="unstyled inline pull-right muted">
                <li>${requestScope.topic.clicknum}次点击</li>
                <li><span id="topicFav">${requestScope.topic.favnum}</span>人收藏</li>
                <li><span id="topicTk">${requestScope.topic.thankyounum}</span>人感谢</li>
            </ul>
        </div>
    </div>

    <!--box end-->

    <div class="box" style="margin-top:20px;">


        <c:forEach items="${replyList}" var="reply" varStatus="vs">
            <div class="talk-item muted" style="font-size: 12px">
                    ${fn:length(replyList)}个回复 | 直到<span id="lastreplytime">${topic.lastreplytime}</span>为止
            </div>

            <div class="talk-item">
            <table class="talk-table">
                <tr>
                    <a name="reply${vs.count}"></a>
                    <td width="50">
                        <img class="avatar" src="${qiniu.domain}${reply.user.avatar}?imageView2/1/w/40/h/40" alt="">
                    </td>
                    <td width="auto">
                        <a href="" style="font-size: 12px" class="replyname">${reply.user.username}</a> <span style="font-size: 12px" class="reply">${reply.createtime}</span>
                        <br>
                        <p style="font-size: 14px">${reply.content}</p>
                    </td>
                    <td width="70" align="right" style="font-size: 12px">
                        <a href="javascript:;" title="回复" class="replylist" rel="${vs.count}" replyname="${reply.user.username}"><i class="fa fa-reply"></i></a>&nbsp;
                        <span class="badge">${vs.count}</span>
                        <div class="div1" hidden >asdasdasd</div>
                    </td>
                </tr>
            </table>
        </div>
        </c:forEach>


    </div>

    <c:choose>
        <c:when test="${not empty sessionScope.curr_user}">
            <div class="box reply_box" style="margin:20px 0px;">
                <div class="talk-item muted" style="font-size: 12px"><i class="fa fa-plus"></i> 添加一条新回复</div>
                <form action="/newReply" method="post" style="padding: 15px;margin-bottom:0px;">
                    <%--<a href name="reply">查看帖子主题</a>--%>
                    <input name="topicid" type="hidden" value="${topic.id}">
                    <textarea name="content" id="editor"></textarea>

                    <div class="talk-item muted" style="text-align: right;font-size: 12px">
                        <span class="pull-left">请尽量让自己的回复能够对别人有帮助回复</span>
                        <button id="btn1" class="btn btn-primary">发布</button>
                    </div>

                </form>

            </div>
        </c:when>
        <c:otherwise>
            <div class="box" style="margin:20px 0px;">
                <div class="talk-item"> 请<a href="/login?redirect=post?topicid=${topic.id}#reply">登录</a>后再回复</div>
            </div>
        </c:otherwise>
    </c:choose>
</div>
<!--container end-->
<script src="http://cdn.bootcss.com/jquery/1.11.2/jquery.min.js"></script>
<script src="/static/js/editer/scripts/module.min.js"></script>
<script src="/static/js/editer/scripts/hotkeys.min.js"></script>
<script src="/static/js/editer/scripts/uploader.min.js"></script>
<script src="/static/js/editer/scripts/simditor.min.js"></script>
<script src="/static/js/jquery.validate.min.js"></script>
<script src="//cdn.bootcss.com/moment.js/2.10.6/moment.min.js"></script>
<script src="//cdn.bootcss.com/moment.js/2.10.6/locale/zh-cn.js"></script>
<script src="/static/js/highlight.pack.js"></script>
<script src="/static/js/user/post.js"></script>

<script>
    hljs.initHighlightingOnLoad();

        $(function(){
            <c:if test="${not empty sessionScope.curr_user}">
                var editor = new Simditor({
                    textarea: $('#editor'),
                    //optional options
                    //toolbar:false,
                });
            $(".replylist").click(function () {
                var count=$(this).attr("rel");
                var replyname=$(this).attr("replyname")

                var html="<a href='#reply"+count+"'>@"+replyname+"</a>";
                editor.setValue(html + editor.getValue());
                window.location.href="#reply";
            });
        </c:if>

        $("#topictime").text(moment($("#topictime").text()).format());
        $("#lastreplytime").text(moment($("#lastreplytime").text()).format('llll'))
        $(".reply").text(function () {
            var time=$(this).text();
            return moment(time).fromNow();
        });

            $("#favtopic").click(function () {
                var $this=$(this);
                var action="";
                if($this.text()=="加入收藏"){
                    action="fav";
                }else {
                    action="unfav";
                }
                $.post("/topicFav",{"topicid":${topic.id},"action":action}).done(
                    function (json) {
                        if(json.state=="success"){
                            if (action=="fav"){
                                $this.text("取消收藏")
                            }else {
                                $this.text("加入收藏")
                            }
                            //alert(json.data);
                            $("#topicFav").text(json.data);
                        }
                    }).error(function () {
                    alert("服务器异常，请稍后再试");
                })
            });

        $("#tktopic").click(function () {

            var $this=$(this);
            var variable="";
            if($this.text()=="感谢"){
                variable="th";
            }else {
                variable="unth";
            }

            $.post("/topicTk",{"topicid":${topic.id},"variable":variable}).done(
                function (json) {
                    if(json.state=="success"){
                        if (variable=="th"){
                            $this.text("取消感谢")
                        }else {
                            $this.text("感谢")
                        }
                        //alert(json.data);
                        $("#topicTk").text(json.data);
                    }
                }).error(function () {
                alert("服务器异常，请稍后再试");
            })


        });


        });
</script>

</body>
</html>