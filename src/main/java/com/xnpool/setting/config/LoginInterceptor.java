package com.xnpool.setting.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xnpool.logaop.util.ResponseResult;
import com.xnpool.setting.common.PassToken;
import com.xnpool.setting.common.UserLoginToken;
import com.xnpool.setting.utils.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.lang.reflect.Method;

/**
 * @Description	拦截器
 * @Author zly
 * @Date 12:46 2020/2/7
 * @Param
 * @return
 */
@Slf4j
@Component
public class LoginInterceptor implements HandlerInterceptor {

	@Autowired
	private ApiContext apiContext;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object){
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json; charset=utf-8");
		response.setHeader("Access-Control-Allow-Credentials", "true");
		response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
		response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
		response.setHeader("Access-Control-Allow-Methods", "GET, HEAD, POST, PUT, DELETE, TRACE, OPTIONS, PATCH");
		String token = request.getHeader("token");// 从 http 请求头中取出 token
		JSONObject jsonObject = TokenUtil.verify(token);
		log.info("解析的token:"+jsonObject);
		Integer success = jsonObject.getInteger("success");
		if (success==200){
			JSONObject data = jsonObject.getJSONObject("data");
			Integer userId = data.getInteger("id");
			String enterpriseId = data.getString("enterpriseId");
			//这里会有多企业id
			if (enterpriseId.contains(",")){
				String[] split = enterpriseId.split(",");
				//从登录用户的token中获取企业ID,
				apiContext.setTenantId(Long.valueOf(split[0]));
			}else {
				apiContext.setTenantId(Long.valueOf(enterpriseId));
			}
			//apiContext.setTenantId(1L);
		}else {
			//apiContext.setTenantId(1L);
			printJson(response, 403, "请登录!");
			return true;
		}
		// 如果不是映射到方法直接通过
		if(!(object instanceof HandlerMethod)){
			return true;
		}
		HandlerMethod handlerMethod=(HandlerMethod)object;
		Method method=handlerMethod.getMethod();
		//检查是否有passtoken注释，有则跳过认证
		if (method.isAnnotationPresent(PassToken.class)) {
			PassToken passToken = method.getAnnotation(PassToken.class);
			if (passToken.required()) {
				return true;
			}
		}
		//检查有没有需要用户权限的注解
		if (method.isAnnotationPresent(UserLoginToken.class)){
			//此处需要获取token进行校验
		}
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

	}
	@Override
	public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

	}

	private static void printJson(HttpServletResponse response, Integer code, String message) {
		ResponseResult responseResult = new ResponseResult(code, message);
		String content = JSON.toJSONString(responseResult);
		printContent(response, content);
	}


	private static void printContent(HttpServletResponse response, String content) {
		try {
			//response.reset();
			response.setContentType("application/json");
			response.setHeader("Cache-Control", "no-store");
			response.setCharacterEncoding("UTF-8");
			PrintWriter pw = response.getWriter();
			pw.write(content);
			pw.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

