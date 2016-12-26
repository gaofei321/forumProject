package com.kaishengit.web.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;


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
                //  以键值对的方式获取URL里面的所有的值，并存放在map集合中
                Map map=request.getParameterMap();
                //把获取到的值存放到set中，因为获取到的值不能进行修改，只能读取
                //需要放到一个新的集合中，才能进行修改
                Set paramSet=map.entrySet();
                //设置一个迭代器
                Iterator iterator=paramSet.iterator();
                //则是判断当前元素是否存在，并指向下一个元素（即所谓的索引）
                if(iterator.hasNext()){
                    requestUrl+="?";

                    while (iterator.hasNext()){
                        //迭代出来当前元素的键值对
                        Map.Entry me= (Map.Entry) iterator.next();
                        //分别获取迭代出来的键和值
                        Object key=me.getKey();
                        Object value=me.getValue();
                        String valString[]= (String[]) value;
                        String param="";
                        for (int i=0;i<valString.length;i++){
                            param=key+"="+valString[i]+"&";
                            requestUrl+=param;
                        }
                    }
                requestUrl=requestUrl.substring(0,requestUrl.length()-1);
                System.out.println("requestUrl"+requestUrl);
                }

                // 去登录页面
                response.sendRedirect("/login?redirect="+requestUrl);
            }else {
                filterChain.doFilter(request,response);
            }
        }else {
            filterChain.doFilter(request,response);
        }

    }
}
