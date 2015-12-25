package org.abframe.controller;

import com.alibaba.fastjson.JSONArray;
import org.abframe.controller.base.BaseController;
import org.abframe.entity.Menu;
import org.abframe.service.MenuService;
import org.abframe.util.PageData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.List;


@Controller
@RequestMapping(value = "/menu")
public class MenuController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MenuController.class);

    @Resource
    private MenuService menuService;

    @RequestMapping
    public ModelAndView list() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("menu/menuList");
        try {
            List<Menu> menuList = menuService.getParentMenu();
            mv.addObject("menuList", menuList);
        } catch (Exception e) {
            LOGGER.error("Controller menu exception.", e);
        }
        return mv;
    }

    @RequestMapping(value = "/toAdd", method = RequestMethod.GET)
    public ModelAndView toAdd() throws Exception {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("menu/menuAdd");
        try {
            List<Menu> menuList = menuService.getParentMenu();
            mv.addObject("menuList", menuList);
        } catch (Exception e) {
            LOGGER.error("Controller menu exception.", e);
        }
        return mv;
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ModelAndView add(Menu menu) throws Exception {
        ModelAndView mv = new ModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
        try {
            int parentId = menu.getParentId();
            if (!"0".equals(parentId)) {
                pd.put("id", parentId);
                pd = menuService.getMenuById(pd);
                menu.setMenuType(pd.getString("menuType"));
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
    public ModelAndView toEdit(int menuId) throws Exception {
        ModelAndView mv = new ModelAndView();
        PageData pd = new PageData();
        try {
            pd = this.getPageData();
            pd.put("menuId", menuId);
            pd = menuService.getMenuById(pd);
            List<Menu> menuList = menuService.getParentMenu();
            mv.addObject("menuList", menuList);
            mv.addObject("pd", pd);
            mv.setViewName("menu/menuEdit");
        } catch (Exception e) {
            LOGGER.error("Controller menu exception.", e);
        }
        return mv;
    }

    @RequestMapping(value = "/toEditIcon")
    public ModelAndView toEditIcon(int menuId) throws Exception {
        ModelAndView mv = new ModelAndView();
        PageData pd = new PageData();
        try {
            pd = this.getPageData();
            pd.put("menuId", menuId);
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
        ModelAndView mv = new ModelAndView();
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
        ModelAndView mv = new ModelAndView();
        PageData pd = new PageData();
        try {
            pd = this.getPageData();
            String parentId = pd.getString("parentId");
            if (null == parentId || "".equals(parentId)) {
                parentId = "0";
                pd.put("parentId", parentId);
            }

            if ("0".equals(parentId)) {
                menuService.editType(pd);
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
    public void getSub(@RequestParam int menuId, HttpServletResponse response) throws Exception {
        try {
            List<Menu> subMenu = menuService.getSubMenuByParentId(menuId);
            JSONArray arr = new JSONArray();
            arr.addAll(subMenu);
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
    public void delete(@RequestParam int menuId, PrintWriter out) {
        try {
            menuService.deleteMenuById(menuId);
            out.write("success");
            out.flush();
            out.close();
        } catch (Exception e) {
            LOGGER.error("Controller menu exception.", e);
        }
    }
}
