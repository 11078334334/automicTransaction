package com.example.demo.manyDataSource;

/**
 * 多数据源的持有者
 *
 * @author songlj
 * @date 2020/3/7 23:31
 */
public class ManyDataSourceHolder {
    private static ThreadLocal<String> dataSourceKey = new ThreadLocal();

    public static String getDataSourceKey() {
        return dataSourceKey.get();
    }

    public static void setDataSourceKey(String dataSourceKey1) {
        dataSourceKey.set(dataSourceKey1);
    }

    public static void clearDataSourceKey(){
        dataSourceKey.remove();
    }
}
