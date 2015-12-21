package org.abframe.base.config;

import com.google.common.collect.ImmutableMap;
import net.common.utils.config.MapConfig;
import net.common.utils.config.MapConfigOperate;

import javax.annotation.PostConstruct;

/**
 * 配置文件
 * <p/>
 * User : krisibm@163.com
 * Date: 2015/8/31
 * Time: 10:15
 */
public class ConfigService extends MapConfigOperate {

    private String cfgXml;

    private ImmutableMap<String, String> cfgMap;

    @PostConstruct
    public void init() {
        cfgMap = MapConfig.parseConf(cfgXml);
    }

    public void setCfgXml(String cfgXml) {
        this.cfgXml = cfgXml;
    }

    public ImmutableMap<String, String> getCfgMap() {
        return cfgMap;
    }

    public void setCfgMap(ImmutableMap<String, String> cfgMap) {
        this.cfgMap = cfgMap;
    }
}
