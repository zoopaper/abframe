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
    <!--提示框-->
    <script type="text/javascript" src="static/js/jquery.tips.js"></script>
    <script type="text/javascript">
        //保存
        function save() {
            if ($("#stiename").val() == "") {
                $("#stiename").tips({
                    side: 3,
                    msg: '输入网站名称',
                    bg: '#AE81FF',
                    time: 2
                });

                $("#stiename").focus();
                return false;
            }
            if ($("#content").val() == "") {

                $("#nr").tips({
                    side: 3,
                    msg: '输入内容',
                    bg: '#AE81FF',
                    time: 2
                });

                $("#content").focus();
                return false;
            }


            $("#Form").submit();
            $("#zhongxin").hide();
            $("#zhongxin2").show();
        }

        //删除图片
        function delP(tpurl, id) {
            if (confirm("确定要删除图片？")) {
                var url = "<%=basePath%>link/delPic?tpurl=" + tpurl + "&id=" + id + "&guid=" + new Date().getTime();
                $.get(url, function (data) {
                    if (data == "success") {
                        alert("删除成功!");
                        document.location.reload();
                    }
                });
            }
        }
    </script>
</head>
<body>
<form action="link/${msg}" name="Form" id="Form" method="post" enctype="multipart/form-data">
    <input type="hidden" name="id" id="id" value="${pd.id }"/>

    <div id="zhongxin">
        <table id="table_report" class="table table-striped table-bordered table-hover">
            <tr>
                <td>网名:</td>
                <td><input type="text" style="width:95%;" name="stiename" id="stiename" value="${pd.stiename }"
                           placeholder="这里输入网站名称" title="网站名称"/></td>
            </tr>
            <tr>
                <td>内容:</td>
                <td id="nr">
                    <textarea style="width:90%;height:200px" name="sitecontent"
                              id="content">${pd.sitecontent }</textarea>
                </td>
            </tr>
            <tr>
                <td>链接:</td>
                <td><input type="text" style="width:95%;" name="tourl" id="tourl" value="${pd.tourl }"
                           placeholder="这里输入链接" title="链接"/></td>
            </tr>
            <tr>
                <td>图片:</td>
                <td>
                    <c:if test="${pd == null || pd.stieurl == '' || pd.stieurl == null }">
                        <input type="file" id="tp" name="tp"/>
                    </c:if>
                    <c:if test="${pd != null && pd.stieurl != '' && pd.stieurl != null }">
                        <a href="TP/${pd.stieurl}" target="_blank"><img src="TP/${pd.stieurl}" width="210"/></a>
                        <input type="button" class="btn btn-mini btn-danger" value="删除"
                               onclick="delP('${pd.stieurl}','${pd.id }');"/>
                        <input type="hidden" name="tpz" id="tpz" value="${pd.stieurl }"/>
                    </c:if>
                </td>
            </tr>
            <tr>
                <td>权重:</td>
                <td>
                    <input type="number" name="sequence" id="sequence" value="${pd.sequence }" placeholder="权重(输入数字)"
                           title="权重越大,排列越靠前"/>
                </td>
            </tr>
            <tr>
                <td>选择:</td>
                <td>
                    <select name="type" title="类型">
                        <option value="0"
                                <c:if test="${pd.type == '0' }">selected</c:if> >文字
                        </option>
                        <option value="1"
                                <c:if test="${pd.type == '1' }">selected</c:if> >图片
                        </option>
                    </select>
                    <select name="status" title="状态">
                        <option value="0"
                                <c:if test="${pd.status == '0' }">selected</c:if> >未发布
                        </option>
                        <option value="1"
                                <c:if test="${pd.status == '1' }">selected</c:if> >已发布
                        </option>
                    </select>
                </td>
            </tr>

            <tr>
                <td class="center" colspan="2">
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
<!-- 单选框 -->
<script type="text/javascript" src="static/js/bootstrap-datepicker.min.js"></script>
<!-- 日期框 -->

<!-- 编辑框-->
<script type="text/javascript" charset="utf-8">window.UEDITOR_HOME_URL = "<%=path%>/plugins/ueditor/";</script>
<script type="text/javascript" charset="utf-8" src="plugins/ueditor/ueditor.config.js"></script>
<script type="text/javascript" charset="utf-8" src="plugins/ueditor/ueditor.all.js"></script>
<!-- 编辑框-->

<script type="text/javascript">
    $(top.hangge());
    $(function () {

        //单选框
        $(".chzn-select").chosen();
        $(".chzn-select-deselect").chosen({allow_single_deselect: true});

        //日期框
        $('.date-picker').datepicker();

        //上传
        $('#tp').ace_file_input({
            no_file: '请选择图片 ...',
            btn_choose: '选择',
            btn_change: '更改',
            droppable: false,
            onchange: null,
            thumbnail: false //| true | large
            //whitelist:'gif|png|jpg|jpeg'
            //blacklist:'exe|php'
            //onchange:''
            //
        });

    });

    function reurl() {
        UE.getEditor('content');
    }
    setTimeout('reurl()', 500);
</script>

</body>
</html>