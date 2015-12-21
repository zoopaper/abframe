package org.abframe.util;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.File;

/**
 * 路径工具类
 */
public class PathUtil {

    public static String getClasspath() {
        String path = (String.valueOf(Thread.currentThread().getContextClassLoader().getResource("")) + "../../").replaceAll("file:/", "").replaceAll("%20", " ").trim();
        if (path.indexOf(":") != 1) {
            path = File.separator + path;
        }
        return path;
    }

    public static String getClassResources() {
        String path = (String.valueOf(Thread.currentThread().getContextClassLoader().getResource(""))).replaceAll("file:/", "").replaceAll("%20", " ").trim();
        if (path.indexOf(":") != 1) {
            path = File.separator + path;
        }
        return path;
    }

    public static String getApplicationContextPath() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        StringBuffer contentPath = new StringBuffer();
        contentPath.append(request.getScheme() + "://");
        contentPath.append(request.getServerName() + ":");
        contentPath.append(request.getServerPort() + "");
        contentPath.append(request.getContextPath() + "/");
        return contentPath.toString();
    }


    public static String getServerRealPath() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String realPath = request.getSession().getServletContext().getRealPath("/");
        return realPath;
    }
}
