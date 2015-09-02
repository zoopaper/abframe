package org.abframe.controller;

import net.common.utils.date.DateUtil;
import net.common.utils.uuid.UuidUtil;
import org.abframe.common.PermissionHandler;
import org.abframe.controller.base.BaseController;
import org.abframe.entity.Page;
import org.abframe.service.WeixinCommandService;
import org.abframe.util.AppUtil;
import org.abframe.util.Constant;
import org.abframe.util.ObjectExcelView;
import org.abframe.util.PageData;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping(value = "/command")
public class WeixinCommandController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(WeixinCommandController.class);

    String menuUrl = "command/list";

    @Autowired
    private WeixinCommandService weixinCommandService;

    @RequestMapping(value = "/save")
    public ModelAndView save() throws Exception {
        if (!PermissionHandler.buttonJurisdiction(menuUrl, "add")) {
            return null;
        }
        ModelAndView mv = new ModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
        pd.put("COMMAND_ID", UuidUtil.genTerseUuid());
        pd.put("CREATETIME", DateUtil.getDateTimeStr());
        weixinCommandService.save(pd);
        mv.addObject("msg", "success");
        mv.setViewName("save_result");
        return mv;
    }


    @RequestMapping(value = "/delete")
    public void delete(PrintWriter out) {
        if (!PermissionHandler.buttonJurisdiction(menuUrl, "del")) {
            return;
        }
        PageData pd = new PageData();
        try {
            pd = this.getPageData();
            weixinCommandService.delete(pd);
            out.write("success");
            out.close();
        } catch (Exception e) {
            LOGGER.error("Controller weixin command exception", e);
        }

    }


    @RequestMapping(value = "/edit")
    public ModelAndView edit() {
        ModelAndView mv = new ModelAndView();
        if (!PermissionHandler.buttonJurisdiction(menuUrl, "edit")) {
            return null;
        }
        try {
            PageData pd = new PageData();
            pd = this.getPageData();
            weixinCommandService.edit(pd);
            mv.addObject("msg", "success");
            mv.setViewName("save_result");
        } catch (Exception e) {
            LOGGER.error("Controller weixin command exception", e);
        }
        return mv;
    }


    @RequestMapping(value = "/list")
    public ModelAndView list(Page page) {
        //if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;} //校验权限
        ModelAndView mv = new ModelAndView();
        PageData pd = new PageData();
        try {
            pd = this.getPageData();
            String KEYWORD = pd.getString("KEYWORD");
            if (null != KEYWORD && !"".equals(KEYWORD)) {
                pd.put("KEYWORD", KEYWORD.trim());
            }
            page.setPd(pd);
            List<PageData> varList = weixinCommandService.list(page);    //列出Command列表
            mv.setViewName("weixin/command/command_list");
            mv.addObject("varList", varList);
            mv.addObject("pd", pd);
            mv.addObject(Constant.SESSION_QX, this.getHC());    //按钮权限
        } catch (Exception e) {
            LOGGER.error("Controller weixin commond exception", e);
        }
        return mv;
    }


    @RequestMapping(value = "/toAdd")
    public ModelAndView toAdd() {
        ModelAndView mv = new ModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
        try {
            mv.setViewName("weixin/command/command_edit");
            mv.addObject("msg", "save");
            mv.addObject("pd", pd);
        } catch (Exception e) {
            LOGGER.error("Controller weixin command exception", e);
        }
        return mv;
    }

    /**
     * 去修改页面
     */
    @RequestMapping(value = "/toEdit")
    public ModelAndView toEdit() {
        ModelAndView mv = new ModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
        try {
            pd = weixinCommandService.findById(pd);    //根据ID读取
            mv.setViewName("weixin/command/command_edit");
            mv.addObject("msg", "edit");
            mv.addObject("pd", pd);
        } catch (Exception e) {
            LOGGER.error("Controller weixin command exception", e);
        }
        return mv;
    }

    @RequestMapping(value = "/deleteAll")
    @ResponseBody
    public Object deleteAll() {
        if (!PermissionHandler.buttonJurisdiction(menuUrl, "dell")) {
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
                weixinCommandService.deleteAll(ArrayDATA_IDS);
                pd.put("msg", "ok");
            } else {
                pd.put("msg", "no");
            }
            pdList.add(pd);
            map.put("list", pdList);
        } catch (Exception e) {
            LOGGER.error("Controller weixin command exception", e);
        }
        return AppUtil.returnObject(pd, map);
    }

    /*
     * 导出到excel
     * @return
     */
    @RequestMapping(value = "/excel")
    public ModelAndView exportExcel() {
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
            titles.add("应用路径");    //2
            titles.add("创建时间");    //3
            titles.add("状态");    //4
            titles.add("备注");    //5
            dataMap.put("titles", titles);
            List<PageData> varOList = weixinCommandService.listAll(pd);
            List<PageData> varList = new ArrayList<PageData>();
            for (int i = 0; i < varOList.size(); i++) {
                PageData vpd = new PageData();
                vpd.put("var1", varOList.get(i).getString("KEYWORD"));    //1
                vpd.put("var2", varOList.get(i).getString("COMMANDCODE"));    //2
                vpd.put("var3", varOList.get(i).getString("CREATETIME"));    //3
                vpd.put("var4", varOList.get(i).get("STATUS").toString());    //4
                vpd.put("var5", varOList.get(i).getString("BZ"));    //5
                varList.add(vpd);
            }
            dataMap.put("varList", varList);
            ObjectExcelView erv = new ObjectExcelView();
            mv = new ModelAndView(erv, dataMap);
        } catch (Exception e) {
            LOGGER.error("Controller weixin command exception", e);
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
