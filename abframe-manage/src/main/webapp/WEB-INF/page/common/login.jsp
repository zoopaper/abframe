<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">

<head>
    <title>ABFrame</title>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>

    <link rel="stylesheet" href="/static/login/bootstrap.min.css"/>
    <link rel="stylesheet" href="/static/login/bootstrap-responsive.min.css"/>
    <link rel="stylesheet" href="/static/login/matrix-login.css"/>
    <link href="/static/login/font-awesome.css" rel="stylesheet"/>
    <script type="text/javascript" src="/static/js/jquery-1.9.1.min.js"></script>
    <script type="text/javascript" src="/static/js/jquery.backstretch.min.js"></script>
</head>
<body>
<div style="width:100%;text-align: center;margin: 0 auto;position: absolute;">
    <div id="loginbox">
        <form action="" method="post" name="loginForm" id="loginForm">
            <div class="control-group normal_text">
                <h3>
                    ABFrame
                </h3>
            </div>
            <div class="control-group">
                <div class="controls">
                    <div class="main_input_box">
                        <span class="add-on bg_lg">
							<i><img height="37" src="/static/login/user.png"/></i>
						</span>
                        <input type="text" name="userName" id="userName" value="" placeholder="请输入用户名"/>
                    </div>
                </div>
            </div>
            <div class="control-group">
                <div class="controls">
                    <div class="main_input_box">
                        <span class="add-on bg_ly">
							<i><img height="37" src="/static/login/suo.png"/></i>
						</span>
                        <input type="password" name="password" id="password" placeholder="请输入密码" value=""/>
                    </div>
                </div>
            </div>
            <div style="float:right;padding-right:10%;">
                <div style="float: left;margin-top:3px;margin-right:2px;">
                    <font color="white">记住密码</font>
                </div>
                <div style="float: left;">
                    <input name="form-field-checkbox" id="saveid" type="checkbox" onclick="savePaw();"
                           style="padding-top:0px;"/>
                </div>
            </div>
            <div class="form-actions">
                <div style="width:86%;padding-left:8%;">
                    <div style="float: left;">
                        <i><img src="/static/login/yan.png"/></i>
                    </div>
                    <div style="float: left;" class="codediv">
                        <input type="text" name="code" id="code" class="login_code"
                               style="height:16px; padding-top:0px;"/>
                    </div>
                    <div style="float: left;">
                        <i>
                            <img style="height:22px;" id="codeImg" alt="点击更换" title="点击更换" src=""/>
                        </i>
                    </div>

                    <span class="pull-right" style="padding-right:3%;">
                            <a href="javascript:quxiao();" class="btn btn-success">取消</a>
                    </span>
                    <span class="pull-right">
                        <a onclick="severCheck();" class="flip-link btn btn-info" id="to-recover">登录</a>
                    </span>

                </div>
            </div>
        </form>
        <div class="controls">
            <div class="main_input_box">
            </div>
        </div>
    </div>
</div>


<script type="text/javascript">
    $.backstretch([
                "/static/login/images/1.jpg",
                "/static/login/images/2.jpg",
                "/static/login/images/3.jpg"
            ], {
                fade: 1000,
                duration: 6000
            }
    );
    //服务器校验
    function severCheck() {
        if (check()) {
            var userName = $("#userName").val();
            var password = $("#password").val();
            var code = $("#code").val();
            $.ajax({
                type: "POST",
                url: 'login',
                data: {code: code, tm: new Date().getTime(), userName: userName, password: password},
                dataType: 'json',
                cache: false,
                success: function (data) {
                    if ("success" == data.result) {
                        saveCookie();
                        window.location.href = "/main/index";
                    } else if ("usererror" == data.result) {
                        $("#userName").tips({
                            side: 1,
                            msg: "用户名或密码有误",
                            bg: '#FF5080',
                            time: 15
                        });
                        $("#userName").focus();
                    } else if ("codeerror" == data.result) {
                        $("#code").tips({
                            side: 1,
                            msg: "验证码输入有误",
                            bg: '#FF5080',
                            time: 15
                        });
                        $("#code").focus();
                    } else {
                        $("#userName").tips({
                            side: 1,
                            msg: "缺少参数",
                            bg: '#FF5080',
                            time: 15
                        });
                        $("#userName").focus();
                    }
                }
            });
        }
    }

    $(document).ready(function () {
        changeCode();
        $("#codeImg").bind("click", changeCode);
    });

    $(document).keyup(function (event) {
        if (event.keyCode == 13) {
            $("#to-recover").trigger("click");
        }
    });

    function genTimestamp() {
        var time = new Date();
        return time.getTime();
    }

    function changeCode() {
        $("#codeImg").attr("src", "validateCode?t=" + genTimestamp());
    }

    //客户端校验
    function check() {

        if ($("#userName").val() == "") {

            $("#userName").tips({
                side: 2,
                msg: '用户名不为空',
                bg: '#AE81FF',
                time: 3
            });

            $("#userName").focus();
            return false;
        } else {
            $("#userName").val(jQuery.trim($('#userName').val()));
        }

        if ($("#password").val() == "") {

            $("#password").tips({
                side: 2,
                msg: '密码不为空',
                bg: '#AE81FF',
                time: 3
            });

            $("#password").focus();
            return false;
        }
        if ($("#code").val() == "") {

            $("#code").tips({
                side: 1,
                msg: '验证码不为空',
                bg: '#AE81FF',
                time: 3
            });

            $("#code").focus();
            return false;
        }

        $("#loginbox").tips({
            side: 1,
            msg: '正在登录 , 请稍后 ...',
            bg: '#68B500',
            time: 10
        });

        return true;
    }

    function savePaw() {
        if (!$("#saveid").attr("checked")) {
            $.cookie('userName', '', {
                expires: -1
            });
            $.cookie('password', '', {
                expires: -1
            });
            $("#userName").val('');
            $("#password").val('');
        }
    }

    function saveCookie() {
        if ($("#saveid").attr("checked")) {
            $.cookie('userName', $("#userName").val(), {
                expires: 7
            });
            $.cookie('password', $("#password").val(), {
                expires: 7
            });
        }
    }
    function quxiao() {
        $("#userName").val('');
        $("#password").val('');
    }

    jQuery(function () {
        var userName = $.cookie('userName');
        var password = $.cookie('password');
        if (typeof(userName) != "undefined"
                && typeof(password) != "undefined") {
            $("#userName").val(userName);
            $("#password").val(password);
            $("#saveid").attr("checked", true);
            $("#code").focus();
        }
    });


</script>

<script>
    //TOCMAT重启之后 点击左侧列表跳转登录首页
    if (window != top) {
        top.location.href = location.href;
    }
</script>

<script src="/static/js/bootstrap.min.js"></script>
<script src="/static/login/js/jquery.easing.1.3.js"></script>
<script src="/static/login/js/jquery.mobile.customized.min.js"></script>
<script type="text/javascript" src="/static/js/jquery.tips.js"></script>
</body>

</html>