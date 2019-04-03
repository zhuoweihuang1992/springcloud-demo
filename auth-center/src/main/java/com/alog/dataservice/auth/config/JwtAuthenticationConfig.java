package com.alog.dataservice.auth.config;

import lombok.Getter;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Value;

/**
 * token 配置类
 */
public class JwtAuthenticationConfig {
    @Value("${dataservice.security.jwt.url:/login}")
    private String url;

    @Value("${dataservice.security.jwt.header:Authorization}")
    private String header;

    @Value("${dataservice.security.jwt.prefix:Bearer}")
    private String prefix;

    @Value("${dataservice.security.jwt.expiration:#{24*60*60}}")
    private int expiration; // default 24 hours

    @Value("${dataservice.security.jwt.secret}")
    private String secret;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public int getExpiration() {
        return expiration;
    }

    public void setExpiration(int expiration) {
        this.expiration = expiration;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    @Override
    public String toString() {
        return "JwtAuthenticationConfig{" +
                "url='" + url + '\'' +
                ", header='" + header + '\'' +
                ", prefix='" + prefix + '\'' +
                ", expiration=" + expiration +
                ", secret='" + secret + '\'' +
                '}';
    }
}
