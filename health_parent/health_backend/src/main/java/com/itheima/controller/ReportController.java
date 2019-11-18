package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.Result;
import com.itheima.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/report")
public class ReportController {
    @Reference
    private MemberService memberService;
    //会员数量折线图
    @RequestMapping("/getMemberReport")
    public Result getMemberReport(){
        //data: res.data.data.months
        //data: res.data.data.memberCount
        try {
            return memberService.getMemberReport();
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_MEMBER_NUMBER_REPORT_FAIL);
        }

    }
    @RequestMapping("/getSetmealReport")
    public Result getSetmealReport(){
        try {
            return new Result(true,MessageConstant.GET_SETMEAL_COUNT_REPORT_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_SETMEAL_COUNT_REPORT_FAIL);
        }
    }
}
