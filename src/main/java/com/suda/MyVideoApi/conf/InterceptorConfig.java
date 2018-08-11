package com.suda.MyVideoApi.conf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * @author guhaibo
 * @date 2018/8/10
 */
@Component
public class InterceptorConfig implements HandlerInterceptor {

    @Value("${spring.allowOrigin}")
    private String allowOrigin;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        String origin = httpServletRequest.getHeader("Origin");

        if (!StringUtils.isEmpty(allowOrigin)) {
            if ("*".equals(allowOrigin)) {
                httpServletResponse.setHeader("Access-Control-Allow-Origin", "*");
            } else {
                for (String allow : allowOrigin.split(",")) {
                    if (allow.equals(origin)) {
                        httpServletResponse.setHeader("Access-Control-Allow-Origin", "*");
                        break;
                    }
                }
            }
        }

        httpServletResponse.setHeader("Access-Control-Allow-Methods", "*");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", "*");

        logger.info("---------------------开始进入请求地址拦截----------------------------");
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        logger.info("---------------------处理请求完成后视图渲染之前的处理操作---------------");
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        logger.info("---------------------视图渲染之后的操作-------------------------------");
    }
}
