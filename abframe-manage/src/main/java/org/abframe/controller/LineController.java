package org.abframe.controller;

import net.common.utils.uuid.UuidUtil;
import org.abframe.controller.base.BaseController;
import org.abframe.entity.Page;
import org.abframe.service.LineService;
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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping(value = "/line")
public class LineController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(LineController.class);

    @Autowired
    private LineService lineService;


    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ModelAndView save() throws Exception {
        ModelAndView mv = new ModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
        pd.put("LINE_ID", UuidUtil.genTerseUuid());    //主键
        //pd.put("PARENT_ID", "");	//父类ID
        lineService.save(pd);
        mv.addObject("msg", "success");
        mv.setViewName("save_result");
        return mv;
    }


    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public void delete(PrintWriter out) {
        PageData pd = new PageData();
        try {
            pd = this.getPageData();
            lineService.delete(pd);
            out.write("success");
            out.close();
        } catch (Exception e) {
            LOGGER.error("Controller line exception.", e);
        }

    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public ModelAndView edit() throws Exception {
        ModelAndView mv = new ModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
        lineService.edit(pd);
        mv.addObject("msg", "success");
        mv.setViewName("save_result");
        return mv;
    }


    @RequestMapping(value = "/list")
    public ModelAndView list(Page page) {
        ModelAndView mv = new ModelAndView();
        PageData pd = new PageData();
        try {
            pd = this.getPageData();

            String TITLE = pd.getString("TITLE");
            String PARENT_ID = pd.getString("PARENT_ID");
            if (null != TITLE && !"".equals(TITLE)) {
                TITLE = TITLE.trim();
                pd.put("TITLE", TITLE);
            }
            if (null != PARENT_ID && !"".equals(PARENT_ID)) {
                PARENT_ID = PARENT_ID.trim();
                pd.put("PARENT_ID", PARENT_ID);
            }

            page.setPd(pd);
            List<PageData> varList = lineService.list(page);
            mv.setViewName("line/lineList");
            mv.addObject("varList", varList);
            mv.addObject("pd", pd);
            mv.addObject(Constant.SESSION_QX, this.getHC());    //按钮权限
        } catch (Exception e) {
            LOGGER.error("Controller line exception.", e);
        }
        return mv;
    }


    @RequestMapping(value = "/toAdd", method = RequestMethod.GET)
    public ModelAndView toAdd() {
        ModelAndView mv = new ModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
        try {
            mv.setViewName("line/lineEdit");
            mv.addObject("msg", "save");
            mv.addObject("pd", pd);
        } catch (Exception e) {
            LOGGER.error("Controller line exception.", e);
        }
        return mv;
    }


    @RequestMapping(value = "/toEdit", method = RequestMethod.GET)
    public ModelAndView toEdit() {
        ModelAndView mv = new ModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
        try {
            pd = lineService.findById(pd);    //根据ID读取
            mv.setViewName("line/lineEdit");
            mv.addObject("msg", "edit");
            mv.addObject("pd", pd);
        } catch (Exception e) {
            LOGGER.error("Controller line exception.", e);
        }
        return mv;
    }


    @RequestMapping(value = "/deleteAll")
    @ResponseBody
    public Object deleteAll() {
        Map map = new HashMap();
        PageData pd = new PageData();
        try {
            pd = this.getPageData();
            List<PageData> pdList = new ArrayList<PageData>();
            String DATA_IDS = pd.getString("DATA_IDS");
            if (null != DATA_IDS && !"".equals(DATA_IDS)) {
                String ArrayDATA_IDS[] = DATA_IDS.split(",");
                lineService.deleteAll(ArrayDATA_IDS);
                pd.put("msg", "ok");
            } else {
                pd.put("msg", "no");
            }
            pdList.add(pd);
            map.put("list", pdList);
        } catch (Exception e) {
            LOGGER.error("Controller line exception.", e);
        }
        return AppUtil.returnObject(pd, map);
    }

    /*
     * 导出到excel
     * @return
     */
    @RequestMapping(value = "/excel")
    public ModelAndView exportExcel() {
        ModelAndView mv = new ModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
        try {
            Map<String, Object> dataMap = new HashMap<String, Object>();
            List<String> titles = new ArrayList<String>();
            titles.add("名称");    //1
            titles.add("链接");    //2
            titles.add("线路");    //3
            titles.add("类型");    //4
            titles.add("排序");    //5
            titles.add("父类ID");    //6
            dataMap.put("titles", titles);
            List<PageData> varOList = lineService.listAll(pd);
            List<PageData> varList = new ArrayList<PageData>();
            for (int i = 0; i < varOList.size(); i++) {
                PageData vpd = new PageData();
                vpd.put("var1", varOList.get(i).getString("TITLE"));    //1
                vpd.put("var2", varOList.get(i).getString("LINE_URL"));    //2
                vpd.put("var3", varOList.get(i).getString("LINE_ROAD"));    //3
                vpd.put("var4", varOList.get(i).getString("TYPE"));    //4
                vpd.put("var5", varOList.get(i).getString("LINE_ORDER"));    //5
                vpd.put("var6", varOList.get(i).getString("PARENT_ID"));    //6
                varList.add(vpd);
            }
            dataMap.put("varList", varList);
            ObjectExcelView erv = new ObjectExcelView();
            mv = new ModelAndView(erv, dataMap);
        } catch (Exception e) {
            LOGGER.error("Controller line exception.", e);
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
