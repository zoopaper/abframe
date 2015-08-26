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
    <link rel="stylesheet" href="static/css/chosen.css"/>

    <link rel="stylesheet" href="static/css/ace.min.css"/>
    <link rel="stylesheet" href="static/css/ace-responsive.min.css"/>
    <link rel="stylesheet" href="static/css/ace-skins.min.css"/>

    <link rel="stylesheet" href="static/css/datepicker.css"/>
    <!-- 日期框 -->
    <script type="text/javascript" src="static/js/jquery-1.7.2.js"></script>
    <script type="text/javascript" src="static/js/jquery.tips.js"></script>

    <script type="text/javascript">
        //保存
        function save() {
            if ($("#TITLE").val() == "") {
                $("#TITLE").tips({
                    side: 3,
                    msg: '请输入名称',
                    bg: '#AE81FF',
                    time: 2
                });
                $("#TITLE").focus();
                return false;
            }
            if ($("#LINE_URL").val() == "") {
                $("#LINE_URL").tips({
                    side: 3,
                    msg: '请输入链接',
                    bg: '#AE81FF',
                    time: 2
                });
                $("#LINE_URL").focus();
                return false;
            }
            if ($("#LINE_ROAD").val() == "") {
                $("#LINE_ROAD").tips({
                    side: 3,
                    msg: '请输入线路',
                    bg: '#AE81FF',
                    time: 2
                });
                $("#LINE_ROAD").focus();
                return false;
            }
            if ($("#TYPE").val() == "") {
                $("#TYPE").tips({
                    side: 3,
                    msg: '请输入类型',
                    bg: '#AE81FF',
                    time: 2
                });
                $("#TYPE").focus();
                return false;
            }
            if ($("#LINE_ORDER").val() == "") {
                $("#LINE_ORDER").tips({
                    side: 3,
                    msg: '请输入排序',
                    bg: '#AE81FF',
                    time: 2
                });
                $("#LINE_ORDER").focus();
                return false;
            }
            $("#Form").submit();
            $("#zhongxin").hide();
            $("#zhongxin2").show();
        }

    </script>
</head>
<body>
<form action="line/${msg}" name="Form" id="Form" method="post">
    <input type="hidden" name="LINE_ID" id="LINE_ID" value="${pd.LINE_ID}"/>
    <input type="hidden" name="PARENT_ID" id="PARENT_ID" value="${pd.PARENT_ID}"/>

    <div id="zhongxin">
        <table id="table_report" class="table table-striped table-bordered table-hover">
            <tr>
                <td>名称：</td>
                <td><input type="text" name="TITLE" id="TITLE" value="${pd.TITLE}" maxlength="50" placeholder="这里输入名称"
                           title="名称" style="width:90%"/></td>
            </tr>
            <tr>
                <td>链接：</td>
                <td><input type="text" name="LINE_URL" id="LINE_URL" value="${pd.LINE_URL}" maxlength="100"
                           placeholder="这里输入链接" title="链接" style="width:90%"/></td>
            </tr>
            <tr>
                <td>线路：</td>
                <td><input type="text" name="LINE_ROAD" id="LINE_ROAD" value="${pd.LINE_ROAD}" maxlength="100"
                           placeholder="这里输入线路" title="线路" style="width:90%"/></td>
            </tr>
            <tr>
                <td>类型：</td>
                <td><input type="text" name="TYPE" id="TYPE" value="${pd.TYPE}" maxlength="32" placeholder="这里输入类型"
                           title="类型" style="width:90%"/></td>
            </tr>
            <tr>
                <td>排序：</td>
                <td><input type="number" name="LINE_ORDER" id="LINE_ORDER" value="${pd.LINE_ORDER}" maxlength="10"
                           placeholder="这里输入排序" title="排序" style="width:90%"/></td>
            </tr>
            <tr>
                <td style="text-align: center;" colspan="10">
                    <a class="btn btn-mini btn-primary" onclick="save();">保存</a>
                    <a class="btn btn-mini btn-danger" onclick="top.Dialog.close();">取消</a>
                </td>
            </tr>
        </table>
    </div>

    <div id="zhongxin2" class="center" style="display:none"><br/><br/><br/><br/><br/><img
            src="static/images/jiazai.gif"/><br/><h4 class="lighter block green">提交中...</h4></div>

</form>
<!-- 引入 -->
<script type="text/javascript">window.jQuery || document.write("<script src='static/js/jquery-1.7.2.js'>\x3C/script>");</script>
<script src="static/js/bootstrap.min.js"></script>
<script src="static/js/ace-elements.min.js"></script>
<script src="static/js/ace.min.js"></script>
<script type="text/javascript" src="static/js/chosen.jquery.min.js"></script>
<!-- 下拉框 -->
<script type="text/javascript" src="static/js/bootstrap-datepicker.min.js"></script>
<!-- 日期框 -->
<script type="text/javascript">
    $(top.hangge());
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