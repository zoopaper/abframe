<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title></title>
    <meta name="description" content="overview & stats"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <link href="/static/css/bootstrap.min.css" rel="stylesheet"/>
    <link href="/static/css/bootstrap-responsive.min.css" rel="stylesheet"/>
    <link rel="stylesheet" href="/static/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="/static/css/ace.min.css"/>
    <link rel="stylesheet" href="/static/css/ace-responsive.min.css"/>
    <link rel="stylesheet" href="/static/css/ace-skins.min.css"/>

    <script type="text/javascript" src="/static/js/jquery-1.7.2.js"></script>
    <!--提示框-->
    <script type="text/javascript" src="/static/js/jquery.tips.js"></script>
</head>

<script type="text/javascript">
    $(top.hangge());
    //保存
    function save() {
        if ($("#name").val() == "") {
            $("#name").tips({
                side: 3,
                msg: '请输入名称',
                bg: '#AE81FF',
                time: 2
            });

            $("#NAME").focus();
            return false;
        }

        if ($("#code").val() == "" || $("#code").val() == "此编码已存在!") {
            $("#code").tips({
                side: 3,
                msg: '请输入编码',
                bg: '#AE81FF',
                time: 2
            });

            $("#code").focus();
            $("#code").val('');
            $("#code").css("background-color", "white");
            return false;
        }

        if ($("#orderId").val() == "") {

            $("#orderId").tips({
                side: 1,
                msg: '请输入序号',
                bg: '#AE81FF',
                time: 2
            });


            $("#ORDY_BY").focus();
            return false;
        }

        if (isNaN(Number($("#orderId").val()))) {

            $("#ORDY_BY").tips({
                side: 1,
                msg: '请输入数字',
                bg: '#AE81FF',
                time: 2
            });
            $("#orderId").focus();
            $("#orderId").val(1);
            return false;
        }

        has();
    }

    //判断编码是否存在
    function has() {
        var id = $("#id").val();
        var code = $("#code").val();
        var url = "/dict/has?code=" + code + "&id=" + id + "&tm=" + new Date().getTime();
        $.get(url, function (data) {
            if (data == "error") {
                $("#code").css("background-color", "#D16E6C");
                setTimeout("$('#code').val('此编码已存在!')", 500);
            } else {
                $("#Form").submit();
                $("#zhongxin").hide();
                $("#zhongxin2").show();
            }
        });
    }

</script>
<body>
<form action="/dict/save" name="Form" id="Form" method="post">
    <input type="hidden" name="id" id="id" value="${pd.id}"/>
    <input type="hidden" name="parentId" id="parentId" value="${pd.parentId}"/>

    <div id="zhongxin">
        <table>
            <tr class="info">
                <td>
                    <input type="text" name="name" id="name" placeholder="输入名称" value="${pd.name}" title="名称"/>
                </td>
            </tr>
            <tr class="info">
                <td>
                    <input type="text" name="code" id="code" placeholder="输入编码" value="${pd.code}" title="编码"/>
                </td>
            </tr>
            <tr class="info">
                <td>
                    <input type="number" name="orderId" id="orderId" placeholder="输入序号" value="${pd.orderId}"
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
        <img src="/static/images/jzx.gif" style="width: 50px;"/><br/>
        <h4 class="lighter block green"></h4>
    </div>
</form>
</body>
<script type="text/javascript">
    var msg = '${msg}';
    if (msg == 'no') {
        $("#code").attr("readonly", true);
    }
</script>
</html>