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
    <title></title>
    <meta name="description" content="overview & stats"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <link href="static/css/bootstrap.min.css" rel="stylesheet"/>
    <link href="static/css/bootstrap-responsive.min.css" rel="stylesheet"/>
    <link rel="stylesheet" href="static/css/font-awesome.min.css"/>
    <!-- 下拉框 -->
    <link rel="stylesheet" href="static/css/chosen.css"/>

    <link rel="stylesheet" href="static/css/ace.min.css"/>
    <link rel="stylesheet" href="static/css/ace-responsive.min.css"/>
    <link rel="stylesheet" href="static/css/ace-skins.min.css"/>

    <link rel="stylesheet" href="static/css/datepicker.css"/>
    <!-- 日期框 -->
    <script type="text/javascript" src="static/js/jquery-1.9.1.min.js"></script>
    <script type="text/javascript" src="static/js/jquery.tips.js"></script>

    <script type="text/javascript">
        //保存
        function save() {
            if ($("#KEYWORD").val() == "") {
                $("#KEYWORD").tips({
                    side: 3,
                    msg: '请输入关键词',
                    bg: '#AE81FF',
                    time: 2
                });
                $("#KEYWORD").focus();
                return false;
            }
            if ($("#CONTENT").val() == "") {
                $("#CONTENT").tips({
                    side: 3,
                    msg: '请输入内容',
                    bg: '#AE81FF',
                    time: 2
                });
                $("#CONTENT").focus();
                return false;
            }
            if ($("#BZ").val() == "") {
                $("#BZ").tips({
                    side: 3,
                    msg: '请输入备注',
                    bg: '#AE81FF',
                    time: 2
                });
                $("#BZ").focus();
                return false;
            }
            if ($("#STATUS").val() == "") {
                $("#form-field-radio1").tips({
                    side: 3,
                    msg: '请选择状态',
                    bg: '#AE81FF',
                    time: 2
                });
                return false;
            }
            hasK();
        }

        //判断关键词是否存在
        function hasK() {
            var KEYWORD = $("#KEYWORD").val();
            var TEXTMSG_ID = "${pd.TEXTMSG_ID}";
            $.ajax({
                type: "POST",
                url: '<%=basePath%>textmsg/hasK.do',
                data: {KEYWORD: KEYWORD, TEXTMSG_ID: TEXTMSG_ID, tm: new Date().getTime()},
                dataType: 'json',
                cache: false,
                success: function (data) {
                    if ("success" == data.result) {
                        $("#Form").submit();
                        $("#zhongxin").hide();
                        $("#zhongxin2").show();
                    } else {
                        $("#KEYWORD").tips({
                            side: 3,
                            msg: '此关键词已存在(全局)!',
                            bg: '#AE81FF',
                            time: 3
                        });
                        return false;
                    }
                }
            });
        }
        function setType(value) {
            $("#STATUS").val(value);
        }
    </script>
</head>
<body>
<form action="textmsg/${msg}" name="Form" id="Form" method="post">
    <input type="hidden" name="TEXTMSG_ID" id="TEXTMSG_ID" value="${pd.TEXTMSG_ID}"/>
    <input type="hidden" name="STATUS" id="STATUS" value="${pd.STATUS}"/>

    <div id="zhongxin">
        <table id="table_report" class="table table-striped table-bordered table-hover">
            <tr>
                <td style="width:70px;text-align: right;padding-top: 13px;">关键词:</td>
                <td><input style="width:95%;" type="text" name="KEYWORD" id="KEYWORD" value="${pd.KEYWORD}"
                           maxlength="500" placeholder="这里输入关键词" title="关键词"/></td>
            </tr>
            <tr>
                <td style="width:70px;text-align: right;padding-top: 13px;">内容:</td>
                <td>
                    <textarea style="width:95%;height:100px;" rows="10" cols="10" name="CONTENT" id="CONTENT" title="内容"
                              maxlength="1000" placeholder="这里输入内容">${pd.CONTENT}</textarea>

                    <div><font color="#808080">请不要多于1000字否则无法发送</font></div>
                </td>
            </tr>
            <tr>
                <td style="width:70px;text-align: right;padding-top: 13px;">备注:</td>
                <td>
                    <input style="width:95%;" type="text" name="BZ" id="BZ" value="${pd.BZ}" maxlength="500"
                           placeholder="这里输入备注" title="备注"/>
                </td>
            </tr>
            <tr>
                <td style="width:70px;text-align: right;padding-top: 13px;">状态:</td>
                <td>
                    <label style="float:left;padding-left: 12px;"><input name="form-field-radio" id="form-field-radio1"
                                                                         onclick="setType('1');"
                                                                         <c:if test="${pd.STATUS == '1' }">checked="checked"</c:if>
                                                                         type="radio" value="icon-edit"><span
                            class="lbl">有效</span></label>
                    <label style="float:left;padding-left: 5px;"><input name="form-field-radio" id="form-field-radio2"
                                                                        onclick="setType('2');"
                                                                        <c:if test="${pd.STATUS == '2' }">checked="checked"</c:if>
                                                                        type="radio" value="icon-edit"><span
                            class="lbl">无效</span></label>
                </td>
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
<script type="text/javascript">window.jQuery || document.write("<script src='static/js/jquery-1.9.1.min.js'>\x3C/script>");</script>
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