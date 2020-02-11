package com.xnpool.setting.config;

/**
 * @author zly
 * @version 1.0
 * @date 2020/1/10 10:30
 */

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.async.TimeoutCallableProcessingInterceptor;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/***
 * 新建Token拦截器
 * @Title: InterceptorConfig.java
 * @author qiaoyn
 * @date 2019/06/14
 * @version V1.0
 */
@Configuration
public class WebAppConfigurer implements WebMvcConfigurer {
	//文件存储路径
	@Value("${config.filePath}")
	private String filePath;
	//访问URL
	@Value("${config.prifix}")
	private String prifix;

	@Bean
	public LoginInterceptor authenticationInterceptor() {
		return new LoginInterceptor();
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(authenticationInterceptor())
				.addPathPatterns("/**");    // 拦截所有请求，通过判断是否有 @LoginRequired 注解 决定是否需要登录
	}

	/**
	 * @Description	自定义静态资源的访问
	 * @Author zly
	 * @Date 12:43 2020/2/7
	 * @Param
	 * @return
	 */
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		String filePaths = System.getProperty("user.dir")+filePath;
		//创建 显示
		WebMvcConfigurer.super.addResourceHandlers(registry);
		registry.addResourceHandler(prifix+"**").addResourceLocations("file:" + filePaths);
		//System.out.println("自定义静态资源目录、此处功能用于文件映射");
	}

	@Override
	public void configureAsyncSupport(final AsyncSupportConfigurer configurer) {
		configurer.setDefaultTimeout(20000);
		configurer.registerCallableInterceptors(timeoutInterceptor());
	}

	@Bean
	public TimeoutCallableProcessingInterceptor timeoutInterceptor() {
		return new TimeoutCallableProcessingInterceptor();
	}

}

