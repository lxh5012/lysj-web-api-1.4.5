package com.authine.cloudpivot.web.api.config;

import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.rpc.RpcContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author longhai
 */
@Slf4j
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

//    @Value("${cloudpivot.webmvc.corsmappings:true}")
//    private boolean corsMappings;

    @Value("${cloudpivot.webmvc.openRefererInterceptor:true}")
    private boolean openRefererInterceptor;

    @Value("#{'${cloudpivot.webmvc.corsAllowedOrigins}'.split(',')}")
    private String[] corsAllowedOrigins;

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/error").setViewName("error");
        registry.addViewController("/login").setViewName("login");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("/static/");
    }

//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        if (corsMappings) {
//            if (StringUtils.isNotEmpty(corsAllowedOrigins)) {
//                registry.addMapping("/**")
//                        .allowedOrigins(corsAllowedOrigins.split(","))
//                        .allowCredentials(true)
//                        .allowedHeaders("*")
//                        .allowedMethods("*")
//                        .maxAge(3600);
//            }
//        }
//    }

    @Bean
    public ReferenceConfig referenceConfig(@Value("${dubbo.registry.check}") boolean check, @Value("${dubbo.provider.token}") String token) {
        ReferenceConfig<Object> referenceConfig = new ReferenceConfig<>();
        referenceConfig.setCheck(check);
        RpcContext.getContext().setAttachment("token", token);
        return referenceConfig;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        if (openRefererInterceptor) {
            registry.addInterceptor(new HandlerInterceptor() {
                @Override
                public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
                    String referer = request.getHeader("referer");
                    String serverName = request.getServerName();
                    if (!"GET".equals(request.getMethod())) {
                        if (referer == null) {
                            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                            return false;
                        }
                        URL url;
                        try {
                            url = new URL(referer);
                        } catch (MalformedURLException e) {
                            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                            return false;
                        }
                        if (!serverName.equals(url.getHost())) {
                            if (ArrayUtils.isNotEmpty(corsAllowedOrigins)) {
                                for (String corsAllowedOrigin : corsAllowedOrigins) {
                                    if (corsAllowedOrigin.equals(url.getHost())) {
                                        return true;
                                    }
                                }
                            }
                            return false;
                        }
                    }
                    return true;
                }
            }).addPathPatterns("/api/**");
        }
    }
}
