package org.abframe.entity;

import org.abframe.entity.base.IMybatisEntity;

/**
 * <p/>
 * User : krisibm@163.com
 * Date: 2015/9/1
 * Time: 20:38
 */
public class DictBean extends IMybatisEntity {

    private String name;

    private String code;

    private String parentCode;

    private long parentId;

    private int level;

    private int orderId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    public long getParentId() {
        return parentId;
    }

    public void setParentId(long parentId) {
        this.parentId = parentId;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }
}
