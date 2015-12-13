//菜单状态切换
var fmid = "fhindex";
var mid = "fhindex";
function siMenu(id, fid, MENU_NAME, MENU_URL) {
    if (id != mid) {
        $("#" + mid).removeClass();
        mid = id;
    }
    if (fid != fmid) {
        $("#" + fmid).removeClass();
        fmid = fid;
    }
    $("#" + fid).attr("class", "active open");
    $("#" + id).attr("class", "active");
    top.mainFrame.tabAddHandler(id, MENU_NAME, MENU_URL);
    if (MENU_URL != "druid/index.html") {
        jzts();
    }
}

$(function () {

    //换肤
    $("#skin-colorpicker").ace_colorpicker().on("change", function () {
        var b = $(this).find("option:selected").data("class");
        hf(b);
        var url ='/config/setSkin?skin=' + b + '&tm=' + new Date().getTime();
        $.get(url, function (data) {
        });

    });
});

var userId;

var user = "";

$(function () {
    $.ajax({
        type: "POST",
        url: '/config/getUname?tm=' + new Date().getTime(),
        data: encodeURI(""),
        dataType: 'json',
        //beforeSend: validateData,
        cache: false,
        success: function (data) {
            $.each(data.list, function (i, list) {
                //登陆者资料
                $("#user_info").html( list.name + '');
                user = list.userName;
                userId = list.userId;
                hf(list.skin);//皮肤
                if (list.userName != 'admin') {
                    $("#adminmenu").hide();	//隐藏菜单设置
                    $("#adminzidian").hide();	//隐藏数据字典
                    $("#systemset").hide();	//隐藏系统设置
                }
               // online();//连接在线管理
            });
        }
    });
});

//在线管理
var websocket;
function online() {
    if (window.WebSocket) {
        websocket = new WebSocket(encodeURI('ws://127.0.0.1:8889'));

        websocket.onopen = function () {
            //连接成功
            websocket.send('[join]' + user);
        };
        websocket.onerror = function () {
            //连接失败
        };
        websocket.onclose = function () {
            //连接断开
        };
        //消息接收
        websocket.onmessage = function (message) {
            var message = JSON.parse(message.data);
            alert(message.type);
            if (message.type == 'count') {
                userCount = message.msg;
            } else if (message.type == 'goOut') {
                $("body").html("");
                goOut("此用户在其它终端已经早于您登录,您暂时无法登录");
            } else if (message.type == 'thegoout') {
                $("body").html("");
                goOut("您被系统管理员强制下线");
            } else if (message.type == 'userlist') {
                userlist = message.list;
            }
        };
    }
}

//在线总数
var userCount = 0;
function getUserCount() {
    websocket.send('[count]' + user);
    return userCount;
}
//用户列表
var userlist = "";
function getUserlist() {
    websocket.send('[getUserlist]' + user);
    return userlist;
}
//强制下线
function goOut(msg) {
    alert(msg);
    window.location.href = "/logout";
}
//强制某用户下线
function goOutUser(theuser) {
    websocket.send('[goOut]' + theuser);
}


//换肤
function hf(b) {

    var a = $(document.body);
    a.attr("class", a.hasClass("navbar-fixed") ? "navbar-fixed" : "");
    if (b != "default") {
        a.addClass(b);
    }
    if (b == "skin-1") {
        $(".ace-nav > li.grey").addClass("dark");
    } else {
        $(".ace-nav > li.grey").removeClass("dark");
    }
    if (b == "skin-2") {
        $(".ace-nav > li").addClass("no-border margin-1");
        $(".ace-nav > li:not(:last-child)").addClass("white-pink")
            .find('> a > [class*="icon-"]').addClass("pink").end()
            .eq(0).find(".badge").addClass("badge-warning");
    } else {
        $(".ace-nav > li").removeClass("no-border").removeClass("margin-1");
        $(".ace-nav > li:not(:last-child)").removeClass("white-pink")
            .find('> a > [class*="icon-"]').removeClass("pink").end()
            .eq(0).find(".badge").removeClass("badge-warning");
    }
    if (b == "skin-3") {
        $(".ace-nav > li.grey").addClass("red").find(".badge").addClass("badge-yellow");
    } else {
        $(".ace-nav > li.grey").removeClass("red").find(".badge").removeClass("badge-yellow");
    }
}

//修改个人资料
function editUserH() {
    jzts();
    var diag = new top.Dialog();
    diag.Drag = true;
    diag.Title = "修改个人资料";
    diag.URL = '/user/toEditU?userId=' + userId + '&fx=head';
    diag.Width = 225;
    diag.Height = 389;
    diag.CancelEvent = function () {
        diag.close();
    };
    diag.show();
}

//系统设置
function editSys() {
    jzts();
    var diag = new top.Dialog();
    diag.Drag = true;
    diag.Title = "系统设置";
    diag.URL = '/config/toSystem';
    diag.Width = 600;
    diag.Height = 596;
    diag.CancelEvent = function () {
        diag.close();
    };
    diag.show();
}

//代码生成
function productCode() {
    jzts();
    var diag = new top.Dialog();
    diag.Drag = true;
    diag.Title = "代码生成器";
    diag.URL = '/config/toProductCode';
    diag.Width = 800;
    diag.Height = 450;
    diag.CancelEvent = function () {
        hangge();
        diag.close();
    };
    diag.show();
}


function zidian() {
    jzts();
    var diag = new top.Dialog();
    diag.Drag = true;
    diag.Title = "数据字典";
    diag.URL = '/dict?parentId=0';
    diag.Width = 799;
    diag.Height = 460;
    diag.CancelEvent = function () {
        diag.close();
    };
    diag.show();

}

//菜单
function menu() {
    jzts();
    var diag = new top.Dialog();
    diag.Drag = true;
    diag.Title = "菜单编辑";
    diag.URL = '/menu';
    diag.Width = 720;
    diag.Height = 390;
    diag.CancelEvent = function () {
        diag.close();
    };
    diag.show();

}

//切换菜单
function changeMenu() {
    window.location.href = '/main/yes';
    websocket.send('[leave]' + user);
}

//清除加载进度
function hangge() {
    $("#jzts").hide();
}

//显示加载进度
function jzts() {
    $("#jzts").show();
}