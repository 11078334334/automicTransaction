package com.example.demo.dao;

import com.example.demo.entity.UserBaseInfo;
import com.example.demo.manyDataSource.DataSource;
import org.springframework.stereotype.Repository;

/**
 * @author songlj
 * @date 2020/3/8 22:06
 */
@Repository
public interface UserBaseInfoDao {
    @DataSource("mysqlFirstReadDataSource")
    void insertOneDB(UserBaseInfo userBaseInfo);
    @DataSource("mysqlFirstWriteDataSource")
    void insertTwoDB(UserBaseInfo userBaseInfo);
}
