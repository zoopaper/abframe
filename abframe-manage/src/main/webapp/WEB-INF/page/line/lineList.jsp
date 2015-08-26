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
                <form action="line/list" method="post" name="Form" id="Form">
                    <input type="hidden" name="PARENT_ID" id="PARENT_ID" value="${pd.PARENT_ID}"/>
                    <table>
                        <tr>
                            <td>
                                <span class="input-icon">
                                    <input autocomplete="off" id="nav-search-input" type="text" name="field1" value=""
                                           placeholder="这里输入名称"/>
                                    <i id="nav-search-icon" class="icon-search"></i>
                                </span>
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
                            <th>名称</th>
                            <th>链接</th>
                            <th>线路</th>
                            <th>类型</th>
                            <th>排序</th>
                            <th class="center">操作</th>
                        </tr>
                        </thead>
                        <tbody>
                        <!-- 开始循环 -->
                        <c:choose>
                            <c:when test="${not empty varList}">
                                <c:if test="${QX.cha == 1 }">
                                    <c:forEach items="${varList}" var="var" varStatus="vs">
                                        <tr>
                                            <td class='center' style="width: 30px;">
                                                <label><input type='checkbox' name='ids' value="${var.LINE_ID}"/><span
                                                        class="lbl"></span></label>
                                            </td>
                                            <td class='center' style="width: 30px;">${vs.index+1}</td>
                                            <c:if test="${pd.PARENT_ID == '0'}">
                                                <td><a href="<%=basePath%>/line/list.do?PARENT_ID=${var.LINE_ID }"
                                                       title="查看下级"><i class="icon-arrow-right  icon-on-right"></i>&nbsp;${var.TITLE }
                                                </a></td>
                                            </c:if>
                                            <c:if test="${pd.PARENT_ID != '0'}">
                                                <td>${var.TITLE}</td>
                                            </c:if>
                                            <td>${var.LINE_URL}</td>

                                            <c:if test="${pd.PARENT_ID == '0'}">
                                                <td><a href="<%=basePath%>/line/list.do?PARENT_ID=${var.LINE_ID }"
                                                       title="查看线路"><i class="icon-arrow-right  icon-on-right"></i>&nbsp;查看线路</a>&nbsp;(${var.LINE_COUNT }条线路)
                                                </td>
                                            </c:if>
                                            <c:if test="${pd.PARENT_ID != '0'}">
                                                <td>${var.LINE_ROAD}</td>
                                            </c:if>
                                            <td>${var.TYPE}</td>
                                            <td>${var.LINE_ORDER}</td>
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
                                                            <c:if test="${QX.edit == 1 }">
                                                                <li><a style="cursor:pointer;" title="编辑"
                                                                       onclick="edit('${var.LINE_ID}');"
                                                                       class="tooltip-success" data-rel="tooltip"
                                                                       title="" data-placement="left"><span
                                                                        class="green"><i
                                                                        class="icon-edit"></i></span></a></li>
                                                            </c:if>
                                                            <c:if test="${QX.del == 1 }">
                                                                <li><a style="cursor:pointer;" title="删除"
                                                                       onclick="del('${var.LINE_ID}');"
                                                                       class="tooltip-error" data-rel="tooltip" title=""
                                                                       data-placement="left"><span class="red"><i
                                                                        class="icon-trash"></i></span> </a></li>
                                                            </c:if>
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
                                        <a class="btn btn-small btn-success" onclick="add('${pd.PARENT_ID}');">新增</a>
                                    </c:if>
                                    <c:if test="${QX.del == 1 }">
                                        <a class="btn btn-small btn-danger" onclick="makeAll('确定要删除选中的数据吗?');"
                                           title="批量删除"><i class='icon-trash'></i></a>
                                    </c:if>
                                    <c:if test="${pd.PARENT_ID != '0'}">
                                        <a class="btn btn-small btn-info"
                                           onclick="location.href='<%=basePath%>/line/list.do?PARENT_ID=0';">返回</a>
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
        $("#Form").submit();
    }

    //新增
    function add(PARENT_ID) {
        top.jzts();
        var diag = new top.Dialog();
        diag.Drag = true;
        diag.Title = "新增";
        diag.URL = '<%=basePath%>/line/toAdd?PARENT_ID=' + PARENT_ID;
        diag.Width = 400;
        diag.Height = 355;
        diag.CancelEvent = function () {
            if (diag.innerFrame.contentWindow.document.getElementById('zhongxin').style.display == 'none') {
                if ('${page.currentPage}' == '0') {
                    top.jzts();
                    setTimeout("self.location.reload()", 100);
                } else {
                    nextPage(${page.currentPage});
                }
            }
            diag.close();
        };
        diag.show();
    }

    //删除
    function del(Id) {
        bootbox.confirm("确定要删除吗?", function (result) {
            if (result) {
                var url = "<%=basePath%>/line/delete?LINE_ID=" + Id + "&tm=" + new Date().getTime();
                $.get(url, function (data) {
                    if (data == "success") {
                        nextPage(${page.currentPage});
                    }
                });
            }
        });
    }

    //修改
    function edit(Id) {
        top.jzts();
        var diag = new top.Dialog();
        diag.Drag = true;
        diag.Title = "编辑";
        diag.URL = '<%=basePath%>/line/toEdit?LINE_ID=' + Id;
        diag.Width = 400;
        diag.Height = 355;
        diag.CancelEvent = function () {
            if (diag.innerFrame.contentWindow.document.getElementById('zhongxin').style.display == 'none') {
                nextPage(${page.currentPage});
            }
            diag.close();
        };
        diag.show();
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
                    if (msg == '确定要删除选中的数据吗?') {
                        $.ajax({
                            type: "POST",
                            url: '<%=basePath%>/line/deleteAll?tm=' + new Date().getTime(),
                            data: {DATA_IDS: str},
                            dataType: 'json',
                            //beforeSend: validateData,
                            cache: false,
                            success: function (data) {
                                $.each(data.list, function (i, list) {
                                    nextPage(${page.currentPage});
                                });
                            }
                        });
                    }
                }
            }
        });
    }

    //导出excel
    function toExcel() {
        window.location.href = '<%=basePath%>/line/excel';
    }
</script>

</body>
</html>

