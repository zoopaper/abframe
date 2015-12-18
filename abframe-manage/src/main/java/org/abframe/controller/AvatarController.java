package org.abframe.controller;

import com.alibaba.fastjson.JSONObject;
import net.common.utils.json.JsonWrite;
import org.abframe.controller.base.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

/**
 * 用户头像
 * User: shijingui
 * Date: 2015/12/18
 */
@Controller
@RequestMapping("/avatar")
public class AvatarController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AvatarController.class);

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    @ResponseBody
    public Object upload(MultipartFile upload) {
        JsonWrite jsonWrite = new JsonWrite();
        jsonWrite.setMsg("上传成功！");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("path", "/static/img/tulips.jpg");
        jsonObject.put("width", "200");
        jsonObject.put("height", "200");
        jsonWrite.setData(jsonObject);
        return jsonWrite;
    }

    @RequestMapping(value = "/toAvatarEdit", method = RequestMethod.GET)
    public ModelAndView toAvatarEdit() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("avatar/avatarEdit");
        return modelAndView;
    }


    @RequestMapping(value = "/cut", method = RequestMethod.POST)
    public ModelAndView cut() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("avatar/avatarEdit");
        return modelAndView;
    }

    @RequestMapping(value = "/test", method = RequestMethod.POST)
    @ResponseBody
    public JsonWrite test() {
        JsonWrite jsonWrite = new JsonWrite();
        jsonWrite.setMsg("");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", "xxxxxxxx");
        jsonWrite.setData(jsonObject);
        return jsonWrite;

    }
}
