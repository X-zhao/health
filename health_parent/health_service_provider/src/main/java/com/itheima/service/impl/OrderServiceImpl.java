package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.constant.MessageConstant;
import com.itheima.dao.MemberDao;
import com.itheima.dao.OrderDao;
import com.itheima.dao.OrdersettingDao;
import com.itheima.entity.Result;
import com.itheima.pojo.Member;
import com.itheima.pojo.Order;
import com.itheima.pojo.OrderSetting;
import com.itheima.service.OrderService;
import com.itheima.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class
OrderServiceImpl implements OrderService {
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private OrdersettingDao ordersettingDao;
    @Autowired
    private MemberDao memberDao;
    @Override
    public Result order(Map map) throws Exception {
        //1、检查用户所选择的预约日期是否已经提前进行了预约设置，如果没有设置则无法进行预约
        Date date = DateUtils.parseString2Date((String) map.get("orderDate"));
        OrderSetting orderSetting = ordersettingDao.findOrderSettingByDate(date);
        if (null == orderSetting){
            return new Result(false, MessageConstant.SELECTED_DATE_CANNOT_ORDER);
        }
        //2、检查用户所选择的预约日期是否已经约满，如果已经约满则无法预约
        if (orderSetting.getNumber()<=orderSetting.getReservations()){
            return new Result(false,MessageConstant.ORDER_FULL);
        }
        //3、检查用户是否重复预约（同一个用户在同一天预约了同一个套餐），如果是重复预约则无法完成再次预约
        String telephone = (String) map.get("telephone");
        Member member = memberDao.findByTelephone(telephone);
        String setmealId = (String) map.get("setmealId");
        if (member!=null){
            //该手机号已注册
            Order order = new Order(member.getId(),date,Integer.parseInt(setmealId));
            List<Order> orderList = orderDao.findByCondition(order);
            if (orderList==null||orderList.size()>=1){
                //重复预约
                return new Result(false,MessageConstant.HAS_ORDERED);
            }
        }else {
            //该手机号还未注册
            //4、检查当前用户是否为会员，如果是会员则直接完成预约，如果不是会员则自动完成注册并进行预约
            //封装会员对象,并注册
            member = new Member();
            member.setName((String) map.get("name"));
            member.setSex((String) map.get("sex"));
            member.setPhoneNumber(telephone);
            member.setIdCard((String) map.get("idCard"));
            member.setRegTime(new Date());
            memberDao.add(member);
        }
        //完成预约
        Order order = new Order(member.getId(),date, (String) map.get("orderType"),Order.ORDERSTATUS_NO,Integer.parseInt(setmealId));
        orderDao.add(order);
        //5、预约成功，更新当日的已预约人数
        ordersettingDao.addReservationsByOrderDate(orderSetting);
        return new Result(true,MessageConstant.ORDER_SUCCESS,order.getId());

    }

    @Override
    public Map findById(Integer id) throws Exception {
        Map map = orderDao.findById4Detail(id);
        if (map!=null){
           map.put("orderDate",DateUtils.parseDate2String((Date) map.get("orderDate")));
        }
        return map;
    }
}
