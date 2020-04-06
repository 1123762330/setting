package com.xnpool.setting.domain.mapper;

import com.xnpool.setting.domain.pojo.FeeSetting;import org.apache.ibatis.annotations.Param;import java.util.List;

/**
 * @author zly
 * @version 1.0
 * @date 2020/4/6 15:36
 */
public interface FeeSettingMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FeeSetting record);

    int insertSelective(FeeSetting record);

    FeeSetting selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(FeeSetting record);

    int updateByPrimaryKey(FeeSetting record);

    List<FeeSetting> selectByOther(@Param("keyWord") String keyWord);

    int updateById(int id);

    List<String> selectFeeNameList(@Param("id") Integer id);
}