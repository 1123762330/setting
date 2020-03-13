package com.xnpool.setting.service.impl;

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
     * @Description 矿机算力曲线图
     * @Author zly
     * @Date 15:05 2020/3/13
     * @Param
     * @return
     */
    public Map<Object, Object>  getWorkerHashByDay(String token) {
        int userId=0;
        Map<Object, Object> resultMap = new HashMap<>();
        //先生成一个96个点的数据map
        HashMap<Object, Object> middleMap = qiegeMin(15);
        String bigKey = (HASHRATE_DATA+userId);
        Boolean result = jedisUtil.exists(bigKey);
        if (result) {
            //取出24小时数据
            Map<String, String> workerAvgHash = jedisUtil.hgetall(bigKey);
            for (Map.Entry<Object, Object> entry : middleMap.entrySet()) {
                String value = workerAvgHash.get(entry.getKey());
                if (!StringUtils.isEmpty(value)){
                    resultMap.put(entry.getKey(),value);
                }else {
                    resultMap.put(entry.getKey(),"0");
                }
            }
        }
        if (middleMap.size() == 0) {
            return null;
        } else {
            //将map转成treeMap排序,然后键和values直接转
            Map<Object, Object> sortedMap = new TreeMap<>(resultMap);
            return sortedMap;
        }
    }

    /**
     * @Description 矿机数量折线图
     * @Author zly
     * @Date 15:03 2020/3/13
     * @Param
     * @return
     */
    public Map<Object, Object> getWorkerTotalByDay(String token) {
        //后期从token中获取用户Id
        int userId=0;
        Map<Object, Object> resultMap = new HashMap<>();
        //先生成一个96个点的数据map
        HashMap<Object, Object> middleMap = qiegeMin(15);
        String bigKey = (ON_LINE_DATA+userId);
        Boolean result = jedisUtil.exists(bigKey);
        if (result) {
            //取出24小时数据
            Map<String, String> workerAvgHash = jedisUtil.hgetall(bigKey);
            for (Map.Entry<Object, Object> entry : middleMap.entrySet()) {
                String value = workerAvgHash.get(entry.getKey());
                if (!StringUtils.isEmpty(value)){
                    resultMap.put(entry.getKey(),value);
                }else {
                    resultMap.put(entry.getKey(),"0");
                }
            }
        }
        if (middleMap.size() == 0) {
            return null;
        } else {
            //将map转成treeMap排序,然后键和values直接转
            Map<Object, Object> sortedMap = new TreeMap<>(resultMap);
            return sortedMap;
        }
    }
}



