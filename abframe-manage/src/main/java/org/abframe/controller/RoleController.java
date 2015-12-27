package org.abframe.controller;

import com.alibaba.fastjson.JSONArray;
import com.google.common.base.Strings;
import org.abframe.controller.base.BaseController;
import org.abframe.entity.Menu;
import org.abframe.entity.Page;
import org.abframe.entity.RoleBean;
import org.abframe.service.MenuService;
import org.abframe.service.RoleService;
import org.abframe.util.AppUtil;
import org.abframe.util.MenuHelper;
import org.abframe.util.PageData;
import org.abframe.util.Tools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.util.*;

@Controller
@RequestMapping(value = "/role")
public class RoleController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RoleController.class);
    @Resource
    private MenuService menuService;
    @Resource
    private RoleService roleService;

    @RequestMapping
    public ModelAndView list(Page page) throws Exception {
        ModelAndView mv = new ModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();

        List<RoleBean> roleList = roleService.listAllRoles();
        mv.addObject("pd", pd);
        mv.addObject("roleList", roleList);
        mv.setViewName("role/roleList");
        return mv;
    }

    @RequestMapping(value = "/toAdd")
    public ModelAndView toAdd(Page page) {
        ModelAndView mv = new ModelAndView();
        PageData pd = new PageData();
        try {
            pd = this.getPageData();
            mv.setViewName("role/roleAdd");
            mv.addObject("pd", pd);
        } catch (Exception e) {
            LOGGER.error("Controller role toAdd exception.", e);
        }
        return mv;
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ModelAndView add() {
        ModelAndView mv = new ModelAndView();
        PageData pd = new PageData();
        try {
            pd = this.getPageData();
            pd.put("createTime", new Date(System.currentTimeMillis()));
            roleService.addRole(pd);
            mv.addObject("msg", "success");
        } catch (Exception e) {
            LOGGER.error("Controller role exception.", e);
            mv.addObject("msg", "failed");
        }
        mv.setViewName("save_result");
        return mv;
    }


    @RequestMapping(value = "/toEdit")
    public ModelAndView toEdit(long roleId) throws Exception {
        ModelAndView mv = new ModelAndView();
        try {
            RoleBean roleBean = roleService.getRoleById(roleId);
            mv.setViewName("role/roleEdit");
            mv.addObject("role", roleBean);
        } catch (Exception e) {
            LOGGER.error("Controller role exception.", e);
        }
        return mv;
    }

    @RequestMapping(value = "/edit")
    public ModelAndView edit() throws Exception {
        ModelAndView mv = new ModelAndView();
        PageData pd = new PageData();
        try {
            pd = this.getPageData();
            roleService.updateRoleById(pd);
            mv.addObject("msg", "success");
        } catch (Exception e) {
            LOGGER.error("Controller role exception.", e);
            mv.addObject("msg", "failed");
        }
        mv.setViewName("save_result");
        return mv;
    }

    /**
     * 请求角色菜单授权页面
     */
    @RequestMapping(value = "/auth")
    public String auth(@RequestParam(value = "id") long id, Model model) {

        try {
            List<Menu> menuList = menuService.getAllMenu();
            RoleBean role = roleService.getRoleById(id);
            String menuId = role.getMenuId();
            if (!Strings.isNullOrEmpty(menuId)) {
                String[] menuStr = menuId.split(",");
                List menuIds = Arrays.asList(menuStr);
                for (Menu menu : menuList) {
                    menu.setHasMenu(menuIds.contains(String.valueOf(menu.getId())));
                    if (menu.isHasMenu()) {
                        List<Menu> subMenuList = menu.getSubMenu();
                        for (Menu sub : subMenuList) {
                            sub.setHasMenu(menuIds.contains(String.valueOf(sub.getId())));
                        }
                    }
                }
            }
            JSONArray arr = new JSONArray();
            arr.addAll(menuList);
            String json = arr.toString();
            json = json.replaceAll("menuName", "name").replaceAll("subMenu", "nodes").replaceAll("hasMenu", "checked");
            model.addAttribute("zTreeNodes", json);
            model.addAttribute("id", id);
        } catch (Exception e) {
            LOGGER.error("Controller role exception.", e);
        }

        return "auth/auth";
    }

    /**
     * 请求角色按钮授权页面
     */
    @RequestMapping(value = "/button")
    public ModelAndView button(@RequestParam long id, @RequestParam String msg, Model model) throws Exception {
        ModelAndView mv = new ModelAndView();
        try {
            List<Menu> menuList = menuService.getAllMenu();
            RoleBean role = roleService.getRoleById(id);

            String roleRights = "";

            if (!Strings.isNullOrEmpty(roleRights)) {
                for (Menu menu : menuList) {
                    menu.setHasMenu(MenuHelper.testRights(roleRights, menu.getId()));
                    if (menu.isHasMenu()) {
                        List<Menu> subMenuList = menu.getSubMenu();
                        for (Menu sub : subMenuList) {
                            sub.setHasMenu(MenuHelper.testRights(roleRights, sub.getId()));
                        }
                    }
                }
            }
            JSONArray arr = new JSONArray();
            arr.addAll(menuList);
            String json = arr.toString();
            json = json.replaceAll("MENU_ID", "id").replaceAll("MENU_NAME", "name").replaceAll("subMenu", "nodes").replaceAll("hasMenu", "checked");
            mv.addObject("zTreeNodes", json);
            mv.addObject("id", id);
            mv.addObject("msg", msg);
        } catch (Exception e) {
            LOGGER.error("Controller role exception.", e);
        }
        mv.setViewName("role/roleButton");
        return mv;
    }

    /**
     * 保存角色菜单权限
     */
    @RequestMapping(value = "/auth/save")
    public void saveAuth(@RequestParam long id, @RequestParam String menuIds, PrintWriter out) throws Exception {
        PageData pd = new PageData();
        try {
            RoleBean role = roleService.getRoleById(id);
            role.setMenuId(menuIds);
            roleService.updateRoleRights(role);

            pd.put("roleId", id);
            out.write("success");
            out.close();
        } catch (Exception e) {
            LOGGER.error("Controller role exception.", e);
        }
    }

    /**
     * 保存角色按钮权限
     */
    @RequestMapping(value = "/roleBtn/save")
    public void roleBtn(@RequestParam String id, @RequestParam String menuIds, @RequestParam String msg, PrintWriter out) throws Exception {
        PageData pd = new PageData();
        pd = this.getPageData();
        try {
            if (!Strings.isNullOrEmpty(menuIds)) {
                BigInteger rights = MenuHelper.sumRights(Tools.str2StrArray(menuIds));
                pd.put("value", rights.toString());
            } else {
                pd.put("value", "");
            }
            pd.put("roleId", id);
            out.write("success");
            out.close();
        } catch (Exception e) {
            LOGGER.error("Controller role exception.", e);
        }
    }

    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object deleteRole(@RequestParam long roleId) throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        PageData pd = new PageData();
        String errInfo = "";
        try {
            roleService.deleteRoleById(roleId);
        } catch (Exception e) {
            LOGGER.error("Controller role exception.", e);
        }
        map.put("result", errInfo);
        return AppUtil.returnObject(new PageData(), map);
    }


}
