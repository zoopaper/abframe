package org.abframe.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.abframe.controller.base.BaseController;
import org.abframe.entity.Page;
import org.abframe.service.RecommendService;
import org.abframe.util.Constant;
import org.abframe.util.DateUtil;
import org.abframe.util.PageData;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

/**
 * 推荐管理
 */
@Controller
@RequestMapping(value = "/recommend")
public class RecommendController extends BaseController {

    @Resource(name = "recommendService")
    private RecommendService recommendService;


    @RequestMapping(value = "/list")
    public ModelAndView listUsers(HttpSession session, Page page) throws Exception {
        logBefore(logger, "特别推荐列表");
        ModelAndView mv = new ModelAndView();
        PageData pd = new PageData();
        try {
            pd = this.getPageData();
            //检索条件================================
            String title = pd.getString("title");
            if (null != title && !"".equals(title)) {
                title = title.trim();
                pd.put("title", title);
            }
            //检索条件================================
            page.setPd(pd);
            List<PageData> varList = recommendService.list(page);

            mv.setViewName("recommend/recList");
            mv.addObject("varList", varList);
            mv.addObject("pd", pd);
            //按钮权限
            mv.addObject(Constant.SESSION_QX, this.getHC());
        } catch (Exception e) {
            logger.error(e.toString(), e);
        }
        return mv;
    }


    @RequestMapping(value = "/toAdd", method = RequestMethod.GET)
    public ModelAndView toAdd() {
        ModelAndView mv = new ModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
        try {
            mv.setViewName("recommend/recEdit");
            mv.addObject("msg", "save");
        } catch (Exception e) {
            logger.error(e.toString(), e);
        }
        return mv;
    }


    @RequestMapping(value = "/toEdit", method = RequestMethod.GET)
    public ModelAndView toEdit() {
        ModelAndView mv = new ModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
        try {
            pd = recommendService.findById(pd);
            mv.setViewName("recommend/recEdit");
            mv.addObject("msg", "edit");
            mv.addObject("pd", pd);
        } catch (Exception e) {
            logger.error(e.toString(), e);
        }
        return mv;
    }


    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public ModelAndView edit(PrintWriter out) throws Exception {
        ModelAndView mv = new ModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
        String sequence = pd.getString("sequence");

        pd.put("uptime", DateUtil.getTime());
        pd.put("sequence", "".equals(sequence) ? 0 : sequence);

        recommendService.edit(pd);
        mv.addObject("msg", "success");
        mv.setViewName("save_result");
        return mv;
    }


    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ModelAndView save(PrintWriter out) throws Exception {
        ModelAndView mv = new ModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
        String sequence = pd.getString("sequence");
        pd.put("addtime", DateUtil.getTime());
        pd.put("uptime", DateUtil.getTime());
        pd.put("sequence", "".equals(sequence) ? 0 : sequence);
        recommendService.save(pd);
        mv.addObject("msg", "success");
        mv.setViewName("save_result");
        return mv;
    }

    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public void delete(PrintWriter out) throws Exception {
        ModelAndView mv = new ModelAndView();
        PageData pd = new PageData();
        try {
            pd = this.getPageData();
            recommendService.delete(pd);
            out.write("success");
            out.close();
        } catch (Exception e) {
            logger.error(e.toString(), e);
        }
    }

    /* ===============================权限================================== */
    public Map<String, String> getHC() {
        Subject currentUser = SecurityUtils.getSubject();  //shiro管理的session
        Session session = currentUser.getSession();
        return (Map<String, String>) session.getAttribute(Constant.SESSION_QX);
    }
    /* ===============================权限================================== */
}
