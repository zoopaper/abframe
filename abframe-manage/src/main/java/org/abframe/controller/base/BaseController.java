package org.abframe.controller.base;


import org.abframe.entity.Page;
import org.abframe.util.Constant;
import org.abframe.util.PageData;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class BaseController {

    /**
     * 封装请求数据
     */
    public PageData getPageData() {
        return new PageData(this.getRequest());
    }

    public HttpServletRequest getRequest() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return request;
    }

    /**
     * 得到分页列表的信息
     */
    public Page getPage() {
        return new Page();
    }

    /**
     * shiro管理的session
     *
     * @return
     */
    public Map<String, String> getHC() {
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession();
        return (Map<String, String>) session.getAttribute(Constant.SESSION_QX);
    }
}
