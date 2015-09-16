package org.abframe.service;

import org.abframe.dao.BaseDaoSupport;
import org.abframe.entity.Page;
import org.abframe.util.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p/>
 * User : krisibm@163.com
 * Date: 2015/9/16
 * Time: 14:26
 */
@Service
public class CompanyService {

    @Autowired
    private BaseDaoSupport daoSupport;

    /**
     * @param page
     * @return
     * @throws Exception
     */
    public List<PageData> getCompanyListWithPage(Page page) throws Exception {
        return (List<PageData>) daoSupport.findForList("CompanyMapper.companylistPage", page);
    }

}
