package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.OrdersettingDao;
import com.itheima.pojo.OrderSetting;
import com.itheima.service.OrdersettingService;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.*;

@Service(interfaceClass = OrdersettingService.class)
public class OrdersettingServiceImpl implements OrdersettingService {
    @Autowired
    private OrdersettingDao ordersettingDao;

    @Override
    public void add(ArrayList<OrderSetting> list) {
        for (OrderSetting orderSetting : list) {
            //先查
            if (null != ordersettingDao.findByOrderDate(orderSetting.getOrderDate())) {
                //已存在
                ordersettingDao.updata(orderSetting);
            } else {
                //不存在
                ordersettingDao.add(orderSetting);
            }
        }

    }

    @Override
    public List<Map> findByDate(String time) {
        String start = time + "-1";
        String end = time + "-31";
        List<OrderSetting> orderSettings = ordersettingDao.findByDate(start, end);
        ArrayList<Map> list = new ArrayList<>();
        for (OrderSetting orderSetting : orderSettings) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("date", orderSetting.getOrderDate().getDate());
            // map.put("date",orderSetting.getId());
            map.put("number", orderSetting.getNumber());
            map.put("reservations", orderSetting.getReservations());
            list.add(map);
        }
        return list;
    }

    @Override
    public void handleOrderSet(OrderSetting orderSetting) {
        if (null==ordersettingDao.findByOrderDate(orderSetting.getOrderDate())){
            ordersettingDao.add(orderSetting);
        }else {
            ordersettingDao.updata(orderSetting);
        }
    }
}
