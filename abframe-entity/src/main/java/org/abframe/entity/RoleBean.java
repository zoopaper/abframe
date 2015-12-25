package org.abframe.entity;

import org.abframe.entity.base.IMybatisEntity;

/**
 * 角色
 * <p/>
 * User : krisibm@163.com
 * Date: 2015/9/2
 * Time: 17:28
 */
public class RoleBean extends IMybatisEntity {

    private String roleId;

    private String name;

    private String roleName;

    private String parentId;

    private String perms;

    private String permId;

    private String permAdd;

    private String permDel;

    private String permEdit;

    private String permQuery;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
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

    public String getPermAdd() {
        return permAdd;
    }

    public void setPermAdd(String permAdd) {
        this.permAdd = permAdd;
    }

    public String getPermDel() {
        return permDel;
    }

    public void setPermDel(String permDel) {
        this.permDel = permDel;
    }

    public String getPermEdit() {
        return permEdit;
    }

    public void setPermEdit(String permEdit) {
        this.permEdit = permEdit;
    }

    public String getPermQuery() {
        return permQuery;
    }

    public void setPermQuery(String permQuery) {
        this.permQuery = permQuery;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }
}
