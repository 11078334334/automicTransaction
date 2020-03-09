package com.example.demo.config;

import com.alibaba.druid.pool.xa.DruidXADataSource;
import com.example.demo.manyDataSource.ManyDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jta.atomikos.AtomikosDataSourceBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * 数据源配置
 *
 * @author songlj
 * @date 2020/3/7 23:10
 */
@Configuration
public class DataSourceConfig{
    @Value("${jdbc.guide.parent.initialSize}")
    private Integer initialSize;
    @Value("${jdbc.guide.parent.maxActive}")
    private Integer maxActive;
    @Value("${jdbc.guide.parent.minIdle}")
    private Integer minIdle;
    @Value("${jdbc.guide.parent.logAbandoned}")
    private Boolean logAbandoned;
    @Value("${jdbc.guide.parent.removeAbandonedTimeout}")
    private Integer removeAbandonedTimeout;
    @Value("${jdbc.guide.parent.maxWait}")
    private Long maxWait;
    @Value("${jdbc.guide.parent.testOnBorrow}")
    private Boolean testOnBorrow;
    @Value("${jdbc.guide.parent.testWhileIdle}")
    private Boolean testWhileIdle;
    @Value("${jdbc.guide.parent.timeBetweenEvictionRunsMillis}")
    private Long timeBetweenEvictionRunsMillis;
    @Value("${jdbc.guide.parent.minEvictableIdleTimeMillis}")
    private Long minEvictableIdleTimeMillis;
    @Value("${jdbc.guide.parent.validationQuery}")
    private String validationQuery;


    @Value("${jdbc.mysql.driverClassName}")
    private String mysqlDriverClassName;


    @Value("${jdbc.atomikos.testQuery}")
    private String atoTestQuery;
    @Value("${jdbc.atomikos.maxLifetime}")
    private Integer atoMaxLifetime;
    @Value("${jdbc.atomikos.maxIdleTime}")
    private Integer atoMaxIdleTime;
    @Value("${jdbc.atomikos.maintenanceInterval}")
    private Integer atoMaintenanceInterval;
    @Value("${jdbc.atomikos.poolSize}")
    private Integer atoPoolSize;
    @Value("${jdbc.atomikos.maxPoolSize}")
    private Integer atoMaxPoolSize;
    @Value("${jdbc.atomikos.borrowConnectionTimeout}")
    private Integer atoBorrowConnectionTimeout;
    /**
     * to save all dataSourceName and DataSource
     */
    private Map<Object,Object> targetDatasource = new HashMap<>();
    @Value("${jdbc.defaultDBId}")
    private String defaultDBId;

    @Value("${jdbc.mysql.firstRead.url}")
    private String firstReadUrl;
    @Value("${jdbc.mysql.firstRead.username}")
    private String firstReadUsername;
    @Value("${jdbc.mysql.firstRead.password}")
    private String firstReadPassword;
    /**
     * mysqlFirstRead  DB
     */
    @Bean
    public DataSource mysqlFirstReadDataSource(){
        DataSource dataSource = getDataSource(
                mysqlDriverClassName,
                firstReadUrl,
                firstReadUsername,
                firstReadPassword);
        return dataSource;
    }
    /**
     * mysqlFirstWrite  DB
     */
    @Value("${jdbc.mysql.firstWrite.url}")
    private String firstWriteUrl;
    @Value("${jdbc.mysql.firstWrite.username}")
    private String firstWriteUsername;
    @Value("${jdbc.mysql.firstWrite.password}")
    private String firstWritePassword;
    @Bean
    public DataSource mysqlFirstWriteDataSource(){
        DataSource dataSource = getDataSource(
                mysqlDriverClassName,
                firstWriteUrl,
                firstWriteUsername,
                firstWritePassword);
        return dataSource;
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

    private DataSource getDataSource(String driverClassName, String url, String username, String password){
        AtomikosDataSourceBean ds = new AtomikosDataSourceBean();
        DruidXADataSource druidXADataSource = new DruidXADataSource();
        druidXADataSource.setInitialSize(initialSize);
        druidXADataSource.setMaxActive(maxActive);
        druidXADataSource.setMinIdle(minIdle);
        druidXADataSource.setLogAbandoned(logAbandoned);
        druidXADataSource.setRemoveAbandoned(true);
        druidXADataSource.setRemoveAbandonedTimeout(removeAbandonedTimeout);
        druidXADataSource.setMaxWait(maxWait);
        druidXADataSource.setValidationQuery(validationQuery);
        druidXADataSource.setTestOnBorrow(testOnBorrow);
        druidXADataSource.setTestWhileIdle(testWhileIdle);
        druidXADataSource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
        druidXADataSource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
        druidXADataSource.setDriverClassName(driverClassName);
        druidXADataSource.setUrl(url);
        druidXADataSource.setUsername(username);
        druidXADataSource.setPassword(password);

        //mysql支持表情
        if (driverClassName!= null && driverClassName.equals(mysqlDriverClassName)){
            String connectionInitSqls = "SET NAMES utf8mb4";
            StringTokenizer tokenizer = new StringTokenizer(connectionInitSqls, ";");
            druidXADataSource.setConnectionInitSqls(Collections.list(tokenizer));
        }


        ds.setXaDataSource(druidXADataSource);
        ds.setPoolSize(atoPoolSize);
        ds.setMaxPoolSize(atoMaxPoolSize);
        ds.setBorrowConnectionTimeout(atoBorrowConnectionTimeout);
        ds.setXaDataSourceClassName("com.alibaba.druid.pool.xa.DruidXADataSource");
        ds.setMaxLifetime(atoMaxLifetime);
        ds.setMaxIdleTime(atoMaxIdleTime);
        ds.setMaintenanceInterval(atoMaintenanceInterval);


        return ds;
    }
}
