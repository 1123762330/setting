package com.xnpool.setting.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xnpool.logaop.service.exception.*;
import com.xnpool.logaop.util.ResponseResult;
import com.xnpool.setting.config.ApiContext;
import com.xnpool.setting.domain.mapper.MineSettingMapper;
import com.xnpool.setting.domain.pojo.*;
import com.xnpool.setting.domain.redismodel.*;
import com.xnpool.setting.utils.JedisUtil;
import com.xnpool.setting.utils.PrimaryKeyUtils;
import com.xnpool.setting.utils.TokenUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Pipeline;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @Description 当前项目中所有控制器类基类
 * @Author zly
 * @Date 15:44 2020/2/7
 * @Param
 * @return
 */
@Component
@Slf4j
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
    public static final String USERWORKER_TOTAL = "xnpool:user:tenant:mine:total:count:";
    //用户矿机在线总数量
    public static final String USERWORKER_ONLINE_TOTAL = "xnpool:user:tenant:mine:onLine:count:";


    @Autowired
    private PrimaryKeyUtils primaryKeyUtils;

    @Autowired
    private JedisUtil jedisUtil;

    @Autowired
    private ApiContext apiContext;

    @Autowired
    private MineSettingMapper mineSettingMapper;

    @Autowired
    private JedisPool jedisPool;


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
    public void insertRedis(String table, String user, Object record, Integer mineId) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("table", table);
        hashMap.put("use", user);
        hashMap.put("data", record);
        String jsonString = JSON.toJSONString(hashMap);
        //System.out.println(jsonString);
        try {
            String orderIdPrefix = primaryKeyUtils.getOrderIdPrefix(new Date());
            Long global_id = primaryKeyUtils.orderId(orderIdPrefix);
            if (mineId == null) {
                jedisUtil.zadd("syncing:" + apiContext.getTenantId(), Double.valueOf(global_id), jsonString);
            } else {
                jedisUtil.zadd("syncing:" + apiContext.getTenantId() + ":" + mineId, Double.valueOf(global_id), jsonString);
            }
        } catch (Exception e) {
            throw new InsertException("添加缓存失败");
        }
    }

    /**
     * @return
     * @Description 批量添加
     * @Author zly
     * @Date 16:02 2020/2/26
     * @Param
     */
    private void insertRedisToBatch(String table, String user, HashMap<String, Object> data, Integer mineId) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("table", table);
        hashMap.put("use", user);
        hashMap.put("data", data);
        String jsonString = JSON.toJSONString(hashMap);
        try {
            String orderIdPrefix = primaryKeyUtils.getOrderIdPrefix(new Date());
            Long global_id = primaryKeyUtils.orderId(orderIdPrefix);
            if (mineId == null) {
                jedisUtil.zadd("syncing:" + apiContext.getTenantId(), Double.valueOf(global_id), jsonString);
            } else {
                jedisUtil.zadd("syncing:" + apiContext.getTenantId() + ":" + mineId, Double.valueOf(global_id), jsonString);
            }
        } catch (Exception e) {
            throw new InsertException("添加缓存失败");
        }
    }

    /**
     * @return
     * @Description 管道流批量添加
     * @Author zly
     * @Date 16:02 2020/2/26
     * @Param
     */
    private void pipelinedToRedis(String table, String user, List recordList, Integer mineId) {
        Jedis jedis = null;
        try {
            String orderIdPrefix = primaryKeyUtils.getOrderIdPrefix(new Date());
            Long global_id = primaryKeyUtils.orderId(orderIdPrefix);
            jedis = jedisPool.getResource();
            Pipeline pipelined = jedis.pipelined();
            for (Object obj : recordList) {
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("table", table);
                hashMap.put("use", user);
                hashMap.put("data", obj);
                String jsonString = JSON.toJSONString(hashMap);
                if (mineId == null) {
                    pipelined.zadd("syncing:" + apiContext.getTenantId(), Double.valueOf(global_id), jsonString);
                } else {
                    pipelined.zadd("syncing:" + apiContext.getTenantId() + ":" + mineId, Double.valueOf(global_id), jsonString);
                }
            }
            pipelined.sync();
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * @return
     * @Description 新增数据
     * @Author zly
     * @Date 15:22 2020/2/21
     * @Param
     */
    public void redisToInsert(Integer rows, String table, Object record, Integer mineId) {
        if (rows != 0) {
            //入库成功,写缓存
            insertRedis(table, INSERT, record, mineId);
        } else {
            throw new InsertException("添加失败");
        }
    }

    /**
     * @return
     * @Description 修改数据
     * @Author zly
     * @Date 15:26 2020/2/21
     * @Param
     */
    public void redisToUpdate(Integer rows, String table, Object record, Integer mineId) {
        if (rows != 0) {
            //操作数据库成功,写缓存
            insertRedis(table, UPDATE, record, mineId);
        } else {
            throw new UpdateException("修改失败");
        }
    }

    /**
     * @return
     * @Description 删除数据
     * @Author zly
     * @Date 15:28 2020/2/21
     * @Param
     */
    public void redisToDelete(Integer rows, String table, Object record, Integer mineId) {
        if (rows != 0) {
            //操作数据库成功,写缓存
            insertRedis(table, DELETE, record, mineId);
        } else {
            throw new UpdateException("修改失败");
        }
    }

    /**
     * @return
     * @Description 批量出库数据
     * @Author zly
     * @Date 15:32 2020/2/21
     * @Param
     */
    public void batchComeIn(Integer rows, String table, Object recordList, Integer mineId) {
        if (rows != 0) {
            //操作数据库成功,写缓存
            HashMap<String, Object> hashMap = new HashMap<>();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            hashMap.put("list", recordList);
            hashMap.put("updateTime", sdf.format(new Date()));
            insertRedisToBatch(table, BATCHCOMEIN, hashMap, mineId);
        } else {
            throw new UpdateException("批量入库失败");
        }
    }

    /**
     * @return
     * @Description 批量出库数据
     * @Author zly
     * @Date 15:35 2020/2/21
     * @Param
     */
    public void batchMoveOut(Integer rows, String table, Object recordList, Integer mineId) {
        if (rows != 0) {
            //操作数据库成功,写缓存
            HashMap<String, Object> hashMap = new HashMap<>();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            hashMap.put("list", recordList);
            hashMap.put("updateTime", sdf.format(new Date()));
            insertRedisToBatch(table, BATCHMOVEOUT, hashMap, mineId);
        } else {
            throw new UpdateException("批量出库失败");
        }
    }

    /**
     * @return
     * @Description 批量删除数据
     * @Author zly
     * @Date 15:37 2020/2/21
     * @Param
     */
    public void redisToBatchDelete(Integer rows, String table, Object recordList, Integer mineId) {
        if (rows != 0) {
            //操作数据库成功,写缓存
            HashMap<String, Object> hashMap = new HashMap<>();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            hashMap.put("list", recordList);
            hashMap.put("updateTime", sdf.format(new Date()));
            insertRedis(table, BATCHDELETE, hashMap.toString(), mineId);
        } else {
            throw new DeleteException("批量删除失败");
        }
    }

    /**
     * @return
     * @Description 批量更新操作
     * @Author zly
     * @Date 16:03 2020/2/26
     * @Param
     */
    public void batchPipelinedToRedis(Integer rows, String table, List recordList, Integer mineId) {
        if (rows != 0) {
            //操作数据库成功,写缓存
            pipelinedToRedis(table, BATCHINSERT, recordList, mineId);
        } else {
            throw new DeleteException("批量添加失败");
        }
    }

    /**
     * @return
     * @Description 批量更新操作
     * @Author zly
     * @Date 16:03 2020/2/26
     * @Param
     */
    public void redisToBatchUpdate(Integer rows, String table, Object recordList, Integer mineId) {
        if (rows != 0) {
            //操作数据库成功,写缓存
            HashMap<String, Object> hashMap = new HashMap<>();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            hashMap.put("list", recordList);
            hashMap.put("updateTime", sdf.format(new Date()));
            insertRedisToBatch(table, BATCHUPDATE, hashMap, mineId);
        } else {
            throw new DeleteException("批量更新失败");
        }
    }

    /**
     * @return
     * @Description 计算时间(传入的是秒值)
     * @Author zly
     * @Date 10:46 2020/3/11
     * @Param
     */
    public String calculTime(Long totalTime) {
        String DateTimes = null;
        if (totalTime != 0L) {
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
     * @return
     * @Description ip地址转长整型
     * @Author zly
     * @Date 10:38 2020/3/12
     * @Param
     */
    public static long getStringIpToLong(String ip) {
        long result = 0;
        java.util.StringTokenizer token = new java.util.StringTokenizer(ip, ".");
        result += Long.parseLong(token.nextToken()) << 24;
        result += Long.parseLong(token.nextToken()) << 16;
        result += Long.parseLong(token.nextToken()) << 8;
        result += Long.parseLong(token.nextToken());
        return result;
    }

    //长整型转ip字符串
    public static String longToIp(long ipLong) {
        StringBuilder sb = new StringBuilder();
        sb.append(ipLong >>> 24);
        sb.append(".");
        sb.append(String.valueOf((ipLong & 0x00FFFFFF) >>> 16));
        sb.append(".");
        sb.append(String.valueOf((ipLong & 0x0000FFFF) >>> 8));
        sb.append(".");
        sb.append(String.valueOf(ipLong & 0x000000FF));
        return sb.toString();
    }

    //*******************************redis缓存json实体类*************************
    public MineSettingRedisModel getMineSettingRedisModel(MineSetting record) {
        MineSettingRedisModel mineSettingRedisModel = new MineSettingRedisModel();
        mineSettingRedisModel.setId(record.getId());
        mineSettingRedisModel.setMine_name(record.getMineName());
        mineSettingRedisModel.setDescription(record.getDescription());
        mineSettingRedisModel.setIs_delete(record.getIsDelete());
        mineSettingRedisModel.setFrame_num(record.getFrameNum());
        mineSettingRedisModel.setUpdate_time(record.getUpdateTime());
        mineSettingRedisModel.setCreate_time(record.getCreateTime());
        return mineSettingRedisModel;
    }

    public FactoryHouseRedisModel getFactoryHouseRedisModel(FactoryHouse record) {
        FactoryHouseRedisModel factoryHouseRedisModel = new FactoryHouseRedisModel();
        factoryHouseRedisModel.setId(record.getId());
        factoryHouseRedisModel.setFactory_name(record.getFactoryName());
        factoryHouseRedisModel.setDescription(record.getDescription());
        factoryHouseRedisModel.setMine_id(record.getMineId());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        factoryHouseRedisModel.setUpdate_time(record.getUpdateTime());
        factoryHouseRedisModel.setCreate_time(record.getCreateTime());
        factoryHouseRedisModel.setIs_delete(record.getIsDelete());
        return factoryHouseRedisModel;
    }

    public FrameSettingRedisModel getFrameSettingRedisModel(FrameSetting record) {
        FrameSettingRedisModel redisModel = new FrameSettingRedisModel();
        redisModel.setId(record.getId());
        redisModel.setFrame_name(record.getFrameName());
        redisModel.setNumber(record.getNumber());
        redisModel.setStorage_racks_num(record.getStorageRacksNum());
        redisModel.setRow_num(record.getRowNum());
        redisModel.setFactory_id(record.getFactoryId());
        redisModel.setMine_id(record.getMineId());
        redisModel.setUpdate_time(record.getUpdateTime());
        redisModel.setCreate_time(record.getCreateTime());
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

    public IpSettingRedisModel getIpSettingRedisModel(IpSetting record) {
        IpSettingRedisModel redisModel = new IpSettingRedisModel();
        redisModel.setId(record.getId());
        redisModel.setStart_ip(record.getStartIp());
        redisModel.setEnd_ip(record.getEndIp());
        redisModel.setMine_id(record.getMineId());
        redisModel.setIs_delete(record.getIsDelete());
        redisModel.setUpdate_time(record.getUpdateTime());
        redisModel.setCreate_time(record.getCreateTime());
        redisModel.setIs_to_int(record.getIsToInt());
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
        sysUserRedisModel.setCreate_time(sysUser.getCreateTime());
        sysUserRedisModel.setUpdate_time(sysUser.getUpdateTime());
        return sysUserRedisModel;
    }

    public WorkerAssignRedisModel getWorkerAssignRedisModel(WorkerAssign workerAssign) {
        WorkerAssignRedisModel workerAssignRedisModel = new WorkerAssignRedisModel();
        workerAssignRedisModel.setId(workerAssign.getId());
        workerAssignRedisModel.setUser_id(workerAssign.getUserId());
        workerAssignRedisModel.setMine_id(workerAssign.getMineId());
        workerAssignRedisModel.setFactory_id(workerAssign.getFactoryId());
        workerAssignRedisModel.setFrame_id(workerAssign.getFrameId());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        workerAssignRedisModel.setCreate_time(sdf.format(new Date()));
        workerAssignRedisModel.setUpdate_time(sdf.format(new Date()));
        workerAssignRedisModel.setIs_del(workerAssign.getIsDel());
        return workerAssignRedisModel;
    }

    public IpAssignRedisModel getIpAssignRedisModel(IpAssign ipAssign) {
        IpAssignRedisModel ipAssignRedisModel = new IpAssignRedisModel();
        ipAssignRedisModel.setId(ipAssign.getId());
        ipAssignRedisModel.setUser_id(ipAssign.getUserId());
        ipAssignRedisModel.setMine_id(ipAssign.getMineId());
        ipAssignRedisModel.setIp_id(ipAssign.getIpId());
        ipAssignRedisModel.setCreate_time(ipAssign.getCreateTime());
        ipAssignRedisModel.setUpdate_time(ipAssign.getCreateTime());
        ipAssignRedisModel.setIs_del(0);
        return ipAssignRedisModel;
    }

    //当前时间按15分钟取整
    public HashMap<String, Date> nowTimeAfter15min() {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(new Date());
        int min = calendar.get(Calendar.MINUTE);
        if (min >= 45) {
            calendar.set(Calendar.MINUTE, 45);
        } else if (min >= 30) {
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
        hashMap.put("startDate", startDate);
        hashMap.put("endDate", endDate);
        return hashMap;
    }

    //切割1天的时间戳,按传入的分钟数划分
    public HashMap<Object, Object> qiegeMin(int min) {
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
        HashMap shiJianMap = new HashMap<>();
        //遍历24小时的键
        for (String timeStr : list) {
            shiJianMap.put(timeStr, "0");
        }
        return shiJianMap;
    }

    /**
     * @return
     * @Description 获取token的数据
     * @Author zly
     * @Date 8:57 2020/3/20
     * @Param
     */
    public HashMap<String, Object> getTokenData(String token) {
        JSONObject jsonObject = TokenUtil.verify(token);
        Integer success = jsonObject.getInteger("success");
        HashMap<String, Object> result = new HashMap<>();
        if (success == 200) {
            JSONObject data = jsonObject.getJSONObject("data");
            Integer userId = data.getInteger("id");
            Integer tenant_id = data.getInteger("tenant_id");
            result.put("userId", userId);
            result.put("tenantId", tenant_id);
        }
        return result;
    }

    /**
     * @return
     * @Description 从token中获取用户id
     * @Author zly
     * @Date 16:38 2020/3/30
     * @Param
     */
    public Integer getUserId(String token) {
        Integer userId = 0;
        Map<String, Object> verify = verify(token);
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
     * @return
     * @Description 从token中获取企业id
     * @Author zly
     * @Date 16:38 2020/3/30
     * @Param
     */
    public Long getTenantId(String token) {
        Long tenant_id = -1L;
        Map<String, Object> verify = verify(token);
        if (verify != null) {
            Object tenantIdObj = verify.get("tenant_id");
            if (!StringUtils.isEmpty(tenantIdObj) && !"null".equals(tenantIdObj)) {
                String data = tenantIdObj.toString();
                if (!data.contains(",")) {
                    tenant_id = Long.valueOf(data);
                }
            }
        }
        return tenant_id;
    }

    /**
     * @return
     * @Description 从token中获取矿场id
     * @Author zly
     * @Date 11:52 2020/4/10
     * @Param
     */
    public List<Integer> getMineId(String token) {
        List<Integer> list = new ArrayList<>();
        Map<String, Object> verify = verify(token);
        if (verify != null) {
            Object mineIdObj = verify.get("mine_id");
            if (!StringUtils.isEmpty(mineIdObj)) {
                JSONArray array = JSONObject.parseArray(mineIdObj.toString());
                if (array.size() == 1) {
                    Integer mineId = Integer.valueOf(array.get(0).toString());
                    if (mineId == -1) {
                        //查询全部矿场id
                        List<Integer> selectMineId = mineSettingMapper.selectMineId();
                        return selectMineId;
                    } else {
                        list.add(mineId);
                        return list;
                    }
                } else {
                    for (Object obj : array) {
                        list.add(Integer.valueOf(obj.toString()));
                    }
                    return list;
                }
            } else {
                throw new CheckException("未获取到该账户的数据权限,权限为空");
            }
        } else {
            throw new CheckException("token解析失败!");
        }
    }

    public Integer getSuperMineId(String token) {
        Map<String, Object> verify = verify(token);
        if (verify != null) {
            Object mineIdObj = verify.get("mine_id");
            if (!StringUtils.isEmpty(mineIdObj)) {
                JSONArray array = JSONObject.parseArray(mineIdObj.toString());
                if (array.size() == 1) {
                    Integer mineId = Integer.valueOf(array.get(0).toString());
                    if (mineId == -1) {
                        return mineId;
                    }
                }
            }
        }
        return null;
    }

    /**
     * @return
     * @Description 同步用户信息
     * @Author zly
     * @Date 12:32 2020/4/10
     * @Param
     */
    public ResponseResult syncinUserUpdate(SysUser sysUser, String token) {
        try {
            Long tenantId = getTenantId(token);
            apiContext.setTenantId(tenantId);
            List<Integer> list = getMineId(token);
            log.info("sysUser==" + sysUser + "; list===" + list);
            for (Integer mineId : list) {
                SysUserRedisModel sysUserRedisModel = getSysUserRedisModel(sysUser);
                redisToUpdate(1, "sys_user", sysUserRedisModel, mineId);
            }
        } catch (Exception e) {
            return new ResponseResult(FAIL, "同步缓存失败!");
        }
        return new ResponseResult(SUCCESS);
    }

    /**
     * @return
     * @Description 删除同步用户信息
     * @Author zly
     * @Date 12:32 2020/4/10
     * @Param
     */
    public ResponseResult syncinUserDelete(SysUser sysUser, String token) {
        try {
            Long tenantId = getTenantId(token);
            apiContext.setTenantId(tenantId);
            List<Integer> list = getMineId(token);
            log.info("sysUser==" + sysUser + "; list===" + list);
            for (Integer mineId : list) {
                SysUserRedisModel sysUserRedisModel = getSysUserRedisModel(sysUser);
                redisToDelete(1, "sys_user", sysUserRedisModel, mineId);
            }
        } catch (Exception e) {
            return new ResponseResult(FAIL, "同步缓存失败!");
        }
        return new ResponseResult(SUCCESS);
    }

    public Map<String, List> groupList(List list) {
        int listSize = list.size();
        int toIndex = 3000;
        Map<String, List> map = new HashMap(); //用map存起来新的分组后数据
        int keyToken = 0;
        for (int i = 0; i < list.size(); i += 3000) {
            if (i + 3000 > listSize) {//作用为toIndex最后没有100条数据则剩余几条newList中就装几条
                toIndex = listSize - i;
            }
            List newList = list.subList(i, i + toIndex);
            map.put("keyName" + keyToken, newList);
            keyToken++;
        }
        return map;
    }

    public static Map<String, Object> verify(String token) {
        HashMap<String, Object> reslut = new HashMap();
        if (token == null) {
            reslut.put("username", "匿名");
            reslut.put("roles", "游客");
            reslut.put("tenant_id", "-2");
            reslut.put("mine_id", (Object) null);
            reslut.put("id", "-1");
            return reslut;
        } else {
            try {
                Claims claims = (Claims) Jwts.parser().setSigningKey("test_key".getBytes(StandardCharsets.UTF_8)).parseClaimsJws(token).getBody();
                String username = claims.get("user_name").toString();
                String roles = "";

                try {
                    roles = claims.get("authorities").toString();
                } catch (Exception var8) {
                    System.err.println("==authorities is null=");
                }

                String tenant_id = claims.get("tenant_id").toString();
                Object mine_id = claims.get("mine_id");
                String id = claims.get("id").toString();
                reslut.put("username", username);
                reslut.put("roles", roles);
                reslut.put("tenant_id", tenant_id);
                reslut.put("mine_id", mine_id);
                reslut.put("id", id);
                return reslut;
            } catch (Exception var9) {
                HashMap<String, Object> err = new HashMap();
                err.put("username", "error");
                err.put("roles", var9.getMessage());
                err.put("tenant_id", "-2");
                err.put("mine_id", (Object) null);
                err.put("id", "-1");
                return err;
            }
        }
    }
}
