package org.abframe.service;

import org.abframe.dao.BaseDaoSupport;
import org.abframe.entity.Page;
import org.abframe.util.PageData;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("advertService")
public class AdvertService {

    @Resource(name = "daoSupport")
    private BaseDaoSupport dao;


    public List<PageData> list(Page page) throws Exception {
        return (List<PageData>) dao.findForList("AdvertMapper.andorralistPage", page);
    }


    public PageData findById(PageData pd) throws Exception {
        return (PageData) dao.findForObject("AdvertMapper.findById", pd);
    }


    public void save(PageData pd) throws Exception {
        dao.save("AdvertMapper.save", pd);
    }


    public void edit(PageData pd) throws Exception {
        dao.update("AdvertMapper.edit", pd);
    }


    public void delete(PageData pd) throws Exception {
        dao.update("AdvertMapper.delete", pd);
    }

    /*
    * 删除图片
    */
    public void delTp(PageData pd) throws Exception {
        dao.update("AdvertMapper.delTp", pd);
    }
}
