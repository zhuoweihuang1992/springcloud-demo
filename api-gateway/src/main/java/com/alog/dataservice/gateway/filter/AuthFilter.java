package com.alog.dataservice.gateway.filter;

import com.alog.dataservice.auth.common.utils.AccessAuthorityRequestUtil;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.http.HttpServletRequestWrapper;
import com.netflix.zuul.http.ServletInputStreamWrapper;
import net.sf.json.JSONObject;
import org.springframework.util.StreamUtils;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class AuthFilter extends ZuulFilter {

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 1;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }


    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        HttpServletResponse response = ctx.getResponse();

        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer" + " ")) {
            //请求认证授权中心（auth-center）进行鉴权,然后返回数据范围资源码
            String codes = AccessAuthorityRequestUtil.verifyToken(request, token);
            if (codes.equals("1")) {
                System.out.println("没有该接口访问权限！");
            } else {
                ctx.setSendZuulResponse(true);
                ctx.setResponseStatusCode(200);
                //ctx.addZuulRequestHeader("codes",codes);
                try {
                    InputStream in = ctx.getRequest().getInputStream();

                    String body = StreamUtils.copyToString(in, Charset.forName("UTF-8"));
                    System.out.println("body:" + body);

                    /**
                     * 传过来的请求数据访问范围码与查出来的该接口授权的数据访问范围码对比取交集，转json，传到接口
                     */
                    JSONObject json = JSONObject.fromObject(body);
                    //这个是请求传过来的
                    String jsonCodes = (String) json.get("codes");

                    String[] jsonCodesStr = jsonCodes.split(",");
                    //这个是数据库查出来的
                    String[] codesStr = codes.split(",");

                    List _a = Arrays.asList(jsonCodesStr);
                    List _b = Arrays.asList(codesStr);

                    Collection realA = new ArrayList<String>(_a);
                    Collection realB = new ArrayList<String>(_b);
                    // 求交集
                    realA.retainAll(realB);
                    System.out.println("交集结果：" + realA.toString());

                    Object[] objects = realA.toArray();
                    if (objects.length < 1) {
                        json.put("codes", "");
                    } else {
                        StringBuilder sb = new StringBuilder();
                        for (int i = 0; i < objects.length; i++) {
                            if (i != objects.length - 1) {
                                json.put("codes", sb.append(objects[i]).append(",").toString());
                            } else {
                                json.put("codes", sb.append(objects[i]).toString());
                            }
                        }
                    }

                    String newBody = json.toString();
                    System.out.println("newBody:" + newBody);

                    final byte[] reqBodyBytes = newBody.getBytes();
                    ctx.setRequest(new HttpServletRequestWrapper(request) {
                        @Override
                        public ServletInputStream getInputStream() throws IOException {
                            return new ServletInputStreamWrapper(reqBodyBytes);
                        }

                        @Override
                        public int getContentLength() {
                            return reqBodyBytes.length;
                        }

                        @Override
                        public long getContentLengthLong() {
                            return reqBodyBytes.length;
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }

                //ctx.setResponseBody(codes);
                //response.addHeader("data_area_code",codes);
                //ctx.setResponse(response);
            }
        } else {
            System.out.println("没有携带token访问");
        }
        return null;
    }
}
