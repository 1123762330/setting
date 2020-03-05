package com.xnpool.setting.service;

import com.github.pagehelper.PageInfo;
import com.xnpool.setting.domain.pojo.WorkerbrandSetting;

import java.util.HashMap;
import java.util.List;

/**
 * @author zly
 * @version 1.0
 * @date 2020/2/6 13:22
 */
public interface WorkerbrandSettingService {


    int deleteByPrimaryKey(Integer id);

    int insert(WorkerbrandSetting record);

    int insertSelective(WorkerbrandSetting record);

    WorkerbrandSetting selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(WorkerbrandSetting record);

    int updateByPrimaryKey(WorkerbrandSetting record);

    int updateById(int id);

    PageInfo<WorkerbrandSetting> selectByOther(String keyWord, int pageNum, int pageSize);

    HashMap<Integer, String> selectWorkerbrandMap();
}

