package com.xnpool.setting.service;

import com.github.pagehelper.PageInfo;
import com.xnpool.setting.domain.pojo.FeeSetting;

import java.util.HashMap;
import java.util.List;

/**
 * @author zly
 * @version 1.0
 * @date 2020/2/6 16:05
 */
public interface FeeSettingService {


    int deleteByPrimaryKey(Integer id);

    int insert(FeeSetting record);

    int insertSelective(FeeSetting record);

    FeeSetting selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(FeeSetting record);

    int updateByPrimaryKey(FeeSetting record);

    int updateById(int id);

    PageInfo<FeeSetting> selectByOther(String keyWord, int pageNum, int pageSize);

    HashMap<Integer,String> selectFeeMap();
}

