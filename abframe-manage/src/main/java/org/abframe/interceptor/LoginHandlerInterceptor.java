package org.abframe.interceptor;

import org.abframe.entity.UserBean;
import org.abframe.util.Constant;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginHandlerInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String path = request.getServletPath();
        if (path.matches(Constant.NO_INTERCEPTOR_PATH)) {
            return true;
        } else {
            //shiro管理的session
            Subject currentUser = SecurityUtils.getSubject();
            Session session = currentUser.getSession();
            UserBean user = (UserBean) session.getAttribute(Constant.SESSION_USER);
            if (user != null) {
                path = path.substring(1, path.length());
//                boolean b = PermissionHandler.hasPerm(path);
//                if (!b) {
//                    response.sendRedirect(request.getContextPath() + Constant.LOGIN);
//                }
                return true;
            } else {
                //登陆过滤
                response.sendRedirect(request.getContextPath() + Constant.LOGIN);
                return false;

            }
        }
    }

}
