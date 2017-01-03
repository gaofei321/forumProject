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
    <form action="" id="updateForm">
        <legend>编辑节点</legend>
        <label>节点名称</label>
        <input type="text" name="nodename" id="nodename" value="${node.nodename}">
        <div class="form-actions">
            <button class="btn btn-primary" id="updatenode" type="button">保存</button>
        </div>
    </form>
</div>
<!--container end-->
<script src="/static/js/jquery-1.12.4.min.js"></script>
<script src="/static/js/jquery.twbsPagination.min.js"></script>
<script src="/static/js/jquery.validate.min.js"></script>
<script src="/static/js/sweetalert.min.js"></script>

<script>

    $(function () {

        $("#updatenode").click(function () {
            $("#updateForm").submit();
        });


        $("#updateForm").validate({

            errorElement:'span',
            errorClass:'text-error',
            rules:{
                nodename:{
                    required:true,
                    remote:'/admin/validateUpdateNode?nodeid=${param.nodeid}'
                }
            },
            messages:{
                nodename:{
                    required:"请输入将要编辑的节点",
                    remote:"该节点已存在",
                }
            },

            submitHandler:function (form) {
                $.ajax({
                    url:'/admin/updatenode',
                    type:'post',
                    data:{"nodeid":${param.nodeid},"nodename":$("#nodename").val()},
                    success:function (json) {
                        if(json.state=='success'){
                            alert("编辑成功");
                            window.location.href="/admin/nodeManage";
                            //window.history.go(-1);
                        }else {
                            alert(data.message)
                        }
                    },
                    error:function () {
                        alert("服务器异常");
                    }
                })
            }

        })

    });

</script>






</body>
</html>

