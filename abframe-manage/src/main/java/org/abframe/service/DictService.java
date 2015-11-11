package org.abframe.service;

import org.abframe.dao.BaseDaoSupport;
import org.abframe.entity.Page;
import org.abframe.util.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DictService {

    @Autowired
    private BaseDaoSupport dao;


    public void save(PageData pd) throws Exception {
        dao.save("DictMapper.save", pd);
    }

    public void edit(PageData pd) throws Exception {
        dao.update("DictMapper.edit", pd);
    }

    //通过id获取数据
    public PageData findById(PageData pd) throws Exception {
        return (PageData) dao.findForObject("DictMapper.findById", pd);
    }

    //查询总数
    public PageData findCount(PageData pd) throws Exception {
        return (PageData) dao.findForObject("DictMapper.findCount", pd);
    }

    //查询某编码
    public PageData findBmCount(PageData pd) throws Exception {
        return (PageData) dao.findForObject("DictMapper.findBmCount", pd);
    }

    //列出同一父类id下的数据
    public List<PageData> dictlistPage(Page page) throws Exception {
        return (List<PageData>) dao.findForList("DictMapper.dictlistPage", page);

    }

    public void delete(PageData pd) throws Exception {
        dao.delete("DictMapper.delete", pd);
    }

}
