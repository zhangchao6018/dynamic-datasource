package com.demo.dynamic;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Description: 数据源驱动配置信息
 * @Author: zhangchao22
 **/
@ConfigurationProperties(prefix = "spring.datasource")
public class DataSourceProperties {

    private String url;

    private String username;

    private String password;

    private String driverClassName;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDriverClassName() {
        return driverClassName;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }
}
