package com.itheima.dao;

import com.github.pagehelper.Page;

import com.itheima.pojo.CheckItem;


import java.util.List;
import java.util.Map;

public interface CheckitemDao {
//    @Insert("insert into t_checkitem (code,name,sex,age,type,price,remark,attention) " +
//            "values(#{code},#{name},#{sex},#{age},#{type},#{price},#{remark},#{attention})")
    void add(CheckItem checkItem);
    Page<CheckItem> pageQuery(String queryString);
    int delById(int id);
    public void updata(CheckItem checkItem) ;
    long findCountByCheckItemId(int id);
    List<Integer> findIteamIdsByGid(Integer gid);
    CheckItem findIteamByIid(Integer iid);
}
