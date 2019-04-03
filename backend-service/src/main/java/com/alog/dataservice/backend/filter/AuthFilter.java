package com.alog.dataservice.backend.filter;

import com.alog.dataservice.auth.common.filter.UrlFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import javax.servlet.annotation.WebFilter;

/**
 * 权限过滤器
 */
@Configuration
@Order(1)
@WebFilter(filterName = "urlfilter", urlPatterns = "/*")
public class AuthFilter extends UrlFilter {

}
