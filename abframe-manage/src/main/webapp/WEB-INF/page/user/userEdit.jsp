<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <base href="<%=basePath%>">
    <meta charset="utf-8"/>
    <title></title>
    <meta name="description" content="overview & stats"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <link href="static/css/bootstrap.min.css" rel="stylesheet"/>
    <link href="static/css/bootstrap-responsive.min.css" rel="stylesheet"/>
    <link rel="stylesheet" href="static/css/font-awesome.min.css"/>
    <!-- 下拉框 -->
    <link rel="stylesheet" href="static/css/chosen.css"/>
    <link rel="stylesheet" href="static/css/ace.min.css"/>
    <link rel="stylesheet" href="static/css/ace-responsive.min.css"/>
    <link rel="stylesheet" href="static/css/ace-skins.min.css"/>
    <script type="text/javascript" src="static/js/jquery-1.7.2.js"></script>
    <!--提示框-->
    <script type="text/javascript" src="static/js/jquery.tips.js"></script>

    <script type="text/javascript">
        $(top.hangge());
        $(document).ready(function () {
            if ($("#user_id").val() != "") {
                $("#loginname").attr("readonly", "readonly");
                $("#loginname").css("color", "gray");
            }
        });

        //保存
        function save() {
            if ($("#role_id").val() == "") {

                $("#role_id").tips({
                    side: 3,
                    msg: '选择角色',
                    bg: '#AE81FF',
                    time: 2
                });

                $("#role_id").focus();
                return false;
            }
            if ($("#loginname").val() == "" || $("#loginname").val() == "此用户名已存在!") {

                $("#loginname").tips({
                    side: 3,
                    msg: '输入用户名',
                    bg: '#AE81FF',
                    time: 2
                });

                $("#loginname").focus();
                $("#loginname").val('');
                $("#loginname").css("background-color", "white");
                return false;
            } else {
                $("#loginname").val(jQuery.trim($('#loginname').val()));
            }

            if ($("#NUMBER").val() == "") {

                $("#NUMBER").tips({
                    side: 3,
                    msg: '输入编号',
                    bg: '#AE81FF',
                    time: 3
                });
                $("#NUMBER").focus();
                return false;
            } else {
                $("#NUMBER").val($.trim($("#NUMBER").val()));
            }

            if ($("#user_id").val() == "" && $("#password").val() == "") {

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
            if ($("#PHONE").val() == "") {

                $("#PHONE").tips({
                    side: 3,
                    msg: '输入手机号',
                    bg: '#AE81FF',
                    time: 3
                });
                $("#PHONE").focus();
                return false;
            } else if ($("#PHONE").val().length != 11 && !myreg.test($("#PHONE").val())) {
                $("#PHONE").tips({
                    side: 3,
                    msg: '手机号格式不正确',
                    bg: '#AE81FF',
                    time: 3
                });
                $("#PHONE").focus();
                return false;
            }

            if ($("#EMAIL").val() == "") {

                $("#EMAIL").tips({
                    side: 3,
                    msg: '输入邮箱',
                    bg: '#AE81FF',
                    time: 3
                });
                $("#EMAIL").focus();
                return false;
            } else if (!ismail($("#EMAIL").val())) {
                $("#EMAIL").tips({
                    side: 3,
                    msg: '邮箱格式不正确',
                    bg: '#AE81FF',
                    time: 3
                });
                $("#EMAIL").focus();
                return false;
            }

            if ($("#user_id").val() == "") {
                hasU();
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
        function hasU() {
            var USERNAME = $.trim($("#loginname").val());
            $.ajax({
                type: "POST",
                url: '<%=basePath%>user/hasU.do',
                data: {USERNAME: USERNAME, tm: new Date().getTime()},
                dataType: 'json',
                cache: false,
                success: function (data) {
                    if ("success" == data.result) {
                        $("#userForm").submit();
                        $("#zhongxin").hide();
                        $("#zhongxin2").show();
                    } else {
                        $("#loginname").css("background-color", "#D16E6C");
                        setTimeout("$('#loginname').val('此用户名已存在!')", 500);
                    }
                }
            });
        }

        //判断邮箱是否存在
        function hasE(USERNAME) {
            var EMAIL = $.trim($("#EMAIL").val());
            $.ajax({
                type: "POST",
                url: '<%=basePath%>user/hasE.do',
                data: {EMAIL: EMAIL, USERNAME: USERNAME, tm: new Date().getTime()},
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
                        setTimeout("$('#EMAIL').val('')", 2000);
                    }
                }
            });
        }

        //判断编码是否存在
        function hasN(USERNAME) {
            var NUMBER = $.trim($("#NUMBER").val());
            $.ajax({
                type: "POST",
                url: '<%=basePath%>user/hasN.do',
                data: {NUMBER: NUMBER, USERNAME: USERNAME, tm: new Date().getTime()},
                dataType: 'json',
                cache: false,
                success: function (data) {
                    if ("success" != data.result) {
                        $("#NUMBER").tips({
                            side: 3,
                            msg: '编号已存在',
                            bg: '#AE81FF',
                            time: 3
                        });
                        setTimeout("$('#NUMBER').val('')", 2000);
                    }
                }
            });
        }

    </script>
</head>
<body>
<form action="user/${msg }.do" name="userForm" id="userForm" method="post">
    <input type="hidden" name="USER_ID" id="user_id" value="${pd.USER_ID }"/>

    <div id="zhongxin">
        <table>

            <c:if test="${fx != 'head'}">
                <c:if test="${pd.ROLE_ID != '1'}">
                    <tr class="info">
                        <td>
                            <select class="chzn-select" name="ROLE_ID" id="role_id" data-placeholder="请选择职位"
                                    style="vertical-align:top;">
                                <option value=""></option>
                                <c:forEach items="${roleList}" var="role">
                                    <option value="${role.ROLE_ID }"
                                            <c:if test="${role.ROLE_ID == pd.ROLE_ID }">selected</c:if>>${role.ROLE_NAME }</option>
                                </c:forEach>
                            </select>
                        </td>
                    </tr>
                </c:if>
                <c:if test="${pd.ROLE_ID == '1'}">
                    <input name="ROLE_ID" id="role_id" value="1" type="hidden"/>
                </c:if>
            </c:if>

            <c:if test="${fx == 'head'}">
                <input name="ROLE_ID" id="role_id" value="${pd.ROLE_ID }" type="hidden"/>
            </c:if>

            <tr>
                <td><input type="text" name="USERNAME" id="loginname" value="${pd.USERNAME }" maxlength="32"
                           placeholder="这里输入用户名" title="用户名"/></td>
            </tr>
            <tr>
                <td><input type="text" name="NUMBER" id="NUMBER" value="${pd.NUMBER }" maxlength="32"
                           placeholder="这里输入编号" title="编号" onblur="hasN('${pd.USERNAME }')"/></td>
            </tr>
            <tr>
                <td><input type="password" name="PASSWORD" id="password" maxlength="32" placeholder="输入密码" title="密码"/>
                </td>
            </tr>
            <tr>
                <td><input type="password" name="chkpwd" id="chkpwd" maxlength="32" placeholder="确认密码" title="确认密码"/>
                </td>
            </tr>
            <tr>
                <td><input type="text" name="NAME" id="name" value="${pd.NAME }" maxlength="32" placeholder="这里输入姓名"
                           title="姓名"/></td>
            </tr>
            <tr>
                <td><input type="number" name="PHONE" id="PHONE" value="${pd.PHONE }" maxlength="32"
                           placeholder="这里输入手机号" title="手机号"/></td>
            </tr>
            <tr>
                <td><input type="email" name="EMAIL" id="EMAIL" value="${pd.EMAIL }" maxlength="32" placeholder="这里输入邮箱"
                           title="邮箱" onblur="hasE('${pd.USERNAME }')"/></td>
            </tr>
            <tr>
                <td><input type="text" name="BZ" id="BZ" value="${pd.BZ }" placeholder="这里输入备注" maxlength="64"
                           title="备注"/></td>
            </tr>
            <tr>
                <td style="text-align: center;">
                    <a class="btn btn-mini btn-primary" onclick="save();">保存</a>
                    <a class="btn btn-mini btn-danger" onclick="top.Dialog.close();">取消</a>
                </td>
            </tr>
        </table>
    </div>

    <div id="zhongxin2" class="center" style="display:none"><br/><br/><br/><br/><img
            src="static/images/jiazai.gif"/><br/><h4 class="lighter block green"></h4></div>

</form>

<!-- 引入 -->
<script type="text/javascript">window.jQuery || document.write("<script src='static/js/jquery-1.9.1.min.js'>\x3C/script>");</script>
<script src="static/js/bootstrap.min.js"></script>
<script src="static/js/ace-elements.min.js"></script>
<script src="static/js/ace.min.js"></script>
<script type="text/javascript" src="static/js/chosen.jquery.min.js"></script>
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