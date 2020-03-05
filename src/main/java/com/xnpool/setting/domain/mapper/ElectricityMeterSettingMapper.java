package com.xnpool.setting.domain.mapper;

import com.xnpool.setting.domain.pojo.ElectricityMeterSetting;import com.xnpool.setting.domain.pojo.ElectricityMeterSettingExample;import org.apache.ibatis.annotations.Param;import java.util.List;

/**
 * @author zly
 * @version 1.0
 * @date 2020/3/5 20:52
 */
public interface ElectricityMeterSettingMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ElectricityMeterSetting record);

    int insertSelective(ElectricityMeterSetting record);

    ElectricityMeterSetting selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ElectricityMeterSetting record);

    int updateByPrimaryKey(ElectricityMeterSetting record);

    void updateById(int id);

    List<ElectricityMeterSettingExample> selectByOther(@Param("keyWord") String keyWord);
}