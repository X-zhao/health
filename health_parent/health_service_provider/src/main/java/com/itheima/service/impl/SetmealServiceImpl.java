package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.itheima.dao.CheckgroupDao;
import com.itheima.dao.CheckitemDao;
import com.itheima.dao.OrderDao;
import com.itheima.dao.SetmealDao;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.CheckItem;
import com.itheima.pojo.Setmeal;
import com.itheima.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = SetmealService.class)
public class SetmealServiceImpl implements SetmealService {
    @Autowired
    private SetmealDao setmealDao;
    @Autowired
    private CheckgroupDao checkgroupDao;
    @Autowired
    private CheckitemDao checkitemDao;
    @Autowired
    private OrderDao orderDao;
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

    @Override
    public Map getSetmealReport() {
        //data: res.data.data.setmealNames
        HashMap<String, Object> data = new HashMap<>();
        List<Setmeal> list = setmealDao.findByQuery(null);
        List<String> setmealNames = new ArrayList<>();
        List<Map> setmealCount= new ArrayList<>();

        for (Setmeal setmeal : list) {
            setmealNames.add(setmeal.getName());
            int count = orderDao.findOrderCountBySetmealId(setmeal.getId());
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("value",count);
            hashMap.put("name",setmeal.getName());
            setmealCount.add(hashMap);
        }
        data.put("setmealNames",setmealNames);
        data.put("setmealCount",setmealCount);
        //data:res.data.data.setmealCount,
            /*
            [
                {value:335, name:'直接访问'},
                {value:310, name:'邮件营销'},
                {value:274, name:'联盟广告'},
                {value:235, name:'视频广告'},
                {value:400, name:'搜索引擎'}
            ].sort(function (a, b) { return a.value - b.value; }),
             */

        return data;
    }
}
