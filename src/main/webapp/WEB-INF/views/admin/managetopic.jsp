<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
    <link href="http://cdn.bootcss.com/font-awesome/4.5.0/css/font-awesome.min.css" rel="stylesheet">
    <link href="http://cdn.bootcss.com/bootstrap/2.3.1/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="/static/css/admin.css">
    <link rel="stylesheet" href="/static/js/sweetalert.css">
</head>
<body>
<%@include file="../include/adminnavbar.jsp" %>
<!--header-bar end-->
<div class="container-fluid" style="margin-top:20px">
    <table class="table">
        <thead>
        <tr>
            <th>名称</th>
            <th>发布人</th>
            <th>发布时间</th>
            <th>回复数量</th>
            <th>最后回复时间</th>
            <th>操作</th>
        </tr>
        </thead>
        <tbody>

      <c:forEach items="${page.items}" var="topic">
        <tr>
            <td>
                <a href="/post?topicid=${topic.id}" target="_blank">${topic.title}</a>
            </td>
            <td>${topic.user.username}</td>
            <td>${topic.createtime}</td>
            <td>${topic.replynum}</td>
            <td>${topic.lastreplytime}</td>
            <td>
                <a href="javascript:;" rel="${topic.id}" class="del">删除</a>
            </td>
        </tr>
      </c:forEach>
        </tbody>
    </table>
    <div class="pagination pagination-mini pagination-centered">
        <ul id="pagination" style="margin-bottom:20px;"></ul>
    </div>
</div>
<!--container end-->

<script src="/static/js/jquery-1.12.4.min.js"></script>
<script src="/static/js/jquery.twbsPagination.min.js"></script>
<script src="/static/js/sweetalert.min.js"></script>

<script>

    $(function(){
        $("#pagination").twbsPagination({
            totalPages:${page.totalPage},
            visiblePages:5,
            first:'首页',
            last:'末页',
            prev:'上一页',
            next:'下一页',
            href: '?p={{number}}'
        });



        $(".del").click(function () {
            var topicid=$(this).attr("rel");

            swal({
                    title: "你确定要删除吗?",
                    text: "你将要删除这个主题帖!",
                    type: "warning",
                    showCancelButton: true,
                    confirmButtonColor: "#DD6B55",
                    confirmButtonText: "确定!",
                    closeOnConfirm: false
                },
                function(){
                    $.ajax({
                        url:'/admin/topicManage',
                        type:'post',
                        data:{'topicid':topicid},
                        success:function (data) {
                            if(data == 'success') {
                                swal({title:"删除成功!"},function () {
                                    window.history.go(0);
                                });
                            } else {
                                swal(data);
                            }
                        },
                        error:function () {
                            swal("服务器异常,删除失败请稍后再试")
                        }
                    })
                });

        });




    });


</script>


</body>
</html>

