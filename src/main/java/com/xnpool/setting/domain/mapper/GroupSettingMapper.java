package com.xnpool.setting.domain.mapper;

import com.xnpool.setting.domain.pojo.GroupSetting;import com.xnpool.setting.domain.pojo.GroupSettingExample;import org.apache.ibatis.annotations.Param;import java.util.List;

/**
 * @author zly
 * @version 1.0
 * @date 2020/2/11 9:56
 */
public interface GroupSettingMapper {
    int deleteByPrimaryKey(Integer groupid);

    int insert(GroupSetting record);

    int insertSelective(GroupSetting record);

    GroupSetting selectByPrimaryKey(Integer groupid);

    int updateByPrimaryKeySelective(GroupSetting record);

    int updateByPrimaryKey(GroupSetting record);

    int updateById(int id);

    List<GroupSettingExample> selectByOther(@Param("keyWord") String keyWord);

    List<GroupSetting> selectGroupMap();
}