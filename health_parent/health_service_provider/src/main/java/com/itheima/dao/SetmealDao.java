package com.itheima.dao;

import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.Setmeal;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SetmealDao {

    void addSetmeal(Setmeal setmeal);

    void add_tsc(@Param("setmealId")Integer setmealId,@Param("checkgroupId") Integer checkgroupId);

    List<Setmeal> findByQuery(String queryString);

    Setmeal findSetmealById(int id);

    Setmeal testSetmeal(int id);


}
