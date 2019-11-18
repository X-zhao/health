package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.dao.CheckgroupDao;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.CheckGroup;
import com.itheima.service.CheckgroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service//(interfaceClass = CheckgroupService.class)

public class CheckgroupServiceImpl implements CheckgroupService {
    @Autowired
    private CheckgroupDao checkgroupDao;
    @Override
    public void add(CheckGroup checkGroup, Integer[] checkitemIds) {
        checkgroupDao.addCheckGroup(checkGroup);
        int cgid = checkGroup.getId();
        for (Integer checkitemId : checkitemIds) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("cgid",cgid);
            map.put("checkitemId",checkitemId);
            checkgroupDao.addCheckgroup_checkitem(map);
        }
    }

    @Override
    public PageResult pageQuery(QueryPageBean queryPageBean) {
        String queryString = queryPageBean.getQueryString();
        Integer currentPage = queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();

        if (queryString!=null){
            queryString="%"+queryString+"%";
        }

        PageHelper.startPage(currentPage,pageSize);
        Page<CheckGroup> page = checkgroupDao.pageQuery(queryString);
        PageResult pageResult = new PageResult(page.getTotal(), page.getResult());
        return pageResult;
    }

    @Override
    public void delById(int id) {

        checkgroupDao.delete_tcc(id);

        checkgroupDao.delete_tcg(id);

    }

    @Override
    public List<CheckGroup> findById(int id) {
        List<CheckGroup> list = new ArrayList<>();
        CheckGroup groupById = checkgroupDao.findGroupById(id);
        list.add(groupById);
        return list;
    }

    @Override
    public Integer[] find_tccById(int id) {

        Integer[] ids=checkgroupDao.find_tccById(id);
        return ids;
    }

    @Override
    public void updata(CheckGroup checkGroup, Integer[] ids) {
        checkgroupDao.updata_tcg(checkGroup);
        Integer cgid = checkGroup.getId();
        checkgroupDao.delete_tcc(cgid);
        for (Integer id : ids) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("cgid",cgid);
            map.put("checkitemId",id);
            checkgroupDao.addCheckgroup_checkitem(map);
        }
    }

    @Override
    public List<CheckGroup> findAll() {
        PageHelper.startPage(1,999999999);
        Page<CheckGroup> page = checkgroupDao.pageQuery(null);
        return page.getResult();
    }
}
