$(function () {

    var editor = new Simditor({
        textarea: $('#editor')
        //optional options
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
                url:"/newpost",
                data:$(form).serialize(),
                beforeSend:function () {
                    $("#postBtn").text("发布主题中...").attr("disabled","disabled")
                },
                success:function (json) {
                    if(json.state=='success'){
                        alert("发帖成功");
                        window.location.href='/post?topicid='+json.data.id;
                    }else{
                        alert("发帖失败");
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



