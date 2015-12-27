package org.abframe.controller;

import org.abframe.common.PermissionHandler;
import org.abframe.controller.base.BaseController;
import org.abframe.entity.Page;
import org.abframe.util.Constant;
import org.abframe.util.PageData;
import org.slf4j.LoggerFactory;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
@RequestMapping(value = "/onlineUser")
public class OnlineUserController extends BaseController {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(OnlineUserController.class);

    String menuUrl = "onlineUser/list";

    @RequestMapping(value = "/delete")
    public void delete(PrintWriter out) {
        if (!PermissionHandler.buttonJurisdiction(menuUrl, "del")) {
            return;
        } //校验权限
        PageData pd = new PageData();
        try {
            pd = this.getPageData();
            out.write("success");
            out.close();
        } catch (Exception e) {
            LOGGER.error("Controller onlineUser exception.", e);
        }

    }

    @RequestMapping(value = "/edit")
    public ModelAndView edit() throws Exception {

        if (!PermissionHandler.buttonJurisdiction(menuUrl, "updateRoleById")) {
            return null;
        }
        ModelAndView mv = new ModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
        mv.addObject("msg", "success");
        mv.setViewName("save_result");
        return mv;
    }

    @RequestMapping(value = "/list")
    public ModelAndView list(Page page) {

        //if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;} //校验权限
        ModelAndView mv = new ModelAndView();
        PageData pd = new PageData();
        try {
            pd = this.getPageData();
            page.setPd(pd);
            mv.setViewName("onlineUser/onlineUserList");
            mv.addObject("pd", pd);
            mv.addObject(Constant.SESSION_QX, this.getHC());    //按钮权限
        } catch (Exception e) {
            LOGGER.error("Controller onlineUser exception.", e);
        }
        return mv;
    }


    @RequestMapping(value = "/goAdd")
    public ModelAndView toAdd() {

        ModelAndView mv = new ModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
        try {
            mv.setViewName("onlineUser/onlineUserEdit");
            mv.addObject("msg", "save");
            mv.addObject("pd", pd);
        } catch (Exception e) {
            LOGGER.error("Controller onlineUser exception.", e);
        }
        return mv;
    }


    @InitBinder
    public void initBinder(WebDataBinder binder) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        binder.registerCustomEditor(Date.class, new CustomDateEditor(format, true));
    }
}
