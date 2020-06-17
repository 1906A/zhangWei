package com.leyou.filter;

import com.leyou.auth.common.JwtUtils;
import com.leyou.auth.common.UserInfo;
import com.leyou.config.FilterProperties;
import com.leyou.config.JwtProperties;
import com.leyou.utils.CookieUtils;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
@EnableConfigurationProperties(JwtProperties.class)
public class LoginFilter extends ZuulFilter {

    @Autowired
    JwtProperties jwtProperties;
    @Autowired
    FilterProperties filterProperties;

    @Override
    public String filterType() {
        return "pre";   //请求前过滤
    }

    @Override
    public int filterOrder() {
        return 5; //级别
    }

    //返回是否拦截状态
    @Override
    public boolean shouldFilter() {
        //获取上下文
        RequestContext context = RequestContext.getCurrentContext();
//获取request
        HttpServletRequest request = context.getRequest();
//获取请求的url
        String requestURL = request.getRequestURI();

        //遍历白名单
        for (String allowPath : filterProperties.getAllowPaths()) {

            //判断请求是否符合白名单
             if (requestURL.startsWith(allowPath)){
                 System.out.println(allowPath+"=================白名单==========");
                 //符合就放行
                 return false;
             }
        }
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        System.out.println("========zuul===== filter ====");
        //获取上下文
        RequestContext context = RequestContext.getCurrentContext();

        HttpServletRequest request = context.getRequest();
//获取token
        String token = CookieUtils.getCookieValue(request, jwtProperties.getCookieName());

        try {
            //解析
            UserInfo userInfo = JwtUtils.getInfoFromToken(token, jwtProperties.getPublicKey());
        } catch (Exception e) {
            e.printStackTrace();
            //解析出现异常
            context.setSendZuulResponse(false);//关闭zuul响应
            context.setResponseStatusCode(HttpStatus.FORBIDDEN.value());//返回403
        }
        return null;
    }
}
