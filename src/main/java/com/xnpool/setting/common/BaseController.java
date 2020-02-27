package com.xnpool.setting.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.google.gson.Gson;
import com.xnpool.setting.common.exception.*;
import com.xnpool.setting.config.ApiContext;
import com.xnpool.setting.utils.JedisUtil;
import com.xnpool.setting.utils.PrimaryKeyUtils;
import com.xnpool.setting.utils.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

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
	public static final String BATCHCOMEIN = "batchComeIn";
	public static final String BATCHMOVEOUT = "batchMoveOut";
	public static final String BATCHDELETE = "batchDelete";
	public static final String BATCHUPDATE = "batchupdate";

	@Autowired
	private PrimaryKeyUtils primaryKeyUtils;

	@Autowired
	private JedisUtil jedisUtil;

	@Autowired
	private ApiContext apiContext;


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
	public void insertRedis(String table, String user, Object record,Integer mineId) {
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("table", table);
		hashMap.put("use", user);
		hashMap.put("data", record);
		String jsonString = JSON.toJSONString(hashMap);
		System.out.println(jsonString);
		try {
			String orderIdPrefix = primaryKeyUtils.getOrderIdPrefix(new Date());
			Long global_id = primaryKeyUtils.orderId(orderIdPrefix);
			if (mineId==null){
				jedisUtil.zadd("syncing:"+apiContext.getTenantId(), Double.valueOf(global_id), jsonString);
			}else {
				jedisUtil.zadd("syncing:"+apiContext.getTenantId()+":"+mineId, Double.valueOf(global_id), jsonString);
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new InsertException("添加缓存失败");
		}
	}

	/**
	 * @Description	批量添加
	 * @Author zly
	 * @Date 16:02 2020/2/26
	 * @Param
	 * @return
	 */
	private void insertRedisToBatch(String table, String user, HashMap<String, Object> data,Integer mineId) {
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("table", table);
		hashMap.put("use", user);
		hashMap.put("data", data);
		String jsonString = JSON.toJSONString(hashMap);
		System.out.println(jsonString);
		try {
			String orderIdPrefix = primaryKeyUtils.getOrderIdPrefix(new Date());
			Long global_id = primaryKeyUtils.orderId(orderIdPrefix);
			if (mineId==null){
				jedisUtil.zadd("syncing:"+apiContext.getTenantId(), Double.valueOf(global_id), jsonString);
			}else {
				jedisUtil.zadd("syncing:"+apiContext.getTenantId()+":"+mineId, Double.valueOf(global_id), jsonString);
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new InsertException("添加缓存失败");
		}
	}

	/**
	 * @Description 新增数据
	 * @Author zly
	 * @Date 15:22 2020/2/21
	 * @Param
	 * @return
	 */
	public void redisToInsert(Integer rows,String table,Object record,Integer mineId){
		if (rows == 1) {
			//入库成功,写缓存
			insertRedis(table, INSERT, record,mineId);
		} else {
			throw new InsertException("添加失败");
		}
	}

	/**
	 * @Description	修改数据
	 * @Author zly
	 * @Date 15:26 2020/2/21
	 * @Param
	 * @return
	 */
	public void redisToUpdate(Integer rows,String table,Object record,Integer mineId){
		if (rows == 1) {
			//操作数据库成功,写缓存
			insertRedis(table, UPDATE, record,mineId);
		} else {
			throw new UpdateException("修改失败");
		}
	}

	/**
	 * @Description	删除数据
	 * @Author zly
	 * @Date 15:28 2020/2/21
	 * @Param
	 * @return
	 */
	public void redisToDelete(Integer rows,String table,Object record,Integer mineId){
		if (rows == 1) {
			//操作数据库成功,写缓存
			insertRedis(table, DELETE, record,mineId);
		} else {
			throw new UpdateException("修改失败");
		}
	}

	/**
	 * @Description 批量出库数据
	 * @Author zly
	 * @Date 15:32 2020/2/21
	 * @Param
	 * @return
	 */
	public void batchComeIn(Integer rows, String table, Object recordList,Integer mineId){
		if (rows !=0) {
			//操作数据库成功,写缓存
			HashMap<String, Object> hashMap = new HashMap<>();
			hashMap.put("list",recordList);
			hashMap.put("updateTime",new Date());
			insertRedis(table, BATCHCOMEIN, hashMap.toString(),mineId);
		} else {
			throw new UpdateException("批量入库失败");
		}
	}

	/**
	 * @Description	批量出库数据
	 * @Author zly
	 * @Date 15:35 2020/2/21
	 * @Param
	 * @return
	 */
	public void batchMoveOut(Integer rows, String table,Object recordList,Integer mineId){
		if (rows !=0) {
			//操作数据库成功,写缓存
			HashMap<String, Object> hashMap = new HashMap<>();
			hashMap.put("list",recordList);
			hashMap.put("updateTime",new Date());
			insertRedisToBatch(table, BATCHMOVEOUT, hashMap,mineId);
		} else {
			throw new UpdateException("批量出库失败");
		}
	}

	/**
	 * @Description	批量删除数据
	 * @Author zly
	 * @Date 15:37 2020/2/21
	 * @Param
	 * @return
	 */
	public void redisToBatchDelete(Integer rows, String table, Object recordList,Integer mineId){
		if (rows !=0) {
			//操作数据库成功,写缓存
			HashMap<String, Object> hashMap = new HashMap<>();
			hashMap.put("list",recordList);
			hashMap.put("updateTime",new Date());
			insertRedis(table, BATCHDELETE, hashMap.toString(),mineId);
		} else {
			throw new DeleteException("批量删除失败");
		}
	}

	/**
	 * @Description	批量更新操作
	 * @Author zly
	 * @Date 16:03 2020/2/26
	 * @Param
	 * @return
	 */
	public void redisToBatchInsert(Integer rows, String table, Object recordList, Integer mineId){
		if (rows !=0) {
			//操作数据库成功,写缓存
			HashMap<String, Object> hashMap = new HashMap<>();
			hashMap.put("list",recordList);
			hashMap.put("updateTime",new Date());
			insertRedisToBatch(table, BATCHINSERT, hashMap,mineId);
		} else {
			throw new DeleteException("批量添加失败");
		}
	}

	/**
	 * @Description	批量更新操作
	 * @Author zly
	 * @Date 16:03 2020/2/26
	 * @Param
	 * @return
	 */
	public void redisToBatchUpdate(Integer rows, String table, Object recordList, Integer mineId){
		if (rows !=0) {
			//操作数据库成功,写缓存
			HashMap<String, Object> hashMap = new HashMap<>();
			hashMap.put("list",recordList);
			hashMap.put("updateTime",new Date());
			insertRedisToBatch(table, BATCHUPDATE, hashMap,mineId);
		} else {
			throw new DeleteException("批量更新失败");
		}
	}

}
