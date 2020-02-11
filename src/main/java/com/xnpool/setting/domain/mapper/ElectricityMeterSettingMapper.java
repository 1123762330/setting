package com.xnpool.setting.domain.mapper;

import com.xnpool.setting.domain.pojo.ElectricityMeterSetting;import org.apache.ibatis.annotations.Param;import java.util.List;

/**
 * @author zly
 * @version 1.0
 * @date 2020/2/11 10:36
 */
public interface ElectricityMeterSettingMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ElectricityMeterSetting record);

    int insertSelective(ElectricityMeterSetting record);

    ElectricityMeterSetting selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ElectricityMeterSetting record);

    int updateByPrimaryKey(ElectricityMeterSetting record);

    void updateById(int id);

    List<ElectricityMeterSetting> selectByOther(@Param("keyWord") String keyWord);
}