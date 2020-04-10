package com.xnpool.setting.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xnpool.logaop.service.exception.DeleteException;
import com.xnpool.logaop.service.exception.InsertException;
import com.xnpool.logaop.service.exception.UpdateException;
import com.xnpool.logaop.util.JwtUtil;
import com.xnpool.logaop.util.ResponseResult;
import com.xnpool.setting.config.ApiContext;
import com.xnpool.setting.domain.mapper.MineSettingMapper;
import com.xnpool.setting.domain.pojo.*;
import com.xnpool.setting.domain.redismodel.*;
import com.xnpool.setting.utils.JedisUtil;
import com.xnpool.setting.utils.PrimaryKeyUtils;
import com.xnpool.setting.utils.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Description 当前项目中所有控制器类基类
 * @Author zly
 * @Date 15:44 2020/2/7
 * @Param
 * @return
 */
@Component
public class BaseController {
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
	//在线折线图
	public static final String ON_LINE_DATA = "xnpool:user:share:";
	//矿机平均算力图(有效)
	public static final String HASHRATE_DATA = "xnpool:user:hashrate:";
	//用户矿机总数量
	public static final String USERWORKER_TOTAL = "xnpool:user:mine:ship";
	//用户矿机在线总数量
	public static final String USERWORKER_ONLINE_TOTAL = "xnpool:user:mine:onLine:count";


	@Autowired
	private PrimaryKeyUtils primaryKeyUtils;

	@Autowired
	private JedisUtil jedisUtil;

	@Autowired
	private ApiContext apiContext;

	@Autowired
	private MineSettingMapper mineSettingMapper;


	//@ExceptionHandler({ServiceException.class})
	//@ResponseBody
	//public ResponseResult<Void> handleException(Exception e) {
	//	Integer status = null;
	//	//400-验证异常
	//	if(e instanceof CheckException) {
	//		status = 400;
	//	//401-数据不存在
	//	}else if(e instanceof DataNotFoundException){
	//		status = 401;
	//	//402-数据不存在
	//	}else if(e instanceof DataExistException){
	//		status = 402;
	//	//长度不正确
	//	}else if(e instanceof OutLengthException){
	//		status = 403;
	//	//解密异常
	//	}else if(e instanceof VerifyException){
	//		status = 405;
	//	//数据添加失败
	//	}else if (e instanceof InsertException){
	//		status = 501;
	//	}else if(e instanceof UpdateException){
	//		status = 502;
	//	}else if (e instanceof TokenException){
	//		//todo
	//		status =700;
	//	}else if(e instanceof NoMessageException){
	//		//无需返回消息给前端异常
	//		status= 201;
	//	}else if(e instanceof ParseException){
	//		//时间转换异常
	//		status= 701;
	//	}
	//
	//	return new ResponseResult<>(status,e);
	//}

	//执行添加数据到缓存里面
	public void insertRedis(String table, String user, Object record,Integer mineId) {
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("table", table);
		hashMap.put("use", user);
		hashMap.put("data", record);
		String jsonString = JSON.toJSONString(hashMap);
		//System.out.println(jsonString);
		try {
			String orderIdPrefix = primaryKeyUtils.getOrderIdPrefix(new Date());
			Long global_id = primaryKeyUtils.orderId(orderIdPrefix);
			if (mineId==null){
				jedisUtil.zadd("syncing:"+apiContext.getTenantId(), Double.valueOf(global_id), jsonString);
			}else {
				jedisUtil.zadd("syncing:"+apiContext.getTenantId()+":"+mineId, Double.valueOf(global_id), jsonString);
			}
		} catch (Exception e) {
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
		if (rows != 0) {
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
		if (rows !=0) {
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
		if (rows !=0) {
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
				DateTimes = days + "天 " + hours + "小时 " + minutes + "分钟 " + seconds + "秒";
			} else if (hours > 0) {
				DateTimes = hours + "小时 " + minutes + "分钟 " + seconds + "秒";
			} else if (minutes > 0) {
				DateTimes = minutes + "分钟 " + seconds + "秒";
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
		long result = 0;
		java.util.StringTokenizer token = new java.util.StringTokenizer(ip,".");
		result += Long.parseLong(token.nextToken())<<24;
		result += Long.parseLong(token.nextToken())<<16;
		result += Long.parseLong(token.nextToken())<<8;
		result += Long.parseLong(token.nextToken());
		return result;
	}

	//长整型转ip字符串
	public static String longToIp(long ipLong){
		StringBuilder sb = new StringBuilder();
		sb.append(ipLong>>>24);sb.append(".");
		sb.append(String.valueOf((ipLong&0x00FFFFFF)>>>16));sb.append(".");
		sb.append(String.valueOf((ipLong&0x0000FFFF)>>>8));sb.append(".");
		sb.append(String.valueOf(ipLong&0x000000FF));
		return sb.toString();
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


	public FrameSettingRedisModel getFrameSettingRedisModel(FrameSetting record) {
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
		if (record.getUpdateTime() != null) {
			String updateTime = sdf.format(record.getUpdateTime());
			redisModel.setUpdate_time(updateTime);
		}
		if (record.getCreateTime() != null) {
			String createTime = sdf.format(record.getCreateTime());
			redisModel.setCreate_time(createTime);
		}
		return redisModel;
	}

	public WorkerDetailedRedisModel getWorkerDetailedRedisModel(WorkerDetailed record) {
		WorkerDetailedRedisModel workerDetailedRedisModel = new WorkerDetailedRedisModel();
		workerDetailedRedisModel.setId(record.getId());
		workerDetailedRedisModel.setWorker_id(record.getWorkerId());
		workerDetailedRedisModel.setUser_id(record.getUserId());
		workerDetailedRedisModel.setWorkerbrand_id(record.getWorkerbrandId());
		workerDetailedRedisModel.setFactory_id(record.getFactoryId());
		workerDetailedRedisModel.setFrame_id(record.getFrameId());
		workerDetailedRedisModel.setFrame_number(record.getFrameNumber());
		workerDetailedRedisModel.setGroup_id(record.getGroupId());
		workerDetailedRedisModel.setMine_id(record.getMineId());
		workerDetailedRedisModel.setIs_come_in(record.getIsComeIn());
		workerDetailedRedisModel.setRemarks(record.getRemarks());
		workerDetailedRedisModel.setIs_delete(record.getIsDelete());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if (record.getUpdateTime() != null) {
			String updateTime = sdf.format(record.getUpdateTime());
			workerDetailedRedisModel.setUpdate_time(updateTime);
		}
		if (record.getCreateTime() != null) {
			String createTime = sdf.format(record.getCreateTime());
			workerDetailedRedisModel.setCreate_time(createTime);
		}
		workerDetailedRedisModel.setWorker_ip(record.getWorkerIp());
		return workerDetailedRedisModel;
	}

	public WorkerbrandSettingRedisModel getWorkerbrandSettingRedisModel(WorkerbrandSetting record) {
		WorkerbrandSettingRedisModel workerbrandSettingRedisModel = new WorkerbrandSettingRedisModel();
		workerbrandSettingRedisModel.setId(record.getId());
		workerbrandSettingRedisModel.setBrand_name(record.getBrandName());
		workerbrandSettingRedisModel.setWorker_type(record.getWorkerType());
		workerbrandSettingRedisModel.setBusiness(record.getBusiness());
		workerbrandSettingRedisModel.setWorker_size(record.getWorkerSize());
		workerbrandSettingRedisModel.setPower_waste(record.getPowerWaste());
		workerbrandSettingRedisModel.setDifficulty(record.getDifficulty());
		workerbrandSettingRedisModel.setIs_delete(0);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		workerbrandSettingRedisModel.setCreate_time(sdf.format(new Date()));
		workerbrandSettingRedisModel.setUpdate_time(sdf.format(new Date()));
		workerbrandSettingRedisModel.setAlgorithm_id(record.getAlgorithmId());
		return workerbrandSettingRedisModel;
	}

	public OperatorWorkerHisRedisModel getOperatorWorkerHisRedisModel(OperatorWorkerHistory record) {
		OperatorWorkerHisRedisModel operatorWorkerHisRedisModel = new OperatorWorkerHisRedisModel();
		operatorWorkerHisRedisModel.setId(record.getId());
		operatorWorkerHisRedisModel.setMine_id(record.getMineId());
		operatorWorkerHisRedisModel.setWorker_id(record.getWorkerId());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		operatorWorkerHisRedisModel.setReason(record.getReason());
		operatorWorkerHisRedisModel.setOperator_id(record.getOperatorId());
		if (record.getMoveOutTime() != null) {
			String moveOutTime = sdf.format(record.getMoveOutTime());
			operatorWorkerHisRedisModel.setMove_out_time(moveOutTime);
		}
		if ( record.getComeInTime()!= null) {
			String comeInTime = sdf.format(record.getComeInTime());
			operatorWorkerHisRedisModel.setCome_in_time(comeInTime);
		}
		return operatorWorkerHisRedisModel;
	}

	public SysUserRedisModel getSysUserRedisModel(SysUser sysUser) {
		SysUserRedisModel sysUserRedisModel = new SysUserRedisModel();
		sysUserRedisModel.setId(sysUser.getId());
		sysUserRedisModel.setUsername(sysUser.getUsername());
		sysUserRedisModel.setPassword(sysUser.getPassword());
		sysUserRedisModel.setNickname(sysUser.getNickname());
		sysUserRedisModel.setHead_img_url(sysUser.getHeadImgUrl());
		sysUserRedisModel.setMobile(sysUser.getMobile());
		sysUserRedisModel.setSex(sysUser.getSex());
		sysUserRedisModel.setEnabled(sysUser.getEnabled());
		sysUserRedisModel.setType(sysUser.getType());
		sysUserRedisModel.setCompany(sysUser.getCompany());
		sysUserRedisModel.setOpen_id(sysUser.getOpenId());
		sysUserRedisModel.setIs_del(sysUser.getIsDel());
		sysUserRedisModel.setTenant_id(sysUser.getTenantId());
		sysUserRedisModel.setContact_person(sysUser.getContactPerson());
		sysUserRedisModel.setAddress(sysUser.getAddress());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if (sysUser.getCreateTime() != null) {
			String createTime = sdf.format(sysUser.getCreateTime());
			sysUserRedisModel.setCreate_time(createTime);
		}
		if ( sysUser.getUpdateTime()!= null) {
			String updateTime = sdf.format(sysUser.getUpdateTime());
			sysUserRedisModel.setUpdate_time(updateTime);
		}
		return sysUserRedisModel;
	}

	//当前时间按15分钟取整
	public HashMap<String, Date> nowTimeAfter15min(){
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(new Date());
		int min = calendar.get(Calendar.MINUTE);
		if (min >= 45) {
			calendar.set(Calendar.MINUTE, 45);
		}else if (min >= 30) {
			calendar.set(Calendar.MINUTE, 30);
		} else if (min >= 15) {
			calendar.set(Calendar.MINUTE, 15);
		} else {
			calendar.set(Calendar.MINUTE, 0);
		}
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		Date endDate = calendar.getTime();
		//前一天开始的时间
		calendar.add(calendar.DATE, -1);
		Date startDate = calendar.getTime();
		HashMap<String, Date> hashMap = new HashMap<>();
		hashMap.put("startDate",startDate);
		hashMap.put("endDate",endDate);
		return hashMap;
	}

	//切割1天的时间戳,按传入的分钟数划分
	public HashMap<Object, Object> qiegeMin(int min){
		HashMap<String, Date> stringDateHashMap = nowTimeAfter15min();
		Date startDate = stringDateHashMap.get("startDate");
		Date endDate = stringDateHashMap.get("endDate");

		//然后按照开始时间和终止时间进行时间切割
		List<String> list = new ArrayList<>();
		SimpleDateFormat simpleDateTime = new SimpleDateFormat("yyyyMMddHHmm");
		while (startDate.getTime() <= endDate.getTime()) {
			list.add(simpleDateTime.format(startDate));
			Calendar calendar2 = Calendar.getInstance();
			calendar2.setTime(startDate);
			calendar2.add(Calendar.MINUTE, min);
			if (calendar2.getTime().getTime() > endDate.getTime()) {
				if (!startDate.equals(endDate)) {
					list.add(simpleDateTime.format(startDate));
				}
				startDate = calendar2.getTime();
			} else {
				startDate = calendar2.getTime();
			}
		}
		HashMap shiJianMap=new HashMap<>();
		//遍历24小时的键
		for (String timeStr : list) {
			shiJianMap.put(timeStr,"0");
		}
		return shiJianMap;
	}

	/**
	 * @Description	获取token的数据
	 * @Author zly
	 * @Date 8:57 2020/3/20
	 * @Param
	 * @return
	 */
	public HashMap<String, Object> getTokenData(String token){
		JSONObject jsonObject = TokenUtil.verify(token);
		Integer success = jsonObject.getInteger("success");
		HashMap<String, Object> result = new HashMap<>();
		if (success==200){
			JSONObject data = jsonObject.getJSONObject("data");
			Integer userId = data.getInteger("id");
			Integer tenant_id = data.getInteger("tenant_id");
			result.put("userId",userId);
			result.put("tenantId",tenant_id);
		}
		return result;
	}

	/**
	 * @Description	从token中获取用户id
	 * @Author zly
	 * @Date 16:38 2020/3/30
	 * @Param
	 * @return
	 */
	public Integer getUserId(String token){
		Integer userId=0;
		Map<String, Object> verify = JwtUtil.verify(token);
		System.out.println("解析的token:"+verify);
		if (verify != null) {
			Object userIdObj = verify.get("id");
			if (userIdObj != null) {
				String data = userIdObj.toString();
				if (!data.contains(",")) {
					userId = Integer.valueOf(data);
				}
			}
		}
		return userId;
	}
	/**
	 * @Description 从token中获取企业id
	 * @Author zly
	 * @Date 16:38 2020/3/30
	 * @Param
	 * @return
	 */
	public Long getTenantId(String token){
		Long tenant_id=-1L;
		Map<String, Object> verify = JwtUtil.verify(token);
		if(verify!=null){
			Object tenantIdObj = verify.get("tenant_id");
			if(!StringUtils.isEmpty(tenantIdObj)&&!"null".equals(tenantIdObj)){
				String data = tenantIdObj.toString();
				if(!data.contains(",")){
					tenant_id = Long.valueOf(data);
				}
			}
		}
		return tenant_id;
	}

	/**
	 * @Description	从token中获取矿场id
	 * @Author zly
	 * @Date 11:52 2020/4/10
	 * @Param
	 * @return
	 */
	public List<Integer> getMineId(String token){
		List<Integer> list = new ArrayList<>();
		Map<String, Object> verify = JwtUtil.verify(token);
		System.out.println(verify);
		if(verify!=null){
			Object mineIdObj = verify.get("mine_id");
			JSONArray array = JSONObject.parseArray(mineIdObj.toString());
			if (array.size()==1){
				Integer mineId= Integer.valueOf(array.get(0).toString());
				if (mineId==-1){
					//查询全部矿场id
					List<Integer> selectMineId = mineSettingMapper.selectMineId();
					return selectMineId;
				}else {
					list.add(mineId);
					return list;
				}
			}else{
				for (Object obj : array) {
					list.add(Integer.valueOf(obj.toString()));
				}
				return list;
			}
		}else {
			return null;
		}
	}

	/**
	 * @Description	同步用户信息
	 * @Author zly
	 * @Date 12:32 2020/4/10
	 * @Param
	 * @return
	 */
	public ResponseResult syncinUser(SysUser sysUser, String token){
		try {
			Long tenantId = getTenantId(token);
			apiContext.setTenantId(tenantId);
			List<Integer> list = getMineId(token);
			for (Integer mineId : list) {
				SysUserRedisModel sysUserRedisModel = getSysUserRedisModel(sysUser);
				redisToInsert(1, "sys_user", sysUserRedisModel,mineId );
			}
		}catch (Exception e){
			return new ResponseResult(FAIL,"同步缓存失败!");
		}
		return new ResponseResult(SUCCESS);
	}
}
