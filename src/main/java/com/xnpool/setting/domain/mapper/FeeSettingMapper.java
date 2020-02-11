package com.xnpool.setting.domain.mapper;

import com.xnpool.setting.domain.pojo.FeeSetting;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author  zly
 * @date  2020/2/6 16:05
 * @version 1.0
 */
public interface FeeSettingMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FeeSetting record);

    int insertSelective(FeeSetting record);

    FeeSetting selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(FeeSetting record);

    int updateByPrimaryKey(FeeSetting record);

    int updateById(int id);

    List<FeeSetting> selectByOther(@Param("keyWord") String keyWord);
}