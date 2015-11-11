package org.abframe.controller;

import org.abframe.controller.base.BaseController;
import org.abframe.entity.Page;
import org.abframe.service.CompanyService;
import org.abframe.util.PageData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * 招聘公司管理
 * <p/>
 * User : krisibm@163.com
 * Date: 2015/9/16
 * Time: 14:08
 */
@Controller
@RequestMapping("/company/*")
public class CompanyController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CompanyController.class);

    @Autowired
    private CompanyService companyService;

    @RequestMapping(value = "companyList")
    public ModelAndView companyList(Page page) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("company/companyList");
        PageData pageData;

        try {
            pageData = this.getPageData();
            page.setPd(pageData);
            List<PageData> companyList = companyService.getCompanyListWithPage(page);

            modelAndView.addObject("companyList", companyList);
            modelAndView.addObject("pd", pageData);
        } catch (Exception e) {
            LOGGER.error("Controller companyList exception.", e);
        }
        return modelAndView;
    }

}
