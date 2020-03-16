package com.xnpool.setting.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xnpool.setting.common.BaseController;
import com.xnpool.setting.utils.JedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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

    /**
     * @return
     * @Description 矿机算力曲线图
     * @Author zly
     * @Date 15:05 2020/3/13
     * @Param
     */
    public Map<Object, Object> getWorkerHashByDay(String token) {
        int userId = 1;
        Map<Object, Object> resultMap = new HashMap<>();
        //先生成一个96个点的数据map
        HashMap<Object, Object> middleMap = qiegeMin(15);
        String bigKey = (HASHRATE_DATA + userId);
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
    public Map<Object, Object> getWorkerTotalByDay(String token) {
        //后期从token中获取用户Id
        int userId = 1;
        Map<Object, Object> resultMap = new HashMap<>();
        //先生成一个96个点的数据map
        HashMap<Object, Object> middleMap = qiegeMin(15);
        String bigKey = (ON_LINE_DATA + userId);
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
    public Integer getWorkerTotal(String token) {
        //后期从token中获取用户Id
        int userId = 1;
        String hashSet = jedisUtil.hget(USERWORKER_TOTAL, String.valueOf(userId));
        log.info(userId+"用户的redis中取出的矿机数集合是"+hashSet);
        JSONObject jsonObject = JSON.parseObject(hashSet);
        Set<String> keySet = jsonObject.keySet();
        int total=keySet.size();
        return total;
    }
}



