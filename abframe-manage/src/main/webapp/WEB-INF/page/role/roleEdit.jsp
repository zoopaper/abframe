<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
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
    <script type="text/javascript">
        top.hangge();
        //保存
        function save() {
            if ($("#name").val() == "") {
                $("#name").focus();
                return false;
            }
            $("#form1").submit();
            $("#zhongxin").hide();
            $("#zhongxin2").show();
        }

    </script>
</head>
<body>
<form action="/role/edit" name="form1" id="form1" method="post">
    <input type="hidden" name="id" id="id" value="${pd.id}"/>

    <div id="zhongxin">
        <table>
            <tr>
                <td>
                    <input type="text" name="name" id="name" value="${pd.name}" placeholder="输入名称"
                           title="名称"/>
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
</form>
<div id="zhongxin2" class="center" style="display:none">
    <img src="/static/images/jzx.gif" style="width: 50px;"/><br/><h4
        class="lighter block green"></h4>
</div>
<!-- 引入 -->
<script src="/static/js/jquery-1.9.1.min.js"></script>
<script type="text/javascript">window.jQuery || document.write("<script src='/static/js/jquery-1.9.1.min.js'>\x3C/script>");</script>
<script src="/static/js/bootstrap.min.js"></script>
<script src="/static/js/ace-elements.min.js"></script>
<script src="/static/js/ace.min.js"></script>
</body>
</html>
