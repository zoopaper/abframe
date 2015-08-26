package org.abframe.service;

import org.abframe.dao.BaseDaoSupport;
import org.abframe.entity.Page;
import org.abframe.util.PageData;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


@Service("weixinTextMsgService")
public class WeixinTextMsgService {

    @Resource(name = "daoSupport")
    private BaseDaoSupport dao;


    public void save(PageData pd) throws Exception {
        dao.save("WeixinTextMsgMapper.save", pd);
    }

    public void delete(PageData pd) throws Exception {
        dao.delete("WeixinTextMsgMapper.delete", pd);
    }


    public void edit(PageData pd) throws Exception {
        dao.update("WeixinTextMsgMapper.edit", pd);
    }


    public List<PageData> list(Page page) throws Exception {
        return (List<PageData>) dao.findForList("WeixinTextMsgMapper.datalistPage", page);
    }


    public List<PageData> listAll(PageData pd) throws Exception {
        return (List<PageData>) dao.findForList("WeixinTextMsgMapper.listAll", pd);
    }

    /*
    * 通过id获取数据
    */
    public PageData findById(PageData pd) throws Exception {
        return (PageData) dao.findForObject("WeixinTextMsgMapper.findById", pd);
    }

    /*
    * 批量删除
    */
    public void deleteAll(String[] ArrayDATA_IDS) throws Exception {
        dao.delete("WeixinTextMsgMapper.deleteAll", ArrayDATA_IDS);
    }

    /*
    * 匹配关键词
    */
    public PageData findByKw(PageData pd) throws Exception {
        return (PageData) dao.findForObject("WeixinTextMsgMapper.findByKw", pd);
    }
}

