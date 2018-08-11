package com.suda.MyVideoApi.conf;

import lombok.AllArgsConstructor;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @author guhaibo
 * @date 2018/8/10
 */
@AllArgsConstructor
@SpringBootConfiguration
public class WebAppConfig extends WebMvcConfigurerAdapter {


    private InterceptorConfig interceptorConfig;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //注册自定义拦截器，添加拦截路径和排除拦截路径
        registry.addInterceptor(interceptorConfig).addPathPatterns("/video/**");
    }
}