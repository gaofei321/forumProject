package com.kaishengit.web.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;


public class ManageFilter extends AbstractFilter {

    List<String> urlList=null;
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        String adminUrl=filterConfig.getInitParameter("adminUrl");
        urlList= Arrays.asList(adminUrl.split(","));

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest req= (HttpServletRequest) servletRequest;
        HttpServletResponse resp= (HttpServletResponse) servletResponse;
        //获取登录管理员所要访问的URL
        String requestUrl=req.getRequestURI();

        if(urlList!=null&&urlList.contains(requestUrl)){

            if(req.getSession().getAttribute("curr_admin")==null){
                resp.sendRedirect("/admin/login?redirect="+requestUrl);
            }else {
                filterChain.doFilter(req,resp);
            }
        }else {
            filterChain.doFilter(req,resp);
        }



    }
}
