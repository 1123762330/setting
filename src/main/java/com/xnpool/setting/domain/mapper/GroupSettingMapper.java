package com.xnpool.setting.domain.mapper;

import com.xnpool.setting.domain.pojo.GroupSetting;import com.xnpool.setting.domain.pojo.GroupSettingExample;import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;

/**
 * @author zly
 * @version 1.0
 * @date 2020/3/5 19:50
 */
public interface GroupSettingMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(GroupSetting record);

    int insertSelective(GroupSetting record);

    GroupSetting selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(GroupSetting record);

    int updateByPrimaryKey(GroupSetting record);

    int updateById(int id);

    List<GroupSettingExample> selectByOther(@Param("keyWord") String keyWord);

    List<GroupSetting> selectGroupMap();

    List<HashMap> selectGroupAndIp();
}