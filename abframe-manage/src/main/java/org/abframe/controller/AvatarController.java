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
import org.abframe.util.PageData;
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
 * user avatar
 * User: shijingui
 * Date: 2015/12/18
 */
@Controller
@RequestMapping("/avatar/*")
public class AvatarController extends BaseController {

    private static final Logger log = LoggerFactory.getLogger(AvatarController.class);
    private static final String IMAGE_SUFFIX = ".jpg";

    @Autowired
    private ConfigService configService;
    @Autowired
    private UserService userService;

    /**
     * to edit user avatar
     *
     * @return
     */
    @RequestMapping(value = "/toAvatarEdit", method = RequestMethod.GET)
    public ModelAndView toAvatarEdit() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("/avatar/avatarEdit");
        return modelAndView;
    }

    /**
     * upload user avatar
     *
     * @param file
     * @return
     */
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    @ResponseBody
    public Object upload(MultipartFile file, HttpServletRequest request) {
        JsonWrite jsonWrite = new JsonWrite();
        JSONObject jsonObject = new JSONObject();
        createAvatarUpload(file);
        String filePath = configService.getString(GlobalConstant.USER_AVATAR_PATH, configService.getCfgMap(), "") + GlobalConstant.USER_CUT_AVATAR_PATH + MD5Util.digestHex(getUserName()) + IMAGE_SUFFIX;
        try {
            int[] imageProp = getImageWidthAndHeight(file.getInputStream());
            jsonObject.put("width", imageProp[0]);
            jsonObject.put("height", imageProp[1]);
        } catch (Exception e) {
            jsonWrite.setSuccess(Boolean.FALSE);
            e.printStackTrace();
        }
        jsonObject.put("path", filePath);
        jsonWrite.setData(jsonObject);
        return jsonWrite;
    }

    @RequestMapping(value = "/cut", method = RequestMethod.GET)
    @ResponseBody
    public Object cut(int width, int height, int offsetLeft, int offsetTop, String name) {
        PageData pageData = new PageData();
        JsonWrite jsonWrite = new JsonWrite();
        JSONObject jsonObject = new JSONObject();
        String serverPath = PathUtil.getServerRealPath();
        String filePath = configService.getString(GlobalConstant.USER_AVATAR_PATH, configService.getCfgMap(), "") + GlobalConstant.USER_CUT_AVATAR_PATH + MD5Util.digestHex(getUserName()) + IMAGE_SUFFIX;
        String imageSavePath = serverPath + filePath;

        String filePath2 = configService.getString(GlobalConstant.USER_AVATAR_PATH, configService.getCfgMap(), "") + "/" + MD5Util.digestHex(getUserName()) + IMAGE_SUFFIX;
        String imageSavePath2 = serverPath + filePath2;

        jsonWrite.setData(jsonObject);
        try {
            Thumbnails.of(new File(imageSavePath)).sourceRegion(offsetLeft, offsetTop, width, height).size(width, height).toFile(new File(imageSavePath2));
            String env = configService.getString(GlobalConstant.ENV_PROFILE, configService.getCfgMap(), "");
            pageData.put("avatar_url", filePath2);
            pageData.put("id", getUserId());
            userService.updateUserAvatarById(pageData);
            if (env.equals(GlobalConstant.ENV_DEVELOPMENT)) {
                String serverRealPath = getServerRealPath();
                Thumbnails.of(new File(imageSavePath)).sourceRegion(offsetLeft, offsetTop, width, height).size(width, height).toFile(new File(serverRealPath + filePath2));
            }
        } catch (Exception e) {
            jsonWrite.setSuccess(Boolean.FALSE);
        }
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
        return user.getAccount();
    }

    public long getUserId() {
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession();
        UserBean user = (UserBean) session.getAttribute(Constant.SESSION_USER);
        return user.getId();
    }

    public void createAvatarUpload(MultipartFile file) {
        String serverPath = PathUtil.getServerRealPath();
        File tmpFile = new File(serverPath + configService.getString(GlobalConstant.USER_AVATAR_PATH, configService.getCfgMap(), "") + GlobalConstant.USER_CUT_AVATAR_PATH);
        if (!tmpFile.exists()) {
            tmpFile.mkdirs();
        }
        String filePath = configService.getString(GlobalConstant.USER_AVATAR_PATH, configService.getCfgMap(), "") + GlobalConstant.USER_CUT_AVATAR_PATH + MD5Util.digestHex(getUserName()) + IMAGE_SUFFIX;
        String imageSavePath = serverPath + filePath;
        try {
            IOUtils.copy(file.getInputStream(), new FileOutputStream(imageSavePath));
        } catch (IOException e) {
            e.printStackTrace();
        }

        String env = configService.getString(GlobalConstant.ENV_PROFILE, configService.getCfgMap(), "");
        if (env.equals(GlobalConstant.ENV_DEVELOPMENT)) {
            serverPath = getServerRealPath();
            File tmpFile2 = new File(serverPath + configService.getString(GlobalConstant.USER_AVATAR_PATH, configService.getCfgMap(), "") + GlobalConstant.USER_CUT_AVATAR_PATH);
            if (!tmpFile2.exists()) {
                tmpFile2.mkdirs();
            }
            imageSavePath = serverPath + filePath;
            try {
                IOUtils.copy(file.getInputStream(), new FileOutputStream(imageSavePath));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * this method used development env
     *
     * @return
     */
    public String getServerRealPath() {
        String serverPath = PathUtil.getServerRealPath();
        int index = serverPath.indexOf("target");
        serverPath = serverPath.substring(0, index) + "/src/main/webapp/";
        return serverPath;
    }
}
