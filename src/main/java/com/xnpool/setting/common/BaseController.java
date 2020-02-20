package com.xnpool.setting.common;

import com.xnpool.setting.common.exception.*;
import com.xnpool.setting.utils.JedisUtil;
import com.xnpool.setting.utils.PrimaryKeyUtils;
import com.xnpool.setting.utils.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.HashMap;

/**
 * @Description 当前项目中所有控制器类基类
 * @Author zly
 * @Date 15:44 2020/2/7
 * @Param
 * @return
 */
@ControllerAdvice
@Slf4j
public abstract class BaseController {
	public static final Integer SUCCESS = 200;
	public static final Integer FAIL = 500;
	public static final String INSERT = "insert";
	public static final String UPDATE = "update";
	public static final String DELETE = "delete";
	public static final String BATCHINSERT = "batchInsert";
	public static final String BATCHUPDATE = "batchUpdate";
	public static final String BATCHDELETE = "batchDelete";

	@Autowired
	private PrimaryKeyUtils primaryKeyUtils;

	@Autowired
	private JedisUtil jedisUtil;


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

		return new ResponseResult<>(status,e);
	}

	//执行添加数据到缓存里面
	public void insertRedis(String table, String user, String record) {
		HashMap<String, String> hashMap = new HashMap<>();
		hashMap.put("table", table);
		hashMap.put("use", user);
		hashMap.put("data", record);
		try {
			String orderIdPrefix = primaryKeyUtils.getOrderIdPrefix(new Date());
			Long global_id = primaryKeyUtils.orderId(orderIdPrefix);
			jedisUtil.zadd("syncing", Double.valueOf(global_id), hashMap.toString());
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new InsertException("添加缓存失败");
		}
	}

}
