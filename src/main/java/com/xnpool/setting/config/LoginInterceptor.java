package com.xnpool.setting.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xnpool.logaop.util.JwtUtil;
import com.xnpool.logaop.util.ResponseResult;
import com.xnpool.logaop.util.WriteLogUtil;
import com.xnpool.setting.common.PassToken;
import com.xnpool.setting.common.UserLoginToken;
import com.xnpool.setting.utils.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Map;

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

	@Autowired
	private WriteLogUtil writeLogUtil;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object){
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json; charset=utf-8");
		response.setHeader("Access-Control-Allow-Credentials", "true");
		response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
		response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
		response.setHeader("Access-Control-Allow-Methods", "GET, HEAD, POST, PUT, DELETE, TRACE, OPTIONS, PATCH");
		//获取token
		String token = writeLogUtil.getToken(request);
		Map<String, String> verify = JwtUtil.verify(token);
		log.info("解析的token:"+verify);
		Long tenant_id=-1L;
		if(verify!=null){
			Object enterpriseIdObj = verify.get("tenant_id");
			if(!StringUtils.isEmpty(enterpriseIdObj)){
				String data = enterpriseIdObj.toString();
				if(!data.contains(",")){
					tenant_id = Long.valueOf(data);
				}else {
					String[] split = data.split(",");
					//从登录用户的token中获取企业ID,
					tenant_id=Long.valueOf(split[0]);
				}
			}
		}
		apiContext.setTenantId(Long.valueOf(tenant_id));
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

