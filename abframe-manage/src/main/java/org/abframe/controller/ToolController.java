package org.abframe.controller;


import org.abframe.controller.base.BaseController;
import org.abframe.util.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value = "/tool")
public class ToolController extends BaseController {


    /**
     * 接口测试页面
     */
    @RequestMapping(value = "/interface")
    public ModelAndView toInterface() throws Exception {
        ModelAndView mv = this.getModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
        mv.setViewName("interface/interfaceTest");
        mv.addObject("pd", pd);
        return mv;
    }

    /**
     * 接口内部请求
     *
     * @param
     * @throws Exception
     */
    @RequestMapping(value = "/severTest")
    @ResponseBody
    public Object severTest() {
        Map<String, String> map = new HashMap<String, String>();
        PageData pd = new PageData();
        pd = this.getPageData();
        String errInfo = "success", str = "", rTime = "";
        try {
            long startTime = System.currentTimeMillis();
            URL url = new URL(pd.getString("serverUrl"));
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(pd.getString("requestMethod"));        //请求类型  POST or GET
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            long endTime = System.currentTimeMillis();
            String temp = "";
            while ((temp = in.readLine()) != null) {
                str = str + temp;
            }
            rTime = String.valueOf(endTime - startTime);
        } catch (Exception e) {
            errInfo = "error";
        }
        map.put("errInfo", errInfo);    //状态信息
        map.put("result", str);            //返回结果
        map.put("rTime", rTime);        //服务器请求时间 毫秒
        return AppUtil.returnObject(new PageData(), map);
    }


    @RequestMapping(value = "/toSendEmail")
    public ModelAndView toSendEmail() throws Exception {
        ModelAndView mv = this.getModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
        mv.setViewName("mail/email");
        mv.addObject("pd", pd);
        return mv;
    }

    @RequestMapping(value = "/toQrCode")
    public ModelAndView toQrCode() throws Exception {
        ModelAndView mv = this.getModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
        mv.setViewName("qrCode/qrCode");
        mv.addObject("pd", pd);
        return mv;
    }


    @RequestMapping(value = "/createQrCode")
    @ResponseBody
    public Object createQrCode() {
        Map<String, String> map = new HashMap<String, String>();
        PageData pd = new PageData();
        pd = this.getPageData();
        String errInfo = "success";
        String encoderImgId = this.get32UUID() + ".png";
        String encoderContent = pd.getString("encoderContent");
        if (null == encoderContent) {
            errInfo = "error";
        } else {
            try {
                String filePath = PathUtil.getClasspath() + Constant.FILEPATHTWODIMENSIONCODE + encoderImgId;
                TwoDimensionCode.encoderQRCode(encoderContent, filePath, "png");
            } catch (Exception e) {
                errInfo = "error";
            }
        }
        map.put("result", errInfo);
        map.put("encoderImgId", encoderImgId);
        return AppUtil.returnObject(new PageData(), map);
    }

    @RequestMapping(value = "/parseQrCode")
    @ResponseBody
    public Object parseQrCode() {
        Map<String, String> map = new HashMap<String, String>();
        PageData pd = new PageData();
        pd = this.getPageData();
        String errInfo = "success", readContent = "";
        String imgId = pd.getString("imgId");//内容
        if (null == imgId) {
            errInfo = "error";
        } else {
            try {
                String filePath = PathUtil.getClasspath() + Constant.FILEPATHTWODIMENSIONCODE + imgId;
                readContent = TwoDimensionCode.decoderQRCode(filePath);//执行读取二维码
            } catch (Exception e) {
                errInfo = "error";
            }
        }
        map.put("result", errInfo);
        map.put("readContent", readContent);
        return AppUtil.returnObject(new PageData(), map);
    }


    @RequestMapping(value = "/ztree")
    public ModelAndView ztree() throws Exception {
        ModelAndView mv = this.getModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
        mv.setViewName("ztree/ztree");
        mv.addObject("pd", pd);
        return mv;
    }

    @RequestMapping(value = "/map")
    public ModelAndView map() {
        ModelAndView mv = this.getModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
        mv.setViewName("map/map");
        mv.addObject("pd", pd);
        return mv;
    }

    /**
     * 获取地图坐标页面
     */
    @RequestMapping(value = "/mapXY")
    public ModelAndView mapXY() {
        ModelAndView mv = this.getModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
        mv.setViewName("map/mapXY");
        mv.addObject("pd", pd);
        return mv;
    }

    /**
     * 根据经纬度计算距离
     *
     * @throws Exception
     */
    @RequestMapping(value = "/getDistance")
    @ResponseBody
    public Object getDistance() {
        Map<String, String> map = new HashMap<String, String>();
        PageData pd = new PageData();
        pd = this.getPageData();
        String errInfo = "success", distance = "";
        try {
            distance = MapDistance.getDistance(pd.getString("ZUOBIAO_Y"), pd.getString("ZUOBIAO_X"), pd.getString("ZUOBIAO_Y2"), pd.getString("ZUOBIAO_X2"));
        } catch (Exception e) {
            errInfo = "error";
        }
        map.put("result", errInfo);
        map.put("distance", distance);
        return AppUtil.returnObject(new PageData(), map);
    }

}
