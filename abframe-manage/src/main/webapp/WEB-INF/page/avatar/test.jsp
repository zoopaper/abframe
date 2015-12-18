<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="../common/common.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8"/>
    <title>修改个人账户</title>
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


        //判断用户名是否存在
        function hasU() {
            $.ajax({
                type: "POST",
                url: '/avatar/test',
                data: {},
                dataType: 'json',
                cache: false,
                success: function (data) {
                    if (data.success) {
                        alert(data.data.name);
                    } else {
                       alert(111);
                    }
                }
            });
        }


    </script>
</head>
<body>
<a>dfsa</a>
</body>
</html>