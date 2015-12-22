<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="../common/common.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8"/>
    <meta name="description" content="overview & stats"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <link href="/plugins/bootstrap/3.2/css/bootstrap.min.css" rel="stylesheet"/>
    <link href="/plugins/bootstrap/3.2/css/bootstrap-theme.min.css" rel="stylesheet"/>
    <link href="/plugins/imgareaselect/css/imgareaselect-default.css" rel="stylesheet" type="text/css">
    <link href="/plugins/uploadify3.2.1/uploadify.css" rel="stylesheet" type="text/css">

    <script type="text/javascript" src="/static/js/jquery-1.7.2.js"></script>
    <script type="text/javascript" src="/plugins/uploadify3.2.1/jquery.uploadify.min.js"></script>
    <script type="text/javascript" src="/plugins/imgareaselect/scripts/jquery.imgareaselect.min.js"></script>
    <script type="text/javascript" src="/static/js/jquery.tips.js"></script>


    <style type="text/css">
        #image-uploaded,
        #image-cuted {
            position: relative;
            max-width: 100%;
        }
        #cut-preview-wrap {
            position: relative;
            display: block;
            padding: 0;
            margin: 0;
            border: 0;
            width: 100%;
            overflow: hidden;
        }
        #cut-preview {
            position: absolute;
            padding: 0;
            margin: 0;
            border: 0;
            top: 0;
            left: 0;
        }
    </style>
</head>

<div class="container-fluid">
    <div class="row" style="margin-top: 10px;">
        <div class="col-xs-3">
            <div id="upload-wrap">
                <input type="file" id="file" name="file"/>

                <p>
                    <a id="upload" class="btn btn-sm btn-success" style="display:none;" href="#">上传</a>
                </p>

                <br>

                <div class="col-xs-12" id="ratio-wrap" style="margin-top:30px;display:none;">
                    <div id="ratio-input" class="input-group">
                        <span class="input-group-addon">裁剪宽高比</span>
                        <input type="text" id="ratio" class="form-control" placeholder="Ratio" value="1.33">
                    </div>
                    <span id="cut-help" class="help-block">输入宽高比进行裁剪初始化。例如1.33</span>

                    <p>
                        <a id="cutInit" class="btn btn-info" href="#">裁剪区域初始化</a>
                        <a id="cut" style="display:none;" class="btn btn-warning" href="#">确定裁剪区</a>
                    </p>
                </div>
            </div>
        </div>

        <div class="col-xs-4">
            <label>裁剪区域</label>

            <div class="row">
                <div class="col-xs-12" id="uploaded-wrap" style="display:none;">
                </div>
            </div>
        </div>
        <div class="col-xs-4" id="preview-wrap" style="display:none;">
            <label>裁剪预览</label>

            <div id="cut-preview-wrap">
                <img id="cut-preview" src="" alt="">
            </div>
            <p>
                <small id="log"></small>
            </p>

        </div>
    </div>

    <div class="row" id="cuted-wrap" style="display:none;">
        <div class="col-xs-offset-2 col-xs-8 text-center">
            <div class="page-header">
                <h4>生成裁剪图片</h4>
            </div>
            <p>
                <img id="image-cuted" src="" alt="">
            </p>

            <p>
                <a id="download" style="display:none;" class="btn btn-block btn-danger" href="#">下载</a>
            </p>
        </div>
    </div>
</div>


<script type="text/javascript">
    var field=$("#file");
    field.uploadify({
        'buttonText': '选择图片'
        , 'swf': '/plugins/uploadify3.2.1/uploadify.swf?v=' + ( parseInt(Math.random() * 1000) )
        , 'uploader': '/avatar/upload'
        , 'auto': false
        , 'multi': false
        , 'method': 'post'
        , 'fileObjName': 'file'
        , 'queueSizeLimit': 1
        , 'fileSizeLimit': '8000KB'
        , 'fileTypeExts': '*.gif; *.jpg; *.png; *.jpeg'
        , 'fileTypeDesc': '只允许.gif .jpg .png .jpeg 图片！'
        , 'onSelect': function (file) {
            $("#upload").show();
        }
        , 'onUploadSuccess': function (file, data, response) {  //上传成功后的触发事件
            field.uploadify('disable', true);
            $("#upload").remove();

            var rst = JSON.parse(data);
            if (!rst.success) {
                alert('上传失败:' + rst.info);
            } else {
                var imageData = rst.data;
                var image = $("<img src='" + imageData.path + "' id='image-uploaded' data-width='" + imageData.width + "' data-height='" + imageData.height + "' data-name='" + imageData.name + "' />");
                $("#uploaded-wrap").append(image).show();
                $("#ratio-wrap").show();
                image.bind('click', function (e) {
                    e.preventDefault();
                    alert('请先设置裁剪宽高比！');
                });
            }
        }
        , 'onUploadError': function (file, errorCode, errorMsg, errorString) {
            alert(errorString);
        }
    });

    //点击上传
    $("#upload").click(function (e) {
        e.preventDefault();
        field.uploadify('upload', '*');
    });

    //点击裁剪初始化时
    $("#cutInit").click(function (e) {
        e.preventDefault();

        //确定裁剪宽高比
        var ratio = parseFloat($("#ratio").val());
        if (isNaN(ratio)) {
            alert("请输入正确的宽高比，必须为数字，例如0.6或1.3");
            return;
        }

        //相关元素
        var uploaded = $("#image-uploaded"), previewWrap = $("#cut-preview-wrap"), preview = $("#cut-preview");

        //图片宽高参数
        var realWidth = uploaded.data('width'),
                realHeight = uploaded.data('height'),
                uploadedWidth = uploaded.outerWidth(),
                uploadedHeight = uploaded.outerHeight(),
                uploadedRate = uploadedWidth / realWidth; //缩放比例

        //其他操作
        $(this).hide();
        $("#ratio-input").hide();
        $("#cut-help").html('图片宽:' + realWidth + ' 高:' + realHeight + ' 裁剪比例:' + ratio + '<span style="color:red;"> 在图片上进行拖拽确定裁剪区域！</span>');
        $("#preview-wrap").show();
        uploaded.unbind('click');

        //预览框宽高参数
        var previewWrapWidth = previewWrap.outerWidth();
        previewWrapHeight = Math.round(previewWrapWidth / ratio);

        //初始化预览框
        previewWrap.css({
            width: previewWrapWidth + 'px',
            height: previewWrapHeight + 'px'
        });
        //初始化预览图
        preview.prop('src', uploaded.attr('src'));

        //构造AreaSelect选择器
        var imgArea = uploaded.imgAreaSelect({
            instance: true,
            handles: true,
            fadeSpeed: 300,
            aspectRatio: '1:' + (1 / ratio),
            //选区改变时的触发事件
            onSelectChange: function (img, selection) {//selection包括x1,y1,x2,y2,width,height，分别为选区的偏移和高宽。
                var rate = previewWrapWidth / selection.width;//预览区相对于选择区的倍数
                preview.css({
                    width: Math.round(uploadedWidth * rate) + 'px',
                    height: Math.round(uploadedHeight * rate) + 'px',
                    "left": Math.round(selection.x1 * rate * -1),
                    "top": Math.round(selection.y1 * rate * -1)
                });

                //换算后的真实参数
                var realSize = {
                    width: Math.round(selection.width / uploadedRate),
                    height: Math.round(selection.height / uploadedRate),
                    offsetLeft: Math.round(selection.x1 / uploadedRate),
                    offsetTop: Math.round(selection.y1 / uploadedRate)
                }
                $("#log").text('实际裁剪参数 - 宽:' + realSize.width +
                        ' 高:' + realSize.height +
                        ' 左偏移:' + realSize.offsetLeft +
                        ' 上偏移:' + realSize.offsetTop
                );
                preview.data(realSize);
            }
        });

        //点击确认裁剪时
        $("#cut").show().click(function (e) {
            e.preventDefault();
            var $this = $(this);
            var data = preview.data();
            if (typeof data['width'] === 'undefined' ||
                    data['width'] == '' ||
                    data['width'] == 0 ||
                    data['height'] == '' ||
                    data['height'] == 0) {
                alert('请先选择裁剪区域！');
                return;
            }
            $this.addClass('active').text('裁剪中...');
            data['name'] = uploaded.data('name');
            $.ajax({
                url: '/avatar/cut',
                type: 'GET',
                data: data,
                dataType: 'json',
                success: function (data) {
                    if (data.success) {
                        $this.hide();
//                        $("#download").show().prop('href', rst.url).prop('target', '_blank');
//                        $("#cuted-wrap").show();
//                        $("#image-cuted").prop('src', rst.path);
                        imgArea.setOptions({'disable': true, 'hide': true});//去掉选区功能
                        alert('图片已裁剪！点击\'下载成品\'可下载！');
                    } else {
                        alert(data.msg);
                    }
                }

            });
        });
    });

</script>

</body>
</html>