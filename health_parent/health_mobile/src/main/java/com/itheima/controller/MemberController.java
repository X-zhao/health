package com.itheima.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.Member;
import com.itheima.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/member")
public class MemberController {
    @Autowired
    private JedisPool jedisPool;
    @Reference
    private MemberService memberService;

    @RequestMapping("/login")
    public Result check(HttpServletResponse response,@RequestBody Map map) {
        try {
            String telephone = (String) map.get("telephone");
            String validateCode = (String) map.get("validateCode");
            Jedis jedis = jedisPool.getResource();
            /*String valid = jedis.get(telephone + RedisMessageConstant.SENDTYPE_LOGIN);
            if (valid == null || valid.isEmpty() || !valid.equals(validateCode)) {
                return new Result(false, MessageConstant.VALIDATECODE_ERROR);
            }*/
            Member member = memberService.findByTelephone(telephone);
            if (member == null) {
                //用户不存在，直接注册
                member = new Member();
                member.setPhoneNumber(telephone);
                member.setRegTime(new Date());
                memberService.addMember(member);
            }
            //登录


            //写入cookie
            Cookie cookie = new Cookie("login_member_telephone", telephone);
            cookie.setMaxAge(60 * 60 * 24 * 30);
            cookie.setPath("/");
            response.addCookie(cookie);
            //  写入redis
            String memberJson = JSON.toJSON(member).toString();
            jedis.setex(telephone, 60 * 30, memberJson);
            return new Result(true, MessageConstant.LOGIN_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "登录失败");
        }

    }

}
