package com.example.demo.controller;

import com.example.demo.dao.UserBaseInfoDao;
import com.example.demo.entity.UserBaseInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 自动切换数据源
 *
 * @author songlj
 * @date 2020/3/8 22:01
 */
@RestController
@RequestMapping(value = "automaticSwitch")
public class AutomaticSwitchController {

    @Autowired
    UserBaseInfoDao userBaseInfoDao;

    @RequestMapping(value = "insertTwoDB")
    public String insertTwoDB(String name){
        //这里模拟插入数据库，省略service层，展示出效果即可
        UserBaseInfo userBaseInfo = new UserBaseInfo();
        userBaseInfo.setAge(30);
        userBaseInfo.setName(name);
        userBaseInfoDao.insertOneDB(userBaseInfo);
        userBaseInfoDao.insertTwoDB(userBaseInfo);
        int a = 1/0;
        return "ok";
    }
}
