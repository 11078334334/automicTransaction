package com.example.demo.manyDataSource;

import java.lang.annotation.*;

/**
 * 用来声明修饰的方法所要连接的哪个数据库
 * Created by songlj on 2020/3/8.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DataSource {
    String value();
}
