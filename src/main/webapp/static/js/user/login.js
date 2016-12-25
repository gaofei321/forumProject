$(function () {






    function getParameterByName(name, url) {
        if (!url) {
            url = window.location.href;
        }
        name = name.replace(/[\[\]]/g, "\\$&");
        var regex = new RegExp("[?&]" + name + "(=([^&#]*)|&|#|$)"),
            results = regex.exec(url);
        if (!results) return null;
        if (!results[2]) return '';
        return decodeURIComponent(results[2].replace(/\+/g, " "));
    }



    $("#loginBtn").click(function(){

        $("#loginForm").submit();
    });

    $("#password").keydown(function () {
        if(event.keyCode == '13'){
            $("#loginForm").submit();
        }
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
                        alert("登录成功");

                        var url=getParameterByName("redirect");
                        if(url){
                            var hash=location.hash;
                            if(hash){
                                window.location.href=url+hash;
                            }else {
                                window.location.href=url
                            }
                        }else {
                            window.location.href="/home";
                        }
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
