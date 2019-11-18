package com.itheima.jobs;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.constant.RedisConstant;
import com.itheima.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Date;
import java.util.Set;

public class ClearImgJob {
    @Autowired
    private JedisPool jedisPool;
    public void clearImg(){
        Jedis jedis = jedisPool.getResource();
        Set<String> stringSet = jedis.sdiff(RedisConstant.SETMEAL_PIC_RESOURCES,RedisConstant.SETMEAL_PIC_DB_RESOURCES);
        if (stringSet!=null) {
            for (String resource : stringSet) {
                QiniuUtils.deleteFileFromQiniu(resource);
                jedis.srem(RedisConstant.SETMEAL_PIC_RESOURCES,resource);
            }
        }
        System.out.println("清理垃圾图片成功"+new Date());
    }
}
