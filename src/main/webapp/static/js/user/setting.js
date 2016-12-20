 $(function () {
    $("#basicBtn").click(function(){
       $("#basicForm").submit();
    });

    $("#basicForm").validate({
        errorElement:'span',
        errorClass:'text-error',
        rules:{
            email:{
                required:true,
                email:true,
                remote:'/validate/email?type=1'
            }
        },
        messages:{
            email:{
                required:"请填写新的电子邮箱",
                email:"邮箱格式不正确",
                remote:'电子邮箱已被占用'
            }
        },
        submitHandler:function (form) {
            $.ajax({
              url:"/setting?action=profile",
              type:"post",
              data:$(form).serialize(),
              beforeSend:function () {
                  $("#basicBtn").text("保存中...").attr("disabled","disabled")
              },
              success:function (data) {
                  if(data.state=="success"){
                      alert("邮箱更改成功")
                  }
              },
              error:function () {
                  alert("服务器异常")
              },
              complete:function () {
                  $("#basicBtn").text("保存").removeAttr("disabled")
              }

            });
        }

    })

     $("#passwordBtn").click(function () {
         $("#passwordForm").submit();
     });

     $("#passwordForm").validate({
        errorElement:'span',
        errorClass:'text-error',
         rules:{
             oldpassword:{
                 required:true,
                 rangelength:[6,18]

             },
             newpassword:{
                 required:true,
                 rangelength:[6,18]
             },
             repassword:{
                 required:true,
                 rangelength:[6,18],
                 equalTo:"#newpassword"
             }
         },
         messages:{
             oldpassword:{
                 required:"请输入原始密码",
                 rangelength:"密码长度为6-18个字符",

             },
             newpassword:{
                 required:"请输入新密码",
                 rangelength:"密码长度为6-18个字符",
             },
             repassword:{
                 required:"请重新输入密码",
                 rangelength:"密码长度为6-18个字符",
                 equalTo:"俩次输入的密码不一致，请重新输入"
             }
         },
         submitHandler:function (form) {
             $.ajax({
                 type:"post",
                 url:"/setting?action=password",
                 data:$(form).serialize(),
                 beforeSend:function () {
                    $("#passwordBtn").text("保存中...").attr("disabled","disabled");
                 },
                 success:function (data) {
                    if(data.state=="success"){
                        alert("密码更改成功，请重新登录");
                        window.location.href="/login"
                    }else{
                        alert(data.messages);
                    }
                 },
                 error:function () {
                    alert("服务器异常")
                 },
                 complete:function () {
                    $("#passwordBtn").text("保存").removeAttr("disabled");
                 }
             })
         }
     })

});