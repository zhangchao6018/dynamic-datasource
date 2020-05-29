# dynamic-datasource
基于apollo实现不重启服务情况实时切换数据源


# dynamic-datasource
基于apollo实现不重启项目实时切换数据源



前言：
强依赖apollo组件
本处用的是druid数据源，如果使用其他数据源需要修改
1.引入  dynamic-datasource工程
2.配置文件配置：
###开启数据源动态切换功能
datasource.dynamic.enabled = true
3.启动工程 
4.apollo改变数据源并发布
5.验证：查看druid数据源 
http://localhost:11071/druid/datasource.html
http://localhost:11071/api/test


dynamic-test1
dynamic-test2
CREATE TABLE `tb_test` (
   `id` int(10) NOT NULL,
   `name` varchar(50) DEFAULT NULL,
   PRIMARY KEY (`id`)
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8;



dynamic-datasource工程实现简介：
核心配置类：
###ApolloDataSourceListnerConfiguration

###DataSourceConfiguration
    0.首先实例工程准备了名为dynamic-test1/dynamic-test2的两个数据库，均创建了tb_test表，用于测试多数据源切换

    1.项目启动会首先加载DataSourceConfiguration类，读取数据源配置，初始化ApolloDataSourceListnerConfiguration类 并调用初始化ApolloDataSourceListnerConfiguration的createDruidDataSource()方法初始化druid连接池
    
    2.启动完毕后利用携程apollo框架的@ApolloConfigChangeListener注解监听配置变化
        这里我用DatasourceEnum枚举类保存了这些数据源相关的配置key，只要枚举中的某个数据源key发生变化，便重启数据源
    3.重启数据源
        3.1 重启步骤
            根据apollo变化的数据源key值，重置druid数据源配置（这里我用配置类进行保存，方便存取：DruidDataSourceProperties）
            调用createDruidDataSource方法，重启的核心api：com.alibaba.druid.pool.DruidDataSource.restart
            重启失败/超时了怎么办？
                重试+报警策略通知，当然重启数据源这种事情一般是在开发人员主动操作下发生的，可根据实际情况出方案
            
        3.2 重启期间，新的请求和正在执行的请求怎么处理？
            新请求：
                这里我定义了一个全局标记 isChangeDataSource，表示数据源正在重启
                数据源重启过程不应该有数据源相关操作 ，这里用一个过滤器监听这个标记：ChangeDataSourceFilter
                只要正在重启，让请求休眠--这里我用的是全拦截，实际场景可以用拦截器替代，如：仅拦截数据库写操作
                    
            正在执行的请求：
                如果某个连接一直未释放，将无法重启 （由于上一步我们加了过滤器操作，新的请求会处于等待状态，在执行重启时，一般实不会出现这种情况的，实际生产中也未出现这该情况）
    
            验证：我这里是用ApacheJMeter工具模拟切换数据源时的并发情况，200请求/s的情况仍然，切换过程中会报错：java.sql.SQLNonTransientConnectionException: Could not create connection to database server. Attempted reconnect 3 times. Giving up.  切换成功后系统正常运行
                
        









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
\#management.port = 8080
\#management.context-path = /monitor
management.security.enabled = false
\#security.user.name = admin
\#security.user.password = admin
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

\#配置请求 GZIP 压缩
feign.compression.request.enabled = true
\#配置响应 GZIP 压缩
feign.compression.response.enabled = true
\#配置压缩支持的 MIME TYPE
feign.compression.request.mime-types = text/xml,application/xml,application/json
\#配置压缩数据大小的最小阀值，默认 2048
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
