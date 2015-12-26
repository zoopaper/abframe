package org.abframe.controller;

import org.abframe.base.config.ConfigService;
import org.abframe.controller.base.BaseController;
import org.abframe.service.UserService;
import org.abframe.util.AppUtil;
import org.abframe.util.Constant;
import org.abframe.util.PageData;
import org.abframe.util.Watermark;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/config")
public class ConfigController extends BaseController {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(ConfigController.class);

    @Autowired
    private UserService userService;

    @Autowired
    ConfigService configService;

    @RequestMapping(value = "/getUname")
    @ResponseBody
    public Object getList() {
        PageData pd = null;
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            pd = this.getPageData();
            List<PageData> pdList = new ArrayList<PageData>();


            Subject subject = SecurityUtils.getSubject();
            Session session = subject.getSession();

            PageData pds;
            pds = (PageData) session.getAttribute(Constant.SESSION_userpds);

            if (null == pds) {
                String userName = session.getAttribute(Constant.SESSION_USERNAME).toString();
                pd.put("userName", userName);
                pds = userService.getUserByAccount(pd);
                session.setAttribute(Constant.SESSION_userpds, pds);
            }
            pdList.add(pds);
            map.put("list", pdList);
        } catch (Exception e) {
            LOGGER.error("Controller config exception ", e);
        }
        return AppUtil.returnObject(pd, map);
    }

    /**
     * 保存皮肤
     */
    @RequestMapping(value = "/setSkin")
    public void setSKIN(PrintWriter out) {
        PageData pd;
        try {
            pd = this.getPageData();

            Subject subject = SecurityUtils.getSubject();
            Session session = subject.getSession();

            String userName = session.getAttribute(Constant.SESSION_USERNAME).toString();
            pd.put("userName", userName);
            userService.setSkin(pd);
            session.removeAttribute(Constant.SESSION_userpds);
            session.removeAttribute(Constant.SESSION_USERROL);
            out.write("success");
            out.close();
        } catch (Exception e) {
            LOGGER.error("Controller config exception ", e);
        }

    }

    @RequestMapping(value = "/editEmail")
    public ModelAndView editEmail() throws Exception {
        ModelAndView mv = new ModelAndView();
        PageData pd;
        pd = this.getPageData();
        mv.setViewName("config/editEmail");
        mv.addObject("pd", pd);
        return mv;
    }

    @RequestMapping(value = "/toSendSms")
    public ModelAndView toSendSms() throws Exception {
        ModelAndView mv = new ModelAndView();
        PageData pd;
        pd = this.getPageData();
        mv.setViewName("config/sendSms");
        mv.addObject("pd", pd);
        return mv;
    }


    /**
     * 保存系统设置2
     */
    @RequestMapping(value = "/saveSys2")
    public ModelAndView saveSys2() throws Exception {
        ModelAndView mv = new ModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
        Watermark.fushValue();
        mv.addObject("msg", "OK");
        mv.setViewName("save_result");
        return mv;
    }

    /**
     * 去代码生成器页面
     */
    @RequestMapping(value = "/toProductCode")
    public ModelAndView toProductCode() throws Exception {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("config/productCode");
        return mv;
    }

}
