package com.demo.dynamic;



import com.alibaba.druid.pool.DruidDataSource;
import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfig;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfigChangeListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

/**
 * apollo数据源监听配置类
 *    使用说明：其他工程引用，需要在配置文件添加datasource.dynamic.enabled=true 才生效
 * @Author: zhangchao22
 */
public class ApolloDataSourceListnerConfiguration {

    private final Logger log = LoggerFactory.getLogger(ApolloDataSourceListnerConfiguration.class);
    //数据源驱动信息
    private DataSourceProperties dataSourceProperties;
    //druid 相关配置信息
    private DruidDataSourceProperties druidDataSourceProperties;
    //druid 连接池
    private DruidDataSource dataSource;

    //apollo数据源配置是否改变
    private  boolean isChangeDataSource =false;

    //动态数据源是否生效，默认不生效
    @Value("${datasource.dynamic.enabled:false}")
    private boolean dynamicDatasourceEnabled;

    @ApolloConfig
    private Config config;

    /**
     * apollo 注解监听配置信息更新
     * @param changeEvent
     */
    @ApolloConfigChangeListener
    private void onChange(ConfigChangeEvent changeEvent)  {
        //如果不是数据源配置的改变，直接返回
        boolean changeFlag = false;
        for (String changedKey : changeEvent.changedKeys()) {
            boolean hasKey = DatasourceEnum.hasKey(changedKey);
            if (hasKey){
                changeFlag=true;
                break;
            }
        }
        if (!changeFlag)
            return;

        try {
            Thread.sleep(1000l);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //datasource.dynamic.enabled配置为true时才执行
        if (!dynamicDatasourceEnabled)
            return;

        //数据源属性配置
        onchangeDruidProperties(changeEvent);
        //数据源正在改变，此时请求阻塞
        this.isChangeDataSource = true;
        // 对dataSource更新
        try {
            dataSource = createDruidDataSource();
            System.out.println("dataSource"+dataSource.getName());
        }catch (Exception e){
            log.error("create druidDatasource fail :" + dataSource.getID());
            e.printStackTrace();
        }finally {
            //数据源改变完毕，此时放行请求
            this.isChangeDataSource = false;
        }
    }

    /**
     * 当apollo上druid数据源配置改变时，更新这些改变
     * @param changeEvent
     */
    private void onchangeDruidProperties(ConfigChangeEvent changeEvent) {
        //数据源更新
        if (changeEvent.isChanged("spring.datasource.url"))
            dataSourceProperties.setUrl(config.getProperty("spring.datasource.url", dataSourceProperties.getUrl()));
        if (changeEvent.isChanged("spring.datasource.driver-class-name"))
            dataSourceProperties.setDriverClassName(config.getProperty("spring.datasource.driver-class-name", dataSourceProperties.getDriverClassName()));
        if (changeEvent.isChanged("spring.datasource.username"))
            dataSourceProperties.setUsername(config.getProperty("spring.datasource.username", dataSourceProperties.getUsername()));
        if (changeEvent.isChanged("spring.datasource.password"))
            dataSourceProperties.setPassword(config.getProperty("spring.datasource.password", dataSourceProperties.getPassword()));

        //druid 各项配置更新
        if (changeEvent.isChanged("spring.datasource.druid.maxActive"))
            druidDataSourceProperties.setMaxActive(config.getProperty("spring.datasource.druid.maxActive", druidDataSourceProperties.getMaxActive()));
        if (changeEvent.isChanged("spring.datasource.druid.initialSize"))
            druidDataSourceProperties.setInitialSize(config.getProperty("spring.datasource.druid.initialSize", druidDataSourceProperties.getInitialSize()));
        if (changeEvent.isChanged("spring.datasource.druid.maxWait"))
            druidDataSourceProperties.setMaxWait(config.getProperty("spring.datasource.druid.maxWait", druidDataSourceProperties.getMaxWait()));
        if (changeEvent.isChanged("spring.datasource.druid.minIdle"))
            druidDataSourceProperties.setMinIdle(config.getProperty("spring.datasource.druid.minIdle", druidDataSourceProperties.getMinIdle()));
        if (changeEvent.isChanged("spring.datasource.druid.timeBetweenEvictionRunsMillis"))
            druidDataSourceProperties.setTimeBetweenEvictionRunsMillis(config.getProperty("spring.datasource.druid.timeBetweenEvictionRunsMillis", druidDataSourceProperties.getTimeBetweenEvictionRunsMillis()));
        if (changeEvent.isChanged("spring.datasource.druid.minEvictableIdleTimeMillis"))
            druidDataSourceProperties.setMinEvictableIdleTimeMillis(config.getProperty("spring.datasource.druid.minEvictableIdleTimeMillis", druidDataSourceProperties.getMinEvictableIdleTimeMillis()));
        if (changeEvent.isChanged("spring.datasource.druid.validationQuery"))
            druidDataSourceProperties.setValidationQuery(config.getProperty("spring.datasource.druid.validationQuery", druidDataSourceProperties.getValidationQuery()));
        if (changeEvent.isChanged("spring.datasource.druid.testWhileIdle"))
            druidDataSourceProperties.setTestWhileIdle(config.getProperty("spring.datasource.druid.testWhileIdle", druidDataSourceProperties.getTestWhileIdle()));
        if (changeEvent.isChanged("spring.datasource.druid.testOnBorrow"))
            druidDataSourceProperties.setTestOnBorrow(config.getProperty("spring.datasource.druid.testOnBorrow", druidDataSourceProperties.getTestOnBorrow()));
        if (changeEvent.isChanged("spring.datasource.druid.testOnReturn"))
            druidDataSourceProperties.setTestOnReturn(config.getProperty("spring.datasource.druid.testOnReturn", druidDataSourceProperties.getTestOnReturn()));
        if (changeEvent.isChanged("spring.datasource.druid.poolPreparedStatements"))
            druidDataSourceProperties.setPoolPreparedStatements(config.getProperty("spring.datasource.druid.poolPreparedStatements", druidDataSourceProperties.getPoolPreparedStatements()));
        if (changeEvent.isChanged("spring.datasource.druid.maxOpenPreparedStatements"))
            druidDataSourceProperties.setMaxOpenPreparedStatements(config.getProperty("spring.datasource.druid.maxOpenPreparedStatements", druidDataSourceProperties.getMaxOpenPreparedStatements()));
        if (changeEvent.isChanged("spring.datasource.druid.removeAbandoned"))
            druidDataSourceProperties.setRemoveAbandoned(config.getProperty("spring.datasource.druid.removeAbandoned", druidDataSourceProperties.getRemoveAbandoned()));
        if (changeEvent.isChanged("spring.datasource.druid.removeAbandonedTimeout"))
            druidDataSourceProperties.setRemoveAbandonedTimeout(config.getProperty("spring.datasource.druid.removeAbandonedTimeout", druidDataSourceProperties.getRemoveAbandonedTimeout()));
        if (changeEvent.isChanged("spring.datasource.druid.logAbandoned"))
            druidDataSourceProperties.setLogAbandoned(config.getProperty("spring.datasource.druid.logAbandoned", druidDataSourceProperties.getLogAbandoned()));
        if (changeEvent.isChanged("spring.datasource.druid.connectionInitSqls"))
            druidDataSourceProperties.setConnectionInitSqls(config.getProperty("spring.datasource.druid.connectionInitSqls", druidDataSourceProperties.getConnectionInitSqls()));
        if (changeEvent.isChanged("spring.datasource.druid.filters"))
            druidDataSourceProperties.setFilters(config.getProperty("spring.datasource.druid.filters", druidDataSourceProperties.getFilters()));
    }


    /**
     * 创建DruidDataSource
     * @return
     */
    public DruidDataSource createDruidDataSource() {
        log.warn(String.format("apollo configutation = datasource connection factory driver-class-name = %s, " +
                        "url = %s ,username = %s ,password = %s",
                dataSourceProperties.getDriverClassName(), dataSourceProperties.getUrl(),
                dataSourceProperties.getUsername(), dataSourceProperties.getPassword()));

        // 判断是否存在dataSource
        if(dataSource == null)
            dataSource = new DruidDataSource();

        boolean changeFlag = true;
        //尝试重启
        //todo 报警机制 考虑死循环情况
        long beginTime = System.currentTimeMillis();
        while (changeFlag){
            boolean isChange = true;
            try {
                dataSource.restart();
            } catch (SQLException e) {
                isChange = false;
                long tryTime = System.currentTimeMillis();
                long time = (tryTime-beginTime)/1000;
                //重启超时，抛出异常
                if (time>120){
                    log.error("the datasource restart fail for "+time+" seconds " );
                    throw new RuntimeException("restart fail for"+time+"seconds，please check out your datasource config");
                }
            }
            if(isChange){
                changeFlag = false;
            }
        }
        // 设置数据源配置信息，重置dataSource
        setDatasourceInfo();
        log.warn("the datasource id is " + dataSource.getID());
        return dataSource;
    }

    /**
     * 设置druid配置信息
     * @throws SQLException
     */
    private void setDatasourceInfo()  {
        // 设置配置信息
        dataSource.setDriverClassName(dataSourceProperties.getDriverClassName());
        dataSource.setUrl(dataSourceProperties.getUrl());
        dataSource.setUsername(dataSourceProperties.getUsername());
        dataSource.setPassword(dataSourceProperties.getPassword());

        dataSource.setMaxActive(Integer.valueOf(druidDataSourceProperties.getMaxActive()));
        dataSource.setInitialSize(Integer.valueOf(druidDataSourceProperties.getInitialSize()));
        dataSource.setMaxWait(Long.valueOf(druidDataSourceProperties.getMaxWait()));
        dataSource.setMinIdle(Integer.valueOf(druidDataSourceProperties.getMinIdle()));
        dataSource.setTimeBetweenEvictionRunsMillis(Long.valueOf(druidDataSourceProperties.getTimeBetweenEvictionRunsMillis()));
        dataSource.setMinEvictableIdleTimeMillis(Long.valueOf(druidDataSourceProperties.getMinEvictableIdleTimeMillis()));
        dataSource.setValidationQuery(druidDataSourceProperties.getValidationQuery());
        dataSource.setTestWhileIdle(Boolean.valueOf(druidDataSourceProperties.getTestWhileIdle()));
        dataSource.setTestOnBorrow(Boolean.valueOf(druidDataSourceProperties.getTestOnBorrow()));
        dataSource.setTestOnReturn(Boolean.valueOf(druidDataSourceProperties.getTestOnReturn()));
        dataSource.setPoolPreparedStatements(Boolean.valueOf(druidDataSourceProperties.getPoolPreparedStatements()));
        dataSource.setMaxOpenPreparedStatements(Integer.valueOf(druidDataSourceProperties.getMaxOpenPreparedStatements()));
        dataSource.setRemoveAbandoned(Boolean.valueOf(druidDataSourceProperties.getRemoveAbandoned()));
        dataSource.setRemoveAbandonedTimeout(Integer.valueOf(druidDataSourceProperties.getRemoveAbandonedTimeout()));
        dataSource.setLogAbandoned(Boolean.valueOf(druidDataSourceProperties.getLogAbandoned()));
        List<String> sqls = Arrays.asList(druidDataSourceProperties.getConnectionInitSqls().split(";"));
        dataSource.setConnectionInitSqls(sqls);
        try {
            dataSource.setFilters(druidDataSourceProperties.getFilters());
        } catch (SQLException e) {
            log.error("set druidDatasource filters fail :" + dataSource.getID());
            e.printStackTrace();
        }
    }

    public boolean isChangeDataSource() {
        return isChangeDataSource;
    }
    public void setDruidDataSourceProperties(DruidDataSourceProperties druidDataSourceProperties) {
        this.druidDataSourceProperties = druidDataSourceProperties;
    }

    public void setDataSourceProperties(DataSourceProperties dataSourceProperties){
        this.dataSourceProperties = dataSourceProperties;
    }

}