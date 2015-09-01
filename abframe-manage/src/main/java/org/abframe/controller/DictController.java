package org.abframe.controller;

import net.common.utils.uuid.UuidUtil;
import org.abframe.controller.base.BaseController;
import org.abframe.entity.Page;
import org.abframe.service.DictService;
import org.abframe.util.AppUtil;
import org.abframe.util.PageData;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.io.PrintWriter;
import java.util.*;

/**
 * 数据字典
 */
@Controller
@RequestMapping(value = "/dict")
public class DictController extends BaseController {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(DictController.class);

    @Autowired
    private DictService dictService;


    @RequestMapping(value = "/save")
    public ModelAndView save(PrintWriter out) throws Exception {
        ModelAndView mv = this.getModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
        PageData pdp = new PageData();
        pdp = this.getPageData();

        String PARENT_ID = pd.getString("PARENT_ID");
        pdp.put("ZD_ID", PARENT_ID);

        if (null == pd.getString("ZD_ID") || "".equals(pd.getString("ZD_ID"))) {
            if (null != PARENT_ID && "0".equals(PARENT_ID)) {
                pd.put("JB", 1);
                pd.put("P_BM", pd.getString("BIANMA"));
            } else {
                pdp = dictService.findById(pdp);
                pd.put("JB", Integer.parseInt(pdp.get("JB").toString()) + 1);
                pd.put("P_BM", pdp.getString("BIANMA") + "_" + pd.getString("BIANMA"));
            }
            pd.put("ZD_ID", UuidUtil.genTerseUuid());    //ID
            dictService.save(pd);
        } else {
            pdp = dictService.findById(pdp);
            if (null != PARENT_ID && "0".equals(PARENT_ID)) {
                pd.put("P_BM", pd.getString("BIANMA"));
            } else {
                pd.put("P_BM", pdp.getString("BIANMA") + "_" + pd.getString("BIANMA"));
            }
            dictService.edit(pd);
        }
        mv.addObject("msg", "success");
        mv.setViewName("save_result");
        return mv;
    }


    List<PageData> szdList;

    @RequestMapping
    public ModelAndView list(Page page) {

        ModelAndView modelAndView = this.getModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
        String PARENT_ID = pd.getString("PARENT_ID");

        try {
            if (null != PARENT_ID && !"".equals(PARENT_ID) && !"0".equals(PARENT_ID)) {

                //返回按钮用
                PageData pdp = new PageData();
                pdp = this.getPageData();

                pdp.put("ZD_ID", PARENT_ID);
                pdp = dictService.findById(pdp);
                modelAndView.addObject("pdp", pdp);

                //头部导航
                szdList = new ArrayList<PageData>();
                this.getDictName(PARENT_ID);    //	逆序
                Collections.reverse(szdList);

            }

            String NAME = pd.getString("NAME");
            if (null != NAME && !"".equals(NAME)) {
                NAME = NAME.trim();
                pd.put("NAME", NAME);
            }
            page.setShowCount(5);    //设置每页显示条数
            page.setPd(pd);
            List<PageData> varList = dictService.dictlistPage(page);

            modelAndView.setViewName("dict/dictList");
            modelAndView.addObject("varList", varList);
            modelAndView.addObject("varsList", szdList);
            modelAndView.addObject("pd", pd);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return modelAndView;
    }

    //递归
    public void getDictName(String parentId) {
        try {
            PageData pdps = new PageData();
            pdps.put("ZD_ID", parentId);
            pdps = dictService.findById(pdps);
            if (pdps != null) {
                szdList.add(pdps);
                String PARENT_IDs = pdps.getString("PARENT_ID");
                this.getDictName(PARENT_IDs);
            }
        } catch (Exception e) {
            LOGGER.error("Controller dict exception.", e);
        }
    }


    @RequestMapping(value = "/toAdd", method = RequestMethod.GET)
    public ModelAndView toAdd(Page page) {
        ModelAndView mv = this.getModelAndView();
        PageData pd = new PageData();
        try {
            pd = this.getPageData();
            mv.setViewName("dict/dictEdit");
            mv.addObject("pd", pd);
        } catch (Exception e) {
            LOGGER.error("Controller dict exception.", e);
        }
        return mv;
    }


    @RequestMapping(value = "/toEdit", method = RequestMethod.GET)
    public ModelAndView toEdit(String ROLE_ID) throws Exception {
        ModelAndView mv = this.getModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
        pd = dictService.findById(pd);
        if (Integer.parseInt(dictService.findCount(pd).get("ZS").toString()) != 0) {
            mv.addObject("msg", "no");
        } else {
            mv.addObject("msg", "ok");
        }
        mv.setViewName("dict/dictEdit");
        mv.addObject("pd", pd);
        return mv;
    }


    /**
     * 判断编码是否存在
     */
    @RequestMapping(value = "/has")
    public void has(PrintWriter out) {
        PageData pd = new PageData();
        try {
            pd = this.getPageData();
            if (dictService.findBmCount(pd) != null) {
                out.write("error");
            } else {
                out.write("success");
            }
            out.close();
        } catch (Exception e) {
            LOGGER.error("Controller dict exception.", e);
        }

    }


    @RequestMapping(value = "/del", method = RequestMethod.GET)
    @ResponseBody
    public Object del() {
        Map<String, String> map = new HashMap<String, String>();
        PageData pd = new PageData();
        String errInfo = "";
        try {
            pd = this.getPageData();
            if (Integer.parseInt(dictService.findCount(pd).get("ZS").toString()) != 0) {
                errInfo = "false";
            } else {
                dictService.delete(pd);
                errInfo = "success";
            }
        } catch (Exception e) {
            LOGGER.error("Controller dict exception.", e);
        }
        map.put("result", errInfo);
        return AppUtil.returnObject(new PageData(), map);
    }

}
