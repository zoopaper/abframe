package org.abframe.service;

import org.abframe.dao.BaseDaoSupport;
import org.abframe.entity.Page;
import org.abframe.util.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecommendService {

    @Autowired
    private BaseDaoSupport dao;

    public List<PageData> list(Page page) throws Exception {
        return (List<PageData>) dao.findForList("RecommendMapper.featuredlistPage", page);
    }

    /*
    * 通过id获取数据
    */
    public PageData findById(PageData pd) throws Exception {
        return (PageData) dao.findForObject("RecommendMapper.findById", pd);
    }


    public void save(PageData pd) throws Exception {
        dao.save("RecommendMapper.save", pd);
    }


    public void edit(PageData pd) throws Exception {
        dao.update("RecommendMapper.edit", pd);
    }


    public void delete(PageData pd) throws Exception {
        dao.update("RecommendMapper.delete", pd);
    }
}
