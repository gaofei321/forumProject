<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="navbar navbar-inverse  navbar-static-top">
    <div class="navbar-inner">

        <a class="brand" href="/home">论坛首页</a>
        <c:if test="${not empty sessionScope}">
        <ul class="nav">

            <li class="${empty paramValues?'active':''}"><a href="/admin/home">首页</a></li>
            <li class="${param._==1?'active':''}"><a href="/admin/topicManage?_=1">主题管理</a></li>
            <li class="${param._==2?'active':''}"><a href="/admin/nodeManage?_=2">节点管理</a></li>
            <li class="${param._==3?'active':''}"><a href="/admin/user?_=3">用户管理</a></li>
        </ul>
       </c:if>
        <ul class="nav pull-right">
            <li><a href="/admin/logout">安全退出</a></li>
        </ul>
    </div>
</div>
