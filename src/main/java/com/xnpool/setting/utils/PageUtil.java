package com.xnpool.setting.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 自定义分页工具类
 *
 * @Author: zly
 * @Date: 2019/7/31 10:07
 */
public class PageUtil {

    /**
     * 开始分页
     *
     * @param list
     * @param pageNum  页码
     * @param pageSize 每页多少条数据
     * @return
     */
    public static HashMap<String, Object> startPage(List list, Integer pageNum, Integer pageSize) {
        if (list == null) {
            return null;
        }
        if (list.size() == 0) {
            return null;
        }

        Integer count = list.size(); //记录总数

        Integer pageCount = 0; //页数

        if (count % pageSize == 0) {
            pageCount = count / pageSize;
        } else {
            pageCount = count / pageSize + 1;
        }

        int fromIndex = (pageNum - 1) * pageSize;

        HashMap<String, Object> resultMap = new HashMap<>();

        if (fromIndex >= count) {
            resultMap.put("total", count);
            resultMap.put("pages", pageCount);
            resultMap.put("list", null);
            resultMap.put("pageNum", pageNum);
            resultMap.put("pageSize", pageSize);
            return resultMap;
        }


        int toIndex = pageNum * pageSize;
        if (toIndex >= count) {
            toIndex = count;
        }

        //List pageList = list.subList(fromIndex, toIndex);
        List pageList = new ArrayList<>(list.subList(fromIndex, toIndex));
        resultMap.put("total", count);
        resultMap.put("pages", pageCount);
        resultMap.put("list", pageList);
        resultMap.put("pageNum", pageNum);
        resultMap.put("pageSize", pageSize);
        return resultMap;

    }




}