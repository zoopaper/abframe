package org.abframe.controller;

import net.common.utils.encrypt.MD5Util;
import net.common.utils.uuid.UuidUtil;
import org.abframe.common.PermissionHandler;
import org.abframe.controller.base.BaseController;
import org.abframe.entity.Page;
import org.abframe.entity.RoleBean;
import org.abframe.service.MemberUserService;
import org.abframe.service.RoleService;
import org.abframe.util.AppUtil;
import org.abframe.util.Constant;
import org.abframe.util.ObjectExcelView;
import org.abframe.util.PageData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping(value = "/member")
public class MemberUserController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MemberUserController.class);

    String menuUrl = "member/listUser";

    @Autowired
    private MemberUserService memberUserService;

    @Autowired
    private RoleService roleService;

    @RequestMapping(value = "/saveU")
    public ModelAndView saveU(PrintWriter out) throws Exception {
        ModelAndView mv = new ModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();

        pd.put("USER_ID", UuidUtil.genTerseUuid());    //ID
        pd.put("RIGHTS", "");                    //权限
        pd.put("LAST_LOGIN", "");                //最后登录时间
        pd.put("IP", "");                        //IP
        //pd.put("STATUS", "0");				//状态

        pd.put("PASSWORD", MD5Util.digestHex(pd.getString("PASSWORD")));

        if (null == memberUserService.findByUId(pd)) {
            if (PermissionHandler.buttonJurisdiction(menuUrl, "add")) {
                memberUserService.saveU(pd);
            }
            mv.addObject("msg", "success");
        } else {
            mv.addObject("msg", "failed");
        }
        mv.setViewName("save_result");
        return mv;
    }


    @RequestMapping(value = "/editU")
    public ModelAndView editU(PrintWriter out) throws Exception {
        ModelAndView mv = new ModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
        if (pd.getString("PASSWORD") != null && !"".equals(pd.getString("PASSWORD"))) {
            pd.put("PASSWORD", MD5Util.digestHex(pd.getString("PASSWORD")));
        }
        if (PermissionHandler.buttonJurisdiction(menuUrl, "edit")) {
            memberUserService.editU(pd);
        }
        mv.addObject("msg", "success");
        mv.setViewName("save_result");
        return mv;
    }

    /**
     * 判断用户名是否存在
     */
    @RequestMapping(value = "/hasU")
    @ResponseBody
    public Object hasU() {
        Map<String, String> map = new HashMap<String, String>();
        String errInfo = "success";
        PageData pd = new PageData();
        try {
            pd = this.getPageData();
            if (memberUserService.findByUId(pd) != null) {
                errInfo = "error";
            }
        } catch (Exception e) {
            LOGGER.error("Controller member exception.", e);
        }
        map.put("result", errInfo);                //返回结果
        return AppUtil.returnObject(new PageData(), map);
    }

    /**
     * 判断邮箱是否存在
     */
    @RequestMapping(value = "/hasE")
    @ResponseBody
    public Object hasE() {
        Map<String, String> map = new HashMap<String, String>();
        String errInfo = "success";
        PageData pd = new PageData();
        try {
            pd = this.getPageData();
            if (memberUserService.findByUE(pd) != null) {
                errInfo = "error";
            }
        } catch (Exception e) {
            LOGGER.error("Controller member exception.", e);
        }
        map.put("result", errInfo);                //返回结果
        return AppUtil.returnObject(new PageData(), map);
    }

    /**
     * 判断编码是否存在
     */
    @RequestMapping(value = "/hasN")
    @ResponseBody
    public Object hasN() {
        Map<String, String> map = new HashMap<String, String>();
        String errInfo = "success";
        PageData pd = new PageData();
        try {
            pd = this.getPageData();
            if (memberUserService.findByUN(pd) != null) {
                errInfo = "error";
            }
        } catch (Exception e) {
            LOGGER.error("Controller member exception.", e);
        }
        map.put("result", errInfo);
        return AppUtil.returnObject(new PageData(), map);
    }


    @RequestMapping(value = "/toEditU")
    public ModelAndView toEditU() {
        ModelAndView mv = new ModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
        try {
            List<RoleBean> roleList = roleService.listAllappERRoles();            //列出所有二级角色
            pd = memberUserService.findByUiId(pd);                                //根据ID读取
            mv.setViewName("memberUser/memberUserEdit");
            mv.addObject("msg", "editU");
            mv.addObject("pd", pd);
            mv.addObject("roleList", roleList);
        } catch (Exception e) {
            LOGGER.error("Controller member exception.", e);
        }
        return mv;
    }


    @RequestMapping(value = "/toAddU")
    public ModelAndView toAddU() {
        ModelAndView mv = new ModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
        try {
            List<RoleBean> roleList;
            roleList = roleService.listAllappERRoles();
            mv.setViewName("memberUser/memberUserEdit");
            mv.addObject("msg", "saveU");
            mv.addObject("pd", pd);
            mv.addObject("roleList", roleList);
        } catch (Exception e) {
            LOGGER.error("Controller member exception.", e);
        }
        return mv;
    }

    @RequestMapping(value = "/listUser")
    public ModelAndView listUsers(Page page) {
        ModelAndView mv = new ModelAndView();
        PageData pd = new PageData();
        try {
            pd = this.getPageData();

            String USERNAME = pd.getString("USERNAME");

            if (null != USERNAME && !"".equals(USERNAME)) {
                USERNAME = USERNAME.trim();
                pd.put("USERNAME", USERNAME);
            }

            page.setPd(pd);
            List<PageData> userList = memberUserService.listPdPageUser(page);
            List<RoleBean> roleList = roleService.listAllappERRoles();

            mv.setViewName("memberUser/memberUserList");
            mv.addObject("userList", userList);
            mv.addObject("roleList", roleList);
            mv.addObject("pd", pd);
            mv.addObject(Constant.SESSION_QX, this.getHC());
        } catch (Exception e) {
            LOGGER.error("Controller member exception.", e);
        }
        return mv;
    }

    /**
     * 删除用户
     */
    @RequestMapping(value = "/deleteU")
    public void deleteU(PrintWriter out) {
        PageData pd = new PageData();
        try {
            pd = this.getPageData();
            if (PermissionHandler.buttonJurisdiction(menuUrl, "del")) {
                memberUserService.deleteU(pd);
            }
            out.write("success");
            out.close();
        } catch (Exception e) {
            LOGGER.error("Controller member exception.", e);
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
                    memberUserService.deleteAllU(ArrayUSER_IDS);
                }
                pd.put("msg", "ok");
            } else {
                pd.put("msg", "no");
            }

            pdList.add(pd);
            map.put("list", pdList);
        } catch (Exception e) {
            LOGGER.error("Controller member exception.", e);
        }
        return AppUtil.returnObject(pd, map);
    }


    /*
     * 导出会员信息到excel
     * @return
     */
    @RequestMapping(value = "/excel")
    public ModelAndView exportExcel() {
        ModelAndView mv = new ModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
        try {
            if (PermissionHandler.buttonJurisdiction(menuUrl, "cha")) {
                //检索条件===
                String USERNAME = pd.getString("USERNAME");
                if (null != USERNAME && !"".equals(USERNAME)) {
                    USERNAME = USERNAME.trim();
                    pd.put("USERNAME", USERNAME);
                }
                String lastLoginStart = pd.getString("lastLoginStart");
                String lastLoginEnd = pd.getString("lastLoginEnd");
                if (lastLoginStart != null && !"".equals(lastLoginStart)) {
                    lastLoginStart = lastLoginStart + " 00:00:00";
                    pd.put("lastLoginStart", lastLoginStart);
                }
                if (lastLoginEnd != null && !"".equals(lastLoginEnd)) {
                    lastLoginEnd = lastLoginEnd + " 00:00:00";
                    pd.put("lastLoginEnd", lastLoginEnd);
                }
                //检索条件===

                Map<String, Object> dataMap = new HashMap<String, Object>();
                List<String> titles = new ArrayList<String>();

                titles.add("用户名");        //1
                titles.add("编号");        //2
                titles.add("姓名");            //3
                titles.add("手机号");        //4
                titles.add("身份证号");        //5
                titles.add("等级");            //6
                titles.add("邮箱");            //7
                titles.add("最近登录");        //8
                titles.add("到期时间");        //9
                titles.add("上次登录IP");    //10

                dataMap.put("titles", titles);

                List<PageData> userList = memberUserService.listAllUser(pd);
                List<PageData> varList = new ArrayList<PageData>();
                for (int i = 0; i < userList.size(); i++) {
                    PageData vpd = new PageData();
                    vpd.put("var1", userList.get(i).getString("USERNAME"));        //1
                    vpd.put("var2", userList.get(i).getString("NUMBER"));        //2
                    vpd.put("var3", userList.get(i).getString("NAME"));            //3
                    vpd.put("var4", userList.get(i).getString("PHONE"));        //4
                    vpd.put("var5", userList.get(i).getString("SFID"));            //5
                    vpd.put("var6", userList.get(i).getString("ROLE_NAME"));    //6
                    vpd.put("var7", userList.get(i).getString("EMAIL"));        //7
                    vpd.put("var8", userList.get(i).getString("LAST_LOGIN"));    //8
                    vpd.put("var9", userList.get(i).getString("END_TIME"));        //9
                    vpd.put("var10", userList.get(i).getString("IP"));            //10
                    varList.add(vpd);
                }

                dataMap.put("varList", varList);

                ObjectExcelView erv = new ObjectExcelView();
                mv = new ModelAndView(erv, dataMap);
            }
        } catch (Exception e) {
            LOGGER.error("Controller member exception.", e);
        }
        return mv;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        binder.registerCustomEditor(Date.class, new CustomDateEditor(format, true));
    }

}
