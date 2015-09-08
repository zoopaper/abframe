package org.abframe.entity;

public class UserBean {
    private String userId;        //用户id
    private String userName;    //用户名
    private String password;    //密码
    private String name;        //姓名
    private String perms;        //权限
    private String roleId;        //角色id
    private String lastLogin;    //最后登录时间
    private String ip;            //用户登录ip地址
    private String status;        //状态
    private RoleBean role;            //角色对象
    private Page page;            //分页对象
    private String skin;        //皮肤

    public String getSkin() {
        return skin;
    }

    public void setSkin(String sKIN) {
        skin = sKIN;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String uSER_ID) {
        userId = uSER_ID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String uSERNAME) {
        userName = uSERNAME;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String pASSWORD) {
        password = pASSWORD;
    }

    public String getName() {
        return name;
    }

    public void setName(String nAME) {
        name = nAME;
    }

    public String getPerms() {
        return perms;
    }

    public void setPerms(String rIGHTS) {
        perms = rIGHTS;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String rOLE_ID) {
        roleId = rOLE_ID;
    }

    public String getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(String lAST_LOGIN) {
        lastLogin = lAST_LOGIN;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String iP) {
        ip = iP;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String sTATUS) {
        status = sTATUS;
    }

    public RoleBean getRole() {
        return role;
    }

    public void setRole(RoleBean role) {
        this.role = role;
    }

    public Page getPage() {
        if (page == null)
            page = new Page();
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

}
