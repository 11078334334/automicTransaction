package com.example.demo.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.example.demo.manyDataSource.ManyDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * 数据源配置
 *
 * @author songlj
 * @date 2020/3/7 23:10
 */
@Configuration
public class DataSourceConfig{

    /**
     * to save all dataSourceName and DataSource
     */
    private Map<Object,Object> targetDatasource = new HashMap<>();
    @Value("jdbc.defaultDBId")
    private String defaultDBId;
    /**
     * mysqlFirstRead  DB
     */
    @Bean
    @ConfigurationProperties(prefix = "jdbc.mysql.firstRead")
    public DataSource mysqlFirstReadDataSource(){
        DataSource mysqlFirstReadDataSource = DataSourceBuilder.create().type(DruidDataSource.class).build();
        return mysqlFirstReadDataSource;
    }
    /**
     * mysqlFirstWrite  DB
     */
    @Bean
    @ConfigurationProperties(prefix = "jdbc.mysql.firstWrite")
    public DataSource mysqlFirstWriteDataSource(){
        DataSource mysqlFirstWriteDataSource = DataSourceBuilder.create().type(DruidDataSource.class).build();
        return mysqlFirstWriteDataSource;
    }
    /**
     * 动态数据源定义
     */
    @Bean("manyDataSource")
    @Primary
    public DataSource manyDataSource(){
        ManyDataSource manyDataSource = new ManyDataSource();
        targetDatasource.put("mysqlFirstReadDataSource",mysqlFirstReadDataSource());
        targetDatasource.put("mysqlFirstWriteDataSource",mysqlFirstWriteDataSource());
        manyDataSource.setTargetDataSources(targetDatasource);
        manyDataSource.setDefaultTargetDataSource(targetDatasource.get(defaultDBId));
        return manyDataSource;
    }
}
