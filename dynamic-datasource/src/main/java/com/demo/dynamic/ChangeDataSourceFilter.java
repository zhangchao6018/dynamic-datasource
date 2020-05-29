package com.demo.dynamic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

/**
 * 数据源改变过滤器
 *      当数据源正在改变时，阻塞请求
 * @Description:
 * @Author: zhangchao22
 **/
@Component
@ServletComponentScan//开启filter，listner自动注册
@WebFilter(filterName = "changeDataSourceFilter", urlPatterns = "/*")
public class ChangeDataSourceFilter implements Filter{
    @Autowired
    private ApolloDataSourceListnerConfiguration ApolloDataSourceListner;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    /**
     *  当数据源配置正在改变时，阻塞请求
     * @param servletRequest
     * @param servletResponse
     * @param filterChain
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        while (ApolloDataSourceListner.isChangeDataSource()){
            try {
                Thread.sleep(1000l);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        filterChain.doFilter(servletRequest,servletResponse);
    }

    @Override
    public void destroy() {
    }
}
