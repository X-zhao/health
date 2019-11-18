package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.CheckItem;

import java.util.List;

public interface CheckitemService {
    boolean add(CheckItem checkItem);

    PageResult pageQuery(QueryPageBean queryPageBean);

    boolean delById(int id);

    void updata(CheckItem checkItem);

    List<CheckItem> findAll();
}
