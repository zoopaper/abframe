package org.abframe.controller;

import com.google.common.base.Strings;
import net.common.utils.uuid.UuidUtil;
import org.abframe.controller.base.BaseController;
import org.abframe.entity.Page;
import org.abframe.service.DictService;
import org.abframe.util.AppUtil;
import org.abframe.util.PageData;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
    public ModelAndView save(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ModelAndView mv = this.getModelAndView();

        PageData pd = new PageData();
        pd = this.getPageData();

        PageData pdp = new PageData();
        pdp = this.getPageData();

        String parentId = pd.getString("parentId");

        pdp.put("id", parentId);
        String id = ServletRequestUtils.getStringParameter(request, "id", "");
        String code = ServletRequestUtils.getStringParameter(request, "code", "");

        if (!Strings.isNullOrEmpty(id)) {
            if (null != parentId && "0".equals(parentId)) {
                pd.put("level", 1);
                pd.put("parentCode", pd.getString("code"));
            } else {
                pdp = dictService.findById(pdp);
                pd.put("level", Integer.parseInt(pdp.get("level").toString()) + 1);
                pd.put("P_BM", pdp.getString("code") + "_" + pd.getString("code"));
            }
            pd.put("id", UuidUtil.genTerseUuid());
            dictService.save(pd);
        } else {
            pdp = dictService.findById(pdp);
            if (null != parentId && "0".equals(parentId)) {
                pd.put("parentCode", pd.getString("code"));
            } else {
                pd.put("parentCode", pdp.getString("code") + "_" + pd.getString("code"));
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
        String parentId = pd.getString("parentId");

        try {
            if (!Strings.isNullOrEmpty(parentId) && !"0".equals(parentId)) {

                //返回按钮用
                PageData pdp = new PageData();
                pdp = this.getPageData();

                pdp.put("id", parentId);
                pdp = dictService.findById(pdp);
                modelAndView.addObject("pdp", pdp);

                //头部导航
                szdList = new ArrayList<PageData>();
                this.getDictName(parentId);    //	逆序
                Collections.reverse(szdList);

            }

            String name = pd.getString("name");
            if (!Strings.isNullOrEmpty(name)) {
                name = name.trim();
                pd.put("name", name);
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
            pdps.put("id", parentId);
            pdps = dictService.findById(pdps);
            if (pdps != null) {
                szdList.add(pdps);
                String pid = pdps.getString("parentId");
                this.getDictName(pid);
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
