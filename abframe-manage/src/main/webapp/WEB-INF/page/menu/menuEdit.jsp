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
    <meta charset="utf-8"/>
    <title>菜单</title>
    <meta name="description" content="overview & stats"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <link href="static/css/bootstrap.min.css" rel="stylesheet"/>
    <link href="static/css/bootstrap-responsive.min.css" rel="stylesheet"/>
    <link rel="stylesheet" href="static/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="static/css/ace.min.css"/>
    <link rel="stylesheet" href="static/css/ace-responsive.min.css"/>
    <link rel="stylesheet" href="static/css/ace-skins.min.css"/>
    <script type="text/javascript" src="static/js/jquery-1.9.1.min.js"></script>
    <!--提示框-->
    <script type="text/javascript" src="static/js/jquery.tips.js"></script>

    <script type="text/javascript">
        $(top.hangge());
        $(document).ready(function () {
            if ($("#menuId").val() != "") {
                var parentId = $("#pId").val();
                if (parentId == "0") {
                    $("#parentId").attr("disabled", true);
                } else {
                    $("#parentId").val(parentId);
                    $("#form-field-radio1").attr("disabled", true);
                    $("#form-field-radio2").attr("disabled", true);
                    $("#form-field-radio1").attr("checked", false);
                    $("#form-field-radio2").attr("checked", false);
                }
            }
        });

        var menuUrl = "";
        function setMUR() {
            menuUrly = $("#menuUrl").val();
            if (menuUrly != '') {
                menuUrl = menuUrly;
            }
            if ($("#parentId").val() == "0") {
                $("#menuUrl").attr("readonly", true);
                $("#menuUrl").val("");
                $("#form-field-radio1").attr("disabled", false);
                $("#form-field-radio2").attr("disabled", false);
            } else {
                $("#menuUrl").attr("readonly", false);
                $("#menuUrl").val(menuUrl);
                $("#form-field-radio1").attr("disabled", true);
                $("#form-field-radio2").attr("disabled", true);
                $("#form-field-radio1").attr("checked", false);
                $("#form-field-radio2").attr("checked", false);
            }
        }

        //保存
        function save() {
            if ($("#menuName").val() == "") {

                $("#menuName").tips({
                    side: 3,
                    msg: '请输入菜单名称',
                    bg: '#AE81FF',
                    time: 2
                });

                $("#menuName").focus();
                return false;
            }
            if ($("#menuUrl").val() == "") {
                $("#menuUrl").val('#');
            }
            if ($("#menuOrder").val() == "") {

                $("#menuOrder").tips({
                    side: 1,
                    msg: '请输入菜单序号',
                    bg: '#AE81FF',
                    time: 2
                });

                $("#menuOrder").focus();
                return false;
            }

            if (isNaN(Number($("#menuOrder").val()))) {

                $("#menuOrder").tips({
                    side: 1,
                    msg: '请输入菜单序号',
                    bg: '#AE81FF',
                    time: 2
                });

                $("#menuOrder").focus();
                $("#menuOrder").val(1);
                return false;
            }

            $("#menuForm").submit();
            $("#zhongxin").hide();
            $("#zhongxin2").show();
        }

        function setType(value) {
            $("#menuType").val(value);
        }
    </script>
    <style type="text/css">
        .s-input {
            font-size: 11pt;
            color: #9C9C9C;
        }
    </style>
</head>

<body>
<form action="menu/edit" name="menuForm" id="menuForm" method="post">
    <input type="hidden" name="id" id="menuId" value="${menu.id}"/>
    <%--<input type="hidden" name="pId" id="pId" value="${menu.parentId}"/>--%>
    <input type="hidden" name="menuType" id="menuType" value="${menu.menuType}"/>

    <div id="zhongxin" style="margin-top: 10px;margin-left: 15px;">
        <table>
            <tr>
                <td>
                    <span class="s-input">上级菜单：</span><select name="parentId" id="parentId" onchange="setMUR()" title="菜单">
                    <option value="0">顶级菜单</option>
                    <c:forEach items="${menuList}" var="menu">
                        <option value="${menu.id}">${menu.menuName}</option>
                    </c:forEach>
                </select>
                </td>
            </tr>
            <tr>
                <td>
                    <span class="s-input">菜单名称：</span><input type="text" name="menuName" id="menuName" placeholder="输入菜单名称" value="${menu.menuName}" title="名称"/>
                </td>
            </tr>
            <tr>
                <td>
                    <span class="s-input">URL地址：</span><input type="text" name="menuUrl" id="menuUrl" placeholder="输入URL" value="${menu.menuUrl}" title="URL"/>
                </td>
            </tr>
            <tr>
                <td>
                    <span class="s-input">菜单排序：</span><input type="number" name="sort" id="menuOrder" placeholder="输入序号" value="${menu.sort}" title="序号"/>
                </td>
            </tr>
            <%--<tr>--%>
            <%--<td style="text-align: center;">--%>
            <%--<label style="float:left;padding-left: 32px;">--%>
            <%--<input name="form-field-radio" id="form-field-radio1" onclick="setType('1');"--%>
            <%--<c:if test="${menu.menuType == '1' }">checked="checked"</c:if>--%>
            <%--type="radio" value="icon-updateRoleById">--%>
            <%--<span class="lbl">系统菜单</span>--%>
            <%--</label>--%>
            <%--<label style="float:left;padding-left: 5px;">--%>
            <%--<input name="form-field-radio" id="form-field-radio2" onclick="setType('2');"--%>
            <%--<c:if test="${menu.menuType != '1' }">checked="checked"</c:if> type="radio" value="icon-updateRoleById">--%>
            <%--<span class="lbl">业务菜单</span></label>--%>
            <%--</td>--%>
            <%--</tr>--%>
            <tr>
                <td style="text-align: center; padding-top: 10px;">
                    <a class="btn  btn-primary" onclick="save();">保存</a>
                    <a class="btn  btn-danger" onclick="top.Dialog.close();">取消</a>
                </td>
            </tr>
        </table>
    </div>
    <div id="zhongxin2" class="center" style="display:none">
        <br/><br/><br/>
        <img src="static/images/jiazai.gif"/><br/>
        <h4 class="lighter block green"></h4>
    </div>
</form>
</body>
</html>