package com.xnpool.setting.service;

import com.github.pagehelper.PageInfo;
import com.xnpool.setting.domain.pojo.PowerSetting;

import java.util.List;

/**
 * @author zly
 * @version 1.0
 * @date 2020/2/6 14:13
 */
public interface PowerSettingService {


    int deleteByPrimaryKey(Integer id);

    int insert(PowerSetting record);

    int insertSelective(PowerSetting record);

    PowerSetting selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PowerSetting record);

    int updateByPrimaryKey(PowerSetting record);

    int updateById(int id);

    PageInfo<PowerSetting> selectByOther(String keyWord, int pageNum, int pageSize);
}
