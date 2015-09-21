package org.abframe.util;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

public class PageData extends HashMap implements Map {

    private static final long serialVersionUID = 5226918731997559284L;

    private Map map = null;

    private HttpServletRequest request;

    public PageData() {
        map = new HashMap();
    }

    public PageData(HttpServletRequest request) {
        this.request = request;
        Map properties = request.getParameterMap();
        Map paramMap = new HashMap();
        Iterator iter = properties.entrySet().iterator();


        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            String paramName = (String) entry.getKey();
            Object paramValue = entry.getValue();

            String paramValues = "";
            if (null == paramValue) {
                paramValues = "";
            } else if (paramValue instanceof String[]) {
                String[] values = (String[]) paramValue;
                for (int i = 0; i < values.length; i++) {
                    paramValues = values[i] + ",";
                }
                paramValues = paramValues.substring(0, paramValues.length() - 1);
            } else {
                paramValues = paramValue.toString();
            }
            paramMap.put(paramName, paramValues);
        }
        map = paramMap;
    }


    @Override
    public Object get(Object key) {
        Object obj = null;
        if (map.get(key) instanceof Object[]) {
            Object[] arr = (Object[]) map.get(key);
            obj = request == null ? arr : (request.getParameter((String) key) == null ? arr : arr[0]);
        } else {
            obj = map.get(key);
        }
        return obj;
    }

    public String getString(Object key) {
        return (String) get(key);
    }

    @Override
    public Object put(Object key, Object value) {
        return map.put(key, value);
    }

    @Override
    public Object remove(Object key) {
        return map.remove(key);
    }

    public void clear() {
        map.clear();
    }

    public boolean containsKey(Object key) {
        return map.containsKey(key);
    }

    public boolean containsValue(Object value) {
        return map.containsValue(value);
    }

    public Set entrySet() {
        return map.entrySet();
    }

    public boolean isEmpty() {
        return map.isEmpty();
    }

    public Set keySet() {
        return map.keySet();
    }

    public void putAll(Map t) {
        map.putAll(t);
    }

    public int size() {
        return map.size();
    }

    public Collection values() {
        return map.values();
    }

}
