package com.xnpool.setting.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;

/**
 * 生成全局唯一ID
 * @author zly
 * @version 1.0
 * @date 2020/2/20 13:40
 */
@Component
@Slf4j
public class PrimaryKeyUtils {
    @Autowired
    private JedisUtil jedisUtil;

    /**
     * 获取年的后两位加上一年多少天+当前小时数作为前缀
     * @param date
     * @return
     */
    public String getOrderIdPrefix(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        //补两位,因为一年最多三位数
        String monthFormat = String.format("%1$02d", month+1);
        //补两位，因为日最多两位数
        String dayFormat = String.format("%1$02d", day);
        //补两位，因为小时最多两位数
        String hourFormat = String.format("%1$02d", hour);
        return year + monthFormat + dayFormat+hourFormat;
    }

    /**
     * 生成订单
     * @param prefix
     * @return
     */
    public Long orderId(String prefix) {
        String prefix_month = prefix.substring(0, 8);
        String key = "GLOBAL_ID_" + prefix_month;
        String orderId = null;
        try {
            Long increment = jedisUtil.incrBy(key,1L);
            jedisUtil.expire(key,3*24*3600);
            //往前补6位
            orderId=prefix+String.format("%1$06d",increment);
        } catch (Exception e) {
            log.error("生成全局唯一id失败"+e.getMessage());
        }
        return Long.valueOf(orderId);
    }
}
