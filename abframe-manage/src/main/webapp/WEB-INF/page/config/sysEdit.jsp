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
    <link rel="stylesheet" href="static/css/ace.min.css"/>
    <link rel="stylesheet" href="static/css/ace-responsive.min.css"/>
    <link rel="stylesheet" href="static/css/ace-skins.min.css"/>
    <script type="text/javascript" src="static/js/jquery-1.7.2.js"></script>

    <!-- 上传图片插件 -->
    <link href="plugins/uploadify/uploadify.css" rel="stylesheet" type="text/css">
    <script type="text/javascript" src="plugins/uploadify/swfobject.js"></script>
    <script type="text/javascript" src="plugins/uploadify/jquery.uploadify.v2.1.4.min.js"></script>
    <!-- 上传图片插件 -->
    <script type="text/javascript">
        var jsessionid = "<%=session.getId()%>";  //勿删，uploadify兼容火狐用到
    </script>
    <!--引入属于此页面的js -->
    <script type="text/javascript" src="static/js/myjs/sys.js"></script>
    <script type="text/javascript" src="static/js/jquery.tips.js"></script>


</head>
<body>

<div id="zhongxin">
    <div class="span6">
        <div class="tabbable">
            <ul class="nav nav-tabs" id="myTab">
                <li class="active"><a data-toggle="tab" href="#profile">
                    <i
                            class="green icon-cog bigger-110"></i>水印配置</a></li>
            </ul>
            <div class="tab-content">
                <div id="profile" class="tab-pane">
                    <form action="head/saveSys2.do" name="Form2" id="Form2" method="post">
                        <table id="table_report" class="table table-striped table-bordered table-hover">
                            <tr>
                                <td style="text-align: center;" colspan="100">
                                    文字水印配置
                                    <label style="float:left;padding-left: 15px;">
                                        <input name="fcheckbox"
                                               class="ace-checkbox-2"
                                               type="checkbox" id="check1"
                                               onclick="openThis1();"/><span
                                            class="lbl">开启</span></label>
                                </td>
                            </tr>
                            <tr>
                                <td style="width:50px;text-align: right;padding-top: 12px;">内容:</td>
                                <td><input type="text" name="fcontent" id="fcontent" value="${pd.fcontent }"
                                           style="width:90%" title="水印文字内容"/></td>
                                <td style="width:50px;text-align: right;padding-top: 12px;">字号:</td>
                                <td><input type="number" name="fontSize" id="fontSize" value="${pd.fontSize }"
                                           style="width:90%" title="字号"/></td>
                            </tr>
                            <tr>
                                <td style="width:50px;text-align: right;padding-top: 12px;">X坐标:</td>
                                <td><input type="number" name="fontX" id="fontX" value="${pd.fontX }" style="width:90%"
                                           title="X坐标"/></td>
                                <td style="width:50px;text-align: right;padding-top: 12px;">Y坐标:</td>
                                <td><input type="number" name="fontY" id="fontY" value="${pd.fontY }" style="width:90%"
                                           title="Y坐标"/></td>
                            </tr>
                        </table>

                        <table id="table_report" class="table table-striped table-bordered table-hover">
                            <tr>
                                <td style="text-align: center;" colspan="100">
                                    图片水印配置
                                    <label style="float:left;padding-left: 15px;"><input name="fcheckbox"
                                                                                         class="ace-checkbox-2"
                                                                                         type="checkbox" id="check2"
                                                                                         onclick="openThis2();"/><span
                                            class="lbl">开启</span></label>
                                </td>
                            </tr>
                            <tr>
                                <td style="width:50px;text-align: right;padding-top: 12px;">X坐标:</td>
                                <td><input type="number" name="imgX" id="imgX" value="${pd.imgX }" style="width:90%"
                                           title="X坐标"/></td>
                                <td style="width:50px;text-align: right;padding-top: 12px;">Y坐标:</td>
                                <td><input type="number" name="imgY" id="imgY" value="${pd.imgY }" style="width:90%"
                                           title="Y坐标"/></td>
                            </tr>
                            <tr>
                                <td style="width:50px;text-align: right;padding-top: 12px;">水印:</td>
                                <td colspan="10">
                                    <div style="float:left;"><img src="<%=basePath%>uploadFiles/uploadImgs/${pd.imgUrl}"
                                                                  width="100"/></div>
                                    <div style="float:right;"><input type="file" name="TP_URL" id="uploadify1"
                                                                     keepDefaultStyle="true"/></div>
                                </td>
                            </tr>
                        </table>

                        <table class="center" style="width:100%">
                            <tr>
                                <td style="text-align: center;" colspan="100">
                                    <a class="btn btn-mini btn-primary" onclick="save2();">保存</a>
                                    <a class="btn btn-mini btn-danger" onclick="top.Dialog.close();">取消</a>
                                </td>
                            </tr>
                        </table>
                        <input type="hidden" name="isCheck1" id="isCheck1" value="${pd.isCheck1 }"/>
                        <input type="hidden" name="isCheck2" id="isCheck2" value="${pd.isCheck2 }"/>
                        <input type="hidden" name="imgUrl" id="imgUrl" value="${pd.imgUrl }"/>
                        <input type="hidden" value="no" id="hasTp1"/>
                    </form>
                </div>
            </div>
        </div>
    </div>
    <!--/span-->


</div>

<div id="zhongxin2" class="center" style="display:none"><br/><br/><br/><br/><img src="static/images/jiazai.gif"/><br/>
    <h4 class="lighter block green"></h4></div>

<!-- 引入 -->
<script type="text/javascript">window.jQuery || document.write("<script src='static/js/jquery-1.7.2.js'>\x3C/script>");</script>
<script src="static/js/bootstrap.min.js"></script>
<script src="static/js/ace-elements.min.js"></script>
<script src="static/js/ace.min.js"></script>
<script type="text/javascript">
    $(document).ready(function () {
        if ("${pd.isCheck1 }" == "yes") {
            $("#check1").attr("checked", true);
        } else {
            $("#check1").attr("checked", false);
        }
        if ("${pd.isCheck2 }" == "yes") {
            $("#check2").attr("checked", true);
        } else {
            $("#check2").attr("checked", false);
        }
    });
</script>
</body>
</html>