<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="../common/common.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <%@ include file="../common/top.jsp" %>
</head>
<body>

<div class="container-fluid" id="main-container">
    <div id="page-content" class="clearfix">
        <div class="row-fluid">
            <div class="row-fluid">
                <!-- 检索  -->
                <form action="/user/list" method="post" name="userForm" id="userForm" class="form-horizontal">
                    <table>
                        <tr>
                            <td>
                            <span class="input-icon">
                                <input autocomplete="off" id="nav-search-input" type="text" name="account" value="${pd.account}" placeholder="输入账号"/>
                                <i id="nav-search-icon" class="icon-search"></i>
                            </span>
                            </td>
                            <%--<td style="vertical-align:top;">--%>
                                <%--<select class="chzn-select" name="roleId" id="role_id" data-placeholder="请选择角色"--%>
                                        <%--style="vertical-align:top;width: 120px;">--%>
                                    <%--<option value=""></option>--%>
                                    <%--<option value="">全部</option>--%>
                                    <%--<c:forEach items="${roleList}" var="role">--%>
                                        <%--<option value="${role.id}"--%>
                                                <%--<c:if test="${pd.id==role.id}">selected</c:if>>${role.name}</option>--%>
                                    <%--</c:forEach>--%>
                                <%--</select>--%>
                            <%--</td>--%>
                        </tr>
                    </table>
                    <table id="table_report" class="table table-striped table-bordered table-hover">
                        <thead>
                        <tr>
                            <th class="center">
                                <label><input type="checkbox" id="zcheckbox"/><span class="lbl"></span></label>
                            </th>
                            <th>序号</th>
                            <th>账号</th>
                            <th>姓名</th>
                            <th>角色</th>
                            <th>邮箱</th>
                            <th>最近登录</th>
                            <th>上次登录IP</th>
                            <th class="center">操作</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:choose>
                            <c:when test="${not empty userList}">
                                <c:forEach items="${userList}" var="user" varStatus="vs">
                                    <tr>
                                        <td class='center' style="width: 30px;">
                                            <label>
                                                <input type='checkbox' name='ids' value="${user.id}" id="${user.email}"/>
                                                <span class="lbl"></span>
                                            </label>
                                        </td>
                                        <td class='center' style="width: 30px;">${vs.index+1}</td>
                                        <td><a>${user.account}</a></td>
                                        <td>${user.name}</td>
                                        <td>${user.roleName}</td>
                                        <td>
                                            <a title="发送电子邮件" style="text-decoration:none;cursor:pointer;" onclick="sendEmail('${user.email}');">
                                                    ${user.email }
                                                <i class="icon-envelope"></i>
                                            </a>
                                        </td>
                                        <td>${user.lastLogin}</td>
                                        <td>${user.ip}</td>
                                        <td style="width: 60px;">
                                            <div class='hidden-phone visible-desktop btn-group'>
                                                <%--<a class='btn btn-mini btn-warning' title="发送短信" onclick="sendSms('${user.phone}');">--%>
                                                    <%--<i class='icon-envelope'></i>--%>
                                                <%--</a>--%>
                                                <a class='btn btn-mini btn-info' title="编辑" onclick="editUser('${user.id}');">
                                                    <i class='icon-edit'></i>
                                                </a>

                                                <a class='btn btn-mini btn-danger' title="删除" onclick="delUser('${user.id}','${user.account}');">
                                                    <i class='icon-trash'></i>
                                                </a>
                                            </div>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <tr class="main_info">
                                    <td colspan="10" class="center">没有相关数据</td>
                                </tr>
                            </c:otherwise>
                        </c:choose>
                        </tbody>
                    </table>
                    <div class="page-header position-relative">
                        <table style="width:100%;">
                            <tr>
                                <td style="vertical-align:top;">
                                    <a class="btn btn-small btn-success" onclick="add();">新增</a>
                                    <%--<a title="批量发送电子邮件" class="btn btn-small btn-info" onclick="makeAll('确定要给选中的用户发送邮件吗?');"><i class="icon-envelope-alt"></i></a>--%>
                                    <%--<a title="批量发送短信" class="btn btn-small btn-warning" onclick="makeAll('确定要给选中的用户发送短信吗?');"><i class="icon-envelope"></i></a>--%>
                                    <a title="批量删除" class="btn btn-small btn-danger" onclick="makeAll('确定要删除选中的数据吗?');"><i class='icon-trash'></i></a>
                                </td>
                                <td style="vertical-align:top;">
                                    <div class="pagination" style="float: right;padding-top: 0px;margin-top: 0px;">${page.pageStr}</div>
                                </td>
                            </tr>
                        </table>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

<!-- 返回顶部  -->
<a href="#" id="btn-scroll-up" class="btn btn-small btn-inverse">
    <i class="icon-double-angle-up icon-only"></i>
</a>

<!-- 引入 -->
<script type="text/javascript">window.jQuery || document.write("<script src='/static/js/jquery-1.9.1.min.js'>\x3C/script>");</script>
<script src="/static/js/bootstrap.min.js"></script>
<script src="/static/js/ace-elements.min.js"></script>
<script src="/static/js/ace.min.js"></script>

<script type="text/javascript" src="/static/js/chosen.jquery.min.js"></script>
<script type="text/javascript" src="/static/js/bootstrap-datepicker.min.js"></script>
<script type="text/javascript" src="/static/js/bootbox.min.js"></script>
<script type="text/javascript" src="/static/js/jquery.tips.js"></script>
<!--提示框-->
<script type="text/javascript">
    $(top.hangge());
    //检索
    function search() {
        top.jzts();
        $("#userForm").submit();
    }


    //去发送电子邮件页面
    function sendEmail(email) {
        top.jzts();
        var diag = new top.Dialog();
        diag.Drag = true;
        diag.Title = "发送电子邮件";
        diag.URL = '/mail/toSendEmail?email=' + email;
        diag.Width = 660;
        diag.Height = 470;
        diag.CancelEvent = function () {
            diag.close();
        };
        diag.show();
    }

    //去发送短信页面
    function sendSms(phone) {
        top.jzts();
        var diag = new top.Dialog();
        diag.Drag = true;
        diag.Title = "发送短信";
        diag.URL = '/config/toSendSms?phone=' + phone;
        diag.Width = 600;
        diag.Height = 265;
        diag.CancelEvent = function () {
            diag.close();
        };
        diag.show();
    }
    //新增
    function add() {
        top.jzts();
        var diag = new top.Dialog();
        diag.Drag = true;
        diag.Title = "新增用户";
        diag.URL = '/user/toAddUser';
        diag.Width = 520;
        diag.Height = 450;
        diag.CancelEvent = function () {
            if (diag.innerFrame.contentWindow.document.getElementById('zhongxin').style.display == 'none') {
                if ('${page.currentPage}' == '0') {
                    top.jzts();
                    setTimeout("self.location=self.location", 100);
                } else {
                    nextPage(${page.currentPage});
                }
            }
            diag.close();
        };
        diag.show();
    }

    //修改
    function editUser(user_id) {
        top.jzts();
        var diag = new top.Dialog();
        diag.Drag = true;
        diag.Title = "修改用户信息";
        diag.URL = '/user/toEditUser?userId=' + user_id;
        diag.Width = 520;
        diag.Height = 450;
        diag.CancelEvent = function () {
            if (diag.innerFrame.contentWindow.document.getElementById('zhongxin').style.display == 'none') {
                nextPage(${page.currentPage});
            }
            diag.close();
        };
        diag.show();
    }


    function delUser(userId, msg) {
        bootbox.confirm("确定要删除[" + msg + "]吗?", function (result) {
            if (result) {
                top.jzts();
                var url = "/user/deleteUser?userId=" + userId + "&tm=" + new Date().getTime();
                $.get(url, function (data) {
                    nextPage(${page.currentPage});
                });
            }
        });
    }

    //批量操作
    function makeAll(msg) {
        bootbox.confirm(msg, function (result) {
            if (result) {
                var str = '';
                var emstr = '';
                var phones = '';
                for (var i = 0; i < document.getElementsByName('ids').length; i++) {
                    if (document.getElementsByName('ids')[i].checked) {
                        if (str == '') str += document.getElementsByName('ids')[i].value;
                        else str += ',' + document.getElementsByName('ids')[i].value;

                        if (emstr == '') emstr += document.getElementsByName('ids')[i].id;
                        else emstr += ';' + document.getElementsByName('ids')[i].id;

                        if (phones == '') phones += document.getElementsByName('ids')[i].alt;
                        else phones += ';' + document.getElementsByName('ids')[i].alt;
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
                    if (msg == '确定要删除选中的数据吗?') {
                        top.jzts();
                        $.ajax({
                            type: "POST",
                            url: '/user/deleteAllU?tm=' + new Date().getTime(),
                            data: {USER_IDS: str},
                            dataType: 'json',
                            //beforeSend: validateData,
                            cache: false,
                            success: function (data) {
                                $.each(data.list, function (i, list) {
                                    nextPage(${page.currentPage});
                                });
                            }
                        });
                    } else if (msg == '确定要给选中的用户发送邮件吗?') {
                        sendEmail(emstr);
                    } else if (msg == '确定要给选中的用户发送短信吗?') {
                        sendSms(phones);
                    }


                }
            }
        });
    }

</script>

<script type="text/javascript">

    $(function () {

        //日期框
        $('.date-picker').datepicker();

        //下拉框
        $(".chzn-select").chosen();
        $(".chzn-select-deselect").chosen({allow_single_deselect: true});

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
</script>

</body>
</html>

