<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title></title>
    <meta http-equiv="Cache-control" content="no-cache">
    <meta name="description" content="overview & stats"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>

    <link href="/static/css/bootstrap.min.css" rel="stylesheet"/>
    <link rel="stylesheet" type="text/css" href="/plugins/webuploader/webuploader.css"/>
    <link rel="stylesheet" type="text/css" href="/plugins/webuploader/style.css"/>
</head>
<body>
<div id="zhongxin">

    <div id="wrapper">
        <div id="container">
            <div id="uploader">
                <div class="queueList">
                    <div id="dndArea" class="placeholder">
                        <div id="filePicker"></div>
                        <p>或将照片拖到这里，单次最多可选300张</p>
                    </div>
                </div>
                <div class="statusBar" style="display:none;">
                    <div class="progress">
                        <span class="text">0%</span>
                        <span class="percentage"></span>
                    </div>
                    <div class="info"></div>
                    <div class="btns">
                        <div id="filePicker2"></div>
                        <div class="uploadBtn">开始上传</div>
                    </div>
                </div>
            </div>
        </div>
    </div>

</div>


<script type="text/javascript" src="/static/js/jquery-1.7.2.js"></script>
<script src="/static/js/bootstrap.min.js"></script>
<script type="text/javascript" src="/plugins/webuploader/webuploader.js"></script>
<script type="text/javascript" src="/plugins/webuploader/upload.js"></script>
<script type="text/javascript">
    $(top.hangge());
</script>
</body>
</html>
