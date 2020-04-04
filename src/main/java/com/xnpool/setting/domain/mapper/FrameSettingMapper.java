package com.xnpool.setting.domain.mapper;

import com.xnpool.setting.domain.pojo.FrameSetting;import com.xnpool.setting.domain.model.FrameSettingExample;import org.apache.ibatis.annotations.Param;import java.util.HashMap;import java.util.List;

/**
 * @author zly
 * @version 1.0
 * @date 2020/3/5 13:03
 */
public interface FrameSettingMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FrameSetting record);

    int insertSelective(FrameSetting record);

    FrameSetting selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(FrameSetting record);

    int updateByPrimaryKey(FrameSetting record);

    int updateById(int id);

    List<FrameSettingExample> selectByOther(@Param("keyWord") String keyWord);

    List<HashMap> selectFrameNameByFactoryId(Integer factoryId);

    List<HashMap> selectMineFactoryAndFrame(@Param("factoryId")Integer factoryId);
    Integer selectMineId(@Param("id")Integer id);

    List<String> selectFrameNameList(@Param("factoryId")Integer factoryId,@Param("id")Integer id);

    Integer equalsFrameName(@Param("frameName")String frameStr,@Param("factoryId") Integer factoryId, @Param("mineId")Integer mineId);
}