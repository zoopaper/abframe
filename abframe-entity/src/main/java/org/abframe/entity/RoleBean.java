package org.abframe.entity;

import org.abframe.entity.base.IMybatisEntity;

import java.util.Date;

/**
 * 角色
 * <p/>
 * User : krisibm@163.com
 * Date: 2015/9/2
 * Time: 17:28
 */
public class RoleBean extends IMybatisEntity {

    private String roleName;

    private String perms;

    private String permId;

    private Date createTime;

    private String menuId;


    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getPerms() {
        return perms;
    }

    public void setPerms(String perms) {
        this.perms = perms;
    }

    public String getPermId() {
        return permId;
    }

    public void setPermId(String permId) {
        this.permId = permId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }
}
