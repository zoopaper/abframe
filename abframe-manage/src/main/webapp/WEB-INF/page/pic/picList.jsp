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
    <!-- jsp文件头和头部 -->
    <%@ include file="../common/top.jsp" %>
    <!--查看图片插件 -->
    <link rel="stylesheet" media="screen" type="text/css" href="plugins/zoomimage/css/zoomimage.css"/>
    <link rel="stylesheet" media="screen" type="text/css" href="plugins/zoomimage/css/custom.css"/>
    <script type="text/javascript" src="plugins/zoomimage/js/jquery.js"></script>
    <script type="text/javascript" src="plugins/zoomimage/js/eye.js"></script>
    <script type="text/javascript" src="plugins/zoomimage/js/utils.js"></script>
    <script type="text/javascript" src="plugins/zoomimage/js/zoomimage.js"></script>
    <script type="text/javascript" src="plugins/zoomimage/js/layout.js"></script>
    <!--查看图片插件 -->
</head>
<body>
<div class="container-fluid" id="main-container">
    <div id="page-content" class="clearfix">
        <div class="row-fluid">
            <div class="row-fluid">
                <!-- 检索  -->
                <form action="pic/list" method="post" name="Form" id="Form">
                    <table>
                        <tr>
                            <td>
                                <span class="input-icon">
                                    <input autocomplete="off" id="nav-search-input" type="text" name="keyword"
                                           value="${pd.keyword}" placeholder="这里输入关键词"/>
                                    <i id="nav-search-icon" class="icon-search"></i>
                                </span>
                            </td>
                            <td style="vertical-align:top;">
                                <button class="btn btn-mini btn-light" onclick="search();" title="检索"><i
                                        id="nav-search-icon" class="icon-search"></i></button>
                            </td>
                            <c:if test="${QX.cha == 1 }">
                                <td style="vertical-align:top;">
                                    <a class="btn btn-mini btn-light" onclick="toExcel();" title="导出到EXCEL"><i
                                            id="nav-search-icon"
                                            class="icon-download-alt"></i></a>
                                </td>
                            </c:if>
                        </tr>
                    </table>
                    <!-- 检索  -->
                    <table id="table_report" class="table table-striped table-bordered table-hover">
                        <thead>
                        <tr>
                            <th class="center" onclick="selectAll()">
                                <label><input type="checkbox" id="zcheckbox"/><span class="lbl"></span></label>
                            </th>
                            <th>序号</th>
                            <th>图片</th>
                            <th>标题</th>
                            <th>ID</th>
                            <th>创建时间</th>
                            <th>属于</th>
                            <th>备注</th>
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
                                                <label><input type='checkbox' name='ids'
                                                              value="${var.PICTURES_ID}"/><span
                                                        class="lbl"></span></label>
                                            </td>
                                            <td class='center' style="width: 30px;">${vs.index+1}</td>
                                            <td>
                                                <a href="<%=basePath%>uploadFiles/uploadImgs/${var.PATH}"
                                                   title="${var.TITLE}" class="bwGal"><img
                                                        src="<%=basePath%>uploadFiles/uploadImgs/${var.PATH}"
                                                        alt="${var.TITLE}" width="100"></a>
                                            </td>
                                            <td>${var.TITLE}</td>
                                            <td>${var.PICTURES_ID}</td>
                                            <td>${var.CREATETIME}</td>
                                            <td>${var.MASTER_ID}</td>
                                            <td>${var.BZ}</td>
                                            <td style="width: 30px;" class="center">
                                                <c:if test="${QX.edit != 1 && QX.del != 1 }">
                                                    <span class="label label-large label-grey arrowed-in-right arrowed-in"><i
                                                            class="icon-lock" title="无权限"></i></span>
                                                </c:if>
                                                <div>
                                                    <c:if test="${QX.edit == 1 }">
                                                        <li><a style="cursor:pointer;" title="编辑"
                                                               onclick="edit('${var.PICTURES_ID}');"
                                                               class="tooltip-success" data-rel="tooltip" title=""
                                                               data-placement="left"><span class="green"><i
                                                                class="icon-edit"></i></span></a></li>
                                                    </c:if>
                                                    <c:if test="${QX.del == 1 }">
                                                        <li><a style="cursor:pointer;" title="删除"
                                                               onclick="del('${var.PICTURES_ID}','${var.PATH}');"
                                                               class="tooltip-error" data-rel="tooltip" title=""
                                                               data-placement="left"><span class="red"><i
                                                                class="icon-trash"></i></span> </a></li>
                                                    </c:if>
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
<!-- 引入 -->
<script type="text/javascript">window.jQuery || document.write("<script src='static/js/jquery-1.9.1.min.js'>\x3C/script>");</script>
<script src="static/js/bootstrap.min.js"></script>
<script src="static/js/ace-elements.min.js"></script>
<script src="static/js/ace.min.js"></script>
<!-- 引入 -->
<script type="text/javascript">

    $(top.hangge());

    //检索
    function search() {
        top.jzts();
        $("#Form").submit();
    }

    //新增
    function add() {
        top.jzts();
        var diag = new top.Dialog();
        diag.Drag = true;
        diag.Title = "新增";
        diag.URL = '<%=basePath%>pic/toAdd';
        diag.Width = 800;
        diag.Height = 490;
        diag.CancelEvent = function () {
            if ('${page.currentPage}' == '0') {
                top.jzts();
                setTimeout("self.location=self.location", 100);
            } else {
                nextPage(${page.currentPage});
            }
            diag.close();
        };
        diag.show();
    }

    //删除
    function del(Id, PATH) {

        if (confirm("确定要删除?")) {
            top.jzts();
            var url = "<%=basePath%>pic/delete?PICTURES_ID=" + Id + "&PATH=" + PATH + "&tm=" + new Date().getTime();
            $.get(url, function (data) {
                nextPage(${page.currentPage});
            });
        }
    }

    //修改
    function edit(Id) {
        top.jzts();
        var diag = new top.Dialog();
        diag.Drag = true;
        diag.Title = "编辑";
        diag.URL = '<%=basePath%>pic/toEdit?PICTURES_ID=' + Id;
        diag.Width = 600;
        diag.Height = 465;
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

    //全选 （是/否）
    function selectAll() {
        var checklist = document.getElementsByName("ids");
        if (document.getElementById("zcheckbox").checked) {
            for (var i = 0; i < checklist.length; i++) {
                checklist[i].checked = 1;
            }
        } else {
            for (var j = 0; j < checklist.length; j++) {
                checklist[j].checked = 0;
            }
        }
    }


    //批量操作
    function makeAll(msg) {

        if (confirm(msg)) {

            var str = '';
            for (var i = 0; i < document.getElementsByName('ids').length; i++) {
                if (document.getElementsByName('ids')[i].checked) {
                    if (str == '') str += document.getElementsByName('ids')[i].value;
                    else str += ',' + document.getElementsByName('ids')[i].value;
                }
            }
            if (str == '') {
                alert("您没有选择任何内容!");
                return;
            } else {
                if (msg == '确定要删除选中的数据吗?') {
                    top.jzts();
                    $.ajax({
                        type: "POST",
                        url: '<%=basePath%>pic/deleteAll?tm=' + new Date().getTime(),
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
    }

    //导出excel
    function toExcel() {
        window.location.href = '<%=basePath%>pic/excel';
    }
</script>
<style type="text/css">
    li {
        list-style-type: none;
    }
</style>
<ul class="navigationTabs">
    <li><a></a></li>
    <li></li>
</ul>
</body>
</html>

