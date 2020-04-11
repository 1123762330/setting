package com.xnpool.setting.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xnpool.setting.common.BaseController;
import com.xnpool.setting.domain.mapper.WorkerbrandSettingMapper;
import com.xnpool.setting.utils.JedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author zly
 * @version 1.0
 * @date 2020/3/13 11:16
 */
@Slf4j
@Service
public class UserWebService extends BaseController {
    @Autowired
    private JedisUtil jedisUtil;

    @Autowired
    private WorkerbrandSettingMapper workerbrandSettingMapper;

    /**
     * @return
     * @Description 矿机算力曲线图
     * @Author zly
     * @Date 15:05 2020/3/13
     * @Param
     */
    public Map<Object, Object> getWorkerHashByDay(Integer algorithmId, String token,Long tenantId) {
        //通过算法id去查询矿机品牌
        List<String> list = workerbrandSettingMapper.selectBrandNameByAlgorithmId(algorithmId);
        Set<String> totalTypeSet = new HashSet<>();
        for (String type : list) {
            totalTypeSet.add(URLEncoder.encode(type));
        }
        Integer userId = getUserId(token);
        //先生成一个96个点的数据map,然后再去请求其他的数据集合,进行累加
        HashMap<Object, Object> middleMap = qiegeMin(15);
        HashMap<String, Date> stringDateHashMap = nowTimeAfter15min();
        Date startDate = stringDateHashMap.get("startDate");
        Date endDate = stringDateHashMap.get("endDate");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String startTime = sdf.format(startDate);
        String endTime = sdf.format(endDate);
        log.info("开始时间==" + startTime + " 结束时间==" + endTime);
        //遍历其他的矿机类型键做合并
        //最终的矿机类型交集
        Set<String> resultSetForStartTime = new HashSet<>();
        String startTimekey = HASHRATE_DATA +userId+":"+tenantId.toString()+ ":" + startTime + ":";
        Set<String> bigKeySet = jedisUtil.scan(startTimekey + "*");
        HashSet<String> startTimetypeSet = new HashSet<>();
        for (String bigKey : bigKeySet) {
            String type = bigKey.substring(startTimekey.length());
            startTimetypeSet.add(type);
        }
        resultSetForStartTime.addAll(totalTypeSet);
        resultSetForStartTime.retainAll(startTimetypeSet);
        //log.info(startTime+"缓存和数据库的共同type:"+resultSetForStartTime);
        for (String type : resultSetForStartTime) {
            String bigKey = ( startTimekey+ type);
            Boolean result = jedisUtil.exists(bigKey);
            if (result) {
                log.info(startTime + "大键==" + bigKey);
                //取出24小时数据
                Map<String, String> workerAvgHash = jedisUtil.hgetall(bigKey);
                for (Map.Entry<String, String> entry : workerAvgHash.entrySet()) {
                    String value = workerAvgHash.get(entry.getKey());
                    //然后两个value相加
                    if (!StringUtils.isEmpty(value)) {
                        Object valueObj = middleMap.get(entry.getKey());
                        if (!StringUtils.isEmpty(valueObj)) {
                            String firstMap_Value = String.valueOf(valueObj);
                            BigDecimal bignum1 = new BigDecimal(firstMap_Value);
                            BigDecimal bignum2 = new BigDecimal(value);
                            BigDecimal total = bignum1.add(bignum2);
                            middleMap.put(entry.getKey(), total.toPlainString());
                        }else {
                            BigDecimal total = new BigDecimal(value);
                            middleMap.put(entry.getKey(), total.toPlainString());
                        }
                    }
                }
            }
        }

        Set<String> resultSetForEndTime = new HashSet<>();
        Set<String> EndTimeTypeSet = new HashSet<>();
        String endTimekey = HASHRATE_DATA +userId+":"+tenantId+ ":" + endTime + ":";
        Set<String> endTimekeySet = jedisUtil.scan(endTimekey + "*");
        for (String bigKey : endTimekeySet) {
            String type = bigKey.substring(endTimekey.length());
            EndTimeTypeSet.add(type);
        }
        resultSetForEndTime.addAll(totalTypeSet);
        resultSetForEndTime.retainAll(EndTimeTypeSet);
        //log.info(endTime+"缓存和数据库的共同type:"+resultSetForEndTime);

        for (String type : resultSetForEndTime) {
            String bigKey = ( endTimekey+ type);
            Boolean result = jedisUtil.exists(bigKey);
            if (result) {
                log.info(endTime + "大键==" + bigKey);
                //取出24小时数据
                Map<String, String> workerAvgHash = jedisUtil.hgetall(bigKey);
                for (Map.Entry<String, String> entry : workerAvgHash.entrySet()) {
                    String value = workerAvgHash.get(entry.getKey());
                    //然后两个value相加
                    if (!StringUtils.isEmpty(value)) {
                        Object valueObj = middleMap.get(entry.getKey());
                        if (!StringUtils.isEmpty(valueObj)) {
                            String firstMap_Value = String.valueOf(valueObj);
                            BigDecimal bignum1 = new BigDecimal(firstMap_Value);
                            BigDecimal bignum2 = new BigDecimal(value);
                            BigDecimal total = bignum1.add(bignum2);
                            middleMap.put(entry.getKey(), total.toPlainString());
                        }else {
                            BigDecimal total = new BigDecimal(value);
                            middleMap.put(entry.getKey(), total.toPlainString());
                        }

                    }
                }
            }
        }

        //将map转成treeMap排序,然后键和values直接转
        Map<Object, Object> sortedMap = new TreeMap<>(middleMap);
        return sortedMap;
    }

    /**
     * @return
     * @Description 矿机数量折线图
     * @Author zly
     * @Date 15:03 2020/3/13
     * @Param
     */
    public Map<Object, Object> getWorkerTotalByDay(String token, Integer algorithmId,Long tenantId) {
        //通过算法id去查询矿机品牌
        List<String> list = workerbrandSettingMapper.selectBrandNameByAlgorithmId(algorithmId);
        Set<String> totalTypeSet = new HashSet<>();
        for (String type : list) {
            totalTypeSet.add(URLEncoder.encode(type));
        }
        Integer userId = getUserId(token);
        //先生成一个96个点的数据map,然后再去请求其他的数据集合,进行累加
        HashMap<Object, Object> middleMap = qiegeMin(15);
        HashMap<String, Date> stringDateHashMap = nowTimeAfter15min();
        Date startDate = stringDateHashMap.get("startDate");
        Date endDate = stringDateHashMap.get("endDate");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String startTime = sdf.format(startDate);
        String endTime = sdf.format(endDate);
        log.info("开始时间==" + startTime + " 结束时间==" + endTime);
        //遍历其他的矿机类型键做合并
        //最终的矿机类型交集
        Set<String> resultSetForStartTime = new HashSet<>();
        HashSet<String> startTimetypeSet = new HashSet<>();
        String startTimekey = ON_LINE_DATA + userId + ":" + tenantId + ":" + startTime + ":";
        Set<String> bigKeySet = jedisUtil.scan(startTimekey + "*");
        for (String bigKey : bigKeySet) {
            String type = bigKey.substring(startTimekey.length());
            startTimetypeSet.add(type);
        }
        resultSetForStartTime.addAll(totalTypeSet);
        resultSetForStartTime.retainAll(startTimetypeSet);
        //log.info(startTime+"缓存和数据库的共同type:"+resultSetForStartTime);

        for (String type : resultSetForStartTime) {
            String bigKey = (startTimekey + type);
            Boolean result = jedisUtil.exists(bigKey);
            if (result) {
                log.info(startTime + "大键==" + bigKey);
                //取出24小时数据
                Map<String, String> workerAvgHash = jedisUtil.hgetall(bigKey);
                for (Map.Entry<String, String> entry : workerAvgHash.entrySet()) {
                    String value = workerAvgHash.get(entry.getKey());
                    //然后两个value相加
                    if (!StringUtils.isEmpty(value)) {
                        Object valueObj = middleMap.get(entry.getKey());
                        if (!StringUtils.isEmpty(valueObj)){
                            Integer firstMap_Value = Integer.valueOf(valueObj.toString());
                            Integer total = firstMap_Value + Integer.valueOf(value);
                            middleMap.put(entry.getKey(), total);
                        }else {
                            middleMap.put(entry.getKey(), Integer.valueOf(value));
                        }
                    }
                }
            }
        }

        Set<String> resultSetForEndTime = new HashSet<>();
        Set<String> EndTimeTypeSet = new HashSet<>();
        String endTimekey = ON_LINE_DATA + userId + ":" + tenantId + ":" + endTime + ":";
        Set<String> endTimekeySet = jedisUtil.scan(endTimekey + "*");
        for (String bigKey : endTimekeySet) {
            String type = bigKey.substring(endTimekey.length());
            EndTimeTypeSet.add(type);
        }
        resultSetForEndTime.addAll(totalTypeSet);
        resultSetForEndTime.retainAll(EndTimeTypeSet);
        //log.info(endTime+"缓存和数据库的共同type:"+resultSetForEndTime);
        for (String type : resultSetForEndTime) {
            String bigKey = (endTimekey+ type);
            Boolean result = jedisUtil.exists(bigKey);
            if (result) {
                log.info(endTime + "大键==" + bigKey);
                //取出24小时数据
                Map<String, String> workerAvgHash = jedisUtil.hgetall(bigKey);
                for (Map.Entry<String, String> entry : workerAvgHash.entrySet()) {
                    String value = workerAvgHash.get(entry.getKey());
                    //然后两个value相加
                    if (!StringUtils.isEmpty(value)) {
                        Object valueObj = middleMap.get(entry.getKey());
                        if (!StringUtils.isEmpty(valueObj)){
                            Integer firstMap_Value = Integer.valueOf(valueObj.toString());
                            Integer total = firstMap_Value + Integer.valueOf(value);
                            middleMap.put(entry.getKey(), total);
                        }else {
                            middleMap.put(entry.getKey(), Integer.valueOf(value));
                        }
                    }
                }
            }
        }
        //将map转成treeMap排序,然后键和values直接转
        Map<Object, Object> sortedMap = new TreeMap<>(middleMap);
        return sortedMap;
    }

    /**
     * @return
     * @Description 查询用户的总矿机数
     * @Author zly
     * @Date 19:06 2020/3/16
     * @Param
     */
    public HashMap<String, Integer> getWorkerTotal(String token,Long tenantId) {
        //后期从token中获取用户Id
        Integer userId = getUserId(token);
        Boolean totalHexists = jedisUtil.hexists(USERWORKER_TOTAL+tenantId, String.valueOf(userId));
        int total = 0;
        if (totalHexists) {
            String totalStr = jedisUtil.hget(USERWORKER_TOTAL+tenantId, String.valueOf(userId));
            if (!StringUtils.isEmpty(totalStr)) {
                total =Integer.valueOf(totalStr);
            }
            log.info(userId + "用户的redis中取出的矿机个数是" + total);
        }

        Boolean hexists = jedisUtil.hexists(USERWORKER_ONLINE_TOTAL+tenantId, String.valueOf(userId));
        String onLineSize = "0";
        if (hexists) {
            onLineSize = jedisUtil.hget(USERWORKER_ONLINE_TOTAL+tenantId, String.valueOf(userId));
            log.info(userId + "用户的redis中取出的在线矿机总数是" + onLineSize);
        }
        HashMap<String, Integer> resultMap = new HashMap<>();
        resultMap.put("total", total);
        resultMap.put("onLine", Integer.valueOf(onLineSize));
        return resultMap;
    }
}



