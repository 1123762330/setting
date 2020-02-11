package com.xnpool.setting.service;

import com.github.pagehelper.PageInfo;
import com.xnpool.setting.domain.pojo.ElectricityMeterSetting;

/**
 * @author zly
 * @version 1.0
 * @date 2020/2/7 15:38
 */
public interface ElectricityMeterSettingService {


    int deleteByPrimaryKey(Integer id);

    int insert(ElectricityMeterSetting record);

    int insertSelective(ElectricityMeterSetting record);

    ElectricityMeterSetting selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ElectricityMeterSetting record);

    int updateByPrimaryKey(ElectricityMeterSetting record);

    void updateById(int id);

    PageInfo<ElectricityMeterSetting> selectByOther(String keyWord, int pageNum, int pageSize);
}




