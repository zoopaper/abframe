package org.abframe.service;

import org.abframe.dao.BaseDaoSupport;
import org.abframe.entity.Page;
import org.abframe.util.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class LineService {

    @Autowired
    private BaseDaoSupport dao;

    /*
    * 新增
    */
    public void save(PageData pd) throws Exception {
        dao.save("LineMapper.save", pd);
    }

    /*
    * 删除
    */
    public void delete(PageData pd) throws Exception {
        dao.delete("LineMapper.delete", pd);
    }

    /*
    * 修改
    */
    public void edit(PageData pd) throws Exception {
        dao.update("LineMapper.edit", pd);
    }

    /*
    *列表
    */
    public List<PageData> list(Page page) throws Exception {
        return (List<PageData>) dao.findForList("LineMapper.datalistPage", page);
    }

    /*
    *列表(全部)
    */
    public List<PageData> listAll(PageData pd) throws Exception {
        return (List<PageData>) dao.findForList("LineMapper.listAll", pd);
    }

    /*
    * 通过id获取数据
    */
    public PageData findById(PageData pd) throws Exception {
        return (PageData) dao.findForObject("LineMapper.findById", pd);
    }

    /*
    * 批量删除
    */
    public void deleteAll(String[] ArrayDATA_IDS) throws Exception {
        dao.delete("LineMapper.deleteAll", ArrayDATA_IDS);
    }

}

