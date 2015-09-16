<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

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
                <form action="/company/companyList" method="post" name="companyForm" id="companyForm">
                    <table border='0'>
                        <tr>
                            <td>
                                <span class="input-icon">
                                    <input autocomplete="off" id="nav-search-input" type="text" name="companyName"
                                           value="${pd.companyName}" placeholder="输入公司名称"/>
                                    <i id="nav-search-icon" class="icon-search"></i>
                                </span>
                            </td>
                            <td style="vertical-align:top;">
                                <button class="btn btn-mini btn-light" onclick="search();">
                                    <i id="nav-search-icon" class="icon-search"></i>
                                </button>
                            </td>
                        </tr>
                    </table>
                    <!-- 检索  -->
                    <table id="table_report" class="table table-striped table-bordered table-hover">
                        <thead>
                        <tr>
                            <th>公司名称</th>
                            <th>领域</th>
                            <th>位置</th>
                            <th>规模</th>
                            <th>目前阶段</th>
                            <%--<th class="center">操作</th>--%>
                        </tr>
                        </thead>
                        <tbody>
                        <c:choose>
                            <c:when test="${not empty companyList}">
                                <c:forEach items="${companyList}" var="company" varStatus="vs">
                                    <tr>
                                        <td>
                                            <a href="${company.site}" target="_blank">${fn:substring(company.companyName ,0,15)}</a>
                                        </td>
                                        <td>${company.industry}</td>
                                        <td>${company.location}</td>
                                        <td>${company.scale}</td>
                                        <td>${company.financing}</td>
                                    </tr>
                                </c:forEach>
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
                                    <a class="btn btn-small btn-success" onclick="add();">新增</a>
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
<script type="text/javascript">window.jQuery || document.write("<script src='/static/js/jquery-1.7.2.js'>\x3C/script>");</script>
<script src="/static/js/bootstrap.min.js"></script>
<script src="/static/js/ace-elements.min.js"></script>
<script src="/static/js/ace.min.js"></script>

<script type="text/javascript" src="/static/js/chosen.jquery.min.js"></script>
<!-- 单选框 -->
<script type="text/javascript" src="/static/js/bootstrap-datepicker.min.js"></script>
<!-- 日期框 -->
<script type="text/javascript" src="/static/js/bootbox.min.js"></script>
<!-- 确认窗口 -->
<!-- 引入 -->

<script type="text/javascript">

    $(top.hangge());

    //检索
    function search() {
        $("#companyForm").submit();
    }

    //新增
    function add() {
        top.jzts();
        var diag = new top.Dialog();
        diag.Drag = true;
        diag.Title = "新增";
        diag.URL = '/company/toAdd';
        diag.Width = 800;
        diag.Height = 650;
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

    //修改
    function edit(id) {
        top.jzts();
        var diag = new top.Dialog();
        diag.Drag = true;
        diag.Title = "编辑";
        diag.URL = '/company/toEdit?id=' + id;
        diag.Width = 800;
        diag.Height = 650;
        diag.CancelEvent = function () {
            if (diag.innerFrame.contentWindow.document.getElementById('zhongxin').style.display == 'none') {
                nextPage(${page.currentPage});
            }
            diag.close();
        };
        diag.show();
    }

    //删除
    function del(id) {
        bootbox.confirm("确定要删除该记录?", function (result) {
            if (result) {
                var url = "/company/delete?id=" + id + "&tm=" + new Date().getTime();
                $.get(url, function (data) {
                    if (data == "success") {
                        nextPage(${page.currentPage});
                    }
                });
            }
        });
    }

</script>

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

