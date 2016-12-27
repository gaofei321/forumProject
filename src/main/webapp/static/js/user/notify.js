
$(function(){


    var loadNotify = function(){

        $.post("/notify",function(json){
            if (json.state == "success" && json.data > 0){
                $("#unreadCount").text(json.data);
            }
        });
    };
    var login = $("#isLogin").text();
        if(login=="1"){
        setInterval(loadNotify,10*1000);
    }
    loadNotify();





});







