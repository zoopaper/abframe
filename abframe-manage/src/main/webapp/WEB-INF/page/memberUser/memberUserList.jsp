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
                <!-- 检索  -->
                <form action="member/listUser" method="post" name="userForm" id="userForm">
                    <table border='0'>
                        <tr>
                            <td>
                                <span class="input-icon">
                                    <input autocomplete="off" id="nav-search-input" type="text" name="USERNAME"
                                           value="${pd.USERNAME }" placeholder="这里输入关键词"/>
                                    <i id="nav-search-icon" class="icon-search"></i>
                                </span>
                            </td>

                            <td><input class="span10 date-picker" name="lastLoginStart" id="lastLoginStart"
                                       value="${pd.lastLoginStart}" type="text" data-date-format="yyyy-mm-dd"
                                       readonly="readonly" style="width:88px;" placeholder="开始日期"/></td>
                            <td><input class="span10 date-picker" name="lastLoginEnd" id="lastLoginEnd"
                                       value="${pd.lastLoginEnd}" type="text" data-date-format="yyyy-mm-dd"
                                       readonly="readonly" style="width:88px;" placeholder="到期日期"/></td>
                            <td style="vertical-align:top;">
                                <select class="chzn-select" name="ROLE_ID" id="role_id" data-placeholder="请选择等级"
                                        style="vertical-align:top;width: 120px;">
                                    <option value=""></option>
                                    <c:forEach items="${roleList}" var="role">
                                        <option value="${role.ROLE_ID }"
                                                <c:if test="${pd.ROLE_ID==role.ROLE_ID}">selected</c:if>>${role.ROLE_NAME }</option>
                                    </c:forEach>
                                </select>
                            </td>
                            <td style="vertical-align:top;">
                                <select class="chzn-select" name="STATUS" id="STATUS" data-placeholder="状态"
                                        style="vertical-align:top;width: 79px;">
                                    <option value=""></option>
                                    <option value="">全部</option>
                                    <option value="1"
                                            <c:if test="${pd.STATUS == '1' }">selected</c:if> >正常
                                    </option>
                                    <option value="0"
                                            <c:if test="${pd.STATUS == '0' }">selected</c:if> >冻结
                                    </option>
                                </select>
                            </td>
                            <td style="vertical-align:top;">
                                <button class="btn btn-mini btn-light" onclick="search();" title="检索"><i
                                        id="nav-search-icon" class="icon-search"></i></button>
                            </td>
                            <c:if test="${QX.cha == 1 }">
                                <td style="vertical-align:top;"><a class="btn btn-mini btn-light" onclick="toExcel();"
                                                                   title="导出到EXCEL"><i id="nav-search-icon"
                                                                                       class="icon-download-alt"></i></a>
                                </td>
                            </c:if>
                        </tr>
                    </table>
                    <!-- 检索  -->
                    <table id="table_report" class="table table-striped table-bordered table-hover">
                        <thead>
                        <tr>
                            <th class="center">
                                <label><input type="checkbox" id="zcheckbox"/><span class="lbl"></span></label>
                            </th>
                            <th>序号</th>
                            <th>用户名</th>
                            <th>姓名</th>
                            <th>等级</th>
                            <th><i class="icon-time hidden-phone"></i>到期日期</th>
                            <th>年限</th>
                            <th><i class="icon-time hidden-phone"></i>最近登录</th>
                            <th>上次登录IP</th>
                            <th class="center">状态</th>
                            <th class="center">操作</th>
                        </tr>
                        </thead>

                        <tbody>

                        <!-- 开始循环 -->
                        <c:choose>
                            <c:when test="${not empty userList}">
                                <c:if test="${QX.cha == 1 }">
                                    <c:forEach items="${userList}" var="user" varStatus="vs">

                                        <tr>
                                            <td class='center' style="width: 30px;">
                                                <label><input type='checkbox' name='ids' value="${user.USER_ID }"
                                                              id="${user.EMAIL }" alt="${user.PHONE }"/><span
                                                        class="lbl"></span></label>
                                            </td>
                                            <td class='center' style="width: 30px;">${vs.index+1}</td>
                                            <td>${user.USERNAME }</td>
                                            <td>${user.NAME }</td>
                                            <td>${user.ROLE_NAME }</td>
                                            <td>${user.END_TIME }</td>
                                            <td>${user.YEARS }</td>
                                            <td>${user.LAST_LOGIN}</td>
                                            <td>${user.IP}</td>
                                            <td style="width: 60px;" class="center">
                                                <c:if test="${user.STATUS == '0' }"><span
                                                        class="label label-important arrowed-in">冻结</span></c:if>
                                                <c:if test="${user.STATUS == '1' }"><span
                                                        class="label label-success arrowed">正常</span></c:if>
                                            </td>
                                            <td style="width: 30px;" class="center">
                                                <div class='hidden-phone visible-desktop btn-group'>

                                                    <c:if test="${QX.edit != 1 && QX.del != 1 }">
                                                        <span class="label label-large label-grey arrowed-in-right arrowed-in"><i
                                                                class="icon-lock" title="无权限"></i></span>
                                                    </c:if>
                                                    <div class="inline position-relative">
                                                        <button class="btn btn-mini btn-info" data-toggle="dropdown"><i
                                                                class="icon-cog icon-only"></i></button>
                                                        <ul class="dropdown-menu dropdown-icon-only dropdown-light pull-right dropdown-caret dropdown-close">
                                                            <c:if test="${QX.FX_QX == 1 }">
                                                                <li><a style="cursor:pointer;" title="发送电子邮件"
                                                                       onclick="sendEmail('${user.EMAIL }');"
                                                                       class="tooltip-success" data-rel="tooltip"
                                                                       title="" data-placement="left"><span
                                                                        class="green"><i class='icon-envelope-alt'></i></span></a>
                                                                </li>
                                                            </c:if>
                                                            <c:if test="${QX.FW_QX == 1 }">
                                                                <li><a style="cursor:pointer;" title="发送短信"
                                                                       onclick="sendSms('${user.PHONE }');"
                                                                       class="tooltip-warning" data-rel="tooltip"
                                                                       title="" data-placement="left"><span
                                                                        class="blue"><i
                                                                        class='icon-envelope'></i></span></a></li>
                                                            </c:if>
                                                            <c:if test="${QX.edit == 1 }">
                                                                <li><a style="cursor:pointer;" title="编辑"
                                                                       onclick="editUser('${user.USER_ID }');"
                                                                       class="tooltip-success" data-rel="tooltip"
                                                                       title="" data-placement="left"><span
                                                                        class="green"><i
                                                                        class="icon-edit"></i></span></a></li>
                                                            </c:if>
                                                            <c:choose>
                                                                <c:when test="${user.USERNAME=='admin'}"></c:when>
                                                                <c:otherwise>
                                                                    <c:if test="${QX.del == 1 }">
                                                                        <li><a style="cursor:pointer;" title="删除"
                                                                               onclick="delUser('${user.USER_ID }','${user.USERNAME }');"
                                                                               class="tooltip-error" data-rel="tooltip"
                                                                               title="" data-placement="left"><span
                                                                                class="red"><i
                                                                                class="icon-trash"></i></span> </a></li>
                                                                    </c:if>
                                                                </c:otherwise>
                                                            </c:choose>
                                                        </ul>

                                                    </div>

                                                </div>
                                            </td>
                                        </tr>

                                    </c:forEach>
                                </c:if>

                                <c:if test="${QX.cha == 0 }">
                                    <tr>
                                        <td colspan="100" class="center">您无权查看</td>
                                    </tr>
                                </c:if>
                            </c:when>
                            <c:otherwise>
                                <tr class="main_info">
                                    <td colspan="100" class="center">没有相关数据</td>
                                </tr>
                            </c:otherwise>
                        </c:choose>


                        </tbody>
                    </table>

                    <div class="page-header position-relative">
                        <table style="width:100%;">
                            <tr>
                                <td style="vertical-align:top;">
                                    <c:if test="${QX.add == 1 }">
                                        <a class="btn btn-small btn-success" onclick="add();">新增</a>
                                    </c:if>
                                    <c:if test="${QX.FX_QX == 1 }">
                                        <a title="批量发送电子邮件" class="btn btn-small btn-info"
                                           onclick="makeAll('确定要给选中的用户发送邮件吗?');"><i class="icon-envelope-alt"></i></a>
                                    </c:if>
                                    <c:if test="${QX.FW_QX == 1 }">
                                        <a title="批量发送短信" class="btn btn-small btn-warning"
                                           onclick="makeAll('确定要给选中的用户发送短信吗?');"><i class="icon-envelope"></i></a>
                                    </c:if>
                                    <c:if test="${QX.del == 1 }">
                                        <a class="btn btn-small btn-danger" onclick="makeAll('确定要删除选中的数据吗?');"
                                           title="批量删除"><i class='icon-trash'></i></a>
                                    </c:if>
                                </td>
                                <td style="vertical-align:top;">
                                    <div class="pagination"
                                         style="float: right;padding-top: 0px;margin-top: 0px;">${page.pageStr}</div>
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
<script type="text/javascript">window.jQuery || document.write("<script src='static/js/jquery-1.9.1.min.js'>\x3C/script>");</script>
<script src="static/js/bootstrap.min.js"></script>
<script src="static/js/ace-elements.min.js"></script>
<script src="static/js/ace.min.js"></script>

<script type="text/javascript" src="static/js/chosen.jquery.min.js"></script>
<!-- 下拉框 -->
<script type="text/javascript" src="static/js/bootstrap-datepicker.min.js"></script>
<!-- 日期框 -->
<script type="text/javascript" src="static/js/bootbox.min.js"></script>
<!-- 确认窗口 -->
<!-- 引入 -->
<script type="text/javascript" src="static/js/jquery.tips.js"></script>
<!--提示框-->
<script type="text/javascript">

    $(top.hangge());

    //检索
    function search() {
        top.jzts();
        $("#userForm").submit();
    }

    //去发送电子邮件页面
    function sendEmail(EMAIL) {
        top.jzts();
        var diag = new top.Dialog();
        diag.Drag = true;
        diag.Title = "发送电子邮件";
        diag.URL = '<%=basePath%>config/toSendEmail?EMAIL=' + EMAIL + '&msg=appuser';
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
        diag.URL = '<%=basePath%>config/toSendSms?PHONE=' + phone + '&msg=appuser';
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
        diag.Title = "新增会员";
        diag.URL = '<%=basePath%>member/toAddU';
        diag.Width = 450;
        diag.Height = 355;
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
        diag.Title = "会员资料";
        diag.URL = '<%=basePath%>member/toEditU?USER_ID=' + user_id;
        diag.Width = 450;
        diag.Height = 355;
        diag.CancelEvent = function () {
            if (diag.innerFrame.contentWindow.document.getElementById('zhongxin').style.display == 'none') {
                nextPage(${page.currentPage});
            }
            diag.close();
        };
        diag.show();
    }

    //删除
    function delUser(userId, msg) {
        bootbox.confirm("确定要删除[" + msg + "]吗?", function (result) {
            if (result) {
                top.jzts();
                var url = "<%=basePath%>member/deleteU?USER_ID=" + userId + "&tm=" + new Date().getTime();
                $.get(url, function (data) {
                    nextPage(${page.currentPage});
                });
            }
        });
    }

</script>

<script type="text/javascript">

    $(function () {

        //下拉框
        $(".chzn-select").chosen();
        $(".chzn-select-deselect").chosen({allow_single_deselect: true});

        //日期框
        $('.date-picker').datepicker();

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
                            url: '<%=basePath%>member/deleteAllU?tm=' + new Date().getTime(),
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

    //导出excel
    function toExcel() {
        var USERNAME = $("#nav-search-input").val();
        var lastLoginStart = $("#lastLoginStart").val();
        var lastLoginEnd = $("#lastLoginEnd").val();
        var ROLE_ID = $("#role_id").val();
        var STATUS = $("#STATUS").val();
        window.location.href = '<%=basePath%>member/excel?USERNAME=' + USERNAME + '&lastLoginStart=' + lastLoginStart + '&lastLoginEnd=' + lastLoginEnd + '&ROLE_ID=' + ROLE_ID + '&STATUS=' + STATUS;
    }
</script>

</body>
</html>

