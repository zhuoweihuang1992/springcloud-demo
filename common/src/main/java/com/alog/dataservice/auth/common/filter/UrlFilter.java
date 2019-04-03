package com.alog.dataservice.auth.common.filter;

import com.alog.dataservice.auth.common.utils.AccessAuthorityRequestUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;


/**
 * 公共过滤器：（服务必须引入common模块才具备权限认证的功能，否则相当于无权限认证服务）
 * 在需要权限认证的服务引入该common依赖，然后创建过滤器并继承UrlFilter即可
 * （还需要加上与UrlFilter上面相同的注解）
 */
@Configuration
@Order(1)
@WebFilter(filterName = "urlfilter", urlPatterns = "/*")
public class UrlFilter implements Filter {

    private Logger logger = org.slf4j.LoggerFactory.getLogger(UrlFilter.class);
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info("初始化地址过滤器.....");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        HttpServletRequest request_ = (HttpServletRequest) request;
        HttpServletResponse response_ = (HttpServletResponse) response;
        try {
            String method = StringUtils.trimToEmpty(request_.getMethod()).toUpperCase();
            String strBackUrl = "";
            String entryId = "";
            String common_url = "http://" + request_.getServerName() //服务器地址
                    + ":" + request_.getServerPort() //端口号
                    + request_.getContextPath() //项目名称
                    + request_.getServletPath() //请求页面或其他地址
                    + "?";
            if (method.equals("POST")) {
                Map<String, String[]> params = request.getParameterMap();
                String queryString = "";
                for (String key : params.keySet()) {
                    String[] values = params.get(key);
                    for (int i = 0; i < values.length; i++) {
                        String value = values[i];
                        queryString += key + "=" + value + "&";
                    }
                }
                // 去掉最后一个空格
                try {
                    if (!"".equals(queryString)) {
                        strBackUrl = common_url + queryString.substring(0, queryString.length() - 1);
                    } else {
                        strBackUrl = common_url.substring(0, common_url.length() - 1);
                    }
                } catch (Exception e) {
                    logger.debug("", e);
                }
            } else if (method.equals("GET")) {
                strBackUrl = common_url + (request_.getQueryString()); //参数
            } else {
                logger.debug("此连接不打印");
            }
            logger.info(">>>>>>>>>>>>>>>>>请求地址：" + method + "---->" + strBackUrl);
            logger.info(">>>>>>>>>>>>>>>>>请求时间：" + df.format(new Date()));

            String token = request_.getHeader("Authorization");


            if (token != null && token.startsWith("Bearer" + " ")) {
                //请求认证授权中心（auth-center）进行鉴权,然后返回数据范围资源码
                String codes = AccessAuthorityRequestUtil.verifyToken(request_,token);
                if(codes.equals("1")){
                    logger.info("没有该接口访问权限！");
                    return;
                }else{
                    response_.addHeader("data_area_code",codes);
                }
            }else{
                logger.info("没有携带token访问");
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        chain.doFilter(request, response_);
    }

    @Override
    public void destroy() {
        logger.info("销毁地址过滤器......");
    }
}
