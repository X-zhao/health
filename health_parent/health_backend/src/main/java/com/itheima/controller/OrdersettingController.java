package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.OrderSetting;
import com.itheima.service.OrdersettingService;
import com.itheima.utils.POIUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ordersetting")
public class OrdersettingController {
    @Reference
    private OrdersettingService ordersettingService;
    @RequestMapping("/upload")
    public Result upload(@RequestParam("excelFile") MultipartFile excelFile){
        try {
            ArrayList<OrderSetting> list = new ArrayList<>();
            List<String[]> strings = POIUtils.readExcel(excelFile);
            for (String[] string : strings) {
                OrderSetting setting = new OrderSetting();
                setting.setOrderDate(new Date(string[0]));
                setting.setNumber(Integer.parseInt(string[1]));
                list.add(setting);
            }
             ordersettingService.add(list);


            return new Result(true, MessageConstant.IMPORT_ORDERSETTING_SUCCESS);
        } catch (IOException e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.IMPORT_ORDERSETTING_FAIL);
        }



    }

    @RequestMapping("/findByDate")
    public Result findByDate(String time){
        try {
            List<Map> orderSettings =ordersettingService.findByDate(time);
            //int[] a= new int[30];
           // Map<String,Object>[] array=new Map<String,Object>[];
//            Map[] objects = (Map[]) orderSettings.toArray();

            return new Result(true,MessageConstant.GET_ORDERSETTING_SUCCESS,orderSettings);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_ORDERSETTING_FAIL);
        }
    }

    @RequestMapping("/handleOrderSet")
    public Result handleOrderSet(@RequestBody OrderSetting orderSetting){
        try {
//            OrderSetting orderSetting = new OrderSetting();
            /*SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            String s = dateFormat.format(day);
            String dateString= s.split(" ")[0];
            SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date("2015-2-2 14:34:45");*/
            ordersettingService.handleOrderSet(orderSetting);
            return new Result(true,MessageConstant.ORDERSETTING_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true,MessageConstant.ORDERSETTING_FAIL);
        }


    }

}
