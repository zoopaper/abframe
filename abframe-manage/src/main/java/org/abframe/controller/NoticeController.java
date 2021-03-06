package org.abframe.controller;

import net.common.utils.date.DateUtil;
import org.abframe.controller.base.BaseController;
import org.abframe.entity.Page;
import org.abframe.service.NoticeService;
import org.abframe.util.Constant;
import org.abframe.util.PageData;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.util.List;

@Controller
@RequestMapping(value = "/notice")
public class NoticeController extends BaseController {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(NoticeController.class);

    @Autowired
    private NoticeService noticeService;


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
            List<PageData> varList = noticeService.list(page);
            mv.setViewName("notice/noticeList");
            mv.addObject("varList", varList);
            mv.addObject("pd", pd);
            mv.addObject(Constant.SESSION_QX, this.getHC());
        } catch (Exception e) {
            LOGGER.error("Controller notice exception.", e);
        }

        return mv;
    }

    @RequestMapping(value = "/toAdd", method = RequestMethod.GET)
    public ModelAndView toAdd() {
        ModelAndView mv = new ModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
        try {
            mv.setViewName("notice/noticeEdit");
            mv.addObject("msg", "save");
        } catch (Exception e) {
            LOGGER.error("Controller notice exception.", e);
        }
        return mv;
    }

    @RequestMapping(value = "/toEdit", method = RequestMethod.GET)
    public ModelAndView toEdit() {
        ModelAndView mv = new ModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
        try {
            pd = noticeService.findById(pd);

            mv.setViewName("notice/noticeEdit");
            mv.addObject("msg", "updateRoleById");
            mv.addObject("pd", pd);
        } catch (Exception e) {
            LOGGER.error("Controller notice exception.", e);
        }
        return mv;
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public ModelAndView edit(PrintWriter out) {
        ModelAndView mv = new ModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
        String sequence = pd.getString("sequence");
        try {
            pd.put("uptime", DateUtil.getDateTimeStr());
            pd.put("sequence", "".equals(sequence) ? 0 : sequence);

            noticeService.edit(pd);

            mv.addObject("msg", "success");
            mv.setViewName("save_result");
        } catch (Exception e) {

        }
        return mv;
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ModelAndView save(PrintWriter out) throws Exception {
        ModelAndView mv = new ModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
        String sequence = pd.getString("sequence");
        pd.put("addtime", DateUtil.getDateTimeStr());
        pd.put("uptime", DateUtil.getDateTimeStr());
        pd.put("sequence", "".equals(sequence) ? 0 : sequence);
        noticeService.save(pd);
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
            noticeService.delete(pd);
            out.write("success");
            out.close();
        } catch (Exception e) {
            LOGGER.error("Controller notice exception.", e);
        }

    }

}
