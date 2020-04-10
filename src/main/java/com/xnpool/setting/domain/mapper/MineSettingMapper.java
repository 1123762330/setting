package com.xnpool.setting.domain.mapper;

import com.xnpool.setting.domain.pojo.MineSetting;import org.apache.ibatis.annotations.Param;import java.util.HashMap;import java.util.List;

/**
 * @author zly
 * @version 1.0
 * @date 2020/3/5 12:53
 */
public interface MineSettingMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(MineSetting record);

    int insertSelective(MineSetting record);

    MineSetting selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MineSetting record);

    int updateByPrimaryKey(MineSetting record);

    int updateById(int id);

    List<MineSetting> selectByOther(@Param("keyWord") String keyWord);

    List<String> selectMineNameList(@Param("id") Integer id);

    List<Integer> selectMineId();
}