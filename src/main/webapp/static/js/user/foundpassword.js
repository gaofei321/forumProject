$(function(){

    $("#type").change(function(){
        var value=$(this).val();
        if("email"==value){
            $("#typename").text("电子邮箱")
        }else {
            $("#typename").text("手机号码")
        }

    });



   $("#btn").click(function(){

       $("#form").submit();
   });

   $("#form").validate({
        errorElement:"span",
        errorClass:"text-error",
        rules:{
            value:{
                required:true
            }
        },
        messages:{
            value:{
                required:"请填写内容"
            }
        },
          submitHandler:function (form) {
                $.ajax({
                    type:'post',
                    url:'/foundpassword',
                    data:$(form).serialize(),
                    beforeSend:function () {
                        $("#btn").text("提交中...").attr("disabled","disabled")
                    },
                    success:function (data) {
                        if(data.state=='success'){
                            var type=$("#type").val();
                                if("email"==type){
                                    alert("请查收邮件进行密码找回")
                                    window.location.href="/login"
                                }else{
                                    //TODO 通过手机号找回密码
                                }
                        }
                    },
                    error:function () {
                        alert("服务器异常")
                    },
                    complete:function () {
                        $("#btn").text("提交").removeAttr("disabled")
                    }
                })
            
          } 
   
   
   
   
   });


});
