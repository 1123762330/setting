package com.xnpool.setting.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.xnpool.logaop.service.exception.CheckException;
import com.xnpool.setting.common.BaseController;
import com.xnpool.setting.domain.mapper.WorkerbrandSettingMapper;
import com.xnpool.setting.utils.JedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.regex.Pattern;

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
    public Map<Object, Object> getWorkerHashByDay(Integer algorithmId,String token) {
        HashMap<String, Object> tokenData = getTokenData(token);
        Integer userId=null;
        //通过算法id去查询矿机品牌
        List<String> list = workerbrandSettingMapper.selectBrandNameByAlgorithmId(algorithmId);
        if (tokenData!=null){
            userId = Integer.valueOf(tokenData.get("userId").toString());
        }else {
            throw new CheckException("校验token失败!");
        }
        Map<Object, Object> resultMap = new HashMap<>();
        //先生成一个96个点的数据map,然后再去请求其他的数据集合,进行累加
        String firstType = list.get(0);
        HashMap<Object, Object> middleMap = qiegeMin(15);
        String bigKey = (HASHRATE_DATA + userId+":"+firstType);
        Boolean result = jedisUtil.exists(bigKey);
        String REGEX ="[^(0-9).]";
        String unit="";
        if (result) {
            //取出24小时数据
            Map<String, String> workerAvgHash = jedisUtil.hgetall(bigKey);
            for (Map.Entry<Object, Object> entry : middleMap.entrySet()) {
                String value = workerAvgHash.get(entry.getKey());
                if (!StringUtils.isEmpty(value)) {
                    resultMap.put(entry.getKey(), value);
                } else {
                    resultMap.put(entry.getKey(), "0");
                }
            }
        }else {
            resultMap=middleMap;
        }
        //遍历其他的矿机类型键做合并
        for (int i = 1; i < list.size(); i++) {
            String workerType = list.get(i);
            String bigKey2 = (HASHRATE_DATA + userId+":"+workerType);
            Boolean result2 = jedisUtil.exists(bigKey2);
            if (result2) {
                //取出24小时数据
                Map<String, String> workerAvgHash = jedisUtil.hgetall(bigKey2);
                for (Map.Entry<String, String> entry : workerAvgHash.entrySet()) {
                    String value = workerAvgHash.get(entry.getKey());
                    String firstMap_Value = String.valueOf(resultMap.get(entry.getKey()));
                    String valueStr = Pattern.compile(REGEX).matcher(value).replaceAll("");
                    unit = value.substring(valueStr.length());
                    //然后两个value相加
                    String valueStr_trim = Pattern.compile(REGEX).matcher(value).replaceAll("").trim();
                    String firstMap_ValueStr = Pattern.compile(REGEX).matcher(firstMap_Value).replaceAll("").trim();
                    Double total = Double.valueOf(valueStr_trim) + Double.valueOf(firstMap_ValueStr);
                    String format = String.format("%.2f",total);
                    resultMap.put(entry.getKey(),format+unit);
                }
            }
        }
        //将map转成treeMap排序,然后键和values直接转
        Map<Object, Object> sortedMap = new TreeMap<>(resultMap);
        return sortedMap;
    }

    /**
     * @return
     * @Description 矿机数量折线图
     * @Author zly
     * @Date 15:03 2020/3/13
     * @Param
     */
    public Map<Object, Object> getWorkerTotalByDay(String token,Integer algorithmId) {
        //后期从token中获取用户Id
        HashMap<String, Object> tokenData = getTokenData(token);
        Integer userId=null;
        if (tokenData!=null){
            userId = Integer.valueOf(tokenData.get("userId").toString());
        }else {
            throw new CheckException("校验token失败!");
        }
        List<String> list = workerbrandSettingMapper.selectBrandNameByAlgorithmId(algorithmId);
        Map<Object, Object> resultMap = new HashMap<>();
        //先生成一个96个点的数据map
        HashMap<Object, Object> middleMap = qiegeMin(15);
        for (String workerType : list) {
            String bigKey = (ON_LINE_DATA + userId+":"+workerType);
            Boolean result = jedisUtil.exists(bigKey);
            if (result) {
                //取出24小时数据
                Map<String, String> workerAvgHash = jedisUtil.hgetall(bigKey);
                for (Map.Entry<Object, Object> entry : middleMap.entrySet()) {
                    String value = workerAvgHash.get(entry.getKey());
                    if (!StringUtils.isEmpty(value)) {
                        resultMap.put(entry.getKey(), value);
                    } else {
                        resultMap.put(entry.getKey(), "0");
                    }
                }
            }else {
                resultMap=middleMap;
            }
        }
        //将map转成treeMap排序,然后键和values直接转
        Map<Object, Object> sortedMap = new TreeMap<>(resultMap);
        return sortedMap;

    }

    /**
     * @Description 查询用户的总矿机数
     * @Author zly
     * @Date 19:06 2020/3/16
     * @Param
     * @return
     */
    public HashMap<String, Integer> getWorkerTotal(String token) {
        //后期从token中获取用户Id
        HashMap<String, Object> tokenData = getTokenData(token);
        Integer userId=null;
        if (tokenData!=null){
            userId = Integer.valueOf(tokenData.get("userId").toString());
        }else {
            throw new CheckException("校验token失败!");
        }
        Boolean totalHexists = jedisUtil.hexists(USERWORKER_TOTAL, String.valueOf(userId));
        int total=0;
        if (totalHexists){
            String hashSetStr = jedisUtil.hget(USERWORKER_TOTAL, String.valueOf(userId));
            log.info(userId+"用户的redis中取出的矿机数集合是"+hashSetStr);
            HashSet hashSet = new Gson().fromJson(hashSetStr, HashSet.class);
            total=hashSet.size();
        }

        Boolean hexists = jedisUtil.hexists(USERWORKER_ONLINE_TOTAL, String.valueOf(userId));
        String onLineSize="0";
        if (hexists){
            onLineSize = jedisUtil.hget(USERWORKER_ONLINE_TOTAL,String.valueOf(userId) );
            log.info(userId+"用户的redis中取出的在线矿机总数是"+onLineSize);
        }
        HashMap<String, Integer> resultMap = new HashMap<>();
        resultMap.put("total",total);
        resultMap.put("onLine",Integer.valueOf(onLineSize));
        return resultMap;
    }
}



