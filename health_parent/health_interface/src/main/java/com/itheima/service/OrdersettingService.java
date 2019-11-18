package com.itheima.service;

import com.itheima.pojo.OrderSetting;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface OrdersettingService {

    void add(ArrayList<OrderSetting> list);

    List<Map> findByDate(String time);

    void handleOrderSet(OrderSetting orderSetting);
}
