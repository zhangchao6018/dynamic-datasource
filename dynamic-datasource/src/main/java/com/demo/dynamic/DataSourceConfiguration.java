package com.demo.dynamic;

import com.alibaba.druid.pool.DruidDataSource;
import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.sql.SQLException;

/**
 * 动态数据源配置类
 *      初始化ApolloDataSourceListnerConfiguration对象，初始化druid连接池
 * @Author: zhangchao22
 */
@Order(1)
@Configuration
@EnableApolloConfig
@EnableConfigurationProperties({DataSourceProperties.class,DruidDataSourceProperties.class})
public class DataSourceConfiguration /*implements Filter*/ {

    private final Logger log = LoggerFactory.getLogger(DataSourceConfiguration.class);

    @Autowired
    private DataSourceProperties dataSourceProperties;

    @Autowired
    private DruidDataSourceProperties druidDataSourceProperties;


    /**
     * 初始化配置对象
     * @return
     */
    @Bean
    public ApolloDataSourceListnerConfiguration apolloDataSourceConfiguration(){
        log.info("--------------------------------init ApolloDataSourceListnerConfiguration...-------------------------------------");
        ApolloDataSourceListnerConfiguration apolloDataSourceConfiguration = new ApolloDataSourceListnerConfiguration();
        apolloDataSourceConfiguration.setDataSourceProperties(dataSourceProperties);
        apolloDataSourceConfiguration.setDruidDataSourceProperties(druidDataSourceProperties);
        return apolloDataSourceConfiguration;
    }

    /**
     * 注册数据源
     * @return
     * @throws SQLException
     */
    @Bean
    public DruidDataSource druidDataSource() throws SQLException {
        log.info("-------------------------------- init druidDataSource-------------------------------------");
        ApolloDataSourceListnerConfiguration apolloDataSourceConfiguration = apolloDataSourceConfiguration();
        return apolloDataSourceConfiguration.createDruidDataSource();
    }

}
