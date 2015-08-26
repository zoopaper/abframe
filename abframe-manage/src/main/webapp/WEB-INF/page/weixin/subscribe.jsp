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
</head>
<body>

<div class="container-fluid" id="main-container">

    <div id="page-content" class="clearfix">

        <div class="row-fluid">

            <div class="span12">
                <div class="widget-box">
                    <div class="widget-header widget-header-blue widget-header-flat wi1dget-header-large">
                        <h4 class="lighter">关注回复</h4>
                    </div>
                    <div class="widget-body">
                        <div class="widget-main">


                            <div class="step-content row-fluid position-relative">
                                <div style="width:715px;padding-left: 20px;">
                                    <div style="width:285px;height:399px;float: left;">
                                        <div class="widget-box">
                                            <div class="widget-header widget-header-flat widget-header-small">
                                                <h5><i class="icon-credit-card"></i>
                                                    回复类型
                                                </h5>

                                                <div class="widget-toolbar no-border">
                                                </div>
                                            </div>
                                            <div class="widget-body">
                                                <div class="widget-main">
                                                    ${msg}
                                                </div>
                                                <!--/widget-main-->
                                            </div>
                                            <!--/widget-body-->
                                        </div>
                                        <!--/widget-box-->

                                        <div class="widget-box">
                                            <div class="widget-header widget-header-flat widget-header-small">
                                                <h5><i class="icon-credit-card"></i>
                                                    回复内容
                                                </h5>

                                                <div class="widget-toolbar no-border">
                                                </div>
                                            </div>
                                            <div class="widget-body">
                                                <div class="widget-main">
                                                    ${content}
                                                </div>
                                                <!--/widget-main-->
                                            </div>
                                            <!--/widget-body-->
                                        </div>
                                        <!--/widget-box-->
                                    </div>
                                    <!--/span-->
                                    &nbsp;
                                    <div class="step-content row-fluid position-relative">
                                        <ul class="unstyled spaced2">
                                            <li class="text-warning green"><i class="icon-star blue"></i>
                                                在文本回复、图文回复、应用命令中
                                            </li>
                                            <li class="text-warning green"><i class="icon-star blue"></i>
                                                添加一个关键词为"关注"的记录
                                            </li>
                                            <li class="text-warning green"><i class="icon-star blue"></i>
                                                即为关注回复内容
                                            </li>
                                        </ul>
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
<script type="text/javascript">
    $(top.hangge());
</script>
</body>
</html>

