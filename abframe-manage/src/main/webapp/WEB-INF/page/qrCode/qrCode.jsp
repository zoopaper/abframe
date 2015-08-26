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
    <%@ include file="../common/top.jsp" %>
    <!-- 上传图片插件 -->
    <link href="plugins/uploadify/uploadify.css" rel="stylesheet" type="text/css">
    <script type="text/javascript" src="plugins/uploadify/swfobject.js"></script>
    <script type="text/javascript" src="plugins/uploadify/jquery.uploadify.v2.1.4.min.js"></script>
    <!-- 上传图片插件 -->
    <script type="text/javascript">
        var jsessionid = "<%=session.getId()%>";  //勿删，uploadify兼容火狐用到
    </script>
    <!--引入属于此页面的js -->
    <script type="text/javascript" src="static/js/myjs/readTwoD.js"></script>
</head>
<body>
<div class="container-fluid" id="main-container">
    <div id="page-content" class="clearfix">
        <div class="row-fluid">
            <div class="span12">
                <div class="widget-box">
                    <div class="widget-header widget-header-blue widget-header-flat wi1dget-header-large">
                        <h4 class="lighter">二维码</h4>
                    </div>
                    <div class="widget-body">
                        <div class="widget-main">
                            <div class="step-content row-fluid position-relative">
                                <div style="float: left;">
							<span class="input-icon">
								<input type="text" id="encoderContent" title="输入内容" value="https://www.baidu.com"
                                       style="width:300px;">
								<i class="icon-edit"></i>
							</span>
                                </div>
                                <div style="margin-top: -5px;">
                                    &nbsp;&nbsp;
                                    <button class="btn btn-app btn-light btn-mini" onclick="createTwoD();">
                                        <i class="icon-print"></i>
                                    </button>

                                </div>
                                <div style="width:715px;padding-left: 20px;">
                                    <div style="width:285px;float: left;">
                                        <div class="widget-box">
                                            <div class="widget-header widget-header-flat widget-header-small">
                                                <h5><i class="icon-credit-card"></i>
                                                    生成二维码
                                                </h5>

                                                <div class="widget-toolbar no-border">

                                                </div>
                                            </div>
                                            <div class="widget-body">
                                                <div class="widget-main">
                                                    <img id="encoderImgId" cache="false"
                                                         src="<%=basePath%>static/img/default.png" width="265px"
                                                         height="265px;"/>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <div style="width:399px;float:right;">
                                        <div class="widget-box">
                                            <div class="widget-header widget-header-flat widget-header-small">
                                                <h5><i class="icon-credit-card"></i>
                                                    解析二维码
                                                </h5>

                                                <div class="widget-toolbar no-border">

                                                </div>
                                            </div>

                                            <div class="widget-body">
                                                <div class="widget-main">
                                                    <div>
											<textarea id="readContent" title="解析结果" placeholder="显示解析结果"
                                                      class="autosize-transition span12"
                                                      style="width:375px;height:160px;">
											</textarea>
                                                    </div>
                                                    <div>
                                                        <div style="float: left;" id="tipsTwo">
                                                            <input type="file" name="TP_URL" id="uploadify1"
                                                                   keepDefaultStyle="true"/>
                                                        </div>
                                                        <div><a class="btn btn-mini btn-success" onclick="uploadTwo();">解析</a>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>

                            </div>
                            <div class="step-content row-fluid position-relative">
                                <input type="hidden" value="no" id="hasTp1"/>
                            </div>
                        </div>
                    </div>
                </div>
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
<!--提示框-->
<script type="text/javascript" src="static/js/jquery.tips.js"></script>
</body>
</html>

