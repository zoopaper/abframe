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
    <%@ include file="../common/top.jsp" %>
</head>
<body>
<div class="container-fluid" id="main-container">
    <div id="page-content" class="clearfix">
        <div class="row-fluid">
            <div class="row-fluid">
                <table>
                    <tr bgcolor="#E0E0E0">
                        <td>在线人数：</td>
                        <td style="width:39px;" id="onlineCount">0</td>
                    </tr>
                </table>

                <table id="table_report" class="table table-striped table-bordered table-hover">
                    <thead>
                    <tr>
                        <th class="center" style="width: 30px;">
                            <label><input type="checkbox" id="zcheckbox"/><span class="lbl"></span></label>
                        </th>
                        <th class='center' style="width:50px;">序号</th>
                        <th>用户名</th>
                        <th class="center" style="width: 100px;">操作</th>
                    </tr>
                    </thead>

                    <tbody id="userlist">
                    </tbody>
                </table>

            </div>

            <div class="page-header position-relative">
                <table style="width:100%;">
                    <tr>
                        <td style="vertical-align:top;">
                            <c:if test="${QX.del == 1 }">
                            </c:if>
                        </td>
                    </tr>
                </table>
            </div>
        </div>
    </div>
</div>

<!-- 返回顶部  -->
<a href="#" id="btn-scroll-up" class="btn btn-small btn-inverse">
    <i class="icon-double-angle-up icon-only"></i>
</a>

<!-- 引入 -->
<script type="text/javascript">window.jQuery || document.write("<script src='static/js/jquery-1.9.1.min.js'>\x3C/script>");</script>
<script src="static/js/bootstrap.min.js"></script>
<script src="static/js/ace-elements.min.js"></script>
<script src="static/js/ace.min.js"></script>

<script type="text/javascript" src="static/js/bootbox.min.js"></script>
<!-- 确认窗口 -->
<!-- 引入 -->

<script type="text/javascript" src="static/js/jquery.tips.js"></script>
<!--提示框-->

<script type="text/javascript">
    $(top.hangge());
    $(function () {
        //复选框
        $('table th input:checkbox').on('click', function () {
            var that = this;
            $(this).closest('table').find('tr > td:first-child input:checkbox')
                    .each(function () {
                        this.checked = that.checked;
                        $(this).closest('tr').toggleClass('selected');
                    });

        });
    });
    //批量删除
    function delAll() {
        bootbox.confirm("确定要删除选中的数据吗?", function (result) {
            if (result) {
                var str = '';
                for (var i = 0; i < document.getElementsByName('ids').length; i++) {
                    if (document.getElementsByName('ids')[i].checked) {
                        if (str == '') str += document.getElementsByName('ids')[i].value;
                        else str += ',' + document.getElementsByName('ids')[i].value;
                    }
                }
                if (str == '') {
                    bootbox.dialog("您没有选择任何内容!",
                            [
                                {
                                    "label": "关闭",
                                    "class": "btn-small btn-success",
                                    "callback": function () {
                                        //Example.show("great success");
                                    }
                                }
                            ]
                    );

                    $("#zcheckbox").tips({
                        side: 3,
                        msg: '点这里全选',
                        bg: '#AE81FF',
                        time: 8
                    });

                    return;
                } else {
                    $.ajax({
                        type: "POST",
                        url: '<%=basePath%>user/deleteAllU?tm=' + new Date().getTime(),
                        data: {USER_IDS: str},
                        dataType: 'json',
                        //beforeSend: validateData,
                        cache: false,
                        success: function (data) {
                            $.each(data.list, function (i, list) {
                                document.location.reload();
                                top.jzts();
                            });
                        }
                    });

                }
            }
        });
    }

</script>

<c:if test="${QX.cha == 1 }">
    <script type="text/javascript">
        $(function () {
            top.getUserlist();
            $("#onlineCount").html(top.getUserCount());
            $("#onlineCount").tips({
                side: 3,
                msg: '正在计算,请稍等',
                bg: '#AE81FF',
                time: 5
            });
            setInterval("updateUsercount()", 5000);
        });
        function updateUsercount() {
            $("#onlineCount").html(top.getUserCount());
            $("#userlist").html('');
            $.each(top.getUserlist(), function (i, user) {
                $("#userlist").append(
                        '<tr>' +
                        '<td class="center">' +
                        '<label><input type="checkbox" name="ids" value="' + user + '"/><span class="lbl"></span></label>' +
                        '</td>' +
                        '<td class="center">' + (i + 1) + '</td>' +
                        '<td><a>' + user + '</a></td>' +
                        '<td class="center">' +
                        '<button class="btn btn-mini btn-danger" onclick="goOutTUser(\'' + user + '\')">强制下线</button>' +
                        '</td>' +
                        '</tr>'
                );
            });

        }

        //强制某用户下线
        function goOutTUser(theuser) {
            bootbox.confirm("确定要强制[" + theuser + "]下线吗?", function (result) {
                if (result) {
                    top.goOutUser(theuser);
                    updateUsercount();
                }
            });
        }
    </script>
</c:if>
<c:if test="${QX.cha == 0 }">
    <script type="text/javascript">
        alert("您无权查看");
    </script>
</c:if>
</body>
</html>

