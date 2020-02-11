package com.xnpool.setting.domain.mapper;

import com.xnpool.setting.domain.pojo.PowerSetting;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author  zly
 * @date  2020/2/6 14:13
 * @version 1.0
 */
public interface PowerSettingMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PowerSetting record);

    int insertSelective(PowerSetting record);

    PowerSetting selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PowerSetting record);

    int updateByPrimaryKey(PowerSetting record);

    int updateById(int id);

    List<PowerSetting> selectByOther(@Param("keyWord") String keyWord);
}