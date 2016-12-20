package com.kaishengit.web.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;


public class LoginFilter extends AbstractFilter {
    List<String> urlList=null;
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        String validateUrl=filterConfig.getInitParameter("validateUrl");
        urlList= Arrays.asList(validateUrl.split(","));
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request= (HttpServletRequest) servletRequest;
        HttpServletResponse response= (HttpServletResponse) servletResponse;

        //获取当前用户要访问的url；
        String requestUrl=request.getRequestURI();
        if(urlList!=null&&urlList.contains(requestUrl)){
            if(request.getSession().getAttribute("curr_user")==null){
                response.sendRedirect("/login?redirect="+requestUrl);
            }else {
                filterChain.doFilter(request,response);
            }
        }else {
            filterChain.doFilter(request,response);
        }

    }
}
