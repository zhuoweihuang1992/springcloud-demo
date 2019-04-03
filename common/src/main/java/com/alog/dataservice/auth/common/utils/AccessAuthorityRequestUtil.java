package com.alog.dataservice.auth.common.utils;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Properties;

/**
 * 请求认证中心的方法
 */
public class AccessAuthorityRequestUtil {

    private static Logger logger = org.slf4j.LoggerFactory.getLogger(AccessAuthorityRequestUtil.class);

    public static String verifyToken(HttpServletRequest request,String token){
        RestTemplate restTemplate = new RestTemplate();
        List<HttpMessageConverter<?>> converterList = restTemplate.getMessageConverters();
        converterList.remove(1);
        HttpMessageConverter<?> converter = new StringHttpMessageConverter(StandardCharsets.UTF_8);
        converterList.add(1, converter);
        restTemplate.setMessageConverters(converterList);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
        httpHeaders.add("Authorization",token);
        httpHeaders.add("url",request.getServletPath());
        HttpEntity<String> httpEntity = new HttpEntity<String>(httpHeaders);
        String url = "http://localhost:8088/verifyToken";
        URI uri = URI.create(url);
        ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.POST, httpEntity, String.class);
        System.err.println(response.getStatusCodeValue());
        if(response.hasBody()) {
            System.err.println(response.getBody());
        }
        if(!response.getBody().equals("1")){
            logger.info("验证token成功！");
            return response.getBody();
        }else{
            logger.info("验证token失败!");
            return "1";
        }
    }

}
