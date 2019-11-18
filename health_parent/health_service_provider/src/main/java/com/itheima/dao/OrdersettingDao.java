package com.itheima.dao;

import com.itheima.pojo.OrderSetting;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface OrdersettingDao {


    void updata(OrderSetting orderSetting);
    void add(OrderSetting orderSetting);
    OrderSetting findOrderSettingByDate(Date orderDate);
    List<OrderSetting> findByDate(@Param("start") String start,@Param("end") String end);
    void addReservationsByOrderDate(OrderSetting orderSetting);
    void updataReservations(OrderSetting orderSetting);

    public void editNumberByOrderDate(OrderSetting orderSetting);
    //更新已预约人数
    public void editReservationsByOrderDate(OrderSetting orderSetting);
    public long findCountByOrderDate(Date orderDate);
    //根据日期范围查询预约设置信息
    public List<OrderSetting> getOrderSettingByMonth(Map date);
    //根据预约日期查询预约设置信息
    public OrderSetting findByOrderDate(Date orderDate);
}
