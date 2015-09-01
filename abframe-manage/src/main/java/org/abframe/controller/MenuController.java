package org.abframe.controller;

import net.sf.json.JSONArray;
import org.abframe.controller.base.BaseController;
import org.abframe.entity.Menu;
import org.abframe.service.MenuService;
import org.abframe.util.PageData;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.List;

@Controller
@RequestMapping(value = "/menu")
public class MenuController extends BaseController {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(MenuController.class);

    @Autowired
    private MenuService menuService;

    @RequestMapping
    public ModelAndView list() {
        ModelAndView mv = this.getModelAndView();
        try {
            List<Menu> menuList = menuService.listAllParentMenu();
            mv.addObject("menuList", menuList);
            mv.setViewName("menu/menuList");
        } catch (Exception e) {
            LOGGER.error("Controller menu exception.", e);
        }
        return mv;
    }

    @RequestMapping(value = "/toAdd")
    public ModelAndView toAdd() throws Exception {
        ModelAndView mv = this.getModelAndView();
        try {
            List<Menu> menuList = menuService.listAllParentMenu();
            mv.addObject("menuList", menuList);
            mv.setViewName("menu/menuAdd");
        } catch (Exception e) {
            LOGGER.error("Controller menu exception.", e);
        }
        return mv;
    }

    @RequestMapping(value = "/add")
    public ModelAndView add(Menu menu) throws Exception {
        ModelAndView mv = this.getModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
        try {
            menu.setMENU_ID(String.valueOf(Integer.parseInt(menuService.findMaxId(pd).get("MID").toString()) + 1));
            String PARENT_ID = menu.getPARENT_ID();
            if (!"0".equals(PARENT_ID)) {
                //处理菜单类型====
                pd.put("MENU_ID", PARENT_ID);
                pd = menuService.getMenuById(pd);
                menu.setMENU_TYPE(pd.getString("MENU_TYPE"));
                //处理菜单类型====
            }
            menuService.saveMenu(menu);
            mv.addObject("msg", "success");
        } catch (Exception e) {
            LOGGER.error("Controller menu exception.", e);
            mv.addObject("msg", "failed");
        }
        mv.setViewName("save_result");
        return mv;
    }

    @RequestMapping(value = "/toEdit")
    public ModelAndView toEdit(String MENU_ID) throws Exception {
        ModelAndView mv = this.getModelAndView();
        PageData pd = new PageData();
        try {
            pd = this.getPageData();
            pd.put("MENU_ID", MENU_ID);
            pd = menuService.getMenuById(pd);
            List<Menu> menuList = menuService.listAllParentMenu();
            mv.addObject("menuList", menuList);
            mv.addObject("pd", pd);
            mv.setViewName("menu/menuEdit");
        } catch (Exception e) {
            LOGGER.error("Controller menu exception.", e);
        }
        return mv;
    }

    @RequestMapping(value = "/toEditIcon")
    public ModelAndView toEditIcon(String MENU_ID) throws Exception {
        ModelAndView mv = this.getModelAndView();
        PageData pd = new PageData();
        try {
            pd = this.getPageData();
            pd.put("MENU_ID", MENU_ID);
            mv.addObject("pd", pd);
            mv.setViewName("menu/menuIcon");
        } catch (Exception e) {
            LOGGER.error("Controller menu exception.", e);
        }
        return mv;
    }

    /**
     * 保存菜单图标 (顶部菜单)
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/editIcon")
    public ModelAndView editIcon() throws Exception {
        ModelAndView mv = this.getModelAndView();
        PageData pd = new PageData();
        try {
            pd = this.getPageData();
            pd = menuService.editicon(pd);
            mv.addObject("msg", "success");
        } catch (Exception e) {
            LOGGER.error("Controller menu exception.", e);
            mv.addObject("msg", "failed");
        }
        mv.setViewName("save_result");
        return mv;
    }

    @RequestMapping(value = "/edit")
    public ModelAndView edit() throws Exception {
        ModelAndView mv = this.getModelAndView();
        PageData pd = new PageData();
        try {
            pd = this.getPageData();

            String PARENT_ID = pd.getString("PARENT_ID");
            if (null == PARENT_ID || "".equals(PARENT_ID)) {
                PARENT_ID = "0";
                pd.put("PARENT_ID", PARENT_ID);
            }

            if ("0".equals(PARENT_ID)) {
                //处理菜单类型====
                menuService.editType(pd);
                //处理菜单类型====
            }

            pd = menuService.edit(pd);
            mv.addObject("msg", "success");
        } catch (Exception e) {
            LOGGER.error("Controller menu exception.", e);
            mv.addObject("msg", "failed");
        }
        mv.setViewName("save_result");
        return mv;
    }

    /**
     * 获取当前菜单的所有子菜单
     */
    @RequestMapping(value = "/sub")
    public void getSub(@RequestParam String MENU_ID, HttpServletResponse response) throws Exception {
        try {
            List<Menu> subMenu = menuService.listSubMenuByParentId(MENU_ID);
            JSONArray arr = JSONArray.fromObject(subMenu);
            PrintWriter out;

            response.setCharacterEncoding("utf-8");
            out = response.getWriter();
            String json = arr.toString();
            out.write(json);
            out.flush();
            out.close();
        } catch (Exception e) {
            LOGGER.error("Controller menu exception.", e);
        }
    }

    @RequestMapping(value = "/del")
    public void delete(@RequestParam String MENU_ID, PrintWriter out) {
        try {
            menuService.deleteMenuById(MENU_ID);
            out.write("success");
            out.flush();
            out.close();
        } catch (Exception e) {
            LOGGER.error("Controller menu exception.", e);
        }
    }
}
