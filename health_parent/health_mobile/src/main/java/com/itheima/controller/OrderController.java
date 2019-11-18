package com.itheima.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.aliyuncs.exceptions.ClientException;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisConstant;
import com.itheima.constant.RedisMessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.Order;
import com.itheima.service.OrderService;
import com.itheima.utils.SMSUtils;
import com.itheima.utils.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Date;
import java.util.Map;

@RequestMapping("/order")
@RestController
public class OrderController {
    @Autowired
    private JedisPool jedisPool;
    @Reference
    private OrderService orderService;

    @RequestMapping("/validate")
    public Result validate(String telephone) {
        try {
            String validateCode = ValidateCodeUtils.generateValidateCode4String(4);
            SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE, telephone, validateCode);
            Jedis jedis = jedisPool.getResource();
            jedis.setex(telephone + RedisMessageConstant.SENDTYPE_ORDER, 60 * 5, validateCode);
            return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
        } catch (ClientException e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }

    }

    @RequestMapping("/reserve")
    public Result reserve(@RequestBody Map map) {
        String telephone = (String) map.get("telephone");
        String validateCode = (String) map.get("validateCode");
        Jedis jedis = jedisPool.getResource();
        String jedisValidateCode = jedis.get(telephone + RedisMessageConstant.SENDTYPE_ORDER);
        //判断验证码
        /* if (jedisValidateCode==null){
            return new Result(false,"验证码已过期");
        }
       if (! jedisValidateCode.equals(validateCode)){
            return new Result(false,MessageConstant.VALIDATECODE_ERROR);
        }*/
        /*Object date2=map.get("orderDate");
        System.out.println(date2.getClass().getName());*/
        Result result = new Result(false, MessageConstant.ORDERSETTING_FAIL);
        try {
            map.put("orderType", Order.ORDERTYPE_WEIXIN);
            result = orderService.order(map);
            //发送预约成功提示短信
            if (result.isFlag()) {
                SMSUtils.sendShortMessage(SMSUtils.ORDER_NOTICE, telephone, (String) map.get("orderDate"));
                //"{\"code\":\""+param+"\"}"
                //"{\"code\":\""+"2014,name:12345"+"\"}"
            }
        } catch (Exception e) {
            e.printStackTrace();
            return result;
        }
        return result;


    }

    @RequestMapping("/findById")
    public Result findById(Integer id){
        try {
            Map map =orderService.findById(id);
            return new Result(true,MessageConstant.QUERY_ORDER_SUCCESS,map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_ORDER_FAIL);
        }
    }
}
