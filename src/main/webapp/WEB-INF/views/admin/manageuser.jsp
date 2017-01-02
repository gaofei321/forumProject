
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
    <link href="http://cdn.bootcss.com/font-awesome/4.5.0/css/font-awesome.min.css" rel="stylesheet">
    <link href="http://cdn.bootcss.com/bootstrap/2.3.1/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<%@include file="../include/adminnavbar.jsp" %>
<!--header-bar end-->
<div class="container-fluid" style="margin-top:20px">
    <table class="table">
        <thead>
        <tr>
            <th>账号</th>
            <th>注册时间</th>
            <th>最后登录时间</th>
            <th>最后登录IP</th>
            <th>操作</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${page.items}" var="voUser">
            <tr>
                <td>${voUser.username}</td>
                <td>${voUser.createtime}</td>
                <td>${voUser.logintime}</td>
                <td>${voUser.ip}</td>
                <td>
                    <a href="javascript:;" class="userState" rel="${voUser.id}" sta="${voUser.state}">${voUser.state==1 ? '禁言':'解除'}</a>
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
</body>
<script src="/static/js/jquery-1.11.1.js"></script>
<script src="/static/js/jquery.twbsPagination.min.js"></script>
<script src="/static/js/user/notify.js"></script>

<script>
    $(function () {

        $("#pagination").twbsPagination({
            totalPages:${page.totalPage},
            visiblePages:5,
            first:'首页',
            last:'末页',
            prev:'上一页',
            next:'下一页',
            href: '?p={{number}}'
        });






        $(".userState").click(function () {
        var $this=$(this);
        var id=$this.attr("rel");
        var stat=$this.attr("sta");

            $.ajax({
                url:'/admin/user',
                type:'post',
                data:{"id":id,"state":stat},
                success:function (json) {
                    if(json.state=="success"){
                        if(json.data=='1'){
                            $this.text("禁言")
                        }else {
                            $this.text("解除")
                        }
                        window.history.go(0);
                    }else {
                        alert(json.message)
                    }
                }
            })
        })

    })


</script>











</html>

