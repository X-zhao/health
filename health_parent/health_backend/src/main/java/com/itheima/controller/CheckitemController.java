package com.itheima.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.CheckItem;
import com.itheima.service.CheckitemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/checkitem")
public class CheckitemController {
    @Reference
    private CheckitemService checkitemService;

    @RequestMapping("/add")
    public Result add(@RequestBody CheckItem checkItem) {
        boolean flag = false;
        try {
            flag = checkitemService.add(checkItem);
            if (flag) {
                return new Result(flag, MessageConstant.ADD_CHECKITEM_SUCCESS);
            } else {
                return new Result(flag, MessageConstant.ADD_CHECKITEM_FAIL);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_CHECKITEM_FAIL);
        }


    }

    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {
        return checkitemService.pageQuery(queryPageBean);
    }

    @RequestMapping("/del")
    @PreAuthorize("hasAuthority('CHECKITEM_DELETE')")
    public Result delById(int id){
        try {
            checkitemService.delById(id);
            return new Result(true,MessageConstant.DELETE_CHECKITEM_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.DELETE_CHECKITEM_FAIL);
        }
    }

    @RequestMapping("/updata")
    public Result updata(@RequestBody CheckItem checkItem){
        try {
            checkitemService.updata(checkItem);
            return new Result(true,MessageConstant.EDIT_CHECKITEM_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.EDIT_CHECKITEM_FAIL);
        }

    }

    @RequestMapping("/findAll")
    public Result findAll(){
        try {
            List<CheckItem> list = checkitemService.findAll();
            return new Result(true,MessageConstant.QUERY_CHECKITEM_SUCCESS,list);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_CHECKGROUP_FAIL) ;
        }
    }

}
