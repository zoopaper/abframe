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
    <script type="text/javascript" src="/static/js/bootbox.min.js"></script>
    <!-- 确认窗口 -->

    <script type="text/javascript">
        $(top.hangge());

        function add(parentId) {
            top.jzts();
            var diag = new top.Dialog();
            diag.Drag = true;
            diag.Title = "新增";
            diag.URL = '/dict/toAdd?parentId=' + parentId;
            diag.Width = 223;
            diag.Height = 175;
            diag.CancelEvent = function () {
                if (diag.innerFrame.contentWindow.document.getElementById('zhongxin').style.display == 'none') {
                    var num = '${page.currentPage}';
                    if (num == '0') {
                        top.jzts();
                        location.href = location.href;
                    } else {
                        nextPage(${page.currentPage});
                    }
                }
                diag.close();
            };
            diag.show();
        }


        function edit(id) {
            top.jzts();
            var diag = new top.Dialog();
            diag.Drag = true;
            diag.Title = "编辑";
            diag.URL = '/dict/toEdit?id=' + id;
            diag.Width = 223;
            diag.Height = 175;
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
            var flag = false;
            if (confirm("确定要删除该数据吗?")) {
                flag = true;
            }
            if (flag) {
                top.jzts();
                var url = "/dict/del?id=" + id + "&guid=" + new Date().getTime();
                $.get(url, function (data) {
                    if ("success" == data.result) {
                        top.jzts();
                        nextPage(${page.currentPage});
                    } else {
                        top.hangge();
                        alert("删除失败，请先删除其下级数据!");
                    }
                });
            }
        }

    </script>
</head>

<body>
<div id="page-content" class="clearfix">
    <div class="row-fluid">

        <!-- 检索  -->
        <form action="/dict" method="post" name="userForm" id="userForm">
            <input name="parentId" id="parentId" type="hidden" value="${pd.parentId }"/>
            <table>
                <tr>
                    <td><font color="#808080">检索：</font></td>
                    <td><input type="text" name="name" value="${pd.name}" placeholder="输入名称" style="width:130px;"/>
                    </td>
                    <td style="vertical-align:top;">
                        <button class="btn btn-mini btn-light" onclick="search();">
                            <i id="nav-search-icon" class="icon-search"></i>
                        </button>
                    </td>
                    <c:if test="${pd.parentId != '0'}">
                        <c:choose>
                            <c:when test="${not empty varsList}">
                                <td style="vertical-align:top;">
                                    <a href="/dict?parentId=0" class="btn btn-mini btn-purple" title="查看">顶级
                                        <i class="icon-arrow-right  icon-on-right"></i>
                                    </a>
                                </td>
                                <c:forEach items="${varsList}" var="var" varStatus="vsd">
                                    <td style="vertical-align:top;">
                                        <a href="/dict?parentId=${var.id}"
                                           class="btn btn-mini btn-purple" title="查看">${var.name }
                                            <i class="icon-arrow-right  icon-on-right"></i>
                                        </a>
                                    </td>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                            </c:otherwise>
                        </c:choose>
                    </c:if>

                </tr>
            </table>
            <!-- 检索  -->

            <table id="table_report" class="table table-striped table-bordered table-hover">
                <thead>
                <tr>
                    <th class="center" style="width: 50px;">序号</th>
                    <th class='center'>名称</th>
                    <th class='center'>编码</th>
                    <th class='center'>级别</th>
                    <th class='center'>操作</th>
                </tr>
                </thead>
                <c:choose>
                    <c:when test="${not empty varList}">
                        <c:forEach items="${varList}" var="var" varStatus="vs">
                            <tr>
                                <td class="center">${var.orderId}</td>
                                <td class='center'>
                                    <a href="/dict?parentId=${var.id}" title="查看下级">
                                        <i class="icon-arrow-right  icon-on-right"></i>&nbsp;${var.name}
                                    </a>
                                </td>
                                <td class='center'>${var.parentCode}</td>
                                <td class='center' style="width:35px;"><b class="green">${var.level}</b></td>
                                <td style="width: 68px;">
                                    <a class='btn btn-mini btn-info' title="编辑" onclick="edit('${var.id}')">
                                        <i class='icon-edit'></i>
                                    </a>
                                    <a class='btn btn-mini btn-danger' title="删除" onclick="del('${var.id}')">
                                        <i class='icon-trash'></i>
                                    </a>
                            </tr>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <tr>
                            <td colspan="100" class="center">没有相关数据</td>
                        </tr>
                    </c:otherwise>
                </c:choose>
            </table>

            <div class="page-header position-relative">
                <table style="width:100%;">
                    <tr>
                        <td style="vertical-align:top;width:50px;">
                            <a class="btn btn-small btn-success" onclick="add('${pd.parentId}');">新增</a>
                        </td>
                        <c:if test="${pd.parentId != '0'}">
                            <td style="vertical-align:top;" class="left">
                                <a class="btn btn-small btn-info"
                                   onclick="location.href='/dict?parentId=${pdp.parentId }';">返回
                                </a>
                            </td>
                        </c:if>
                        <td style="vertical-align:top;">
                            <div class="pagination"
                                 style="float: right;padding-top: 0px;margin-top: 0px;">${page.pageStr}
                            </div>
                        </td>
                    </tr>
                </table>
            </div>
        </form>
    </div>
</div>

</body>
</html>