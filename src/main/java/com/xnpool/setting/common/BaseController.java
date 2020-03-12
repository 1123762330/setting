package com.xnpool.setting.common;

import com.alibaba.fastjson.JSON;
import com.xnpool.setting.common.exception.*;
import com.xnpool.setting.config.ApiContext;
import com.xnpool.setting.domain.pojo.*;
import com.xnpool.setting.domain.redismodel.*;
import com.xnpool.setting.utils.JedisUtil;
import com.xnpool.setting.utils.PrimaryKeyUtils;
import com.xnpool.setting.utils.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
		}else if(e instanceof ParseException){
			//时间转换异常
			status= 701;
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
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			hashMap.put("list",recordList);
			hashMap.put("updateTime",sdf.format(new Date()));
			insertRedisToBatch(table, BATCHCOMEIN, hashMap,mineId);
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
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			hashMap.put("list",recordList);
			hashMap.put("updateTime",sdf.format(new Date()));
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
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			hashMap.put("list",recordList);
			hashMap.put("updateTime",sdf.format(new Date()));
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
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			hashMap.put("list",recordList);
			hashMap.put("updateTime",sdf.format(new Date()));
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
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			hashMap.put("list",recordList);
			hashMap.put("updateTime",sdf.format(new Date()));
			insertRedisToBatch(table, BATCHUPDATE, hashMap,mineId);
		} else {
			throw new DeleteException("批量更新失败");
		}
	}

	/**
	 * @Description	计算时间(传入的是秒值)
	 * @Author zly
	 * @Date 10:46 2020/3/11
	 * @Param
	 * @return
	 */
	public String calculTime(Long totalTime){
		String DateTimes = null;
		if (totalTime !=0L) {
			long days = totalTime / (60 * 60 * 24);
			long hours = (totalTime % (60 * 60 * 24)) / (60 * 60);
			long minutes = (totalTime % (60 * 60)) / 60;
			long seconds = totalTime % 60;
			if (days > 0) {
				DateTimes = days + "天" + hours + "小时" + minutes + "分钟" + seconds + "秒";
			} else if (hours > 0) {
				DateTimes = hours + "小时" + minutes + "分钟" + seconds + "秒";
			} else if (minutes > 0) {
				DateTimes = minutes + "分钟" + seconds + "秒";
			} else {
				DateTimes = seconds + "秒";
			}
		}
		return DateTimes;
	}

	/**
	 * @Description	ip地址转长整型
	 * @Author zly
	 * @Date 10:38 2020/3/12
	 * @Param
	 * @return
	 */
	public static long getStringIpToLong(String ip) {
		String[] ips = ip.split("\\.");
		long num =  16777216L*Long.parseLong(ips[0]) + 65536L*Long.parseLong(ips[1]) + 256*Long.parseLong(ips[2]) + Long.parseLong(ips[3]);
		return num;
	}

//*******************************redis缓存json实体类*************************
	public MineSettingRedisModel getMineSettingRedisModel(MineSetting record) {
		MineSettingRedisModel mineSettingRedisModel = new MineSettingRedisModel();
		mineSettingRedisModel.setId(record.getId());
		mineSettingRedisModel.setMine_name(record.getMineName());
		mineSettingRedisModel.setDescription(record.getDescription());
		mineSettingRedisModel.setIs_delete(record.getIsDelete());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if (record.getUpdateTime()!=null){
			String updateTime = sdf.format(record.getUpdateTime());
			mineSettingRedisModel.setUpdate_time(updateTime);
		}
		if (record.getCreateTime()!=null){
			String createTime= sdf.format(record.getCreateTime());
			mineSettingRedisModel.setCreate_time(createTime);
		}
		return mineSettingRedisModel;
	}

	public FactoryHouseRedisModel getFactoryHouseRedisModel(FactoryHouse record) {
		FactoryHouseRedisModel factoryHouseRedisModel = new FactoryHouseRedisModel();
		factoryHouseRedisModel.setId(record.getId());
		factoryHouseRedisModel.setFactory_name(record.getFactoryName());
		factoryHouseRedisModel.setDescription(record.getDescription());
		factoryHouseRedisModel.setMine_id(record.getMineId());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if (record.getUpdateTime()!=null){
			String updateTime = sdf.format(record.getUpdateTime());
			factoryHouseRedisModel.setUpdate_time(updateTime);
		}
		if (record.getCreateTime()!=null){
			String createTime= sdf.format(record.getCreateTime());
			factoryHouseRedisModel.setCreate_time(createTime);
		}
		factoryHouseRedisModel.setIs_delete(record.getIsDelete());
		return factoryHouseRedisModel;
	}


	public FrameSettingRedisModel getFactoryHouseRedisModel(FrameSetting record) {
		FrameSettingRedisModel redisModel = new FrameSettingRedisModel();
		redisModel.setId(record.getId());
		redisModel.setFrame_name(record.getFrameName());
		redisModel.setNumber(record.getNumber());
		redisModel.setFactory_id(record.getFactoryId());
		redisModel.setMine_id(record.getMineId());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if (record.getUpdateTime()!=null){
			String updateTime = sdf.format(record.getUpdateTime());
			redisModel.setUpdate_time(updateTime);
		}
		if (record.getCreateTime()!=null){
			String createTime= sdf.format(record.getCreateTime());
			redisModel.setCreate_time(createTime);
		}
		redisModel.setIs_delete(record.getIsDelete());
		redisModel.setDetailed(record.getDetailed());
		return redisModel;
	}


	public GroupSettingRedisModel getGroupSettingRedisModel(GroupSetting record) {
		GroupSettingRedisModel redisModel = new GroupSettingRedisModel();
		redisModel.setId(record.getId());
		redisModel.setGroup_name(record.getGroupName());
		redisModel.setIp_id(record.getIpId());
		redisModel.setFactory_id(record.getFactoryId());
		redisModel.setMine_id(record.getMineId());
		redisModel.setFrame_id(record.getFrameId());
		redisModel.setIs_delete(record.getIsDelete());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if (record.getUpdateTime()!=null){
			String updateTime = sdf.format(record.getUpdateTime());
			redisModel.setUpdate_time(updateTime);
		}
		if (record.getCreateTime()!=null){
			String createTime= sdf.format(record.getCreateTime());
			redisModel.setCreate_time(createTime);
		}
		return redisModel;
	}



	public IpSettingRedisModel getIpSettingRedisModel(IpSetting record) {
		IpSettingRedisModel redisModel = new IpSettingRedisModel();
		redisModel.setId(record.getId());
		redisModel.setStart_ip(record.getStartIp());
		redisModel.setEnd_ip(record.getEndIp());
		redisModel.setMine_id(record.getMineId());
		redisModel.setIs_delete(record.getIsDelete());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if (record.getUpdateTime()!=null){
			String updateTime = sdf.format(record.getUpdateTime());
			redisModel.setUpdate_time(updateTime);
		}
		if (record.getCreateTime()!=null){
			String createTime= sdf.format(record.getCreateTime());
			redisModel.setCreate_time(createTime);
		}
		return redisModel;
	}



}
