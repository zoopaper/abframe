package org.abframe.service;

import org.abframe.dao.BaseDaoSupport;
import org.abframe.entity.Role;
import org.abframe.util.PageData;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("roleService")
public class RoleService {

    @Resource(name = "daoSupport")
    private BaseDaoSupport dao;


    public List<Role> listAllERRoles() throws Exception {
        return (List<Role>) dao.findForList("RoleMapper.listAllERRoles", null);

    }


    public List<Role> listAllappERRoles() throws Exception {
        return (List<Role>) dao.findForList("RoleMapper.listAllappERRoles", null);

    }


    public List<Role> listAllRoles() throws Exception {
        return (List<Role>) dao.findForList("RoleMapper.listAllRoles", null);

    }

    //通过当前登录用的角色id获取管理权限数据
    public PageData findGLbyrid(PageData pd) throws Exception {
        return (PageData) dao.findForObject("RoleMapper.findGLbyrid", pd);
    }

    //通过当前登录用的角色id获取用户权限数据
    public PageData findYHbyrid(PageData pd) throws Exception {
        return (PageData) dao.findForObject("RoleMapper.findYHbyrid", pd);
    }

    //列出此角色下的所有用户
    public List<PageData> listAllUByRid(PageData pd) throws Exception {
        return (List<PageData>) dao.findForList("RoleMapper.listAllUByRid", pd);

    }

    //列出此角色下的所有会员
    public List<PageData> listAllAppUByRid(PageData pd) throws Exception {
        return (List<PageData>) dao.findForList("RoleMapper.listAllAppUByRid", pd);

    }

    /**
     * 列出此部门的所有下级
     */
    public List<Role> listAllRolesByPId(PageData pd) throws Exception {
        return (List<Role>) dao.findForList("RoleMapper.listAllRolesByPId", pd);

    }

    //列出K权限表里的数据
    public List<PageData> listAllkefu(PageData pd) throws Exception {
        return (List<PageData>) dao.findForList("RoleMapper.listAllkefu", pd);
    }

    //列出G权限表里的数据
    public List<PageData> listAllGysQX(PageData pd) throws Exception {
        return (List<PageData>) dao.findForList("RoleMapper.listAllGysQX", pd);
    }

    //删除K权限表里对应的数据
    public void deleteKeFuById(String ROLE_ID) throws Exception {
        dao.delete("RoleMapper.deleteKeFuById", ROLE_ID);
    }

    //删除G权限表里对应的数据
    public void deleteGById(String ROLE_ID) throws Exception {
        dao.delete("RoleMapper.deleteGById", ROLE_ID);
    }

    public void deleteRoleById(String ROLE_ID) throws Exception {
        dao.delete("RoleMapper.deleteRoleById", ROLE_ID);

    }

    public Role getRoleById(String roleId) throws Exception {
        return (Role) dao.findForObject("RoleMapper.getRoleById", roleId);

    }

    public void updateRoleRights(Role role) throws Exception {
        dao.update("RoleMapper.updateRoleRights", role);
    }

    /**
     * 权限(增删改查)
     */
    public void updateQx(String msg, PageData pd) throws Exception {
        dao.update("RoleMapper." + msg, pd);
    }

    /**
     * 客服权限
     */
    public void updateKFQx(String msg, PageData pd) throws Exception {
        dao.update("RoleMapper." + msg, pd);
    }

    /**
     * Gc权限
     */
    public void gysqxc(String msg, PageData pd) throws Exception {
        dao.update("RoleMapper." + msg, pd);
    }

    /**
     * 给全部子职位加菜单权限
     */
    public void setAllRights(PageData pd) throws Exception {
        dao.update("RoleMapper.setAllRights", pd);

    }

    /**
     * 添加
     */
    public void add(PageData pd) throws Exception {
        dao.findForList("RoleMapper.insert", pd);
    }

    /**
     * 保存客服权限
     */
    public void saveKeFu(PageData pd) throws Exception {
        dao.findForList("RoleMapper.saveKeFu", pd);
    }

    /**
     * 保存G权限
     */
    public void saveGYSQX(PageData pd) throws Exception {
        dao.findForList("RoleMapper.saveGYSQX", pd);
    }

    /**
     * 通过id查找
     */
    public PageData findObjectById(PageData pd) throws Exception {
        return (PageData) dao.findForObject("RoleMapper.findObjectById", pd);
    }

    /**
     * 编辑角色
     */
    public PageData edit(PageData pd) throws Exception {
        return (PageData) dao.findForObject("RoleMapper.edit", pd);
    }

}
