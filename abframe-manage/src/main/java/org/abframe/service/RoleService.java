package org.abframe.service;

import org.abframe.dao.BaseDaoSupport;
import org.abframe.entity.RoleBean;
import org.abframe.util.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {

    @Autowired
    private BaseDaoSupport dao;

    public List<RoleBean> listAllRoles() throws Exception {
        return (List<RoleBean>) dao.findForList("RoleMapper.listAllRoles", null);

    }

    public List<PageData> getUserByRoleId(PageData pd) throws Exception {
        return (List<PageData>) dao.findForList("RoleMapper.getUserByRoleId", pd);

    }

    public void deleteRoleById(long id) throws Exception {
        dao.delete("RoleMapper.deleteRoleById", id);
    }

    public void updateRoleRights(RoleBean role) throws Exception {
        dao.update("RoleMapper.updateRoleRights", role);
    }

    public void addRole(PageData pd) throws Exception {
        dao.findForList("RoleMapper.insert", pd);
    }

    public RoleBean getRoleById(long roleId) throws Exception {
        return (RoleBean) dao.findForObject("RoleMapper.getRoleById", roleId);
    }

    public PageData updateRoleById(PageData pd) throws Exception {
        return (PageData) dao.findForObject("RoleMapper.updateRoleById", pd);
    }

}
