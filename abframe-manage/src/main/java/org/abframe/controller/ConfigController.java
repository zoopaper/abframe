package org.abframe.controller;

import org.abframe.config.ConfigBean;
import org.abframe.controller.base.BaseController;
import org.abframe.service.MemberUserService;
import org.abframe.service.UserService;
import org.abframe.util.*;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/config")
public class ConfigController extends BaseController {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(ConfigController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private MemberUserService appuserService;

    @Autowired
    ConfigBean configBean;


    @RequestMapping(value = "/getUname")
    @ResponseBody
    public Object getList() {
        PageData pd = new PageData();
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            pd = this.getPageData();
            List<PageData> pdList = new ArrayList<PageData>();

            //shiro管理的session
            Subject currentUser = SecurityUtils.getSubject();
            Session session = currentUser.getSession();

            PageData pds = new PageData();
            pds = (PageData) session.getAttribute(Constant.SESSION_userpds);

            if (null == pds) {
                String USERNAME = session.getAttribute(Constant.SESSION_USERNAME).toString();    //获取当前登录者loginname
                pd.put("USERNAME", USERNAME);
                pds = userService.findByUId(pd);
                session.setAttribute(Constant.SESSION_userpds, pds);
            }

            pdList.add(pds);
            map.put("list", pdList);
        } catch (Exception e) {
            LOGGER.error("Controller config exception ", e);
        }
        return AppUtil.returnObject(pd, map);
    }

    /**
     * 保存皮肤
     */
    @RequestMapping(value = "/setSKIN")
    public void setSKIN(PrintWriter out) {
        PageData pd = new PageData();
        try {
            pd = this.getPageData();

            //shiro管理的session
            Subject currentUser = SecurityUtils.getSubject();
            Session session = currentUser.getSession();

            String USERNAME = session.getAttribute(Constant.SESSION_USERNAME).toString();//获取当前登录者loginname
            pd.put("USERNAME", USERNAME);
            userService.setSKIN(pd);
            session.removeAttribute(Constant.SESSION_userpds);
            session.removeAttribute(Constant.SESSION_USERROL);
            out.write("success");
            out.close();
        } catch (Exception e) {
            LOGGER.error("Controller config exception ", e);
        }

    }

    @RequestMapping(value = "/editEmail")
    public ModelAndView editEmail() throws Exception {
        ModelAndView mv = this.getModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
        mv.setViewName("config/editEmail");
        mv.addObject("pd", pd);
        return mv;
    }

    @RequestMapping(value = "/toSendSms")
    public ModelAndView toSendSms() throws Exception {
        ModelAndView mv = this.getModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
        mv.setViewName("config/sendSms");
        mv.addObject("pd", pd);
        return mv;
    }

    /**
     * 发送短信
     */
    @RequestMapping(value = "/sendSms")
    @ResponseBody
    public Object sendSms() {
        PageData pd = new PageData();
        pd = this.getPageData();
        Map<String, Object> map = new HashMap<String, Object>();
        String msg = "ok";        //发送状态
        int count = 0;            //统计发送成功条数
        int zcount = 0;            //理论条数


        List<PageData> pdList = new ArrayList<PageData>();

        String phone = pd.getString("PHONE");                    //对方邮箱
        String CONTENT = pd.getString("CONTENT");                //内容
        String isAll = pd.getString("isAll");                    //是否发送给全体成员 yes or no
        String TYPE = pd.getString("TYPE");                        //类型 1：短信接口1   2：短信接口2
        String fmsg = pd.getString("fmsg");                        //判断是系统用户还是会员 "appuser"为会员用户

        if ("yes".endsWith(isAll)) {
            try {
                List<PageData> userList = new ArrayList<PageData>();

                userList = "appuser".equals(fmsg) ? appuserService.listAllUser(pd) : userService.listAllUser(pd);

                zcount = userList.size();
                try {
                    for (int i = 0; i < userList.size(); i++) {
                        if (Tools.checkMobileNumber(userList.get(i).getString("PHONE"))) {            //手机号格式不对就跳过
                            if ("1".equals(TYPE)) {
                                SmsUtil.sendSms1(userList.get(i).getString("PHONE"), CONTENT);        //调用发短信函数1
                            } else {
                                SmsUtil.sendSms2(userList.get(i).getString("PHONE"), CONTENT);        //调用发短信函数2
                            }
                            count++;
                        } else {
                            continue;
                        }
                    }
                    msg = "ok";
                } catch (Exception e) {
                    msg = "error";
                }

            } catch (Exception e) {
                msg = "error";
            }
        } else {
            phone = phone.replaceAll("；", ";");
            phone = phone.replaceAll(" ", "");
            String[] arrTITLE = phone.split(";");
            zcount = arrTITLE.length;
            try {
                for (int i = 0; i < arrTITLE.length; i++) {
                    if (Tools.checkMobileNumber(arrTITLE[i])) {            //手机号式不对就跳过
                        if ("1".equals(TYPE)) {
                            SmsUtil.sendSms1(arrTITLE[i], CONTENT);        //调用发短信函数1
                        } else {
                            SmsUtil.sendSms2(arrTITLE[i], CONTENT);        //调用发短信函数2
                        }
                        count++;
                    } else {
                        continue;
                    }
                }
                msg = "ok";
            } catch (Exception e) {
                msg = "error";
            }
        }
        pd.put("msg", msg);
        pd.put("count", count);                        //成功数
        pd.put("ecount", zcount - count);                //失败数
        pdList.add(pd);
        map.put("list", pdList);
        return AppUtil.returnObject(pd, map);
    }


    /**
     * 保存系统设置2
     */
    @RequestMapping(value = "/saveSys2")
    public ModelAndView saveSys2() throws Exception {
        ModelAndView mv = this.getModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
//        Tools.writeFile(Constant.FWATERM, pd.getString("isCheck1") + ",fh," + pd.getString("fcontent") + ",fh," + pd.getString("fontSize") + ",fh," + pd.getString("fontX") + ",fh," + pd.getString("fontY"));    //文字水印配置
//        Tools.writeFile(Constant.IWATERM, pd.getString("isCheck2") + ",fh," + pd.getString("imgUrl") + ",fh," + pd.getString("imgX") + ",fh," + pd.getString("imgY"));    //图片水印配置
        Watermark.fushValue();
        mv.addObject("msg", "OK");
        mv.setViewName("save_result");
        return mv;
    }

    /**
     * 去代码生成器页面
     */
    @RequestMapping(value = "/toProductCode")
    public ModelAndView toProductCode() throws Exception {
        ModelAndView mv = this.getModelAndView();
        mv.setViewName("config/productCode");
        return mv;
    }

}
