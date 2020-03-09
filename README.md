# automicTransaction
一个单体服务的多数据源的切换，以及针对多数据源夸库的事务的保证

sql脚本：
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
