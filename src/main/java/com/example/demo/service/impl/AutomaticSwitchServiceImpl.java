package com.example.demo.service.impl;

import com.example.demo.dao.UserBaseInfoDao;
import com.example.demo.entity.UserBaseInfo;
import com.example.demo.service.AutomaticSwitchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 描述:
 *
 * @author songlianjie
 * @create 2020-03-09 11:15
 */
@Service
public class AutomaticSwitchServiceImpl implements AutomaticSwitchService{

    @Autowired
    UserBaseInfoDao userBaseInfoDao;
    @Override
    @Transactional(value = "transactionManager")
    public void insertDB(UserBaseInfo userBaseInfo) {
        //同时插入两个库
        userBaseInfoDao.insertOneDB(userBaseInfo);
        userBaseInfoDao.insertTwoDB(userBaseInfo);
        //设置除0来观察是否事务能控制的住
        //int a = 1/0;
    }
}