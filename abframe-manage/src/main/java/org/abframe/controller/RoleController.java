package org.abframe.controller;

import com.google.common.base.Strings;
import net.common.utils.uuid.UuidUtil;
import net.sf.json.JSONArray;
import org.abframe.common.PermissionHandler;
import org.abframe.controller.base.BaseController;
import org.abframe.entity.Menu;
import org.abframe.entity.Page;
import org.abframe.entity.RoleBean;
import org.abframe.service.MenuService;
import org.abframe.service.RoleService;
import org.abframe.util.*;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.io.PrintWriter;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/role")
public class RoleController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RoleController.class);

    String menuUrl = "role";

    @Autowired
    private MenuService menuService;

    @Autowired
    private RoleService roleService;

    /**
     * 权限(增删改查)
     */
    @RequestMapping(value = "/qx")
    public ModelAndView qx() {
        ModelAndView mv = new ModelAndView();
        PageData pd = new PageData();
        try {
            pd = this.getPageData();
            String msg = pd.getString("msg");
            if (PermissionHandler.buttonJurisdiction(menuUrl, "edit")) {
                roleService.updateQx(msg, pd);
            }
            mv.setViewName("save_result");
            mv.addObject("msg", "success");
        } catch (Exception e) {
            LOGGER.error("Controller role exception.", e);
        }
        return mv;
    }

    /**
     * K权限
     */
    @RequestMapping(value = "/kfqx")
    public ModelAndView kfqx() throws Exception {
        ModelAndView mv = new ModelAndView();
        PageData pd = new PageData();
        try {
            pd = this.getPageData();
            String msg = pd.getString("msg");
            if (PermissionHandler.buttonJurisdiction(menuUrl, "edit")) {
                roleService.updateKFQx(msg, pd);
            }
            mv.setViewName("save_result");
            mv.addObject("msg", "success");
        } catch (Exception e) {
            LOGGER.error("Controller role exception.", e);
        }
        return mv;
    }

    /**
     * c权限
     */
    @RequestMapping(value = "/gysqxc")
    public ModelAndView gysqxc() throws Exception {
        ModelAndView mv = new ModelAndView();
        PageData pd = new PageData();
        try {
            pd = this.getPageData();
            String msg = pd.getString("msg");
            if (PermissionHandler.buttonJurisdiction(menuUrl, "edit")) {
                roleService.gysqxc(msg, pd);
            }
            mv.setViewName("save_result");
            mv.addObject("msg", "success");
        } catch (Exception e) {
            LOGGER.error("Controller role exception.", e);
        }
        return mv;
    }

    @RequestMapping
    public ModelAndView list(Page page) throws Exception {
        ModelAndView mv = new ModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();

        String roleId = pd.getString("id");
        if (Strings.isNullOrEmpty(roleId)) {
            pd.put("id", "1");
        }
        List<RoleBean> roleList = roleService.listAllRoles();                //列出所有部门
        List<RoleBean> roleList_z = roleService.listAllRolesByPId(pd);        //列出此部门的所有下级

        List<PageData> kefuqxlist = roleService.listAllkefu(pd);        //管理权限列表
        List<PageData> gysqxlist = roleService.listAllGysQX(pd);        //用户权限列表
        pd = roleService.findObjectById(pd);                            //取得点击部门
        mv.addObject("pd", pd);
        mv.addObject("kefuqxlist", kefuqxlist);
        mv.addObject("gysqxlist", gysqxlist);
        mv.addObject("roleList", roleList);
        mv.addObject("roleList_z", roleList_z);
        mv.setViewName("role/roleList");
        mv.addObject(Constant.SESSION_QX, this.getHC());    //按钮权限

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

            String parentId = pd.getString("parentId");
            pd.put("id", parentId);
            if ("0".equals(parentId)) {
                pd.put("perms", "");
            } else {
                String perms = roleService.findObjectById(pd).getString("perms");
                pd.put("perms", (null == perms) ? "" : perms);
            }

            pd.put("permId", "");

            String uuid = UuidUtil.genTerseUuid();

            pd.put("GL_ID", uuid);
            pd.put("FX_QX", 0);                //发信权限
            pd.put("FW_QX", 0);                //服务权限
            pd.put("QX1", 0);                //操作权限
            pd.put("QX2", 0);                //产品权限
            pd.put("QX3", 0);                //预留权限
            pd.put("QX4", 0);                //预留权限
            if (PermissionHandler.buttonJurisdiction(menuUrl, "add")) {
                roleService.saveKeFu(pd);
            }//保存到K权限表

            pd.put("U_ID", uuid);
            pd.put("C1", 0);                //每日发信数量
            pd.put("C2", 0);
            pd.put("C3", 0);
            pd.put("C4", 0);
            pd.put("Q1", 0);                //权限1
            pd.put("Q2", 0);                //权限2
            pd.put("Q3", 0);
            pd.put("Q4", 0);
            if (PermissionHandler.buttonJurisdiction(menuUrl, "add")) {
                roleService.saveGYSQX(pd);
            }//保存到G权限表
            pd.put("QX_ID", uuid);

            pd.put("ROLE_ID", uuid);
            pd.put("ADD_QX", "0");
            pd.put("DEL_QX", "0");
            pd.put("EDIT_QX", "0");
            pd.put("CHA_QX", "0");
            if (PermissionHandler.buttonJurisdiction(menuUrl, "add")) {
                roleService.add(pd);
            }
            mv.addObject("msg", "success");
        } catch (Exception e) {
            LOGGER.error("Controller role exception.", e);
            mv.addObject("msg", "failed");
        }
        mv.setViewName("save_result");
        return mv;
    }

    /**
     * 请求编辑
     */
    @RequestMapping(value = "/toEdit")
    public ModelAndView toEdit(String id) throws Exception {
        ModelAndView mv = new ModelAndView();
        PageData pd = new PageData();
        try {
            pd = this.getPageData();
            pd.put("id", id);
            pd = roleService.findObjectById(pd);
            mv.setViewName("role/roleEdit");
            mv.addObject("pd", pd);
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
            if (PermissionHandler.buttonJurisdiction(menuUrl, "edit")) {
                pd = roleService.edit(pd);
            }
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
    public String auth(@RequestParam String id, Model model) {

        try {
            List<Menu> menuList = menuService.listAllMenu();
            RoleBean role = roleService.getRoleById(id);
            String roleRights = role.getPerms();
            if (!Strings.isNullOrEmpty(roleRights)) {
                for (Menu menu : menuList) {
                    menu.setHasMenu(RightsHelper.testRights(roleRights, menu.getMENU_ID()));
                    if (menu.isHasMenu()) {
                        List<Menu> subMenuList = menu.getSubMenu();
                        for (Menu sub : subMenuList) {
                            sub.setHasMenu(RightsHelper.testRights(roleRights, sub.getMENU_ID()));
                        }
                    }
                }
            }
            JSONArray arr = JSONArray.fromObject(menuList);
            String json = arr.toString();
            json = json.replaceAll("MENU_ID", "id").replaceAll("MENU_NAME", "name").replaceAll("subMenu", "nodes").replaceAll("hasMenu", "checked");
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
    public ModelAndView button(@RequestParam String id, @RequestParam String msg, Model model) throws Exception {
        ModelAndView mv = new ModelAndView();
        try {
            List<Menu> menuList = menuService.listAllMenu();
            RoleBean role = roleService.getRoleById(id);

            String roleRights = "";
            if ("add_qx".equals(msg)) {
                roleRights = role.getPermAdd();
            } else if ("del_qx".equals(msg)) {
                roleRights = role.getPermDel();
            } else if ("edit_qx".equals(msg)) {
                roleRights = role.getPermEdit();
            } else if ("cha_qx".equals(msg)) {
                roleRights = role.getPermQuery();
            }

            if (!Strings.isNullOrEmpty(roleRights)) {
                for (Menu menu : menuList) {
                    menu.setHasMenu(RightsHelper.testRights(roleRights, menu.getMENU_ID()));
                    if (menu.isHasMenu()) {
                        List<Menu> subMenuList = menu.getSubMenu();
                        for (Menu sub : subMenuList) {
                            sub.setHasMenu(RightsHelper.testRights(roleRights, sub.getMENU_ID()));
                        }
                    }
                }
            }
            JSONArray arr = JSONArray.fromObject(menuList);
            String json = arr.toString();
            //System.out.println(json);
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
    public void saveAuth(@RequestParam String id, @RequestParam String menuIds, PrintWriter out) throws Exception {
        PageData pd = new PageData();
        try {
            if (PermissionHandler.buttonJurisdiction(menuUrl, "edit")) {
                if (null != menuIds && !"".equals(menuIds.trim())) {
                    BigInteger rights = RightsHelper.sumRights(Tools.str2StrArray(menuIds));
                    RoleBean role = roleService.getRoleById(id);
                    role.setPerms(rights.toString());
                    roleService.updateRoleRights(role);
                    pd.put("perms", rights.toString());
                } else {
                    RoleBean role = new RoleBean();
                    role.setPerms("");
                    role.setId(id);
                    roleService.updateRoleRights(role);
                    pd.put("perms", "");
                }

                pd.put("id", id);
                roleService.setAllRights(pd);
            }
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
            if (PermissionHandler.buttonJurisdiction(menuUrl, "edit")) {
                if (!Strings.isNullOrEmpty(menuIds)) {
                    BigInteger rights = RightsHelper.sumRights(Tools.str2StrArray(menuIds));
                    pd.put("value", rights.toString());
                } else {
                    pd.put("value", "");
                }
                pd.put("id", id);
                roleService.updateQx(msg, pd);
            }
            out.write("success");
            out.close();
        } catch (Exception e) {
            LOGGER.error("Controller role exception.", e);
        }
    }

    /**
     * 删除
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object deleteRole(@RequestParam String id) throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        PageData pd = new PageData();
        String errInfo = "";
        try {
            if (PermissionHandler.buttonJurisdiction(menuUrl, "del")) {
                pd.put("id", id);
                List<RoleBean> roleList_z = roleService.listAllRolesByPId(pd);        //列出此部门的所有下级
                if (roleList_z.size() > 0) {
                    errInfo = "false";
                } else {

                    List<PageData> userlist = roleService.listAllUByRid(pd);
                    List<PageData> appuserlist = roleService.listAllAppUByRid(pd);
                    if (userlist.size() > 0 || appuserlist.size() > 0) {
                        errInfo = "false2";
                    } else {
                        roleService.deleteRoleById(id);
                        roleService.deleteKeFuById(id);
                        roleService.deleteGById(id);
                        errInfo = "success";
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error("Controller role exception.", e);
        }
        map.put("result", errInfo);
        return AppUtil.returnObject(new PageData(), map);
    }

    /* ===============================权限================================== */
    public Map<String, String> getHC() {
        Subject currentUser = SecurityUtils.getSubject();  //shiro管理的session
        Session session = currentUser.getSession();
        return (Map<String, String>) session.getAttribute(Constant.SESSION_QX);
    }
    /* ===============================权限================================== */


}
