package org.abframe.service;

import org.abframe.dao.BaseDaoSupport;
import org.abframe.entity.Page;
import org.abframe.util.PageData;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


@Service("picService")
public class PicService {

    @Resource(name = "daoSupport")
    private BaseDaoSupport dao;


    public void save(PageData pd) throws Exception {
        dao.save("PicMapper.save", pd);
    }

    public void delete(PageData pd) throws Exception {
        dao.delete("PicMapper.delete", pd);
    }

    public void edit(PageData pd) throws Exception {
        dao.update("PicMapper.edit", pd);
    }

    public List<PageData> list(Page page) throws Exception {
        return (List<PageData>) dao.findForList("PicMapper.datalistPage", page);
    }

    public List<PageData> listAll(PageData pd) throws Exception {
        return (List<PageData>) dao.findForList("PicMapper.listAll", pd);
    }

    /*
    * 通过id获取数据
    */
    public PageData findById(PageData pd) throws Exception {
        return (PageData) dao.findForObject("PicMapper.findById", pd);
    }

    /*
    * 批量删除
    */
    public void deleteAll(String[] ArrayDATA_IDS) throws Exception {
        dao.delete("PicMapper.deleteAll", ArrayDATA_IDS);
    }

    /*
    * 批量获取
    */
    public List<PageData> getAllById(String[] ArrayDATA_IDS) throws Exception {
        return (List<PageData>) dao.findForList("PicMapper.getAllById", ArrayDATA_IDS);
    }

    /*
    * 删除图片
    */
    public void delTp(PageData pd) throws Exception {
        dao.update("PicMapper.delTp", pd);
    }

}

