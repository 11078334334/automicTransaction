package com.example.demo.config;

import com.atomikos.icatch.config.UserTransactionServiceImp;
import com.atomikos.icatch.jta.UserTransactionImp;
import com.atomikos.icatch.jta.UserTransactionManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.jta.JtaTransactionManager;

import javax.transaction.TransactionManager;
import javax.transaction.UserTransaction;
import java.util.Properties;

@EnableTransactionManagement
@Configuration
public class TransactionConfig {

    @Value("${jdbc.atomikos.transaction.timeout}")
    private Integer transactionTimeout;
    @Value("${jdbc.atomikos.transaction.maxActives}")
    private Integer maxActives;



    @Bean(name="userTransactionServiceImp", initMethod = "init", destroyMethod = "shutdownForce")
    public UserTransactionServiceImp userTransactionServiceImp()
    {
        Properties properties = new Properties();
        //指定所有的atomikos配置
        //事务超时时间
        properties.setProperty("com.atomikos.icatch.max_timeout", transactionTimeout*1000+"");
        //注意,优先用default_jta_timeout,如果max_timeout时间短则用max_timeout
        properties.setProperty("com.atomikos.icatch.default_jta_timeout", transactionTimeout*1000+"");
        //最大的活跃事务数量(达到数量后会禁止创建事务,以项目为单位,整个项目最多活跃事务)
        properties.setProperty("com.atomikos.icatch.max_actives", maxActives+"");
        //项目关闭时是否强行终止项目
        properties.setProperty("com.atomikos.icatch.force_shutdown_on_vm_exit", "true");
        //指定事务日志文件基名。默认为tmlog。事务日志使用此名称附加数字和扩展名.log存储在文件中。在检查点，将创建一个新的事务日志文件，并且该数字会递增。
        properties.setProperty("com.atomikos.icatch.log_base_name", System.getProperty("projectName")+"tmlog");
        //指定应存储日志文件的目录。默认为当前工作目录。此目录应该是稳定的存储，如SAN，RAID或至少备份的位置。事务日志文件与数据本身一样重要，以确保在出现故障时保持一致性。
        properties.setProperty("com.atomikos.icatch.log_base_dir", "/data1/logs/app/jar/"+System.getProperty("projectName")+"/"+System.getProperty("server.port"));
        //指定存储调试日志文件的目录。默认为当前工作目录。
        properties.setProperty("com.atomikos.icatch.output_dir", "/data1/logs/app/jar/"+System.getProperty("projectName")+"/"+System.getProperty("server.port"));
        //指定调试日志文件名。默认为tm.out
        properties.setProperty("com.atomikos.icatch.console_file_name", "atomikos_console_log");
        //一些用户报告了MySQL XA的问题（与此MySQL错误相关： http://bugs.mysql.com/bug.php?id=27832）。如果您在同一事务中多次访问同一MySQL数据库，则只会出现此问题。解决方法是在jta.properties中设置以下属性： com.atomikos.icatch.serial_jta_transactions=false
        //另外请在mysql数据源上设置pinGlobalTxToPhysicalConnection = true
        properties.setProperty("com.atomikos.icatch.serial_jta_transactions", "false");
        UserTransactionServiceImp userTransactionServiceImp = new UserTransactionServiceImp(properties);
        return userTransactionServiceImp;
    }

    @Bean
    @DependsOn("userTransactionServiceImp")
    public UserTransaction userTransaction()
    {
        UserTransactionImp userTransactionImp = new UserTransactionImp();
        return userTransactionImp;
    }

    @Bean(initMethod = "init", destroyMethod = "close")
    @DependsOn("userTransactionServiceImp")
    public TransactionManager userTransactionManager()
    {
        UserTransactionManager userTransactionManager = new UserTransactionManager();
        //上面配置的userTransactionServiceImp已经执行这个了,所以这里必须关闭
        userTransactionManager.setStartupTransactionService(false);
        //项目close时是否强行终止事务
        userTransactionManager.setForceShutdown(true);
        return userTransactionManager;
    }

    @Bean(name = "transactionManager")
    @DependsOn("userTransactionServiceImp")
    public PlatformTransactionManager transactionManager()
    {
        return new JtaTransactionManager(userTransaction(), userTransactionManager());
    }




}