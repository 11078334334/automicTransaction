package com.example.demo.manyDataSource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * 多数据源主类
 *
 * @author songlj
 * @date 2020/3/7 23:47
 */
public class ManyDataSource extends AbstractRoutingDataSource{
    @Override
    protected Object determineCurrentLookupKey() {
        return ManyDataSourceHolder.getDataSourceKey();
    }
}
