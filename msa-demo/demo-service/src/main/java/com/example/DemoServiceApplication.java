package com.example;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;
import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;

@EnableApolloConfig
@EnableDiscoveryClient
@EnableTransactionManagement
@SpringBootApplication
@EnableFeignClients
@EnableAsync
@EnableHystrix
@EnableHystrixDashboard
@MapperScan({"com.example.demo.dao.mapper"})
@ComponentScan(basePackages = {"com.example"})
public class DemoServiceApplication {
	
    public static void main(String[] args) {

        SpringApplication.run(DemoServiceApplication.class, args);

    }
    
    @Bean
	RestTemplate restTemplate() {
		SimpleClientHttpRequestFactory simpleClientHttpRequestFactory = new SimpleClientHttpRequestFactory();
		simpleClientHttpRequestFactory.setConnectTimeout(10000);
		simpleClientHttpRequestFactory.setReadTimeout(10000);
		return new RestTemplate(simpleClientHttpRequestFactory);
	}
}
