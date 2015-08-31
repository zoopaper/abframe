package org.abframe.controller;

import net.common.utils.mail.MailInfo;
import net.common.utils.mail.MailUtil;
import org.abframe.config.ConfigBean;
import org.abframe.controller.base.BaseController;
import org.abframe.service.MemberUserService;
import org.abframe.service.UserService;
import org.abframe.util.*;
import org.abframe.util.constants.MailConst;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
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
            logger.error(e.toString(), e);
        } finally {
            logAfter(logger);
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
            logger.error(e.toString(), e);
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

    @RequestMapping(value = "/toSendEmail")
    public ModelAndView toSendEmail() throws Exception {
        ModelAndView mv = this.getModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
        mv.setViewName("config/sendEmail");
        mv.addObject("pd", pd);
        return mv;
    }

    @RequestMapping(value = "/sendEmail")
    @ResponseBody
    public Object sendEmail() {
        String mailHost = configBean.getString(MailConst.MAIL_HOST, configBean.getCfgMap(), "");
        String mailPort = configBean.getString(MailConst.MAIL_PORT, configBean.getCfgMap(), "");
        String mailUserName = configBean.getString(MailConst.MAIL_USERNAME, configBean.getCfgMap(), "");
        String mailPassword = configBean.getString(MailConst.MAIL_PASSWORD, configBean.getCfgMap(), "");
        String mailName = configBean.getString(MailConst.MAIL_NAME, configBean.getCfgMap(), "");
        boolean mailAuth = configBean.getBoolean(MailConst.MAIL_SMTP_AUTH, configBean.getCfgMap(), true);


        MailInfo info = new MailInfo();

        info.setMailServerHost(mailHost);
        info.setIsAuth(mailAuth);
        info.setUsername(mailUserName);
        info.setPassword(mailPassword);
        info.setMailName(mailName);
        info.setMailPort(Integer.valueOf(mailPort));
        info.setMailFrom(mailUserName);


        PageData pd = new PageData();
        pd = this.getPageData();
        Map<String, Object> map = new HashMap<String, Object>();
        String msg = "ok";        //发送状态
        int count = 0;            //统计发送成功条数
        int zcount = 0;            //理论条数

        List<PageData> pdList = new ArrayList<PageData>();

        String mailTo = pd.getString("EMAIL");
        String mailTile = pd.getString("TITLE");
        String mailContent = pd.getString("CONTENT");
        String mailType = pd.getString("TYPE");
        String isAll = pd.getString("isAll");

        String fmsg = pd.getString("fmsg");

        info.setBody(mailContent);
        info.setMailSubject(mailTile);

        if ("yes".endsWith(isAll)) {
            try {
                List<PageData> userList = new ArrayList<PageData>();
                userList = "appuser".equals(fmsg) ? appuserService.listAllUser(pd) : userService.listAllUser(pd);

                zcount = userList.size();
                try {
                    for (int i = 0; i < userList.size(); i++) {
                        if (Tools.checkEmail(userList.get(i).getString("EMAIL"))) {
                            info.setMailTo(userList.get(i).getString("EMAIL"));
                            if (mailType.equals("1")) {
                                MailUtil.sendTextMail(info);
                            } else {
                                MailUtil.sendHtmlEmail(info);
                            }

                            count++;
                        } else {
                            continue;
                        }
                    }
                    msg = "ok";
                } catch (Exception e) {
                    msg = "error";
                    e.printStackTrace();
                }

            } catch (Exception e) {
                msg = "error";
                e.printStackTrace();
            }
        } else {
            mailTo = mailTo.replaceAll("；", ";");
            mailTo = mailTo.replaceAll(" ", "");
            String[] mailToArr = mailTo.split(";");
            zcount = mailToArr.length;
            try {
                for (int i = 0; i < mailToArr.length; i++) {
                    if (Tools.checkEmail(mailToArr[i])) {
                        info.setMailTo(mailToArr[i]);
                        if (mailType.equals("1")) {
                            MailUtil.sendTextMail(info);
                        } else {
                            MailUtil.sendHtmlEmail(info);
                        }
                        count++;
                    } else {
                        continue;
                    }
                }
                msg = "ok";
            } catch (Exception e) {
                msg = "error";
                e.printStackTrace();
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
     * 去系统设置页面
     */
    @RequestMapping(value = "/toSystem")
    public ModelAndView toSysConf() throws Exception {
        ModelAndView mv = this.getModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
        pd.put("YSYNAME", Tools.readTxtFile(Constant.SYSNAME));    //读取系统名称
        pd.put("COUNTPAGE", Tools.readTxtFile(Constant.PAGE));        //读取每页条数
        String strEMAIL = Tools.readTxtFile(Constant.EMAIL);        //读取邮件配置
        String strSMS1 = Tools.readTxtFile(Constant.SMS1);            //读取短信1配置
        String strSMS2 = Tools.readTxtFile(Constant.SMS2);            //读取短信2配置
        String strFWATERM = Tools.readTxtFile(Constant.FWATERM);    //读取文字水印配置
        String strIWATERM = Tools.readTxtFile(Constant.IWATERM);    //读取图片水印配置
        pd.put("Token", Tools.readTxtFile(Constant.WEIXIN));        //读取微信配置

        if (null != strEMAIL && !"".equals(strEMAIL)) {
            String strEM[] = strEMAIL.split(",fh,");
            if (strEM.length == 4) {
                pd.put("SMTP", strEM[0]);
                pd.put("PORT", strEM[1]);
                pd.put("EMAIL", strEM[2]);
                pd.put("PAW", strEM[3]);
            }
        }

        if (null != strSMS1 && !"".equals(strSMS1)) {
            String strS1[] = strSMS1.split(",fh,");
            if (strS1.length == 2) {
                pd.put("SMSU1", strS1[0]);
                pd.put("SMSPAW1", strS1[1]);
            }
        }

        if (null != strSMS2 && !"".equals(strSMS2)) {
            String strS2[] = strSMS2.split(",fh,");
            if (strS2.length == 2) {
                pd.put("SMSU2", strS2[0]);
                pd.put("SMSPAW2", strS2[1]);
            }
        }

        if (null != strFWATERM && !"".equals(strFWATERM)) {
            String strFW[] = strFWATERM.split(",fh,");
            if (strFW.length == 5) {
                pd.put("isCheck1", strFW[0]);
                pd.put("fcontent", strFW[1]);
                pd.put("fontSize", strFW[2]);
                pd.put("fontX", strFW[3]);
                pd.put("fontY", strFW[4]);
            }
        }

        if (null != strIWATERM && !"".equals(strIWATERM)) {
            String strIW[] = strIWATERM.split(",fh,");
            if (strIW.length == 4) {
                pd.put("isCheck2", strIW[0]);
                pd.put("imgUrl", strIW[1]);
                pd.put("imgX", strIW[2]);
                pd.put("imgY", strIW[3]);
            }
        }

        mv.setViewName("config/sysEdit");
        mv.addObject("pd", pd);

        return mv;
    }

    /**
     * 保存系统设置1
     */
    @RequestMapping(value = "/saveSys")
    public ModelAndView saveSys() throws Exception {
        ModelAndView mv = this.getModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
        Tools.writeFile(Constant.SYSNAME, pd.getString("YSYNAME"));    //写入系统名称
        Tools.writeFile(Constant.PAGE, pd.getString("COUNTPAGE"));    //写入每页条数
        Tools.writeFile(Constant.EMAIL, pd.getString("SMTP") + ",fh," + pd.getString("PORT") + ",fh," + pd.getString("EMAIL") + ",fh," + pd.getString("PAW"));    //写入邮件服务器配置
        Tools.writeFile(Constant.SMS1, pd.getString("SMSU1") + ",fh," + pd.getString("SMSPAW1"));    //写入短信1配置
        Tools.writeFile(Constant.SMS2, pd.getString("SMSU2") + ",fh," + pd.getString("SMSPAW2"));    //写入短信2配置
        mv.addObject("msg", "OK");
        mv.setViewName("save_result");
        return mv;
    }

    /**
     * 保存系统设置2
     */
    @RequestMapping(value = "/saveSys2")
    public ModelAndView saveSys2() throws Exception {
        ModelAndView mv = this.getModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
        Tools.writeFile(Constant.FWATERM, pd.getString("isCheck1") + ",fh," + pd.getString("fcontent") + ",fh," + pd.getString("fontSize") + ",fh," + pd.getString("fontX") + ",fh," + pd.getString("fontY"));    //文字水印配置
        Tools.writeFile(Constant.IWATERM, pd.getString("isCheck2") + ",fh," + pd.getString("imgUrl") + ",fh," + pd.getString("imgX") + ",fh," + pd.getString("imgY"));    //图片水印配置
        Watermark.fushValue();
        mv.addObject("msg", "OK");
        mv.setViewName("save_result");
        return mv;
    }

    /**
     * 保存系统设置3
     */
    @RequestMapping(value = "/saveSys3")
    public ModelAndView saveSys3() throws Exception {
        ModelAndView mv = this.getModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
        Tools.writeFile(Constant.WEIXIN, pd.getString("Token"));    //写入微信配置
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
