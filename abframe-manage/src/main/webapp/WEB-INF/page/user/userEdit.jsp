<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="../common/common.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8"/>
    <title>修改个人信息</title>
    <meta name="description" content="overview & stats"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <link href="/static/css/bootstrap.min.css" rel="stylesheet"/>
    <link href="/static/css/bootstrap-responsive.min.css" rel="stylesheet"/>
    <link rel="stylesheet" href="/static/css/font-awesome.min.css"/>
    <!-- 下拉框 -->
    <link rel="stylesheet" href="/static/css/chosen.css"/>
    <link rel="stylesheet" href="/static/css/ace.min.css"/>
    <link rel="stylesheet" href="/static/css/ace-responsive.min.css"/>
    <link rel="stylesheet" href="/static/css/ace-skins.min.css"/>
    <script type="text/javascript" src="/static/js/jquery-1.7.2.js"></script>
    <!--提示框-->
    <script type="text/javascript" src="/static/js/jquery.tips.js"></script>

    <script type="text/javascript">
        $(top.hangge());
        $(document).ready(function () {
            if ($("#userId").val() != "") {
                $("#account").attr("readonly", "readonly");
                $("#account").css("color", "gray");
            }
        });

        //保存
        function save() {
            if ($("#roleId").val() == "") {
                $("#roleId").tips({
                    side: 3,
                    msg: '选择角色',
                    bg: '#AE81FF',
                    time: 2
                });
                $("#role_id").focus();
                return false;
            }
            if ($("#account").val() == "" || $("#account").val() == "此用户名已存在!") {

                $("#account").tips({
                    side: 3,
                    msg: '输入用户名',
                    bg: '#AE81FF',
                    time: 2
                });

                $("#account").focus();
                $("#account").val('');
                $("#account").css("background-color", "white");
                return false;
            } else {
                $("#account").val(jQuery.trim($('#account').val()));
            }

            if ($("#userId").val() == "" && $("#password").val() == "") {
                $("#password").tips({
                    side: 3,
                    msg: '输入密码',
                    bg: '#AE81FF',
                    time: 2
                });
                $("#password").focus();
                return false;
            }
            if ($("#password").val() != $("#chkpwd").val()) {
                $("#chkpwd").tips({
                    side: 3,
                    msg: '两次密码不相同',
                    bg: '#AE81FF',
                    time: 3
                });
                $("#chkpwd").focus();
                return false;
            }
            if ($("#name").val() == "") {
                $("#name").tips({
                    side: 3,
                    msg: '输入姓名',
                    bg: '#AE81FF',
                    time: 3
                });
                $("#name").focus();
                return false;
            }

            var myreg = /^(((13[0-9]{1})|159)+\d{8})$/;
            if ($("#phone").val() == "") {

                $("#phone").tips({
                    side: 3,
                    msg: '输入手机号',
                    bg: '#AE81FF',
                    time: 3
                });
                $("#phone").focus();
                return false;
            } else if ($("#phone").val().length != 11 && !myreg.test($("#phone").val())) {
                $("#phone").tips({
                    side: 3,
                    msg: '手机号格式不正确',
                    bg: '#AE81FF',
                    time: 3
                });
                $("#phone").focus();
                return false;
            }

            if ($("#email").val() == "") {

                $("#email").tips({
                    side: 3,
                    msg: '输入邮箱',
                    bg: '#AE81FF',
                    time: 3
                });
                $("#email").focus();
                return false;
            } else if (!ismail($("#email").val())) {
                $("#email").tips({
                    side: 3,
                    msg: '邮箱格式不正确',
                    bg: '#AE81FF',
                    time: 3
                });
                $("#email").focus();
                return false;
            }

            if ($("#userId").val() == "") {
                hasUser();
            } else {
                $("#userForm").submit();
                $("#zhongxin").hide();
                $("#zhongxin2").show();
            }
        }

        function ismail(mail) {
            return (new RegExp(/^(?:[a-zA-Z0-9]+[_\-\+\.]?)*[a-zA-Z0-9]+@(?:([a-zA-Z0-9]+[_\-]?)*[a-zA-Z0-9]+\.)+([a-zA-Z]{2,})+$/).test(mail));
        }

        //判断用户名是否存在
        function hasUser() {
            var account = $.trim($("#account").val());
            $.ajax({
                type: "POST",
                url: '/user/hasUser',
                data: {account: account},
                dataType: 'json',
                cache: false,
                success: function (data) {
                    if ("success" == data.result) {
                        $("#userForm").submit();
                        $("#zhongxin").hide();
                        $("#zhongxin2").show();
                    } else {
                        $("#account").css("background-color", "#D16E6C");
                        setTimeout("$('#account').val('此用户名已存在!')", 500);
                    }
                }
            });
        }

        //判断邮箱是否存在
        function hasEmail(account) {
            var email = $.trim($("#email").val());
            $.ajax({
                type: "POST",
                url: '/user/hasEmail',
                data: {email: email, userName: account},
                dataType: 'json',
                cache: false,
                success: function (data) {
                    if ("success" != data.result) {
                        $("#EMAIL").tips({
                            side: 3,
                            msg: '邮箱已存在',
                            bg: '#AE81FF',
                            time: 3
                        });
                        setTimeout("$('#email').val('')", 2000);
                    }
                }
            });
        }
    </script>
    <style>
        .s-input{
            width: 80px;
            margin-left: 20px;
            margin-right: 33px;;
            font-size: 11pt;
            color:#9C9C9C;
        }
        table
        {   border-collapse:   separate;   border-spacing:   10px;   }
    </style>
</head>
<body>

<form action="/user/${msg}" name="userForm" id="userForm" method="post" class="form-horizontal">
    <input type="hidden" name="userId" id="userId" value="${user.id}"/>

    <div id="zhongxin" style="margin-top: 20px;">
        <table>
            <tr class="info">
                <td>
                    <span class="s-input">角色</span><select class="chzn-select" name="roleId" id="roleId" data-placeholder="请选择" style="vertical-align:top;width: 384px;">
                        <option value=""></option>
                        <c:forEach items="${roleList}" var="role">
                            <option value="${role.roleId}"
                                    <c:if test="${role.roleId == pd.roleId}">selected</c:if>>${role.name}
                            </option>
                        </c:forEach>
                    </select>
                </td>
            </tr>
            <tr>
                <td>
                  <span class="s-input">账号</span><input type="text" name="account" id="account" value="${user.account}" maxlength="32" placeholder="输入账号" title="登录账号" style="width: 384px;"/>
                </td>
            </tr>
            <tr>
                <td>
                  <span class="s-input">密码</span><input type="password" name="password" id="password" maxlength="32" placeholder="输入密码" title="密码" style="width: 384px;"/>
                </td>
            </tr>
            <tr>
                <td>
                   <span style="margin-left: 20px;font-size: 11pt;color:#9C9C9C;">确认密码</span> <input type="password" name="chkpwd" id="chkpwd" maxlength="32" placeholder="确认密码" title="确认密码" style="width: 384px;"/>
                </td>
            </tr>
            <tr>
                <td>
                    <span class="s-input">姓名</span><input type="text" name="name" id="name" value="${user.name}" maxlength="32" placeholder="输入真实姓名" title="真实姓名" style="width: 384px;"/>
                </td>
            </tr>
            <tr>
                <td>
                    <span class="s-input">手机</span><input type="number" name="phone" id="phone" value="${user.phone}" maxlength="32" placeholder="输入手机号" title="手机号" style="width: 384px;"/>
                </td>
            </tr>
            <tr>
                <td>
                    <span class="s-input">邮箱</span><input type="email" name="email" id="email" value="${user.email}" maxlength="32" placeholder="输入邮箱" title="邮箱" onblur="hasEmail('${user.account}')" style="width: 384px;"/>
                </td>
            </tr>
            <tr>
                <td>
                    <span class="s-input">爱好</span><input type="text" name="interest" id="interest" value="${user.interest}" placeholder="兴趣爱好" title="兴趣爱好" style="width: 384px;"/>
                </td>
            </tr>
            <tr>
                <td style="text-align: center;">
                    <a class="btn  btn-primary btn-lg" onclick="save();">保存</a>
                    <a class="btn  btn-danger btn-lg" onclick="top.Dialog.close();">取消</a>
                </td>
            </tr>
        </table>
    </div>

    <div id="zhongxin2" class="center" style="display:none"><br/><br/><br/><br/>
        <img src="/static/images/jiazai.gif"/><br/><h4 class="lighter block green"></h4>
    </div>

</form>

<!-- 引入 -->
<script type="text/javascript">window.jQuery || document.write("<script src='/static/js/jquery-1.9.1.min.js'>\x3C/script>");</script>
<script src="/static/js/bootstrap.min.js"></script>
<script src="/static/js/ace-elements.min.js"></script>
<script src="/static/js/ace.min.js"></script>
<script type="text/javascript" src="/static/js/chosen.jquery.min.js"></script>
<!-- 下拉框 -->

<script type="text/javascript">
    $(function () {
        //单选框
        $(".chzn-select").chosen();
        $(".chzn-select-deselect").chosen({allow_single_deselect: true});

        //日期框
        $('.date-picker').datepicker();
    });
</script>
</body>
</html>