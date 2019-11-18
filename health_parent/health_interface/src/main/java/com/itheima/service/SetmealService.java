package com.itheima.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.Setmeal;

import java.util.List;
import java.util.Map;


public interface SetmealService {


    void add(Setmeal setmeal, Integer[] checkgroupIds);

    List<Setmeal> findPage(QueryPageBean queryPageBean);

    List<Setmeal> getSetmeal();

    Setmeal findSetmealById(Integer id);

    Setmeal findOneById(Integer id);

    Map getSetmealReport();
}
