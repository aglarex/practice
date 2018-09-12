package com.hello.project.configurer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter4;
import com.hello.project.core.Result;
import com.hello.project.core.ResultCode;
import com.hello.project.core.ServiceException;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.*;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Configuration
public class WebMvcConfigurer extends WebMvcConfigurerAdapter {
	
	private final Logger logger = LoggerFactory.getLogger(WebMvcConfigurer.class);
	
	//当前激活的配置文件
    @Value("${spring.profiles.active}")
    private String env;

    //密钥，需自行修改
    @Value("${api.secret}")
    private String secret;
    
    /**
     * json转换器
     */
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
    	FastJsonHttpMessageConverter4 converter = new FastJsonHttpMessageConverter4();
    	FastJsonConfig config = new FastJsonConfig();
    	
    	config.setSerializerFeatures(
    			SerializerFeature.WriteMapNullValue,//保留空的字段
    			SerializerFeature.WriteNullStringAsEmpty,//String null -> ""
    			SerializerFeature.WriteNullNumberAsZero,    //Number null -> 0
    			SerializerFeature.DisableCircularReferenceDetect); //禁用FastJson循环引用检测特性  
    	
    	converter.setFastJsonConfig(config);
    	converter.setDefaultCharset(Charset.forName("UTF-8"));
    	converters.add(converter);
    }
    
    /**
     * 统一异常处理
     */
    @Override
    public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers) {
	
    	exceptionResolvers.add(new HandlerExceptionResolver(){

    		Result result = new Result();
			@Override
			public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response,
					Object handler, Exception ex) {
				// TODO Auto-generated method stub
				if(ex instanceof ServiceException) {
					 result.setCode(ResultCode.FAIL).setMessage(ex.getMessage());
	                 logger.info(ex.getMessage());
				}else if(ex instanceof NoHandlerFoundException) {
					result.setCode(ResultCode.NOT_FOUND).setMessage("接口 [" + request.getRequestURI() + "] 不存在");
				}else if(ex instanceof ServletException) {
					result.setCode(ResultCode.FAIL).setMessage(ex.getMessage());
				}else {
					result.setCode(ResultCode.INTERNAL_SERVER_ERROR)
						.setMessage("接口 [" + request.getRequestURI() + "] 内部错误，请联系管理员");
					String message;
					if(handler instanceof HandlerMethod) {
						HandlerMethod handlerMethod = (HandlerMethod) handler;
						message = String.format("接口 [%s] 出现异常，方法：%s.%s，异常摘要：%s",
	                                request.getRequestURI(),
	                                handlerMethod.getBean().getClass().getName(),
	                                handlerMethod.getMethod().getName(),
	                                ex.getMessage());
					}else {
						message = ex.getMessage();
					}
					logger.error(message,ex);
				}
				return new ModelAndView();	
			}
    	});
    }
    
    /**
     * 跨域处理问题
     */
    @Override
	public void addCorsMappings(CorsRegistry registry) {
    	registry.addMapping("/**");
	}
    
    
   /* //接口拦截器
    @Override
	public void addInterceptors(InterceptorRegistry registry) {
    	
    	if(!"dev".equals(env)) {
    		registry.addInterceptor(new HandlerInterceptorAdapter() {//添加过滤器
    			@Override
    			public boolean preHandle(HttpServletRequest request, 
    					HttpServletResponse response, Object handler) throws Exception {
    				
    				return false;
    			}
    		});
    	}
	}*/
    
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
    	registry.addResourceHandler("swagger-ui.html")
        .addResourceLocations("classpath:/META-INF/resources/");

    	registry.addResourceHandler("/webjars/**")
        .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
   
    
}
