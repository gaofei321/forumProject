$(function () {

    $("#resetBtn").click(function () {

        $("#resetForm").submit();
    });

    $("#resetForm").validate({
       errorElement:'span',
       errorClass:'text-error',
        rules:{
            password:{
              required:true,
               rangelength:[6,18]
            },
            repassword:{
                required:true,
                rangelength:[6,18],
                equalTo:"#password"
            }
        },
        messages:{
            password:{
                required:"请输入密码",
                rangelength:"密码长度为6-18个字符"
            },
            repassword:{
                required:"请重新输入密码",
                rangelength:"密码长度为6-18个字符",
                equalTo:"俩次密码不一样，请重新输入"
            }
        },
        submitHandler:function (form) {
            $.ajax({
                type:'post',
                url:'/foundpassword/newpassword',
                data:$(form).serialize(),
                beforeSend:function () {
                    $("#resetBtn").text("保存中...").attr("disabled","disabled");
                },
                success:function (data) {
                    if(data.state=="success"){
                        alert("密码重置成功，请重新登录");
                        window.location.href="/login";
                    }
                },
                error:function () {
                    alert("系统错误");
                },
                complete:function () {
                    $("#resetBtn").text("保存").removeAttr("disabled");
                }
            });
        }
    });
});

