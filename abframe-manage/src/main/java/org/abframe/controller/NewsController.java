package org.abframe.controller;

import net.common.utils.date.DateUtil;
import org.abframe.controller.base.BaseController;
import org.abframe.entity.Page;
import org.abframe.service.NewsService;
import org.abframe.util.FileUtil;
import org.abframe.util.Freemarker;
import org.abframe.util.PageData;
import org.abframe.util.PathUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/news")
public class NewsController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(NewsController.class);

    @Autowired
    private NewsService newsService;

    @RequestMapping(value = "/list")
    public ModelAndView listUsers(HttpSession session, Page page) {
        ModelAndView mv = new ModelAndView();
        PageData pd = new PageData();
        try {
            pd = this.getPageData();
            //检索条件================================
            String title = pd.getString("title");
            String publisher = pd.getString("publisher");
            if (null != title && !"".equals(title)) {
                title = title.trim();
                pd.put("title", title);
            }
            if (null != publisher && !"".equals(publisher)) {
                publisher = publisher.trim();
                pd.put("publisher", publisher);
            }
            //检索条件================================
            page.setPd(pd);
            List<PageData> varList = newsService.list(page);
            mv.setViewName("news/newsList");
            mv.addObject("varList", varList);
            mv.addObject("pd", pd);
        } catch (Exception e) {
            LOGGER.error("Controller news exception.", e);
        }
        return mv;
    }


    @RequestMapping(value = "/toAdd", method = RequestMethod.GET)
    public ModelAndView toAdd() {
        ModelAndView mv = new ModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
        try {
            mv.setViewName("news/newsEdit");
            mv.addObject("msg", "save");
        } catch (Exception e) {
            LOGGER.error("Controller news exception.", e);
        }
        return mv;
    }


    @RequestMapping(value = "/toEdit", method = RequestMethod.GET)
    public ModelAndView toEdit() {
        ModelAndView mv = new ModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
        try {
            pd = newsService.findById(pd);
            mv.setViewName("news/newsEdit");
            mv.addObject("msg", "updateRoleById");
            mv.addObject("pd", pd);
        } catch (Exception e) {
            LOGGER.error("Controller news exception.", e);
        }
        return mv;
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public ModelAndView edit(PrintWriter out) throws Exception {
        ModelAndView mv = new ModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();

        HttpServletRequest request = this.getRequest();
        String ip;
        if (request.getHeader("x-forwarded-for") == null) {
            ip = request.getRemoteAddr();
        } else {
            ip = request.getHeader("x-forwarded-for");
        }
        String sequence = pd.getString("sequence");

        pd.put("pip", ip);
        pd.put("uptime", DateUtil.getDateTimeStr());
        pd.put("sequence", "".equals(sequence) ? 0 : sequence);

        newsService.edit(pd);
        mv.addObject("msg", "success");
        mv.setViewName("save_result");
        return mv;
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ModelAndView save(PrintWriter out) throws Exception {
        ModelAndView mv = new ModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();

        HttpServletRequest request = this.getRequest();
        String ip;
        if (request.getHeader("x-forwarded-for") == null) {
            ip = request.getRemoteAddr();
        } else {
            ip = request.getHeader("x-forwarded-for");
        }

        String sequence = pd.getString("sequence");

        pd.put("pip", ip);
        pd.put("addtime", DateUtil.getDateTimeStr());
        pd.put("uptime", DateUtil.getDateTimeStr());
        pd.put("hits", 0);
        pd.put("sequence", "".equals(sequence) ? 0 : sequence);

        newsService.save(pd);
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
            newsService.delete(pd);
            out.write("success");
            out.close();
        } catch (Exception e) {
            LOGGER.error("Controller news exception.", e);
        }
    }


    @RequestMapping(value = "/createHtml")
    public void createHtml(PrintWriter out) throws Exception {
        ModelAndView mv = new ModelAndView();
        PageData pd = new PageData();
        try {
            pd = this.getPageData();
            //生成代码前,先清空之前生成的代码
            FileUtil.delFolder(PathUtil.getClasspath() + "html/news");
            //创建数据模型
            Map<String, Object> root = new HashMap<String, Object>();
            List<PageData> varList = newsService.newslist(pd);
            root.put("varList", varList);
            String filePath = "html/news/";
            String ftlPath = "news";
            /*生成*/
            Freemarker.printFile("index.ftl", root, "index.html", filePath, ftlPath);
            out.write("success");
            out.close();
        } catch (Exception e) {
            LOGGER.error("Controller news exception.", e);
        }

    }


}
