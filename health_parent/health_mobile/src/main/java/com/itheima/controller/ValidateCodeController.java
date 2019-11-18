package com.itheima.controller;


import com.aliyuncs.exceptions.ClientException;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisMessageConstant;
import com.itheima.entity.Result;
import com.itheima.utils.SMSUtils;
import com.itheima.utils.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@RequestMapping("/validateCode")
@RestController
public class ValidateCodeController {
    @Autowired
    private JedisPool jedisPool;
    @RequestMapping("/send4Login")
    public Result send4Login(String telephone){
        try {
            Jedis jedis = jedisPool.getResource();
            String validateCode = ValidateCodeUtils.generateValidateCode4String(4);
            jedis.setex(telephone+ RedisMessageConstant.SENDTYPE_LOGIN,60*5,validateCode);
            SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE,telephone,validateCode);
            return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
        } catch (ClientException e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }

    }
}
