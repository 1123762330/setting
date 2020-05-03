package com.xnpool.setting.config;

/**
 * @author zly
 * @version 1.0
 * @date 2020/1/10 10:30
 */

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;
import org.springframework.web.context.request.async.TimeoutCallableProcessingInterceptor;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

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

	@Value("${config.prifix_2}")
	private String prifix_2;

	@Bean
	public LoginInterceptor authenticationInterceptor() {
		return new LoginInterceptor();
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		//List<String> excludePathPatterns = new ArrayList<>();
		//excludePathPatterns.add("/**");
		registry.addInterceptor(authenticationInterceptor())
				// 拦截所有请求，通过判断是否有 @LoginRequired 注解 决定是否需要登录
				.addPathPatterns("/api/v1/**");
				//.excludePathPatterns(excludePathPatterns);
	}

	/**
	 * @Description	自定义静态资源目录、此处功能用于文件映射
	 * @Author zly
	 * @Date 12:43 2020/2/7
	 * @Param
	 * @return
	 */
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		File path = null;
		try {
			path = new File(ResourceUtils.getURL("classpath:").getPath());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		//静态文件目录
		String filePath = path.getParentFile().getParentFile().getParent() + File.separator + "extPath" + File.separator + "static" + File.separator;
		//addResourceHandler是指你想在url请求的路径
		//addResourceLocations是图片存放的真实路径
		//System.out.println("文件路径:"+filePath);
		//registry.addResourceHandler("/image/**").addResourceLocations("file:D://java-work/xnProject/setting/extPath/static/");
		registry.addResourceHandler(prifix+"**").addResourceLocations(filePath);
		registry.addResourceHandler(prifix_2+"**").addResourceLocations(filePath);
		WebMvcConfigurer.super.addResourceHandlers(registry);
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

