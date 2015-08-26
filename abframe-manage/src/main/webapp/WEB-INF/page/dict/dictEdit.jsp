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
    <title></title>
    <meta name="description" content="overview & stats"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <link href="static/css/bootstrap.min.css" rel="stylesheet"/>
    <link href="static/css/bootstrap-responsive.min.css" rel="stylesheet"/>
    <link rel="stylesheet" href="static/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="static/css/ace.min.css"/>
    <link rel="stylesheet" href="static/css/ace-responsive.min.css"/>
    <link rel="stylesheet" href="static/css/ace-skins.min.css"/>

    <script type="text/javascript" src="static/js/jquery-1.7.2.js"></script>
    <!--提示框-->
    <script type="text/javascript" src="static/js/jquery.tips.js"></script>
</head>

<script type="text/javascript">
    $(top.hangge());
    //保存
    function save() {
        if ($("#NAME").val() == "") {

            $("#NAME").tips({
                side: 3,
                msg: '请输入名称',
                bg: '#AE81FF',
                time: 2
            });

            $("#NAME").focus();
            return false;
        }

        if ($("#BIANMA").val() == "" || $("#BIANMA").val() == "此编码已存在!") {

            $("#BIANMA").tips({
                side: 3,
                msg: '请输入编码',
                bg: '#AE81FF',
                time: 2
            });

            $("#BIANMA").focus();
            $("#BIANMA").val('');
            $("#BIANMA").css("background-color", "white");
            return false;
        }

        if ($("#ORDY_BY").val() == "") {

            $("#ORDY_BY").tips({
                side: 1,
                msg: '请输入序号',
                bg: '#AE81FF',
                time: 2
            });


            $("#ORDY_BY").focus();
            return false;
        }

        if (isNaN(Number($("#ORDY_BY").val()))) {

            $("#ORDY_BY").tips({
                side: 1,
                msg: '请输入数字',
                bg: '#AE81FF',
                time: 2
            });
            $("#ORDY_BY").focus();
            $("#ORDY_BY").val(1);
            return false;
        }

        has();
    }

    //判断编码是否存在
    function has() {
        var ZD_ID = $("#ZD_ID").val();
        var BIANMA = $("#BIANMA").val();
        var url = "dict/has?BIANMA=" + BIANMA + "&ZD_ID=" + ZD_ID + "&tm=" + new Date().getTime();
        $.get(url, function (data) {
            if (data == "error") {
                $("#BIANMA").css("background-color", "#D16E6C");
                setTimeout("$('#BIANMA').val('此编码已存在!')", 500);
            } else {
                $("#Form").submit();
                $("#zhongxin").hide();
                $("#zhongxin2").show();
            }
        });
    }

</script>
<body>
<form action="dict/save" name="Form" id="Form" method="post">
    <input type="hidden" name="ZD_ID" id="ZD_ID" value="${pd.ZD_ID }"/>
    <input type="hidden" name="PARENT_ID" id="PARENT_ID" value="${pd.PARENT_ID }"/>

    <div id="zhongxin">
        <table>
            <tr class="info">
                <td>
                    <input type="text" name="NAME" id="NAME" placeholder="这里输入名称" value="${pd.NAME }" title="名称"/>
                </td>
            </tr>
            <tr class="info">
                <td>
                    <input type="text" name="BIANMA" id="BIANMA" placeholder="这里输入编码" value="${pd.BIANMA }" title="编码"/>
                </td>
            </tr>
            <tr class="info">
                <td>
                    <input type="number" name="ORDY_BY" id="ORDY_BY" placeholder="这里输入序号" value="${pd.ORDY_BY}"
                           title="序号"/>
                </td>
            </tr>
            <tr>
                <td style="text-align: center;">
                    <a class="btn btn-mini btn-primary" onclick="save();">保存</a>
                    <a class="btn btn-mini btn-danger" onclick="top.Dialog.close();">取消</a>
                </td>
            </tr>
        </table>
    </div>
    <div id="zhongxin2" class="center" style="display:none"><br/><br/><br/>
        <img src="static/images/jzx.gif" style="width: 50px;"/><br/>
        <h4 class="lighter block green"></h4>
    </div>
</form>
</body>
<script type="text/javascript">
    var msg = '${msg}';
    if (msg == 'no') {
        $("#BIANMA").attr("readonly", true);
    }
</script>
</html>