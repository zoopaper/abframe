package org.abframe.service;

import org.abframe.dao.BaseDaoSupport;
import org.abframe.entity.Page;
import org.abframe.util.PageData;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("newsService")
public class NewsService {

    @Resource(name = "daoSupport")
    private BaseDaoSupport dao;

    public List<PageData> list(Page page) throws Exception {
        return (List<PageData>) dao.findForList("NewsMapper.newslistPage", page);
    }

    public PageData findById(PageData pd) throws Exception {
        return (PageData) dao.findForObject("NewsMapper.findById", pd);
    }

    public void save(PageData pd) throws Exception {
        dao.save("NewsMapper.save", pd);
    }

    public void edit(PageData pd) throws Exception {
        dao.update("NewsMapper.edit", pd);
    }

    public void delete(PageData pd) throws Exception {
        dao.update("NewsMapper.delete", pd);
    }

    public List<PageData> newslist(PageData pd) throws Exception {
        return (List<PageData>) dao.findForList("NewsMapper.newslist", pd);
    }

}
