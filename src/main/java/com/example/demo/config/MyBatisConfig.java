package com.example.demo.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * mybatis的配置
 *
 * @author songlj
 * @date 2020/3/7 23:10
 */
@Configuration
public class MyBatisConfig {

    @Bean
    @Primary
    public SqlSessionFactory mysqlFirstReadDataSourceSessionFactory(@Qualifier("mysqlFirstReadDataSource") DataSource mysqlFirstReadDataSource) throws Exception{
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(mysqlFirstReadDataSource);
        sqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mapper/**/*.xml"));
        return sqlSessionFactoryBean.getObject();
    }

    @Bean
    public SqlSessionFactory mysqlFirstWriteDataSourceSessionFactory(@Qualifier("mysqlFirstWriteDataSource")DataSource mysqlFirstWriteDataSource) throws Exception{
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(mysqlFirstWriteDataSource);
        sqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mapper/**/*.xml"));
        return sqlSessionFactoryBean.getObject();
    }

    @Bean
    public CustomSqlSessionTemplate sqlSessionTemplate(@Qualifier("mysqlFirstReadDataSourceSessionFactory") SqlSessionFactory mysqlFirstReadDataSourceSessionFactory,
                                                       @Qualifier("mysqlFirstWriteDataSourceSessionFactory") SqlSessionFactory mysqlFirstWriteDataSourceSessionFactory

    ){
        Map<Object, SqlSessionFactory> sf = new HashMap<>();
        sf.put("mysqlFirstReadDataSource",mysqlFirstReadDataSourceSessionFactory);
        sf.put("mysqlFirstWriteDataSource",mysqlFirstWriteDataSourceSessionFactory);

        CustomSqlSessionTemplate customSqlSessionTemplate = new CustomSqlSessionTemplate(mysqlFirstReadDataSourceSessionFactory);
        customSqlSessionTemplate.setDefaultTargetSqlSessionFactory(mysqlFirstReadDataSourceSessionFactory);
        customSqlSessionTemplate.setTargetSqlSessionFactorys(sf);
        return customSqlSessionTemplate;
    }


}
