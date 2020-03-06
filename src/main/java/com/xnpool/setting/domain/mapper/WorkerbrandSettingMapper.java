package com.xnpool.setting.domain.mapper;

import com.xnpool.setting.domain.pojo.WorkerbrandSetting;import org.apache.ibatis.annotations.Param;import java.util.List;

/**
 * @author zly
 * @version 1.0
 * @date 2020/3/6 11:36
 */
public interface WorkerbrandSettingMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(WorkerbrandSetting record);

    int insertSelective(WorkerbrandSetting record);

    WorkerbrandSetting selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(WorkerbrandSetting record);

    int updateByPrimaryKey(WorkerbrandSetting record);

    int updateById(int id);

    List<WorkerbrandSetting> selectByOther(@Param("keyWord") String keyWord);
}