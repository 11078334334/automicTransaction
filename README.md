# automicTransaction
一个单体服务的多数据源的切换，以及针对多数据源夸库的事务的保证
运行步骤：

clone源码之后，需要创建两个mysql的数据库，mytest以及mytest1,这两个库中都创建如下user_base_info这张表，如果采用的是远程的mysql数据库的话，对应修改application.yml的数据库链接

=======sql脚本：======

CREATE TABLE `user_base_info` (
	`id` INT(11) NOT NULL AUTO_INCREMENT,
	`age` INT(11) NOT NULL DEFAULT '0',
	`name` VARCHAR(50) NOT NULL DEFAULT '0',
	PRIMARY KEY (`id`)
)
COLLATE='utf8_general_ci'
ENGINE=InnoDB
AUTO_INCREMENT=54
;

接下来运行，postman或者浏览器运行：
http://127.0.0.1:8080/automaticSwitch/insertTwoDB?name=songlj20
通过控制AutomaticSwitchServiceImpl中int a=1/0;来观察数据是否入口，要么都入，要么都不入


=======实现大体思路=======

多数据源:
1，自定义多数据源主类ManyDataSource继承Spring的AbstractRoutingDataSource，并重新determineCurrentLookupKey(),这个方法动态的获取数据库的名称，这里简称为DataSouceKey
2,注解以及切面，自定义DataSource的注解，并对这个注解作为切面，在beforeExecute中将DataSource注解的value值放入到ThreadLocal中
3，配置自定义多数据源，将dataSource都设置到ManyDataSource中
4，mybatis配置，生成对应的sqlSessionFactoy以及自定义CustomSqlsessionTemplate
事务：
依赖JTA以及automatic进行控制，对应升级DataSource为XADataSource，以及TransactionConfig,TransactionManager是根据JTaTransactionManager来控制的
