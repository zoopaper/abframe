package org.abframe.controller;

import org.abframe.common.PermissionHandler;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.abframe.controller.base.BaseController;
import org.abframe.entity.Page;
import org.abframe.service.WeixinCommandService;
import org.abframe.service.WeixinImgMsgService;
import org.abframe.service.WeixinTextMsgService;
import org.abframe.util.*;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping(value = "/textMsg")
public class WeixinTextMsgController extends BaseController {

    //菜单地址(权限用)
    String menuUrl = "textMsg/list";

    @Resource(name = "weixinTextMsgService")
    private WeixinTextMsgService weixinTextMsgService;

    @Resource(name = "weixinCommandService")
    private WeixinCommandService weixinCommandService;

    @Resource(name = "weixinImgMsgService")
    private WeixinImgMsgService weixinImgMsgService;

    @RequestMapping(value = "/save")
    public ModelAndView save() throws Exception {
        logBefore(logger, "新增Textmsg");
        if (!PermissionHandler.buttonJurisdiction(menuUrl, "add")) {
            return null;
        }
        ModelAndView mv = this.getModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
        pd.put("TEXTMSG_ID", this.get32UUID());    //主键
        pd.put("CREATETIME", Tools.date2Str(new Date())); //创建时间
        weixinTextMsgService.save(pd);
        mv.addObject("msg", "success");
        mv.setViewName("save_result");
        return mv;
    }


    @RequestMapping(value = "/delete")
    public void delete(PrintWriter out) {
        logBefore(logger, "删除Textmsg");
        if (!PermissionHandler.buttonJurisdiction(menuUrl, "del")) {
            return;
        }
        PageData pd = new PageData();
        try {
            pd = this.getPageData();
            weixinTextMsgService.delete(pd);
            out.write("success");
            out.close();
        } catch (Exception e) {
            logger.error(e.toString(), e);
        }
    }


    @RequestMapping(value = "/edit")
    public ModelAndView edit() throws Exception {
        logBefore(logger, "修改Textmsg");
        if (!PermissionHandler.buttonJurisdiction(menuUrl, "edit")) {
            return null;
        }
        ModelAndView mv = this.getModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
        weixinTextMsgService.edit(pd);
        mv.addObject("msg", "success");
        mv.setViewName("save_result");
        return mv;
    }


    @RequestMapping(value = "/list")
    public ModelAndView list(Page page) {
        logBefore(logger, "列表Textmsg");
        //if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;}
        ModelAndView mv = this.getModelAndView();
        PageData pd = new PageData();
        try {
            pd = this.getPageData();
            String KEYWORD = pd.getString("KEYWORD");
            if (null != KEYWORD && !"".equals(KEYWORD)) {
                pd.put("KEYWORD", KEYWORD.trim());
            }
            page.setPd(pd);
            List<PageData> varList = weixinTextMsgService.list(page);    //列出Textmsg列表
            mv.setViewName("weixin/textmsg/textmsg_list");
            mv.addObject("varList", varList);
            mv.addObject("pd", pd);
            mv.addObject(Constant.SESSION_QX, this.getHC());    //按钮权限
        } catch (Exception e) {
            logger.error(e.toString(), e);
        }
        return mv;
    }


    @RequestMapping(value = "/toAdd")
    public ModelAndView toAdd() {
        logBefore(logger, "去新增Textmsg页面");
        ModelAndView mv = this.getModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
        try {
            mv.setViewName("weixin/textmsg/textmsg_edit");
            mv.addObject("msg", "save");
            mv.addObject("pd", pd);
        } catch (Exception e) {
            logger.error(e.toString(), e);
        }
        return mv;
    }

    /**
     * 去关注回复页面
     */
    @RequestMapping(value = "/goSubscribe")
    public ModelAndView goSubscribe() {
        logBefore(logger, "去关注回复页面");
        ModelAndView mv = this.getModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
        try {
            pd.put("KEYWORD", "关注");
            PageData msgpd = weixinTextMsgService.findByKw(pd);
            if (null != msgpd) {
                mv.addObject("msg", "文本消息");
                mv.addObject("content", msgpd.getString("CONTENT"));
            } else {
                msgpd = weixinImgMsgService.findByKw(pd);
                if (null != msgpd) {
                    mv.addObject("msg", "图文消息");
                    mv.addObject("content", "标题：" + msgpd.getString("TITLE1"));
                } else {
                    msgpd = weixinCommandService.findByKw(pd);
                    if (null != msgpd) {
                        mv.addObject("msg", "命令");
                        mv.addObject("content", "执行命令：" + msgpd.getString("COMMANDCODE"));
                    } else {
                        mv.addObject("msg", "无回复");
                    }
                }
            }
            mv.setViewName("weixin/subscribe");
            mv.addObject("pd", msgpd);
        } catch (Exception e) {
            logger.error(e.toString(), e);
        }
        return mv;
    }


    @RequestMapping(value = "/toEdit")
    public ModelAndView toEdit() {
        logBefore(logger, "去修改Textmsg页面");
        ModelAndView mv = this.getModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
        try {
            pd = weixinTextMsgService.findById(pd);    //根据ID读取
            mv.setViewName("weixin/textmsg/textmsg_edit");
            mv.addObject("msg", "edit");
            mv.addObject("pd", pd);
        } catch (Exception e) {
            logger.error(e.toString(), e);
        }
        return mv;
    }


    @RequestMapping(value = "/deleteAll")
    @ResponseBody
    public Object deleteAll() {
        logBefore(logger, "批量删除Textmsg");
        if (!PermissionHandler.buttonJurisdiction(menuUrl, "del")) {
            return null;
        }
        PageData pd = new PageData();
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            pd = this.getPageData();
            List<PageData> pdList = new ArrayList<PageData>();
            String DATA_IDS = pd.getString("DATA_IDS");
            if (null != DATA_IDS && !"".equals(DATA_IDS)) {
                String ArrayDATA_IDS[] = DATA_IDS.split(",");
                weixinTextMsgService.deleteAll(ArrayDATA_IDS);
                pd.put("msg", "ok");
            } else {
                pd.put("msg", "no");
            }
            pdList.add(pd);
            map.put("list", pdList);
        } catch (Exception e) {
            logger.error(e.toString(), e);
        } finally {
            logAfter(logger);
        }
        return AppUtil.returnObject(pd, map);
    }

    /**
     * 判断关键词是否存在
     */
    @RequestMapping(value = "/hasK")
    @ResponseBody
    public Object hasK() {
        Map<String, String> map = new HashMap<String, String>();
        String errInfo = "success";
        PageData pd = new PageData();
        try {
            pd = this.getPageData();
            pd.put("STATUS", "3");
            if (weixinTextMsgService.findByKw(pd) != null || weixinCommandService.findByKw(pd) != null || weixinImgMsgService.findByKw(pd) != null) {
                errInfo = "error";
            }
        } catch (Exception e) {
            logger.error(e.toString(), e);
        }
        map.put("result", errInfo);                //返回结果
        return AppUtil.returnObject(new PageData(), map);
    }

    /*
     * 导出到excel
     * @return
     */
    @RequestMapping(value = "/excel")
    public ModelAndView exportExcel() {
        logBefore(logger, "导出Textmsg到excel");
        if (!PermissionHandler.buttonJurisdiction(menuUrl, "cha")) {
            return null;
        }
        ModelAndView mv = new ModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
        try {
            Map<String, Object> dataMap = new HashMap<String, Object>();
            List<String> titles = new ArrayList<String>();
            titles.add("关键词");    //1
            titles.add("内容");    //2
            titles.add("创建时间");    //3
            titles.add("状态");    //4
            titles.add("备注");    //5
            dataMap.put("titles", titles);
            List<PageData> varOList = weixinTextMsgService.listAll(pd);
            List<PageData> varList = new ArrayList<PageData>();
            for (int i = 0; i < varOList.size(); i++) {
                PageData vpd = new PageData();
                vpd.put("var1", varOList.get(i).getString("KEYWORD"));    //1
                vpd.put("var2", varOList.get(i).getString("CONTENT"));    //2
                vpd.put("var3", varOList.get(i).getString("CREATETIME"));    //3
                vpd.put("var4", varOList.get(i).get("STATUS").toString());    //4
                vpd.put("var5", varOList.get(i).getString("BZ"));    //5
                varList.add(vpd);
            }
            dataMap.put("varList", varList);
            ObjectExcelView erv = new ObjectExcelView();
            mv = new ModelAndView(erv, dataMap);
        } catch (Exception e) {
            logger.error(e.toString(), e);
        }
        return mv;
    }

    /* ===============================权限================================== */
    public Map<String, String> getHC() {
        Subject currentUser = SecurityUtils.getSubject();  //shiro管理的session
        Session session = currentUser.getSession();
        return (Map<String, String>) session.getAttribute(Constant.SESSION_QX);
    }
    /* ===============================权限================================== */

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        binder.registerCustomEditor(Date.class, new CustomDateEditor(format, true));
    }
}
