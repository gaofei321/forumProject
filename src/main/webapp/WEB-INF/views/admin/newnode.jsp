<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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
    <form action="" id="nodeForm">
        <legend>添加新节点</legend>
        <label>节点名称</label>
        <input type="text" name="newnode" id="newnode">
        <div class="form-actions">
            <button class="btn btn-primary" type="nodeBtn">保存</button>
        </div>
    </form>
</div>
<!--container end-->
</body>

<script src="/static/js/jquery-1.12.4.min.js"></script>
<script src="/static/js/jquery.validate.min.js"></script>
<script>

$(function () {
    $("#nodeBtn").click(function () {
        $("#nodeForm").submit();
    });

    $("#nodeForm").validate({
        errorElement:'span',
        errorClass:'text-error',
        rules:{
            newnode:{
                required:true,
            }
        },
        messages:{
            newnode:{
                required:'请输入节点名称'
            }
        },
        submitHandler:function (form) {
            $.ajax({
                url:'/admin/newnode',
                type:'post',
                data:$(form).serialize(),
                success:function (data) {
                    if(data.state=='success'){
                        alert("添加节点成功");
                        $("#newnode").text();
                    }else {
                        alert(data.message);
                    }
                },
                error:function () {
                    alert("服务器异常")
                }
            })
        }
    })
})

</script>

</html>

