package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.CheckGroup;

import java.util.List;

public interface CheckgroupService {

   void add(CheckGroup checkGroup,Integer[] checkitemIds);

   PageResult pageQuery(QueryPageBean queryPageBean);

    void delById(int id);

    List<CheckGroup> findById(int id);

    Integer[] find_tccById(int id);

    void updata(CheckGroup checkGroup, Integer[] ids);

    List<CheckGroup> findAll();
}
