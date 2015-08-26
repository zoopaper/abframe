package org.abframe.controller.api;

import org.abframe.controller.base.BaseController;
import org.abframe.service.MemberUserService;
import org.abframe.util.AppUtil;
import org.abframe.util.PageData;
import org.abframe.util.Tools;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;


/**
 * 会员-接口类
 * <p/>
 * 相关参数协议：
 * 00	请求失败
 * 01	请求成功
 * 02	返回空值
 * 03	请求协议参数不完整
 * 04  用户名或密码错误
 * 05  FKEY验证失败
 */
@Controller
@RequestMapping(value = "/memberApi")
public class MemberUserApiController extends BaseController {

    @Resource(name = "memberUserService")
    private MemberUserService memberUesrService;

    /**
     * 根据用户名获取会员信息
     */
    @RequestMapping(value = "/getMemberUserByUm")
    @ResponseBody
    public Object getAppuserByUsernmae() {
        logBefore(logger, "根据用户名获取会员信息");
        Map<String, Object> map = new HashMap<String, Object>();
        PageData pd = new PageData();
        pd = this.getPageData();
        String result = "00";

        try {
            if (Tools.checkKey("USERNAME", pd.getString("FKEY"))) {
                if (AppUtil.checkParam("getAppuserByUsernmae", pd)) {
                    pd = memberUesrService.findByUId(pd);

                    map.put("pd", pd);
                    result = (null == pd) ? "02" : "01";

                } else {
                    result = "03";
                }
            } else {
                result = "05";
            }
        } catch (Exception e) {
            logger.error(e.toString(), e);
        } finally {
            map.put("result", result);
            logAfter(logger);
        }

        return AppUtil.returnObject(new PageData(), map);
    }
}

 