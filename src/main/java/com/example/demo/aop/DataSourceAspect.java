package com.example.demo.aop;

import com.example.demo.manyDataSource.DataSource;
import com.example.demo.manyDataSource.ManyDataSourceHolder;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;


/**
 * DataSource的切面
 *
 * @author songlj
 * @date 2020/3/8 20:33
 */
@Aspect
@Component
public class DataSourceAspect {

    //@Pointcut("@annotation(com.example.demo.manyDataSource.DataSource)")
    @Pointcut("execution ( * com.example.demo.dao..*(..))")
    public void pointcut(){}

    @Before("pointcut()")
    public void before(JoinPoint point){
        DataSource annotation =  AnnotationUtils.findAnnotation(((MethodSignature) point.getSignature()).getMethod(), DataSource.class);
        if (annotation!=null){
            ManyDataSourceHolder.setDataSourceKey(annotation.value());
            return;
        }
    }

    @After("pointcut()")
    public void after(JoinPoint point){
        ManyDataSourceHolder.clearDataSourceKey();
    }
}
