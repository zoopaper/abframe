package org.abframe.controller;

import com.google.common.base.Strings;
import net.common.utils.date.DateUtil;
import org.abframe.controller.base.BaseController;
import org.abframe.entity.Menu;
import org.abframe.entity.RoleBean;
import org.abframe.entity.UserBean;
import org.abframe.service.MenuService;
import org.abframe.service.RoleService;
import org.abframe.service.UserService;
import org.abframe.util.AppUtil;
import org.abframe.util.Constant;
import org.abframe.util.PageData;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;


@Controller
public class LoginController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private UserService userService;
    @Autowired
    private MenuService menuService;
    @Autowired
    private RoleService roleService;

    @RequestMapping(value = "/toLogin", method = RequestMethod.GET)
    public ModelAndView toLogin() {
        ModelAndView mv = new ModelAndView();
        PageData pd;
        pd = this.getPageData();
        mv.setViewName("common/login");
        mv.addObject("pd", pd);
        return mv;
    }

    @RequestMapping(value = "/login", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public Object login() {
        Map<String, String> map = new HashMap<String, String>();
        PageData pd = this.getPageData();
        String result = "";
        String account = pd.getString("userName");
        String password = pd.getString("password");
        String code = pd.getString("code");
        try {
            if (!Strings.isNullOrEmpty(account) && !Strings.isNullOrEmpty(password) && !Strings.isNullOrEmpty(code)) {
                Subject currentUser = SecurityUtils.getSubject();
                Session session = currentUser.getSession();
                String sessionCode = (String) session.getAttribute(Constant.SESSION_SECURITY_CODE);

                pd.put("account", account);
                if (!Strings.isNullOrEmpty(sessionCode) && sessionCode.equalsIgnoreCase(code)) {
                    String passwd = new SimpleHash("SHA-1", account, password).toString();
                    pd.put("password", passwd);
                    UserBean user = userService.getUserByNameAndPwd(pd);
                    if (pd != null) {
                        if (Strings.isNullOrEmpty(user.getAvatarUrl()))
                            user.setAvatarUrl("/static/img/default-head.png");
                        session.setAttribute(Constant.SESSION_USER, user);
                        session.removeAttribute(Constant.SESSION_SECURITY_CODE);

                        Subject subject = SecurityUtils.getSubject();
                        UsernamePasswordToken token = new UsernamePasswordToken(account, password);

                        pd.put("lastLogin", DateUtil.getDateTimeStr().toString());
                        userService.updateLastLogin(pd);
                        result = "success";
                        try {
                            subject.login(token);
                        } catch (AuthenticationException e) {
                            result = "身份验证失败!";
                        }
                    } else {
                        //用户名或密码有误
                        result = "usererror";
                    }
                } else {
                    //验证码输入有误
                    result = "codeerror";
                }
            } else {
                result = "error";
            }
        } catch (Exception e) {
            LOGGER.error("Controller login exception.", e);
        }
        map.put("result", result);
        return AppUtil.returnObject(new PageData(), map);
    }

    /**
     * 访问系统首页
     */
    @RequestMapping(value = "/main/{changeMenu}")
    public ModelAndView index(@PathVariable("changeMenu") String changeMenu) {
        ModelAndView mv = new ModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
        try {
            Subject currentUser = SecurityUtils.getSubject();
            Session session = currentUser.getSession();
            UserBean user = (UserBean) session.getAttribute(Constant.SESSION_USER);
            if (user != null) {
                UserBean userRole = (UserBean) session.getAttribute(Constant.SESSION_USERROL);
                if (null == userRole) {
                    user = userService.getUserAndRoleById(user.getId());
                    session.setAttribute(Constant.SESSION_USERROL, user);
                } else {
                    user = userRole;
                }
                RoleBean role = user.getRole();
                String rolePerms = role != null ? role.getMenuId() : "";

                //避免每次拦截用户操作时查询数据库，以下将用户所属角色权限、用户权限限都存入session
                session.setAttribute(Constant.SESSION_ROLE_RIGHTS, rolePerms);
                session.setAttribute(Constant.SESSION_USERNAME, user.getAccount());

                List<Menu> menus = new ArrayList<Menu>();

                if (null == session.getAttribute(Constant.SESSION_ALL_MENU_LIST)) {
                    menus = menuService.getAllMenu();
                    if (!Strings.isNullOrEmpty(rolePerms)) {
                        List menuIds = Arrays.asList(rolePerms.split(","));
                        for (Menu menu : menus) {
                            boolean isHasMenu = menuIds.contains(String.valueOf(menu.getId()));
                            menu.setHasMenu(isHasMenu);
                            if (isHasMenu) {
                                List<Menu> subMenuList = menu.getSubMenu();
                                for (Menu sub : subMenuList) {
                                    sub.setHasMenu(menuIds.contains(String.valueOf(sub.getId())));
                                }
                            }
                        }
                    }
                    session.setAttribute(Constant.SESSION_ALL_MENU_LIST, menus);
                } else {
                    menus = (List<Menu>) session.getAttribute(Constant.SESSION_ALL_MENU_LIST);
                }
                mv.setViewName("common/index");
                mv.addObject("user", user);
                mv.addObject("menuList", menus);
            } else {
                mv.setViewName("common/login");
            }
        } catch (Exception e) {
            mv.setViewName("common/login");
            LOGGER.error("Controller login exception.", e);
        }
        mv.addObject("pd", pd);
        return mv;
    }

    /**
     * 进入tab标签
     *
     * @return
     */
    @RequestMapping(value = "/tab")
    public String tab() {
        return "common/tab";
    }

    /**
     * 进入首页后的默认页面
     */
    @RequestMapping(value = "/default")
    public String defaultPage() {
        return "common/default";
    }

    @RequestMapping(value = "/logout")
    public ModelAndView logout() {
        ModelAndView mv = new ModelAndView();
        PageData pd = new PageData();

        //shiro管理的session
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession();

        session.removeAttribute(Constant.SESSION_USER);
        session.removeAttribute(Constant.SESSION_ROLE_RIGHTS);
        session.removeAttribute(Constant.SESSION_ALL_MENU_LIST);
        session.removeAttribute(Constant.SESSION_MENU_LIST);
        session.removeAttribute(Constant.SESSION_QX);
        session.removeAttribute(Constant.SESSION_userpds);
        session.removeAttribute(Constant.SESSION_USERNAME);
        session.removeAttribute(Constant.SESSION_USERROL);
        session.removeAttribute("changeMenu");

        //shiro销毁登录
        Subject subject = SecurityUtils.getSubject();
        subject.logout();

        pd = this.getPageData();
        String msg = pd.getString("msg");
        pd.put("msg", msg);

        mv.setViewName("common/login");
        mv.addObject("pd", pd);
        return mv;
    }

    public void splitMenu(List<Menu> menuList, Session session) {
        List<Menu> menuList1 = new ArrayList<Menu>();
        List<Menu> menuList2 = new ArrayList<Menu>();

        for (int i = 0; i < menuList.size(); i++) {
            Menu menu = menuList.get(i);
            if ("1".equals(menu.getMenuType())) {
                menuList1.add(menu);
            } else {
                menuList2.add(menu);
            }
        }

        session.removeAttribute(Constant.SESSION_MENU_LIST);
        if ("2".equals(session.getAttribute("changeMenu"))) {
            session.setAttribute(Constant.SESSION_MENU_LIST, menuList1);
            session.removeAttribute("changeMenu");
            session.setAttribute("changeMenu", "1");
            menuList = menuList1;
        } else {
            session.setAttribute(Constant.SESSION_MENU_LIST, menuList2);
            session.removeAttribute("changeMenu");
            session.setAttribute("changeMenu", "2");
            menuList = menuList2;
        }
    }
}

