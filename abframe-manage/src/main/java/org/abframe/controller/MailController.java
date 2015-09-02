package org.abframe.controller;

import net.common.utils.mail.MailInfo;
import net.common.utils.mail.MailUtil;
import org.abframe.config.ConfigBean;
import org.abframe.controller.base.BaseController;
import org.abframe.service.MemberUserService;
import org.abframe.service.UserService;
import org.abframe.util.AppUtil;
import org.abframe.util.PageData;
import org.abframe.util.Tools;
import org.abframe.util.constants.MailConst;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 发送邮件
 * <p/>
 * User : krisibm@163.com
 * Date: 2015/8/31
 * Time: 16:54
 */
@RequestMapping("/mail/*")
@Controller
public class MailController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MailController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private MemberUserService appuserService;

    @Autowired
    ConfigBean configBean;


    @RequestMapping(value = "/toSendEmailM")
    public ModelAndView toSendEmailT() throws Exception {
        ModelAndView mv = new ModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
        mv.setViewName("mail/email");
        mv.addObject("pd", pd);
        return mv;
    }

    @RequestMapping(value = "/toSendEmail")
    public ModelAndView toSendEmail() throws Exception {
        ModelAndView mv = new ModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
        mv.setViewName("mail/sendEmail");
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
}
