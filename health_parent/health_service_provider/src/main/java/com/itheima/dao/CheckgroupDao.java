package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.CheckGroup;

import java.util.HashMap;
import java.util.List;


public interface CheckgroupDao {
    void addCheckGroup(CheckGroup checkGroup);

    void addCheckgroup_checkitem(HashMap<String, Object> map);

    Page<CheckGroup> pageQuery(String queryString);


    void delete_tcc(int tcgid);

    void delete_tcg(int id);

    CheckGroup findGroupById(int id);

    Integer[] find_tccById(int id);

    void updata_tcg(CheckGroup checkGroup);

    List<Integer> findGroupIdsBySid(Integer sid);
}
