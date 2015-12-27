<%@ include file="../common/common.jsp" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <%@ include file="../common/top.jsp" %>
</head>
<body>

<div class="container-fluid" id="main-container">
    <div id="page-content">
        <div class="row-fluid">
            <div class="row-fluid">
                <table id="table_report" class="table table-striped table-bordered table-hover">
                    <thead>
                    <tr>
                        <th class="center">序号</th>
                        <th>角色</th>
                        <th style="width:155px;" class="center">操作</th>
                    </tr>
                    </thead>
                    <c:choose>
                        <c:when test="${not empty roleList}">
                            <c:forEach items="${roleList}" var="role" varStatus="vs">
                                <tr>
                                    <td class='center' style="width:30px;">${vs.index+1}</td>
                                    <td id="ROLE_NAMETd${role.id}">${role.roleName}</td>
                                    <td style="width:155px;">
                                        <a class="btn btn-mini btn-purple" onclick="editRights('${role.id}');">
                                            <i class="icon-pencil"></i>菜单权限
                                        </a>
                                        <a class='btn btn-mini btn-info' title="编辑" onclick="editRole('${role.id}');">
                                            <i class='icon-edit'></i>
                                        </a>
                                        <c:choose>
                                        <c:when test="${role.id == '2' or role.id == '1'}">
                                        </c:when>
                                        <c:otherwise>
                                        <a class='btn btn-mini btn-danger' title="删除" onclick="delRole('${role.id}','c','${role.roleName}');">
                                            <i class='icon-trash'></i>
                                        </a>
                                        </c:otherwise>
                                        </c:choose>
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
                            <td style="vertical-align:top;">
                                <a class="btn btn-success" onclick="addRole();">
                                    新增
                                </a>
                            </td>
                        </tr>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>

<a href="#" id="btn-scroll-up" class="btn btn-small btn-inverse">
    <i class="icon-double-angle-up icon-only"></i>
</a>

<script src="/static/js/jquery-1.9.1.min.js"></script>
<script type="text/javascript">window.jQuery || document.write("<script src='/static/js/jquery-1.9.1.min.js'>\x3C/script>");</script>
<script src="/static/js/bootstrap.min.js"></script>
<script src="/static/js/ace-elements.min.js"></script>
<script src="/static/js/ace.min.js"></script>

<script type="text/javascript" src="/static/js/bootbox.min.js"></script>
<script type="text/javascript">

    top.hangge();
    function addRole() {
        top.jzts();
        var diag = new top.Dialog();
        diag.Drag = true;
        diag.Title = "新增角色";
        diag.URL = '/role/toAdd';
        diag.Width = 330;
        diag.Height = 100;
        diag.CancelEvent = function () {
            if (diag.innerFrame.contentWindow.document.getElementById('zhongxin').style.display == 'none') {
                top.jzts();
                setTimeout("self.location.reload()", 100);
            }
            diag.close();
        };
        diag.show();
    }

    function editRole(id) {
        top.jzts();
        var diag = new top.Dialog();
        diag.Drag = true;
        diag.Title = "编辑";
        diag.URL = '/role/toEdit?roleId=' + id;
        diag.Width = 250;
        diag.Height = 90;
        diag.CancelEvent = function () {
            if (diag.innerFrame.contentWindow.document.getElementById('zhongxin').style.display == 'none') {
                top.jzts();
                setTimeout("self.location.reload()", 100);
            }
            diag.close();
        };
        diag.show();
    }

    //删除
    function delRole(id, msg, name) {
        bootbox.confirm("确定要删除[" + name + "]吗?", function (result) {
            if (result) {
                var url = "/role/delete?roleId=" + id + "&guid=" + new Date().getTime();
                top.jzts();
                $.get(url, function (data) {
                    if ("success" == data.result) {
                        if (msg == 'c') {
                            top.jzts();
                            document.location.reload();
                        } else {
                            top.jzts();
                            window.location.href = "role";
                        }

                    } else if ("false" == data.result) {
                        top.hangge();
                        bootbox.dialog("删除失败，请先删除此部门下的职位!",
                                [
                                    {
                                        "label": "关闭",
                                        "class": "btn-small btn-success",
                                        "callback": function () {
                                            //Example.show("great success");
                                        }
                                    }]
                        );
                    } else if ("false2" == data.result) {
                        top.hangge();
                        bootbox.dialog("删除失败，请先删除此职位下的用户!",
                                [
                                    {
                                        "label": "关闭",
                                        "class": "btn-small btn-success",
                                        "callback": function () {
                                            //Example.show("great success");
                                        }
                                    }]
                        );
                    }
                });
            }
        });
    }

</script>

<script type="text/javascript">

    //菜单权限
    function editRights(id) {
        top.jzts();
        var diag = new top.Dialog();
        diag.Drag = true;
        diag.Title = "菜单权限";
        diag.URL = '/role/auth?id=' + id;
        diag.Width = 280;
        diag.Height = 370;
        diag.CancelEvent = function () {
            diag.close();
        };
        diag.show();
    }

    //按钮权限
    function roleButton(id, msg) {
        top.jzts();
        if (msg == 'add_qx') {
            var Title = "授权新增权限";
        } else if (msg == 'del_qx') {
            Title = "授权删除权限";
        } else if (msg == 'edit_qx') {
            Title = "授权修改权限";
        } else if (msg == 'cha_qx') {
            Title = "授权查看权限";
        }

        var diag = new top.Dialog();
        diag.Drag = true;
        diag.Title = Title;
        diag.URL = '/role/button?id=' + id + '&msg=' + msg;
        diag.Width = 200;
        diag.Height = 370;
        diag.CancelEvent = function () {
            diag.close();
        };
        diag.show();
    }

</script>
</body>
</html>

