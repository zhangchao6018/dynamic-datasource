package com.demo.dynamic;

public enum DatasourceEnum {
    url("spring.datasource.url"),
    username("spring.datasource.username"),
    password("spring.datasource.password"),
    driverClassName("spring.datasource.driver-class-name"),

    maxActive("spring.datasource.druid.maxActive"),
    initialSize("spring.datasource.druid.initialSize"),
    maxWait("spring.datasource.druid.maxWait"),
    minIdle("spring.datasource.druid.minIdle"),
    timeBetweenEvictionRunsMillis("spring.datasource.druid.timeBetweenEvictionRunsMillis"),
    minEvictableIdleTimeMillis("spring.datasource.druid.minEvictableIdleTimeMillis"),
    validationQuery("spring.datasource.druid.validationQuery"),
    testWhileIdle("spring.datasource.druid.testWhileIdle"),
    testOnBorrow("spring.datasource.druid.testOnBorrow"),
    testOnReturn("spring.datasource.druid.testOnReturn"),
    poolPreparedStatements("spring.datasource.druid.poolPreparedStatements"),
    maxOpenPreparedStatements("spring.datasource.druid.maxOpenPreparedStatements"),
    removeAbandoned("spring.datasource.druid.removeAbandoned"),
    removeAbandonedTimeout("spring.datasource.druid.removeAbandonedTimeout"),
    logAbandoned("spring.datasource.druid.logAbandoned"),
    connectionInitSqls("spring.datasource.druid.connectionInitSqls"),
    filters("spring.datasource.druid.filters")
    ;
    private String key;

    DatasourceEnum(String key) {
        this.key = key;
    }


    public static DatasourceEnum getByKey(String key) {
        for (DatasourceEnum element : DatasourceEnum.values()) {
            if (key.equals(element.key)) {
                return element;
            }
        }
        return null;
    }


    public static boolean hasKey(String key) {
        for (DatasourceEnum element : DatasourceEnum.values()) {
            if (key.equals(element.key)) {
                return true;
            }
        }
        return false;
    }

    public String getKey() {
        return key;
    }
}
