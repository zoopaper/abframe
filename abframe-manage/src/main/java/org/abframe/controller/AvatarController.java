package org.abframe.controller;

import com.alibaba.fastjson.JSONObject;
import net.common.utils.encrypt.MD5Util;
import net.common.utils.json.JsonWrite;
import net.coobird.thumbnailator.Thumbnails;
import org.abframe.base.config.ConfigService;
import org.abframe.base.constant.GlobalConstant;
import org.abframe.controller.base.BaseController;
import org.abframe.util.PathUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * 用户头像
 * User: shijingui
 * Date: 2015/12/18
 */
@Controller
@RequestMapping("/avatar")
public class AvatarController extends BaseController {

    private static final String JFS_AVATAR_URL = "";

    private static final Logger LOGGER = LoggerFactory.getLogger(AvatarController.class);

    @Autowired
    private ConfigService configService;

    /**
     * @return
     */
    @RequestMapping(value = "/toAvatarEdit", method = RequestMethod.GET)
    public ModelAndView toAvatarEdit() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("/avatar/avatarEdit");
        return modelAndView;
    }

    /**
     * @param file
     * @return
     */
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    @ResponseBody
    public Object upload(MultipartFile file, HttpServletRequest request) {
        JsonWrite jsonWrite = new JsonWrite();
        JSONObject jsonObject = new JSONObject();
        String serverPath = PathUtil.getServerRealPath();
        String filePath = configService.getString(GlobalConstant.USER_AVATAR_PATH, configService.getCfgMap(), "") + MD5Util.digestHex(file.getOriginalFilename());
        String imageSavePath = serverPath + File.separator + filePath;

        try {
            int[] imageProp = getImageWidthAndHeight(file.getInputStream());
            jsonObject.put("width", imageProp[0]);
            jsonObject.put("height", imageProp[1]);
        } catch (IOException e) {
            e.printStackTrace();
        }

        jsonWrite.setMsg("上传成功！");
        jsonObject.put("path", "/static/img/tulips.jpg");
        jsonWrite.setData(jsonObject);
        return jsonWrite;
    }

    @RequestMapping(value = "/cut", method = RequestMethod.POST)
    @ResponseBody
    public Object cut(String width, String height, int offsetLeft, int offsetTop) {
        JsonWrite jsonWrite = new JsonWrite();
        jsonWrite.setMsg("裁剪成功！");
        try {
            Thumbnails.of("").sourceRegion(11, 11, 33, 33).toFile(new File(""));
        } catch (IOException e) {
            e.printStackTrace();
        }
//        ModelAndView modelAndView = new ModelAndView();
//        modelAndView.setViewName("avatar/avatarEdit");
        return jsonWrite;
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

    /**
     * 获取图片的宽度和高度
     *
     * @param is
     * @return
     */
    public int[] getImageWidthAndHeight(InputStream is) {
        int[] image = new int[2];
        if (is != null) {
            try {
                BufferedImage bufferedImage = ImageIO.read(is);
                int width = bufferedImage.getWidth();
                int height = bufferedImage.getHeight();
                image[0] = width;
                image[1] = height;
                return image;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return image;
    }
}
