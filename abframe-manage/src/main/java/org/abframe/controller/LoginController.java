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
import org.abframe.util.RightsHelper;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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
        String userName = pd.getString("userName");
        String password = pd.getString("password");
        String code = pd.getString("code");
        try {
            if (!Strings.isNullOrEmpty(userName) && !Strings.isNullOrEmpty(password) && !Strings.isNullOrEmpty(code)) {
                Subject currentUser = SecurityUtils.getSubject();
                Session session = currentUser.getSession();
                String sessionCode = (String) session.getAttribute(Constant.SESSION_SECURITY_CODE);

                pd.put("userName", userName);
                if (!Strings.isNullOrEmpty(sessionCode) && sessionCode.equalsIgnoreCase(code)) {
                    String passwd = new SimpleHash("SHA-1", userName, password).toString();
                    pd.put("password", passwd);
                    pd = userService.getUserByNameAndPwd(pd);
                    if (pd != null) {
                        UserBean user = new UserBean();
                        user.setUserId(pd.getString("userId"));
                        user.setUserName(pd.getString("userName"));
                        user.setPassword(pd.getString("password"));
                        user.setName(pd.getString("name"));
                        user.setPerms(pd.getString("perms"));
                        user.setRoleId(pd.getString("roleId"));
                        user.setLastLogin(pd.getString("lastLogin"));
                        user.setIp(pd.getString("ip"));
                        user.setStatus(pd.getString("status"));
                        user.setAvatarUrl(pd.getString("avatar_url"));
                        session.setAttribute(Constant.SESSION_USER, user);
                        session.removeAttribute(Constant.SESSION_SECURITY_CODE);

                        Subject subject = SecurityUtils.getSubject();
                        UsernamePasswordToken token = new UsernamePasswordToken(userName, password);

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
                    user = userService.getUserAndRoleById(user.getUserId());
                    session.setAttribute(Constant.SESSION_USERROL, user);
                } else {
                    user = userRole;
                }
                RoleBean role = user.getRole();
                String rolePerms = role != null ? role.getPerms() : "";
                //避免每次拦截用户操作时查询数据库，以下将用户所属角色权限、用户权限限都存入session
                session.setAttribute(Constant.SESSION_ROLE_RIGHTS, rolePerms);        //将角色权限存入session
                session.setAttribute(Constant.SESSION_USERNAME, user.getUserName());    //放入用户名

                List<Menu> allmenuList = new ArrayList<Menu>();

                if (null == session.getAttribute(Constant.SESSION_ALL_MENU_LIST)) {
                    allmenuList = menuService.listAllMenu();
                    if (!Strings.isNullOrEmpty(rolePerms)) {
                        for (Menu menu : allmenuList) {
                            boolean isHasMenu = RightsHelper.testRights(rolePerms, menu.getMENU_ID());
                            menu.setHasMenu(isHasMenu);
                            if (isHasMenu) {
                                List<Menu> subMenuList = menu.getSubMenu();
                                for (Menu sub : subMenuList) {
                                    sub.setHasMenu(RightsHelper.testRights(rolePerms, sub.getMENU_ID()));
                                }
                            }
                        }
                    }
                    session.setAttribute(Constant.SESSION_ALL_MENU_LIST, allmenuList);            //菜单权限放入session中
                } else {
                    allmenuList = (List<Menu>) session.getAttribute(Constant.SESSION_ALL_MENU_LIST);
                }

                //切换菜单=====
                List<Menu> menuList = new ArrayList<Menu>();
                //if(null == session.getAttribute(Const.SESSION_MENU_LIST) || ("yes".equals(pd.getString("changeMenu")))){
                if (null == session.getAttribute(Constant.SESSION_MENU_LIST) || ("yes".equals(changeMenu))) {
                    List<Menu> menuList1 = new ArrayList<Menu>();
                    List<Menu> menuList2 = new ArrayList<Menu>();

                    //拆分菜单
                    for (int i = 0; i < allmenuList.size(); i++) {
                        Menu menu = allmenuList.get(i);
                        if ("1".equals(menu.getMENU_TYPE())) {
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
                } else {
                    menuList = (List<Menu>) session.getAttribute(Constant.SESSION_MENU_LIST);
                }
                //切换菜单=====

                if (null == session.getAttribute(Constant.SESSION_QX)) {
                    session.setAttribute(Constant.SESSION_QX, this.getUQX(session));    //按钮权限放到session中
                }

                //FusionCharts 报表
                String strXML = "<graph caption='前12个月订单销量柱状图' xAxisName='月份' yAxisName='值' decimalPrecision='0' formatNumberScale='0'><set name='2013-05' value='4' color='AFD8F8'/><set name='2013-04' value='0' " +
                        "color='AFD8F8'/><set name='2013-03' value='0' color='AFD8F8'/><set name='2013-02' value='0' color='AFD8F8'/><set name='2013-01' value='0' color='AFD8F8'/><set name='2012-01' value='0' " +
                        "color='AFD8F8'/><set name='2012-11' value='0' color='AFD8F8'/><set name='2012-10' value='0' color='AFD8F8'/><set name='2012-09' value='0' color='AFD8F8'/><set name='2012-08' value='0' " +
                        "color='AFD8F8'/><set name='2012-07' value='0' color='AFD8F8'/><set name='2012-06' value='0' color='AFD8F8'/></graph>";
                mv.addObject("strXML", strXML);
                //FusionCharts 报表

                mv.setViewName("common/index");
                mv.addObject("user", user);
                mv.addObject("menuList", menuList);
            } else {
                //session失效后跳转登录页面
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

    /**
     * 获取用户权限
     */
    public Map<String, String> getUQX(Session session) {
        PageData pd = new PageData();
        Map<String, String> map = new HashMap<String, String>();
        try {
            String userName = session.getAttribute(Constant.SESSION_USERNAME).toString();
            pd.put("userName", userName);
            PageData pageData = userService.findByUId(pd);

            String roleId = pageData.get("roleId").toString();

            pd.put("roleId", roleId);

            PageData pd2 = new PageData();
            pd2.put(userName, userName);
            pd2.put("roleId", roleId);

            pd = roleService.findObjectById(pd);

            pd2 = roleService.findGLbyrid(pd2);
            if (null != pd2) {
                map.put("FX_QX", pd2.get("FX_QX").toString());
                map.put("FW_QX", pd2.get("FW_QX").toString());
                map.put("QX1", pd2.get("QX1").toString());
                map.put("QX2", pd2.get("QX2").toString());
                map.put("QX3", pd2.get("QX3").toString());
                map.put("QX4", pd2.get("QX4").toString());

                pd2.put("ROLE_ID", roleId);
                pd2 = roleService.findYHbyrid(pd2);
                map.put("C1", pd2.get("C1").toString());
                map.put("C2", pd2.get("C2").toString());
                map.put("C3", pd2.get("C3").toString());
                map.put("C4", pd2.get("C4").toString());
                map.put("Q1", pd2.get("Q1").toString());
                map.put("Q2", pd2.get("Q2").toString());
                map.put("Q3", pd2.get("Q3").toString());
                map.put("Q4", pd2.get("Q4").toString());
            }

            map.put("adds", pd.getString("ADD_QX"));
            map.put("dels", pd.getString("DEL_QX"));
            map.put("edits", pd.getString("EDIT_QX"));
            map.put("chas", pd.getString("CHA_QX"));
        } catch (Exception e) {
            LOGGER.error("Controller login exception.", e);
        }
        return map;
    }

}
