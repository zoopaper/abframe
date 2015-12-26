package org.abframe.controller;

import com.google.common.base.Strings;
import net.common.utils.ip.IPUtil;
import org.abframe.common.PermissionHandler;
import org.abframe.controller.base.BaseController;
import org.abframe.entity.Page;
import org.abframe.entity.RoleBean;
import org.abframe.entity.UserBean;
import org.abframe.service.MenuService;
import org.abframe.service.RoleService;
import org.abframe.service.UserService;
import org.abframe.util.AppUtil;
import org.abframe.util.Constant;
import org.abframe.util.PageData;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.slf4j.LoggerFactory;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping(value = "/user")
public class UserController extends BaseController {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    String menuUrl = "user/list";

    @Resource
    private UserService userService;
    @Resource
    private RoleService roleService;
    @Resource
    private MenuService menuService;


    @RequestMapping(value = "/saveUser", method = RequestMethod.POST)
    public ModelAndView saveUser() {
        ModelAndView modelAndView = new ModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();

        pd.put("createTime", new Date(System.currentTimeMillis()));
        pd.put("ip", IPUtil.getIP(getRequest()));
        pd.put("skin", "default");
        pd.put("password", new SimpleHash("SHA-1", pd.getString("account"), pd.getString("password")).toString());
        try {
            userService.saveUser(pd);
            modelAndView.addObject("msg", "success");
        } catch (Exception e) {
            modelAndView.addObject("msg", "failed");
            e.printStackTrace();
        }
        modelAndView.setViewName("save_result");
        return modelAndView;
    }

    /**
     * 判断用户名是否存在
     */
    @RequestMapping(value = "/hasUser", method = RequestMethod.POST)
    @ResponseBody
    public Object hasUser() {
        Map<String, String> map = new HashMap<String, String>();
        String ret = "success";
        PageData pd = new PageData();
        try {
            pd = this.getPageData();
            if (userService.getUserByAccount(pd) != null) {
                ret = "error";
            }
        } catch (Exception e) {
            LOGGER.error("Controller user exception.", e);
        }
        map.put("result", ret);
        return AppUtil.returnObject(new PageData(), map);
    }

    /**
     * 判断邮箱是否存在
     */
    @RequestMapping(value = "/hasEmail", method = RequestMethod.POST)
    @ResponseBody
    public Object hasEmail() {
        Map<String, String> map = new HashMap<String, String>();
        String ret = "success";
        PageData pd = new PageData();
        try {
            pd = this.getPageData();
            if (userService.getUserByEmail(pd) != null) {
                ret = "error";
            }
        } catch (Exception e) {
            LOGGER.error("Controller user exception.", e);
        }
        map.put("result", ret);
        return AppUtil.returnObject(new PageData(), map);
    }

    /**
     * 修改用户
     */
    @RequestMapping(value = "/editUser", method = RequestMethod.POST)
    public ModelAndView editUser() {
        ModelAndView mv = new ModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
        if (!Strings.isNullOrEmpty(pd.getString("password"))) {
            pd.put("password", new SimpleHash("SHA-1", pd.getString("account"), pd.getString("password")).toString());
        }
        try {
            userService.editUser(pd);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mv.addObject("msg", "success");
        mv.setViewName("save_result");
        return mv;
    }

    /**
     * 去修改用户页面
     */
    @RequestMapping(value = "/toEditUser", method = RequestMethod.GET)
    public ModelAndView toEditUser() {
        ModelAndView mv = new ModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
        List<RoleBean> roleList = null;
        UserBean userBean = null;
        try {
            roleList = roleService.listAllERRoles();
            userBean = userService.getUserById(pd);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mv.setViewName("user/userEdit");
        mv.addObject("msg", "editUser");
        mv.addObject("user", userBean);
        mv.addObject("roleList", roleList);
        return mv;
    }

    /**
     * 去新增用户页面
     */
    @RequestMapping(value = "/toAddUser")
    public ModelAndView toAddUser() throws Exception {
        ModelAndView mv = new ModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
        List<RoleBean> roleList;

        roleList = roleService.listAllERRoles();

        mv.setViewName("user/userEdit");
        mv.addObject("msg", "saveUser");
        mv.addObject("pd", pd);
        mv.addObject("roleList", roleList);

        return mv;
    }

    /**
     * 显示用户列表(用户组)
     */
    @RequestMapping(value = "/list")
    public ModelAndView listUsers(Page page) throws Exception {
        ModelAndView mv = new ModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();

        String account = pd.getString("account");

        if (!Strings.isNullOrEmpty(account)) {
            account = account.trim();
            pd.put("account", account);
        }

        page.setPd(pd);
        List<PageData> userList = userService.getUserListPage(page);
        List<RoleBean> roleList = roleService.listAllERRoles();

        mv.setViewName("user/userList");
        mv.addObject("userList", userList);
        mv.addObject("roleList", roleList);
        mv.addObject("pd", pd);
        mv.addObject(Constant.SESSION_QX, this.getHC());
        return mv;
    }


    /**
     * 显示用户列表(tab方式)
     */
    @RequestMapping(value = "/listTabUser")
    public ModelAndView listtabUser(Page page) throws Exception {
        ModelAndView mv = new ModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
        List<PageData> userList = userService.listAllUser(pd);
        mv.setViewName("user/userTbList");
        mv.addObject("userList", userList);
        mv.addObject("pd", pd);
        mv.addObject(Constant.SESSION_QX, this.getHC());    //按钮权限
        return mv;
    }

    /**
     * 删除用户
     */
    @RequestMapping(value = "/deleteUser")
    public void deleteUser(PrintWriter out) {
        PageData pd = new PageData();
        try {
            pd = this.getPageData();
            userService.deleteUser(pd);
            out.write("success");
            out.close();
        } catch (Exception e) {
            LOGGER.error("Controller user exception.", e);
        }

    }

    /**
     * 批量删除
     */
    @RequestMapping(value = "/deleteAllU")
    @ResponseBody
    public Object deleteAllU() {
        PageData pd = new PageData();
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            pd = this.getPageData();
            List<PageData> pdList = new ArrayList<PageData>();
            String USER_IDS = pd.getString("USER_IDS");

            if (null != USER_IDS && !"".equals(USER_IDS)) {
                String ArrayUSER_IDS[] = USER_IDS.split(",");
                if (PermissionHandler.buttonJurisdiction(menuUrl, "del")) {
                    userService.deleteAllU(ArrayUSER_IDS);
                }
                pd.put("msg", "ok");
            } else {
                pd.put("msg", "no");
            }

            pdList.add(pd);
            map.put("list", pdList);
        } catch (Exception e) {
            LOGGER.error("Controller user exception.", e);
        }
        return AppUtil.returnObject(pd, map);
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        binder.registerCustomEditor(Date.class, new CustomDateEditor(format, true));
    }
}
