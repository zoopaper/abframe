package org.abframe.controller;

import net.common.utils.date.DateUtil;
import org.abframe.common.PermissionHandler;
import org.abframe.controller.base.BaseController;
import org.abframe.entity.Page;
import org.abframe.service.PicService;
import org.abframe.util.*;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping(value = "/pic")
public class PicController extends BaseController {

    String menuUrl = "pic/list";
    @Resource(name = "picService")
    private PicService picService;


    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public Object save(@RequestParam(required = false) MultipartFile file) {
        logBefore(logger, "新增Pictures");
        Map<String, String> map = new HashMap<String, String>();
        String ffile = DateUtil.formatDate(new Date(),DateUtil.DATE_STR1), fileName = "";
        PageData pd = new PageData();
        try {
            if (PermissionHandler.buttonJurisdiction(menuUrl, "add")) {
                if (null != file && !file.isEmpty()) {
                    String filePath = PathUtil.getClasspath() + Constant.FILEPATHIMG + ffile;        //文件上传路径
                    fileName = FileUpload.fileUp(file, filePath, this.get32UUID());                //执行上传
                } else {
                    System.out.println("上传失败");
                }

                pd.put("PICTURES_ID", this.get32UUID());            //主键
                pd.put("TITLE", "图片");                                //标题
                pd.put("NAME", fileName);                            //文件名
                pd.put("PATH", ffile + "/" + fileName);                //路径
                pd.put("CREATETIME", Tools.date2Str(new Date()));    //创建时间
                pd.put("MASTER_ID", "1");                            //附属与
                pd.put("BZ", "图片管理处上传");                        //备注
                //加水印
                Watermark.setWatemark(PathUtil.getClasspath() + Constant.FILEPATHIMG + ffile + "/" + fileName);
                picService.save(pd);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        map.put("result", "ok");
        return AppUtil.returnObject(pd, map);
    }

    @RequestMapping(value = "/delete")
    public void delete(PrintWriter out) {
        logBefore(logger, "删除图片");
        PageData pd = new PageData();
        try {
            if (PermissionHandler.buttonJurisdiction(menuUrl, "del")) {
                pd = this.getPageData();
                DelAllFile.delFolder(PathUtil.getClasspath() + Constant.FILEPATHIMG + pd.getString("PATH")); //删除图片
                picService.delete(pd);
            }
            out.write("success");
            out.close();
        } catch (Exception e) {
            logger.error(e.toString(), e);
        }

    }

    @RequestMapping(value = "/edit")
    public ModelAndView edit(
            HttpServletRequest request,
            @RequestParam(value = "tp", required = false) MultipartFile file,
            @RequestParam(value = "tpz", required = false) String tpz,
            @RequestParam(value = "PICTURES_ID", required = false) String PICTURES_ID,
            @RequestParam(value = "TITLE", required = false) String TITLE,
            @RequestParam(value = "MASTER_ID", required = false) String MASTER_ID,
            @RequestParam(value = "BZ", required = false) String BZ
    ) throws Exception {
        logBefore(logger, "修改Pictures");
        ModelAndView mv = this.getModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
        if (PermissionHandler.buttonJurisdiction(menuUrl, "edit")) {
            pd.put("PICTURES_ID", PICTURES_ID);        //图片ID
            pd.put("TITLE", TITLE);                    //标题
            pd.put("MASTER_ID", MASTER_ID);            //属于ID
            pd.put("BZ", BZ);                        //备注

            if (null == tpz) {
                tpz = "";
            }
            String ffile = DateUtil.formatDate(new Date(), "yyyyMMdd"), fileName = "";
            if (null != file && !file.isEmpty()) {
                String filePath = PathUtil.getClasspath() + Constant.FILEPATHIMG + ffile;        //文件上传路径
                fileName = FileUpload.fileUp(file, filePath, this.get32UUID());                //执行上传
                pd.put("PATH", ffile + "/" + fileName);                //路径
                pd.put("NAME", fileName);
            } else {
                pd.put("PATH", tpz);
            }
            Watermark.setWatemark(PathUtil.getClasspath() + Constant.FILEPATHIMG + ffile + "/" + fileName);//加水印
            picService.edit(pd);                //执行修改数据库
        }
        mv.addObject("msg", "success");
        mv.setViewName("save_result");
        return mv;
    }


    @RequestMapping(value = "/list")
    public ModelAndView list(Page page) {
        logBefore(logger, "列表Pictures");
        ModelAndView mv = this.getModelAndView();
        PageData pd = new PageData();
        try {
            pd = this.getPageData();

            String KEYW = pd.getString("keyword");

            if (null != KEYW && !"".equals(KEYW)) {
                KEYW = KEYW.trim();
                pd.put("KEYW", KEYW);
            }

            page.setPd(pd);
            List<PageData> varList = picService.list(page);    //列出Pictures列表
            mv.setViewName("pic/picList");
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
        logBefore(logger, "去新增图片页面");
        ModelAndView mv = this.getModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
        try {
            mv.setViewName("pic/picAdd");
            mv.addObject("pd", pd);
        } catch (Exception e) {
            logger.error(e.toString(), e);
        }
        return mv;
    }

    @RequestMapping(value = "/toEdit")
    public ModelAndView toEdit() {
        logBefore(logger, "去修改图片页面");
        ModelAndView mv = this.getModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
        try {
            pd = picService.findById(pd);    //根据ID读取
            mv.setViewName("pic/picEdit");
            mv.addObject("msg", "edit");
            mv.addObject("pd", pd);
        } catch (Exception e) {
            logger.error(e.toString(), e);
        }
        return mv;
    }

    @RequestMapping(value = "/deleteAll")
    @ResponseBody
    public Object deleteAll() {
        logBefore(logger, "批量删除Pictures");
        PageData pd = new PageData();
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            pd = this.getPageData();
            if (PermissionHandler.buttonJurisdiction(menuUrl, "del")) {
                List<PageData> pdList = new ArrayList<PageData>();
                List<PageData> pathList = new ArrayList<PageData>();
                String DATA_IDS = pd.getString("DATA_IDS");
                if (null != DATA_IDS && !"".equals(DATA_IDS)) {
                    String ArrayDATA_IDS[] = DATA_IDS.split(",");
                    pathList = picService.getAllById(ArrayDATA_IDS);
                    //删除图片
                    for (int i = 0; i < pathList.size(); i++) {
                        DelAllFile.delFolder(PathUtil.getClasspath() + Constant.FILEPATHIMG + pathList.get(i).getString("PATH"));
                    }
                    picService.deleteAll(ArrayDATA_IDS);
                    pd.put("msg", "ok");
                } else {
                    pd.put("msg", "no");
                }
                pdList.add(pd);
                map.put("list", pdList);
            }
        } catch (Exception e) {
            logger.error(e.toString(), e);
        } finally {
            logAfter(logger);
        }
        return AppUtil.returnObject(pd, map);
    }

    /*
     * 导出到excel
     * @return
     */
    @RequestMapping(value = "/excel")
    public ModelAndView exportExcel() {
        logBefore(logger, "导出Pictures到excel");
        ModelAndView mv = new ModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
        try {
            Map<String, Object> dataMap = new HashMap<String, Object>();
            List<String> titles = new ArrayList<String>();
            titles.add("标题");    //1
            titles.add("文件名");    //2
            titles.add("路径");    //3
            titles.add("创建时间");    //4
            titles.add("属于");    //5
            titles.add("备注");    //6
            dataMap.put("titles", titles);
            List<PageData> varOList = picService.listAll(pd);
            List<PageData> varList = new ArrayList<PageData>();
            for (int i = 0; i < varOList.size(); i++) {
                PageData vpd = new PageData();
                vpd.put("var1", varOList.get(i).getString("TITLE"));    //1
                vpd.put("var2", varOList.get(i).getString("NAME"));    //2
                vpd.put("var3", varOList.get(i).getString("PATH"));    //3
                vpd.put("var4", varOList.get(i).getString("CREATETIME"));    //4
                vpd.put("var5", varOList.get(i).getString("MASTER_ID"));    //5
                vpd.put("var6", varOList.get(i).getString("BZ"));    //6
                varList.add(vpd);
            }
            dataMap.put("varList", varList);
            ObjectExcelView erv = new ObjectExcelView();
            mv = new ModelAndView(erv, dataMap);
        } catch (Exception e) {
            logger.error(e.toString(), e);
        }
        return mv;
    }


    //删除图片
    @RequestMapping(value = "/delPic")
    public void delPic(PrintWriter out) {
        logBefore(logger, "删除图片");
        try {
            PageData pd = new PageData();
            pd = this.getPageData();
            String PATH = pd.getString("PATH");                                                            //图片路径
            DelAllFile.delFolder(PathUtil.getClasspath() + Constant.FILEPATHIMG + pd.getString("PATH"));    //删除图片
            if (PATH != null) {
                picService.delTp(pd);                                                                //删除数据中图片数据
            }
            out.write("success");
            out.close();
        } catch (Exception e) {
            logger.error(e.toString(), e);
        }
    }

    /* ===============================权限================================== */
    public Map<String, String> getHC() {
        Subject currentUser = SecurityUtils.getSubject();  //shiro管理的session
        Session session = currentUser.getSession();
        return (Map<String, String>) session.getAttribute(Constant.SESSION_QX);
    }
    /* ===============================权限================================== */

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        binder.registerCustomEditor(Date.class, new CustomDateEditor(format, true));
    }
}
