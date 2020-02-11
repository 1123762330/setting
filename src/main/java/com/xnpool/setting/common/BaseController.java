package com.xnpool.setting.common;

import com.xnpool.setting.common.exception.*;
import com.xnpool.setting.utils.ResponseResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Description 当前项目中所有控制器类基类
 * @Author zly
 * @Date 15:44 2020/2/7
 * @Param
 * @return
 */
@ControllerAdvice
public abstract class BaseController {
	public static final Integer SUCCESS = 200;
	public static final Integer fail = 500;


	@ExceptionHandler({ServiceException.class})
	@ResponseBody
	public ResponseResult<Void> handleException(Exception e) {
		Integer status = null;
		//400-验证异常
		if(e instanceof CheckException) {
			status = 400;
		//401-数据不存在
		}else if(e instanceof DataNotFoundException){
			status = 401;
		//402-数据不存在
		}else if(e instanceof DataExistException){
			status = 402;
		//长度不正确
		}else if(e instanceof OutLengthException){
			status = 403;
		//解密异常
		}else if(e instanceof VerifyException){
			status = 405;
		//数据添加失败
		}else if (e instanceof InsertException){
			status = 501;
		}else if(e instanceof UpdateException){
			status = 502;
		}else if (e instanceof TokenException){
			//todo
			status =700;
		}else if(e instanceof NoMessageException){
			//无需返回消息给前端异常
			status= 201;
		}

		return new ResponseResult<>(status, e);
	}

}
