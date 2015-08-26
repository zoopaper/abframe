package org.abframe.controller;

import org.apache.commons.io.FileUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.abframe.controller.base.BaseController;
import org.abframe.entity.Page;
import org.abframe.service.LinkService;
import org.abframe.util.Constant;
import org.abframe.util.DateUtil;
import org.abframe.util.PageData;
import org.abframe.util.UuidUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping(value = "/link")
public class LinkController extends BaseController {


    @Resource(name = "linkService")
    private LinkService linkService;


    @RequestMapping(value = "/list")
    public ModelAndView listUsers(HttpSession session, Page page) {
        logBefore(logger, "友情链接列表");
        ModelAndView mv = new ModelAndView();
        PageData pd = new PageData();
        try {
            pd = this.getPageData();

            //检索条件================================
            String stiename = pd.getString("stiename!");
            if (null != stiename && !"".equals(stiename)) {
                stiename = stiename.trim();
                pd.put("stiename!", stiename);
            }
            //检索条件================================
            page.setPd(pd);
            List<PageData> varList = linkService.list(page);

            mv.setViewName("link/linkList");
            mv.addObject("varList", varList);
            mv.addObject("pd", pd);
            mv.addObject(Constant.SESSION_QX, this.getHC());    //按钮权限
        } catch (Exception e) {
            logger.error(e.toString(), e);
        }
        return mv;
    }


    @RequestMapping(value = "/toAdd")
    public ModelAndView toAdd() {
        ModelAndView mv = new ModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
        try {
            mv.setViewName("link/linkEdit");
            mv.addObject("msg", "save");
        } catch (Exception e) {
            logger.error(e.toString(), e);
        }
        return mv;
    }


    @RequestMapping(value = "/toEdit")
    public ModelAndView toEdit() {
        ModelAndView mv = new ModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
        try {
            pd = linkService.findById(pd);
            mv.setViewName("link/linkEdit");
            mv.addObject("msg", "edit");
            mv.addObject("pd", pd);
        } catch (Exception e) {
            logger.error(e.toString(), e);
        }
        return mv;
    }


    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public ModelAndView edit(
            HttpServletRequest request,
            @RequestParam(value = "tp", required = false) MultipartFile tp,
            @RequestParam(value = "id", required = false) String id,
            @RequestParam(value = "tpz", required = false) String tpz,
            @RequestParam(value = "stiename", required = false) String stiename,
            @RequestParam(value = "sitecontent", required = false) String sitecontent,
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "sequence", required = false) String sequence,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "tourl", required = false) String tourl
    ) throws Exception {
        ModelAndView mv = new ModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();

        pd.put("id", id);
        pd.put("stiename", stiename);
        pd.put("sitecontent", sitecontent);
        pd.put("type", type);
        pd.put("status", status);
        pd.put("tourl", tourl);
        pd.put("sequence", "".equals(sequence) ? 0 : sequence);
        pd.put("uptime", DateUtil.getTime());

        if (null == tpz) {
            tpz = "";
        }

        //图片上传
        String pictureSaveFilePath = String.valueOf(Thread.currentThread().getContextClassLoader().getResource("")) + "../../";
        pictureSaveFilePath = pictureSaveFilePath.substring(6);        //去掉 'file:/'

        if (null != tp && !tp.isEmpty()) {
            try {
                String tpid = UuidUtil.get32UUID();

                // 扩展名格式：
                String extName = "";
                if (tp.getOriginalFilename().lastIndexOf(".") >= 0) {
                    extName = tp.getOriginalFilename().substring(tp.getOriginalFilename().lastIndexOf("."));
                }

                this.copyFile(tp.getInputStream(), pictureSaveFilePath + "TP", tpid + extName).replaceAll("-", "");
                pd.put("stieurl", tpid + extName);
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            }
        } else {
            pd.put("stieurl", tpz);
        }


        linkService.edit(pd);


        mv.addObject("msg", "success");
        mv.setViewName("save_result");
        return mv;
    }

    @RequestMapping(value = "/save")
    public ModelAndView save(
            HttpServletRequest request,
            @RequestParam(value = "tp", required = false) MultipartFile tp,
            @RequestParam(value = "stiename", required = false) String stiename,
            @RequestParam(value = "sitecontent", required = false) String sitecontent,
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "sequence", required = false) String sequence,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "tourl", required = false) String tourl
    ) throws Exception {
        ModelAndView mv = new ModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();

        pd.put("stiename", stiename);
        pd.put("sitecontent", sitecontent);
        pd.put("type", type);
        pd.put("status", status);
        pd.put("tourl", tourl);
        pd.put("sequence", "".equals(sequence) ? 0 : sequence);
        pd.put("addtime", DateUtil.getTime());
        pd.put("uptime", DateUtil.getTime());

        //图片上传
        String pictureSaveFilePath = String.valueOf(Thread.currentThread().getContextClassLoader().getResource("")) + "../../";
        pictureSaveFilePath = pictureSaveFilePath.substring(6);        //去掉 'file:/'

        if (null != tp && !tp.isEmpty()) {
            try {
                String id = UuidUtil.get32UUID();

                // 扩展名格式：
                String extName = "";
                if (tp.getOriginalFilename().lastIndexOf(".") >= 0) {
                    extName = tp.getOriginalFilename().substring(tp.getOriginalFilename().lastIndexOf("."));
                }

                this.copyFile(tp.getInputStream(), pictureSaveFilePath + "TP", id + extName).replaceAll("-", "");
                pd.put("stieurl", id + extName);
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            }
        } else {
            pd.put("stieurl", "");
        }
        linkService.save(pd);
        mv.addObject("msg", "success");
        mv.setViewName("save_result");
        return mv;
    }

    @RequestMapping(value = "/delete")
    public void delete(PrintWriter out) throws Exception {
        ModelAndView mv = new ModelAndView();
        PageData pd = new PageData();
        try {
            pd = this.getPageData();
            pd = linkService.findById(pd);                                                        //通过ID获取数据
            String adurl = pd.getString("stieurl");

            //删除硬盘上的文件 start
            String xmpath = String.valueOf(Thread.currentThread().getContextClassLoader().getResource("")) + "../../";    //项目路径

            if (adurl != null && !adurl.equals("")) {
                adurl = (xmpath.trim() + "TP/" + adurl.trim()).substring(6).trim();
                File f1 = new File(adurl.trim());
                if (f1.exists()) {
                    f1.delete();
                } else {
                    System.out.println("====" + adurl + "不存在");
                }
            }

            linkService.delete(pd);
            out.write("success");
            out.close();
        } catch (Exception e) {
            logger.error(e.toString(), e);
        }

    }


    @RequestMapping(value = "/delPic")
    public void delPic(PrintWriter out) {
        logBefore(logger, "删除图片");
        try {
            ModelAndView mv = new ModelAndView();
            PageData pd = new PageData();
            pd = this.getPageData();

            String tpurl = pd.getString("tpurl");                                                    //图片路径
            if (tpurl != null) {
                //删除硬盘上的文件 start
                String xmpath = String.valueOf(Thread.currentThread().getContextClassLoader().getResource("")) + "../../";    //项目路径
                tpurl = xmpath.trim() + "TP/" + tpurl.trim();
                tpurl = tpurl.substring(6);                                                            //去掉 'file:/'
                File f = new File(tpurl.trim());
                if (f.exists()) {
                    f.delete();
                } else {
                    System.out.println("====" + tpurl + "不存在");
                }
                //删除硬盘上的文件 end
                linkService.delTp(pd);                                                        //删除数据中图片数据
            }

            out.write("success");
            out.close();
        } catch (Exception e) {
            logger.error(e.toString(), e);
        }
    }

    /**
     * 写文件到当前目录的upload目录中
     *
     * @param in
     * @throws IOException
     */
    private String copyFile(InputStream in, String dir, String realName)
            throws IOException {
        File file = new File(dir, realName);
        if (!file.exists()) {
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            file.createNewFile();
        }
        FileUtils.copyInputStreamToFile(in, file);
        return realName;
    }

    /* ===============================权限================================== */
    public Map<String, String> getHC() {
        Subject currentUser = SecurityUtils.getSubject();  //shiro管理的session
        Session session = currentUser.getSession();
        return (Map<String, String>) session.getAttribute(Constant.SESSION_QX);
    }
    /* ===============================权限================================== */

}

