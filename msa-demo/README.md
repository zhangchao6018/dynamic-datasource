前言：
强依赖apollo组件
本处用的是druid数据源，如果使用其他数据源需要修改
1.引入  dynamic-datasource工程
2.配置文件配置：
#开启数据源动态切换
datasource.dynamic.enabled = true
3.启动工程 
4.apollo改变数据源并发布
5.验证：查看druid数据源 
http://localhost:11071/druid/datasource.html

apollo配置：

spring.application.name = msa-demo
spring.datasource.name = dbsource
spring.datasource.url = jdbc:mysql://your_ip:3306/dynamic-test2?&characterEncoding=utf8&useUnicode=true&useSSL=false&allowMultiQueries=true&zeroDateTimeBehavior=round&autoReconnect=true&serverTimezone=Asia/Shanghai
spring.datasource.username = root
spring.datasource.password = root
spring.datasource.type = com.alibaba.druid.pool.DruidDataSource
spring.datasource.driver-class-name = com.mysql.cj.jdbc.Driver
spring.datasource.filters = mergeStat
#开启数据源动态切换
datasource.dynamic.enabled = true
spring.datasource.druid.maxActive = 60
spring.datasource.druid.initialSize = 2
spring.datasource.druid.maxWait = 60000
spring.datasource.druid.minIdle = 1
spring.datasource.druid.timeBetweenEvictionRunsMillis = 60001
spring.datasource.druid.minEvictableIdleTimeMillis = 60000
spring.datasource.druid.validationQuery = select 'x'
spring.datasource.druid.testWhileIdle = true
spring.datasource.druid.testOnBorrow = false
spring.datasource.druid.testOnReturn = false
spring.datasource.druid.poolPreparedStatements = true
spring.datasource.druid.maxOpenPreparedStatements = 50
spring.datasource.druid.removeAbandoned = true
spring.datasource.druid.removeAbandonedTimeout = 60
spring.datasource.druid.logAbandoned = true
spring.datasource.druid.connectionInitSqls = set names utf8mb4;
spring.datasource.druid.filters = stat
spring.redis.database = 0
spring.redis.host = 192.168.2.102
spring.redis.port = 6379
spring.redis.password = 7ygvUJM
spring.redis.timeout = 0
spring.redis.pool.max-active = 1000
spring.redis.pool.max-wait = 60000
spring.redis.pool.max-idle = 20
spring.redis.pool.min-idle = 1
management.security.enabled = false
endpoints.shutdown.enabled = true
endpoints.shutdown.sensitive = false
pagehelper.helper-dialect = mysql
pagehelper.reasonable = true
pagehelper.support-methods-arguments = true
pagehelper.params = count=countSql
swagger.show = true
sqlfilter.schemaPrefix = nur_
sqlfilter.excludes = nur_test2


spring.http.encoding.charset = UTF-8
spring.http.encoding.enabled = true
spring.zipkin.base-url = http://192.168.2.65:40001
spring.zipkin.compression.enabled = true
spring.zipkin.locator.discovery.enabled = true
spring.sleuth.sampler.percentage = 0.05
spring.metrics.servo.enabled = false
#management.port = 8080
#management.context-path = /monitor
management.security.enabled = false
#security.user.name = admin
#security.user.password = admin
endpoints.shutdown.enabled = false
endpoints.shutdown.sensitive = false
hystrix.command.default.coreSize = 500
hystrix.command.default.execution.timeout.enabled = true
hystrix.command.default.execution.isolation.thread.timeoutInMilliseconds = 10000
hystrix.command.default.execution.isolation.thread.ribbon.ReadTimeout = 60000
hystrix.command.default.execution.isolation.thread.ribbon.ConnectTimeout = 60000
ribbon.ReadTimeout = 60000
ribbon.ConnectTimeout = 60000
ribbon.MaxAutoRetries = 0
ribbon.MaxAutoRetriesNextServer = 1
feign.hystrix.enabled = true
feign.httpclient.enabled = false
feign.okhttp.enabled = true

#配置请求 GZIP 压缩
feign.compression.request.enabled = true
#配置响应 GZIP 压缩
feign.compression.response.enabled = true
#配置压缩支持的 MIME TYPE
feign.compression.request.mime-types = text/xml,application/xml,application/json
#配置压缩数据大小的最小阀值，默认 2048
feign.compression.request.min-request-size = 512


eureka.instance.metadata-map.gated-launch = false
eureka.instance.lease-expiration-duration-in-seconds = 3
eureka.instance.lease-renewal-interval-in-seconds = 10
eureka.instance.instance-id = ${spring.cloud.client.ipAddress}:${server.port}
eureka.instance.prefer-ip-address = true
eureka.instance.hostname = ${spring.cloud.client.ipAddress}
eureka.client.instance-info-replication-interval-seconds = 6
eureka.client.registry-fetch-interval-seconds = 6
eureka.client.healthcheck.enabled = true
eureka.client.service-url.defaultZone = http://localhost:10001/eureka/
