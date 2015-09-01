package org.abframe.controller;

import com.google.common.base.Strings;
import net.common.utils.date.DateUtil;
import org.abframe.controller.base.BaseController;
import org.abframe.entity.Menu;
import org.abframe.entity.Role;
import org.abframe.entity.User;
import org.abframe.service.MenuService;
import org.abframe.service.RoleService;
import org.abframe.service.UserService;
import org.abframe.util.*;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
public class LoginController extends BaseController {

    @Resource(name = "userService")
    private UserService userService;

    @Resource(name = "menuService")
    private MenuService menuService;

    @Resource(name = "roleService")
    private RoleService roleService;

    /**
     * 获取登录用户的IP
     *
     * @throws Exception
     */
    public void getRemortIP(String USERNAME) throws Exception {
        PageData pd = new PageData();
        HttpServletRequest request = this.getRequest();
        String ip = "";
        if (request.getHeader("x-forwarded-for") == null) {
            ip = request.getRemoteAddr();
        } else {
            ip = request.getHeader("x-forwarded-for");
        }
        pd.put("USERNAME", USERNAME);
        pd.put("IP", ip);
        userService.saveIP(pd);
    }


    @RequestMapping(value = "/toLogin", method = RequestMethod.GET)
    public ModelAndView toLogin() throws Exception {
        ModelAndView mv = this.getModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
        mv.setViewName("common/login");
        mv.addObject("pd", pd);
        return mv;
    }


    @RequestMapping(value = "/login", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public Object login() throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        PageData pd = new PageData();
        pd = this.getPageData();
        String errInfo = "";
        String userName = pd.getString("userName");
        String password = pd.getString("password");
        String code = pd.getString("code");

        if (!Strings.isNullOrEmpty(userName) && !Strings.isNullOrEmpty(password) && !Strings.isNullOrEmpty(code)) {
            //shiro管理的session
            Subject currentUser = SecurityUtils.getSubject();
            Session session = currentUser.getSession();
            String sessionCode = (String) session.getAttribute(Constant.SESSION_SECURITY_CODE);        //获取session中的验证码

            if (null == code || "".equals(code)) {
                errInfo = "nullcode"; //验证码为空
            } else {
                pd.put("USERNAME", userName);
                if (!Strings.isNullOrEmpty(code) && sessionCode.equalsIgnoreCase(code)) {
                    String passwd = new SimpleHash("SHA-1", userName, password).toString();    //密码加密
                    pd.put("PASSWORD", passwd);
                    pd = userService.getUserByNameAndPwd(pd);
                    if (pd != null) {
                        pd.put("LAST_LOGIN", DateUtil.getDateTimeStr().toString());
                        userService.updateLastLogin(pd);
                        User user = new User();
                        user.setUSER_ID(pd.getString("USER_ID"));
                        user.setUSERNAME(pd.getString("USERNAME"));
                        user.setPASSWORD(pd.getString("PASSWORD"));
                        user.setNAME(pd.getString("NAME"));
                        user.setRIGHTS(pd.getString("RIGHTS"));
                        user.setROLE_ID(pd.getString("ROLE_ID"));
                        user.setLAST_LOGIN(pd.getString("LAST_LOGIN"));
                        user.setIP(pd.getString("IP"));
                        user.setSTATUS(pd.getString("STATUS"));
                        session.setAttribute(Constant.SESSION_USER, user);
                        session.removeAttribute(Constant.SESSION_SECURITY_CODE);

                        //shiro加入身份验证
                        Subject subject = SecurityUtils.getSubject();
                        UsernamePasswordToken token = new UsernamePasswordToken(userName, password);
                        try {
                            subject.login(token);
                        } catch (AuthenticationException e) {
                            errInfo = "身份验证失败！";
                        }

                    } else {
                        //用户名或密码有误
                        errInfo = "usererror";
                    }
                } else {
                    //验证码输入有误
                    errInfo = "codeerror";
                }
                if (Tools.isEmpty(errInfo)) {
                    //验证成功
                    errInfo = "success";
                }
            }
        } else {
            //缺少参数
            errInfo = "error";
        }
        map.put("result", errInfo);
        return AppUtil.returnObject(new PageData(), map);
    }

    /**
     * 访问系统首页
     */
    @RequestMapping(value = "/main/{changeMenu}")
    public ModelAndView login_index(@PathVariable("changeMenu") String changeMenu) {
        ModelAndView mv = this.getModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
        try {

            //shiro管理的session
            Subject currentUser = SecurityUtils.getSubject();
            Session session = currentUser.getSession();

            User user = (User) session.getAttribute(Constant.SESSION_USER);
            if (user != null) {

                User userr = (User) session.getAttribute(Constant.SESSION_USERROL);
                if (null == userr) {
                    user = userService.getUserAndRoleById(user.getUSER_ID());
                    session.setAttribute(Constant.SESSION_USERROL, user);
                } else {
                    user = userr;
                }
                Role role = user.getRole();
                String roleRights = role != null ? role.getRIGHTS() : "";
                //避免每次拦截用户操作时查询数据库，以下将用户所属角色权限、用户权限限都存入session
                session.setAttribute(Constant.SESSION_ROLE_RIGHTS, roleRights);        //将角色权限存入session
                session.setAttribute(Constant.SESSION_USERNAME, user.getUSERNAME());    //放入用户名

                List<Menu> allmenuList = new ArrayList<Menu>();

                if (null == session.getAttribute(Constant.SESSION_allmenuList)) {
                    allmenuList = menuService.listAllMenu();
                    if (!Strings.isNullOrEmpty(roleRights)) {
                        for (Menu menu : allmenuList) {
                            menu.setHasMenu(RightsHelper.testRights(roleRights, menu.getMENU_ID()));
                            if (menu.isHasMenu()) {
                                List<Menu> subMenuList = menu.getSubMenu();
                                for (Menu sub : subMenuList) {
                                    sub.setHasMenu(RightsHelper.testRights(roleRights, sub.getMENU_ID()));
                                }
                            }
                        }
                    }
                    session.setAttribute(Constant.SESSION_allmenuList, allmenuList);            //菜单权限放入session中
                } else {
                    allmenuList = (List<Menu>) session.getAttribute(Constant.SESSION_allmenuList);
                }

                //切换菜单=====
                List<Menu> menuList = new ArrayList<Menu>();
                //if(null == session.getAttribute(Const.SESSION_menuList) || ("yes".equals(pd.getString("changeMenu")))){
                if (null == session.getAttribute(Constant.SESSION_menuList) || ("yes".equals(changeMenu))) {
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

                    session.removeAttribute(Constant.SESSION_menuList);
                    if ("2".equals(session.getAttribute("changeMenu"))) {
                        session.setAttribute(Constant.SESSION_menuList, menuList1);
                        session.removeAttribute("changeMenu");
                        session.setAttribute("changeMenu", "1");
                        menuList = menuList1;
                    } else {
                        session.setAttribute(Constant.SESSION_menuList, menuList2);
                        session.removeAttribute("changeMenu");
                        session.setAttribute("changeMenu", "2");
                        menuList = menuList2;
                    }
                } else {
                    menuList = (List<Menu>) session.getAttribute(Constant.SESSION_menuList);
                }
                //切换菜单=====

                if (null == session.getAttribute(Constant.SESSION_QX)) {
                    session.setAttribute(Constant.SESSION_QX, this.getUQX(session));    //按钮权限放到session中
                }

                //FusionCharts 报表
                String strXML = "<graph caption='前12个月订单销量柱状图' xAxisName='月份' yAxisName='值' decimalPrecision='0' formatNumberScale='0'><set name='2013-05' value='4' color='AFD8F8'/><set name='2013-04' value='0' color='AFD8F8'/><set name='2013-03' value='0' color='AFD8F8'/><set name='2013-02' value='0' color='AFD8F8'/><set name='2013-01' value='0' color='AFD8F8'/><set name='2012-01' value='0' color='AFD8F8'/><set name='2012-11' value='0' color='AFD8F8'/><set name='2012-10' value='0' color='AFD8F8'/><set name='2012-09' value='0' color='AFD8F8'/><set name='2012-08' value='0' color='AFD8F8'/><set name='2012-07' value='0' color='AFD8F8'/><set name='2012-06' value='0' color='AFD8F8'/></graph>";
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
            logger.error(e.getMessage(), e);
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
     *
     * @return
     */
    @RequestMapping(value = "/default")
    public String defaultPage() {
        return "common/default";
    }

    @RequestMapping(value = "/logout")
    public ModelAndView logout() {
        ModelAndView mv = this.getModelAndView();
        PageData pd = new PageData();

        //shiro管理的session
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession();

        session.removeAttribute(Constant.SESSION_USER);
        session.removeAttribute(Constant.SESSION_ROLE_RIGHTS);
        session.removeAttribute(Constant.SESSION_allmenuList);
        session.removeAttribute(Constant.SESSION_menuList);
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
            String USERNAME = session.getAttribute(Constant.SESSION_USERNAME).toString();
            pd.put(Constant.SESSION_USERNAME, USERNAME);
            String ROLE_ID = userService.findByUId(pd).get("ROLE_ID").toString();

            pd.put("ROLE_ID", ROLE_ID);

            PageData pd2 = new PageData();
            pd2.put(Constant.SESSION_USERNAME, USERNAME);
            pd2.put("ROLE_ID", ROLE_ID);

            pd = roleService.findObjectById(pd);

            pd2 = roleService.findGLbyrid(pd2);
            if (null != pd2) {
                map.put("FX_QX", pd2.get("FX_QX").toString());
                map.put("FW_QX", pd2.get("FW_QX").toString());
                map.put("QX1", pd2.get("QX1").toString());
                map.put("QX2", pd2.get("QX2").toString());
                map.put("QX3", pd2.get("QX3").toString());
                map.put("QX4", pd2.get("QX4").toString());

                pd2.put("ROLE_ID", ROLE_ID);
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
            this.getRemortIP(USERNAME);
        } catch (Exception e) {
            logger.error(e.toString(), e);
        }
        return map;
    }

}
