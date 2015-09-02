package org.abframe.entity.base;

import java.io.Serializable;

/**
 * <p/>
 * User : krisibm@163.com
 * Date: 2015/8/28
 * Time: 17:47
 */
public class IMybatisEntity implements Serializable {

    private String id;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
