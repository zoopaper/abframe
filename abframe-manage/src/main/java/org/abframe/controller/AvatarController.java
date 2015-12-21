package org.abframe.controller;

import com.alibaba.fastjson.JSONObject;
import net.common.utils.encrypt.MD5Util;
import net.common.utils.json.JsonWrite;
import net.coobird.thumbnailator.Thumbnails;
import org.abframe.base.config.ConfigService;
import org.abframe.base.constant.GlobalConstant;
import org.abframe.controller.base.BaseController;
import org.abframe.entity.UserBean;
import org.abframe.service.UserService;
import org.abframe.util.Constant;
import org.abframe.util.PathUtil;
import org.apache.commons.io.IOUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
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
import java.io.FileOutputStream;
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

    @Autowired
    private UserService userService;

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
        String fileName = file.getOriginalFilename();
        String fileSuffix = ".jpg";


        File tmpFile = new File(serverPath + configService.getString(GlobalConstant.USER_AVATAR_PATH, configService.getCfgMap(), "") + GlobalConstant.USER_CUT_AVATAR_PATH);
        if (!tmpFile.exists()) {
            tmpFile.mkdirs();
        }

        String filePath = configService.getString(GlobalConstant.USER_AVATAR_PATH, configService.getCfgMap(), "") + GlobalConstant.USER_CUT_AVATAR_PATH + File.separator + MD5Util.digestHex(getUserName()) + fileSuffix;
        String imageSavePath = serverPath + filePath;
        try {
            IOUtils.copy(file.getInputStream(), new FileOutputStream(imageSavePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            int[] imageProp = getImageWidthAndHeight(file.getInputStream());
            jsonObject.put("width", imageProp[0]);
            jsonObject.put("height", imageProp[1]);
        } catch (IOException e) {
            e.printStackTrace();
        }
        jsonWrite.setMsg("上传成功！");
        jsonObject.put("path", filePath);
        jsonWrite.setData(jsonObject);
        return jsonWrite;
    }

    @RequestMapping(value = "/cut", method = RequestMethod.POST)
    @ResponseBody
    public Object cut(int width, int height, int offsetLeft, int offsetTop) {
        JsonWrite jsonWrite = new JsonWrite();
        jsonWrite.setMsg("裁剪成功！");
        String serverPath = PathUtil.getServerRealPath();
        String filePath = configService.getString(GlobalConstant.USER_AVATAR_PATH, configService.getCfgMap(), "") + GlobalConstant.USER_CUT_AVATAR_PATH + File.separator + MD5Util.digestHex(getUserName()) + ".jpg";
        String imageSavePath = serverPath + filePath;

        String filePath2 = configService.getString(GlobalConstant.USER_AVATAR_PATH, configService.getCfgMap(), "") + File.separator + MD5Util.digestHex(getUserName()) + ".jpg";
        String imageSavePath2 = serverPath + filePath2;


        try {
            Thumbnails.of(new File(imageSavePath)).sourceRegion(offsetLeft, offsetTop, width, height).toFile(new File(imageSavePath2));
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


    public String getUserName() {
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession();
        UserBean user = (UserBean) session.getAttribute(Constant.SESSION_USER);
        return user.getUserName();
    }
}
