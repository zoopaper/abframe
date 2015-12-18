package org.abframe.controller;

import net.common.utils.uuid.UuidUtil;
import org.abframe.common.PermissionHandler;
import org.abframe.controller.base.BaseController;
import org.abframe.entity.Page;
import org.abframe.entity.RoleBean;
import org.abframe.service.MenuService;
import org.abframe.service.RoleService;
import org.abframe.service.UserService;
import org.abframe.util.*;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping(value = "/user")
public class UserController extends BaseController {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    String menuUrl = "user/list";

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private MenuService menuService;


    @RequestMapping(value = "/saveU")
    public ModelAndView saveU(PrintWriter out) throws Exception {
        ModelAndView mv = new ModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();

        pd.put("userId", UuidUtil.genTerseUuid());
        pd.put("perms", "");
        pd.put("lastLogin", "");
        pd.put("ip", "");
        pd.put("status", "0");
        pd.put("skin", "default");

        pd.put("password", new SimpleHash("SHA-1", pd.getString("userName"), pd.getString("password")).toString());

        if (null == userService.findByUId(pd)) {
            if (PermissionHandler.buttonJurisdiction(menuUrl, "add")) {
                userService.saveU(pd);
            } //判断新增权限
            mv.addObject("msg", "success");
        } else {
            mv.addObject("msg", "failed");
        }
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
            if (userService.findByUId(pd) != null) {
                errInfo = "error";
            }
        } catch (Exception e) {
            LOGGER.error("Controller user exception.", e);
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

            if (userService.findByUE(pd) != null) {
                errInfo = "error";
            }
        } catch (Exception e) {
            LOGGER.error("Controller user exception.", e);
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
            if (userService.findByUN(pd) != null) {
                errInfo = "error";
            }
        } catch (Exception e) {
            LOGGER.error("Controller user exception.", e);
        }
        map.put("result", errInfo);                //返回结果
        return AppUtil.returnObject(new PageData(), map);
    }

    /**
     * 修改用户
     */
    @RequestMapping(value = "/editU")
    public ModelAndView editU() throws Exception {
        ModelAndView mv = new ModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
        if (pd.getString("password") != null && !"".equals(pd.getString("password"))) {
            pd.put("password", new SimpleHash("SHA-1", pd.getString("userName"), pd.getString("password")).toString());
        }
//        if (PermissionHandler.buttonJurisdiction(menuUrl, "edit")) {
        userService.editU(pd);
//        }
        mv.addObject("msg", "success");
        mv.setViewName("save_result");
        return mv;
    }

    /**
     * 去修改用户页面
     */
    @RequestMapping(value = "/toEditUser")
    public ModelAndView toEditU() throws Exception {
        ModelAndView mv = new ModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
        //顶部修改个人资料
        String fx = pd.getString("fx");
        if ("head".equals(fx)) {
            mv.addObject("fx", "head");
        } else {
            mv.addObject("fx", "user");
        }
        List<RoleBean> roleList = roleService.listAllERRoles();            //列出所有二级角色
        pd = userService.findByUiId(pd);
        mv.setViewName("user/userEdit");
        mv.addObject("msg", "editU");
        mv.addObject("pd", pd);
        mv.addObject("roleList", roleList);
        return mv;
    }

    /**
     * 去新增用户页面
     */
    @RequestMapping(value = "/toAddU")
    public ModelAndView toAddU() throws Exception {
        ModelAndView mv = new ModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
        List<RoleBean> roleList;

        roleList = roleService.listAllERRoles();            //列出所有二级角色

        mv.setViewName("user/userEdit");
        mv.addObject("msg", "saveU");
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

        String userName = pd.getString("userName");

        if (null != userName && !"".equals(userName)) {
            userName = userName.trim();
            pd.put("userName", userName);
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

        page.setPd(pd);
        List<PageData> userList = userService.listPdPageUser(page);            //列出用户列表
        List<RoleBean> roleList = roleService.listAllERRoles();                        //列出所有二级角色

        mv.setViewName("user/userList");
        mv.addObject("userList", userList);
        mv.addObject("roleList", roleList);
        mv.addObject("pd", pd);
        mv.addObject(Constant.SESSION_QX, this.getHC());    //按钮权限
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
        List<PageData> userList = userService.listAllUser(pd);            //列出用户列表
        mv.setViewName("user/userTbList");
        mv.addObject("userList", userList);
        mv.addObject("pd", pd);
        mv.addObject(Constant.SESSION_QX, this.getHC());    //按钮权限
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
                userService.deleteU(pd);
            }
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
    //===================================================================================================


    /*
     * 导出用户信息到EXCEL
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
                titles.add("职位");            //4
                titles.add("手机");            //5
                titles.add("邮箱");            //6
                titles.add("最近登录");        //7
                titles.add("上次登录IP");    //8

                dataMap.put("titles", titles);

                List<PageData> userList = userService.listAllUser(pd);
                List<PageData> varList = new ArrayList<PageData>();
                for (int i = 0; i < userList.size(); i++) {
                    PageData vpd = new PageData();
                    vpd.put("var1", userList.get(i).getString("USERNAME"));        //1
                    vpd.put("var2", userList.get(i).getString("NUMBER"));        //2
                    vpd.put("var3", userList.get(i).getString("NAME"));            //3
                    vpd.put("var4", userList.get(i).getString("ROLE_NAME"));    //4
                    vpd.put("var5", userList.get(i).getString("PHONE"));        //5
                    vpd.put("var6", userList.get(i).getString("EMAIL"));        //6
                    vpd.put("var7", userList.get(i).getString("LAST_LOGIN"));    //7
                    vpd.put("var8", userList.get(i).getString("IP"));            //8
                    varList.add(vpd);
                }
                dataMap.put("varList", varList);
                ObjectExcelView erv = new ObjectExcelView();                    //执行excel操作
                mv = new ModelAndView(erv, dataMap);
            }
        } catch (Exception e) {
            LOGGER.error("Controller user exception.", e);
        }
        return mv;
    }

    /**
     * 打开上传EXCEL页面
     */
    @RequestMapping(value = "/toUploadExcel")
    public ModelAndView toUploadExcel() throws Exception {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("user/uploadExcel");
        return mv;
    }

    /**
     * 下载模版
     */
    @RequestMapping(value = "/downExcel")
    public void downExcel(HttpServletResponse response) throws Exception {

        FileUtil.fileDownload(response, PathUtil.getClasspath() + Constant.FILEPATHFILE + "Users.xls", "Users.xls");

    }

    /**
     * 从EXCEL导入到数据库
     */
    @RequestMapping(value = "/readExcel")
    public ModelAndView readExcel(
            @RequestParam(value = "excel", required = false) MultipartFile file
    ) throws Exception {
        ModelAndView mv = new ModelAndView();
        PageData pd = new PageData();
        if (!PermissionHandler.buttonJurisdiction(menuUrl, "add")) {
            return null;
        }
        if (null != file && !file.isEmpty()) {
            String filePath = PathUtil.getClasspath() + Constant.FILEPATHFILE;                                //文件上传路径
            String fileName = FileUtil.fileUp(file, filePath, "userexcel");                            //执行上传

            List<PageData> listPd = (List) ObjectExcelRead.readExcel(filePath, fileName, 2, 0, 0);    //执行读EXCEL操作,读出的数据导入List 2:从第3行开始；0:从第A列开始；0:第0个sheet

			/*存入数据库操作======================================*/
            pd.put("RIGHTS", "");                    //权限
            pd.put("LAST_LOGIN", "");                //最后登录时间
            pd.put("IP", "");                        //IP
            pd.put("STATUS", "0");                    //状态
            pd.put("SKIN", "default");                //默认皮肤

            List<RoleBean> roleList = roleService.listAllERRoles();    //列出所有二级角色

            pd.put("ROLE_ID", roleList.get(0).getId());    //设置角色ID为随便第一个
            /**
             * var0 :编号
             * var1 :姓名
             * var2 :手机
             * var3 :邮箱
             * var4 :备注
             */
            for (int i = 0; i < listPd.size(); i++) {
                pd.put("USER_ID", UuidUtil.genTerseUuid());                                        //ID
                pd.put("NAME", listPd.get(i).getString("var1"));                            //姓名

                String USERNAME = GetPinyin.getPingYin(listPd.get(i).getString("var1"));    //根据姓名汉字生成全拼
                pd.put("USERNAME", USERNAME);
                if (userService.findByUId(pd) != null) {                                        //判断用户名是否重复
                    USERNAME = GetPinyin.getPingYin(listPd.get(i).getString("var1")) + Tools.getRandomNum();
                    pd.put("USERNAME", USERNAME);
                }
                pd.put("BZ", listPd.get(i).getString("var4"));                                //备注
                if (Tools.checkEmail(listPd.get(i).getString("var3"))) {                        //邮箱格式不对就跳过
                    pd.put("EMAIL", listPd.get(i).getString("var3"));
                    if (userService.findByUE(pd) != null) {                                    //邮箱已存在就跳过
                        continue;
                    }
                } else {
                    continue;
                }

                pd.put("NUMBER", listPd.get(i).getString("var0"));                            //编号已存在就跳过
                pd.put("PHONE", listPd.get(i).getString("var2"));                            //手机号

                pd.put("PASSWORD", new SimpleHash("SHA-1", USERNAME, "123").toString());    //默认密码123
                if (userService.findByUN(pd) != null) {
                    continue;
                }
                userService.saveU(pd);
            }
            /*存入数据库操作======================================*/

            mv.addObject("msg", "success");
        }

        mv.setViewName("save_result");
        return mv;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        binder.registerCustomEditor(Date.class, new CustomDateEditor(format, true));
    }


}
