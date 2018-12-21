package com.example.demo.filer;

import com.example.demo.util.JWTUtil;
import com.example.demo.util.StaticUtils;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Map;

@WebFilter(filterName = "logCostFilter" , urlPatterns = "/*")
public class LogCostFilter implements Filter {

    @Value("${jwt.base64Encryption}")
    private String base64Encryption;

    //不需要登录就可以访问的路径(比如:注册登录等)
    String[] includeUrls = new String[]{"/login","/register"};


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String uri = request.getRequestURI();
        String authorization = request.getHeader("Authorization");
        long count = Arrays.stream(includeUrls).filter(url -> url.equals(uri)).count();

        AMHttpServletRequestWrapper amHttpServletRequestWrapper = new AMHttpServletRequestWrapper(request, request.getParameterMap());

        if (count > 0){
            filterChain.doFilter(amHttpServletRequestWrapper, servletResponse);
            return;
        }
        if (StringUtils.isEmpty(authorization)){
            restlue(response);
        }else {
            Claims claims = JWTUtil.validateJwt(authorization, base64Encryption);
            if (null == claims){
                restlue(response);
            }else {
                Map<String, Object> stringObjectMap = JWTUtil.extractInfo(authorization, base64Encryption);
                amHttpServletRequestWrapper.setParameter(StaticUtils.USER_ID,stringObjectMap.get(StaticUtils.U_ID).toString());
                filterChain.doFilter(amHttpServletRequestWrapper, servletResponse);
                return;
            }
        }
    }


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void destroy() {

    }

    private void restlue(HttpServletResponse response){
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("{\"code\":5003;\"msg\":\"未登录\";\"odj\":null}");
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
        } catch (IOException e) {
            e.printStackTrace();
        }
        writer.write(stringBuffer.toString());
    }
}