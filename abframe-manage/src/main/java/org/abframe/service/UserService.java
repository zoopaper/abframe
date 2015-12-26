package org.abframe.service;

import org.abframe.dao.BaseDaoSupport;
import org.abframe.entity.Page;
import org.abframe.entity.UserBean;
import org.abframe.util.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class UserService {

    @Autowired
    private BaseDaoSupport dao;

    /**
     * @param pd
     * @return
     * @throws Exception
     */
    @Transactional(readOnly = true)
    public UserBean getUserById(PageData pd) throws Exception {
        return (UserBean) dao.findForObject("UserMapper.getUserById", pd);
    }

    public PageData getUserByAccount(PageData pd) throws Exception {
        return (PageData) dao.findForObject("UserMapper.getUserByAccount", pd);
    }

    /**
     * @param pd
     * @return
     * @throws Exception
     */
    public PageData getUserByEmail(PageData pd) throws Exception {
        return (PageData) dao.findForObject("UserMapper.getUserByEmail", pd);
    }


    public void updateUserAvatarById(PageData pd) throws Exception {
        Object ret = dao.update("UserMapper.updateUserAvatarById", pd);
    }

    @Transactional
    public void saveUser(PageData pd) throws Exception {
        dao.save("UserMapper.saveUser", pd);
    }


    public void editUser(PageData pd) throws Exception {
        dao.update("UserMapper.editUser", pd);
    }

    public void setSkin(PageData pd) throws Exception {
        dao.update("UserXMapper.setSkin", pd);
    }

    public void deleteUser(PageData pd) throws Exception {
        dao.delete("UserMapper.deleteUser", pd);
    }

    /*
    * 批量删除用户
    */
    public void deleteAllU(String[] USER_IDS) throws Exception {
        dao.delete("UserXMapper.deleteAllU", USER_IDS);
    }

    public List<PageData> getUserListPage(Page page) throws Exception {
        return (List<PageData>) dao.findForList("UserMapper.getUserlistPage", page);
    }

    public List<PageData> listAllUser(PageData pd) throws Exception {
        return (List<PageData>) dao.findForList("UserXMapper.listAllUser", pd);
    }

    public UserBean getUserByNameAndPwd(PageData pd) throws Exception {
        return (UserBean) dao.findForObject("UserMapper.getUserInfo", pd);
    }

    /**
     * @param pd
     * @throws Exception
     */
    public void updateLastLogin(PageData pd) throws Exception {
        dao.update("UserMapper.updateLastLogin", pd);
    }

    public UserBean getUserAndRoleById(long userId) throws Exception {
        return (UserBean) dao.findForObject("UserMapper.getUserAndRoleById", userId);
    }
}
