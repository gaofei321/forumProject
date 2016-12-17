$(function () {
    $("#loginBtn").click(function(){

        $("#loginForm").submit();
    });

    $("#loginForm").validate({
        errorElement:"span",
        errorClass:"text-error",
        rules:{
            username:{
                required:true
            },
            password:{
                required:true
            }
        },
        messages:{
            username:{
                required:'账户不能为空'
            },
            password:{
                required:'密码不能为空'
            }
        },

        submitHandler:function (form) {
            $.ajax({
                type:'post',
                url:'/login',
                data:$(form).serialize(),
                beforeSend:function () {
                  $("#loginBtn").text("登录中...").attr("disabled","disabled")
                },
                success:function (data) {
                    if(data.state=='success'){
                        alert("登录成功"),
                        window.location.href="/home";
                    }else{
                        alert(data.message)
                    }
                },
                error:function () {
                    alert("服务器错误")
                },
                complete:function () {
                    $("#loginBtn").text("登录").removeAttr("disabled")
                }
            });
        }



    });




});
