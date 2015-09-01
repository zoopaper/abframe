<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>保存结果</title>
    <meta name="description" content="overview & stats"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <script type="text/javascript" src="/static/js/jquery-1.7.2.js"></script>
</head>
<body>
<div id="zhongxin"></div>
<script type="text/javascript">
    var msg = "${msg}";
    if (msg == "success" || msg == "") {
        document.getElementById('zhongxin').style.display = 'none';
        top.Dialog.close();
    } else {
        top.Dialog.close();
    }
</script>
</body>
</html>