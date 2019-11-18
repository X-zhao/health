package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.itheima.dao.CheckgroupDao;
import com.itheima.dao.CheckitemDao;
import com.itheima.dao.SetmealDao;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.CheckItem;
import com.itheima.pojo.Setmeal;
import com.itheima.service.SetmealService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@Service(interfaceClass = SetmealService.class)
public class SetmealServiceImpl implements SetmealService {
    @Autowired
    private SetmealDao setmealDao;
    @Autowired
    private CheckgroupDao checkgroupDao;
    @Autowired
    private CheckitemDao checkitemDao;
    @Override
    public void add(Setmeal setmeal, Integer[] checkgroupIds) {
        setmealDao.addSetmeal(setmeal);
        Integer setmealId = setmeal.getId();
        for (Integer checkgroupId : checkgroupIds) {
            setmealDao.add_tsc( setmealId,checkgroupId);
        }
    }

    @Override
    public List<Setmeal> findPage(QueryPageBean queryPageBean) {
        String queryString = queryPageBean.getQueryString();
        PageHelper.startPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize());
        List<Setmeal> list = setmealDao.findByQuery(queryString);
        return list;
    }

    @Override
    public List<Setmeal> getSetmeal() {
        return setmealDao.findByQuery(null);
    }

    @Override
    public Setmeal findSetmealById(Integer sid) {
        Setmeal setmeal = setmealDao.findSetmealById(sid);
        List<Integer> gids =checkgroupDao.findGroupIdsBySid(sid);
        List<CheckGroup> checkGroups = new ArrayList<>();
        for (Integer gid : gids) {
             checkGroups.add(checkgroupDao.findGroupById(gid));
        }
        setmeal.setCheckGroups(checkGroups);
        return  setmeal;

    }

    @Override
    public Setmeal findOneById(Integer sid) {
        //查询检查套餐
        Setmeal setmeal = setmealDao.findSetmealById(sid);
        //查询检查套餐对应检查组ID
        List<Integer> gids =checkgroupDao.findGroupIdsBySid(sid);
        List<CheckGroup> checkGroups = new ArrayList<>();
        for (Integer gid : gids) {
            //查询检查组
            CheckGroup group = checkgroupDao.findGroupById(gid);
            //查询检查组对应检查项ID
            List<Integer> iids =checkitemDao.findIteamIdsByGid(gid);
            ArrayList<CheckItem> checkItems = new ArrayList<>();
            for (Integer iid : iids) {
              CheckItem checkItem = checkitemDao.findIteamByIid(iid);
              checkItems.add(checkItem);
            }
            group.setCheckItems(checkItems);
            checkGroups.add(group);
        }
        setmeal.setCheckGroups(checkGroups);
        return setmeal;

    }
}
