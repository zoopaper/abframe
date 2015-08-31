package org.abframe.config;

import com.google.common.collect.ImmutableMap;
import net.common.utils.config.ConfigOper;
import net.common.utils.config.MapConfig;

import javax.annotation.PostConstruct;

/**
 * <p/>
 * User : krisibm@163.com
 * Date: 2015/8/31
 * Time: 10:15
 */
public class ConfigBean extends ConfigOper {

    private String cfgXml;

    private ImmutableMap<String, String> cfgMap;

    @PostConstruct
    public void init() {
        cfgMap = MapConfig.parseConf(cfgXml);
    }


    public String getCfgXml() {
        return cfgXml;
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
