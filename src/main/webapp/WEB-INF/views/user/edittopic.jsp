<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>编辑帖子</title>
    <link href="http://cdn.bootcss.com/font-awesome/4.5.0/css/font-awesome.min.css" rel="stylesheet">
    <link href="http://cdn.bootcss.com/bootstrap/2.3.1/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="/static/css/style.css">
    <link rel="stylesheet" href="/static/js/editer/styles/simditor.css">
    <link rel="stylesheet" href="/static/css/emoji/simditor-emoji.css">
    <link rel="stylesheet" href="/static/css/highlightcss/default.css">

</head>
<body>
<%@include file="../include/navbar.jsp" %>
<!--header-bar end-->
<div class="container">
    <div class="box">
        <div class="box-header">
            <span class="title"><i class="fa fa-plus"></i> 编辑帖子</span>
        </div>

        <form action="" id="postForm" style="padding: 20px">
            <label class="control-label">主题标题</label>
            <input type="text" name="post_title" id="post_title" class="post_title" style="width: 100%;box-sizing: border-box;height: 30px" value="${topic.title}">
            <label class="control-label">正文</label>

            <textarea name="editor" id="editor">${topic.content}</textarea>

            <select name="nodeid" id="nodeid" style="margin-top:15px;">
                <option value="">请选择节点</option>
                <c:forEach items="${nodeList}" var="node">
                    <option value="${node.id}">${node.nodename}</option>
                </c:forEach>
            </select>

        </form>
        <div class="form-actions" style="text-align: right">
            <button id="postBtn" class="btn btn-primary">发布主题</button>
        </div>


    </div>
    <!--box end-->
</div>
<!--container end-->
<script src="http://cdn.bootcss.com/jquery/1.11.2/jquery.min.js"></script>
<script src="/static/js/editer/scripts/module.min.js"></script>
<script src="/static/js/editer/scripts/hotkeys.min.js"></script>
<script src="/static/js/editer/scripts/uploader.min.js"></script>
<script src="/static/js/editer/scripts/simditor.min.js"></script>
<script src="/static/js/jquery.validate.min.js"></script>

<script src="/static/js/simditor-emoji.js"></script>
<script src="/static/js/highlight.pack.js"></script>
<script src="/static/js/user/notify.js"></script>
<script>
    hljs.initHighlightingOnLoad();

    $(function () {

        var editor = new Simditor({
            textarea: $('#editor'),
            //optional options

            toolbar: [ 'emoji', 'title', 'bold', 'italic', 'underline', 'strikethrough', 'fontScale', 'color', 'ol', 'ul', 'blockquote', 'code', 'table', 'link', 'image', 'hr', 'indent', 'outdent', 'alignment'],
            emoji: {
                imagePath: '/static/img/emoji/',
                images:['+1.png', '100.png', '109.png', '1234.png', '-1.png', 'a.png']
            },
            upload:{
                url:"http://up-z1.qiniu.com/",
                params:{"token":"${token}"},
                fileKey:"file",
            },
        });


        $("#postBtn").click(function () {
            $("#postForm").submit();
        });
        $("#postForm").validate({
            errorElement:'span',
            errorClass:'text-error',
            rules:{
                post_title:{
                    required:true
                },
                editor:{
                    required:true
                },
                nodeid:{
                    required:true
                }
            },
            messages:{
                post_title:{
                    required:"标题不能为空",
                },
                editor:{
                    required:"必须填写内容",
                },
                nodeid:{
                    required:"请选择节点"
                }
            },
            submitHandler:function (form) {
                $.ajax({
                    type:"post",
                    url:"/edit?topicid=${topic.id}",
                    data:$(form).serialize(),
                    beforeSend:function () {
                        $("#postBtn").text("发布主题中...").attr("disabled","disabled")
                    },
                    success:function (json) {
                        if(json.state=='success'){
                            alert("更改成功");

                            window.location.href='/post?topicid='+json.data;
                        }else{
                            alert("更改失败");
                        }
                    },
                    error:function () {
                        alert("服务器异常，请稍后再试")
                    },
                    complete:function () {
                        $("#postBtn").text("发布主题").removeAttr("disabled")
                    }
                })
            }

        })












    });


</script>


</body>
</html>