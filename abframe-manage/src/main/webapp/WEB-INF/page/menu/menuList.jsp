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
    <title></title>
    <meta name="description" content="overview & stats"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <link href="static/css/bootstrap.min.css" rel="stylesheet"/>
    <link href="static/css/bootstrap-responsive.min.css" rel="stylesheet"/>
    <link rel="stylesheet" href="static/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="static/css/ace.min.css"/>
    <link rel="stylesheet" href="static/css/ace-responsive.min.css"/>
    <link rel="stylesheet" href="static/css/ace-skins.min.css"/>
    <script type="text/javascript" src="static/js/jquery-1.9.1.min.js"></script>

    <script type="text/javascript">
        $(top.hangge());
        //新增
        function addmenu() {
            top.jzts();
            var diag = new top.Dialog();
            diag.Drag = true;
            diag.Title = "新增菜单";
            diag.URL = '<%=basePath%>menu/toAdd';
            diag.Width = 223;
            diag.Height = 256;
            diag.CancelEvent = function () {
                if (diag.innerFrame.contentWindow.document.getElementById('zhongxin').style.display == 'none') {
                    top.jzts();
                    setTimeout("location.reload()", 100);
                }
                diag.close();
            };
            diag.show();
        }

        //修改
        function editmenu(menuId) {
            top.jzts();
            var diag = new top.Dialog();
            diag.Drag = true;
            diag.Title = "编辑菜单";
            diag.URL = '<%=basePath%>menu/toEdit?menuId=' + menuId;
            diag.Width = 223;
            diag.Height = 256;
            diag.CancelEvent = function () {
                if (diag.innerFrame.contentWindow.document.getElementById('zhongxin').style.display == 'none') {
                    top.jzts();
                    setTimeout("location.reload()", 100);
                }
                diag.close();
            };
            diag.show();
        }

        //编辑顶部菜单图标
        function editTb(menuId) {
            top.jzts();
            var diag = new top.Dialog();
            diag.Drag = true;
            diag.Title = "编辑图标";
            diag.URL = '<%=basePath%>menu/toEditIcon?menuId=' + menuId;
            diag.Width = 530;
            diag.Height = 150;
            diag.CancelEvent = function () {
                if (diag.innerFrame.contentWindow.document.getElementById('zhongxin').style.display == 'none') {
                    top.jzts();
                    setTimeout("location.reload()", 100);
                }
                diag.close();
            };
            diag.show();
        }

        function delmenu(menuId, isParent) {
            var flag = false;
            if (isParent) {
                if (confirm("确定要删除该菜单吗？其下子菜单将一并删除！")) {
                    flag = true;
                }
            } else {
                if (confirm("确定要删除该菜单吗？")) {
                    flag = true;
                }
            }
            if (flag) {
                top.jzts();
                var url = "<%=basePath%>menu/del?menuId=" + menuId + "&guid=" + new Date().getTime();
                $.get(url, function (data) {
                    top.jzts();
                    document.location.reload();
                });
            }
        }

        function openClose(menuId, curObj, trIndex) {
            var txt = $(curObj).text();
            if (txt == "展开") {
                $(curObj).text("折叠");
                $("#tr" + menuId).after("<tr id='tempTr" + menuId + "'><td colspan='5'>数据载入中</td></tr>");
                if (trIndex % 2 == 0) {
                    $("#tempTr" + menuId).addClass("main_table_even");
                }
                var url = "<%=basePath%>menu/sub?menuId=" + menuId + "&guid=" + new Date().getTime();
                $.get(url, function (data) {
                    if (data.length > 0) {
                        var html = "";
                        $.each(data, function (i) {
                            html = "<tr style='height:24px;line-height:24px;' name='subTr" + menuId + "'>";
                            html += "<td></td>";
                            html += "<td><span style='width:80px;display:inline-block;'></span>";
                            if (i == data.length - 1)
                                html += "<img src='static/images/joinbottom.gif' style='vertical-align: middle;'/>";
                            else
                                html += "<img src='static/images/join.gif' style='vertical-align: middle;'/>";
                            html += "<span style='width:100px;text-align:left;display:inline-block;'>" + this.menuName + "</span>";
                            html += "</td>";
                            html += "<td>" + this.menuUrl + "</td>";
                            html += "<td class='center'>" + this.sort + "</td>";
                            html += "<td><a class='btn btn-mini btn-info' title='编辑' onclick='editmenu(\"" + this.id + "\")'><i class='icon-edit'></i></a> <a class='btn btn-mini btn-danger' title='删除' onclick='delmenu(\"" +
                                    this.id + "\",false)'><i class='icon-trash'></i></a></td>";
                            html += "</tr>";
                            $("#tempTr" + menuId).before(html);
                        });
                        $("#tempTr" + menuId).remove();
                        if (trIndex % 2 == 0) {
                            $("tr[name='subTr" + menuId + "']").addClass("main_table_even");
                        }
                    } else {
                        $("#tempTr" + menuId + " > td").html("没有相关数据");
                    }
                }, "json");
            } else {
                $("#tempTr" + menuId).remove();
                $("tr[name='subTr" + menuId + "']").remove();
                $(curObj).text("展开");
            }
        }
    </script>
</head>

<body>
<table id="table_report" class="table table-striped table-bordered table-hover">
    <thead>
    <tr>
        <th class="center" style="width: 50px;">序号</th>
        <th class='center'>名称</th>
        <th class='center'>资源路径</th>
        <th class='center'>排序</th>
        <th class='center'>操作</th>
    </tr>
    </thead>
    <c:choose>
        <c:when test="${not empty menuList}">
            <c:forEach items="${menuList}" var="menu" varStatus="vs">
                <tr id="tr${menu.id}">
                    <td class="center">${vs.index+1}</td>
                    <td class='center'>
                        <i class="${menu.menuIcon}">&nbsp;</i>${menu.menuName}&nbsp;
                        <c:if test="${menu.menuType == '1'}">
                            <span class="label label-success arrowed">系统</span>
                        </c:if>
                        <c:if test="${menu.menuType != '1'}">
                            <span class="label label-important arrowed-in">业务</span>
                        </c:if>
                    </td>
                    <td>${menu.menuUrl == '#'? '': menu.menuUrl}</td>
                    <td class='center'>${menu.sort}</td>
                    <td style="width: 25%;">
                        <a class='btn btn-mini btn-warning' onclick="openClose('${menu.id}',this,${vs.index})">展开</a>
                        <a class='btn btn-mini btn-purple' title="图标" onclick="editTb('${menu.id}')"><i class='icon-picture'></i></a>
                        <a class='btn btn-mini btn-info' title="编辑" onclick="editmenu('${menu.id}')"><i class='icon-edit'></i></a>
                        <a class='btn btn-mini btn-danger' title="删除" onclick="delmenu('${menu.id}',true)"><i class='icon-trash'></i></a>
                </tr>
            </c:forEach>
        </c:when>
        <c:otherwise>
            <tr>
                <td colspan="100">没有相关数据</td>
            </tr>
        </c:otherwise>
    </c:choose>
</table>

<div class="page_and_btn">
    <div>
        &nbsp;&nbsp;<a class="btn btn-small btn-success" onclick="addmenu();">新 增</a>
    </div>
</div>

</body>
</html>