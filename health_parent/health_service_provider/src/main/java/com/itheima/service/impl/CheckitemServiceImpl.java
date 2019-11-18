package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.dao.CheckitemDao;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.CheckItem;
import com.itheima.service.CheckitemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

@Service

public class CheckitemServiceImpl implements CheckitemService {
    @Autowired
    private CheckitemDao checkitemDao;
    @Override
    public boolean add(CheckItem checkItem) {
        checkitemDao.add(checkItem);
        return true;
    }

    @Override
    public PageResult pageQuery(QueryPageBean queryPageBean) {
        Integer pageSize = queryPageBean.getPageSize();
        Integer currentPage = queryPageBean.getCurrentPage();
        String queryString = queryPageBean.getQueryString();


        if (queryString!=null&& ! queryString.isEmpty()){
            queryString="%"+queryString+"%";
        }

        PageHelper.startPage(currentPage, pageSize);
        Page<CheckItem> page = checkitemDao.pageQuery(queryString);

        long total = page.getTotal();
        List<CheckItem> result = page.getResult();
        return  new PageResult(total,result);

    }

    @Override
    public boolean delById(int id) {
        long count =checkitemDao.findCountByCheckItemId(id);
        if (count>0){
            throw new RuntimeException("当前检查项被引用，不能删除");
        }
        int i = checkitemDao.delById(id);
        return true;

    }

    @Override
    public void updata(CheckItem checkItem) {
        checkitemDao.updata(checkItem);
    }

    @Override
    public List<CheckItem> findAll() {
        PageHelper.startPage(1,999999999);
        Page<CheckItem> page = checkitemDao.pageQuery(null);
        return page.getResult();
    }
}
