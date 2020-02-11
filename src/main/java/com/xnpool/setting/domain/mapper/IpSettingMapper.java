package com.xnpool.setting.domain.mapper;

import com.xnpool.setting.domain.pojo.IpSetting;import org.apache.ibatis.annotations.Param;import java.util.List;

/**
 * @author zly
 * @version 1.0
 * @date 2020/2/6 13:21
 */
public interface IpSettingMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(IpSetting record);

    int insertSelective(IpSetting record);

    IpSetting selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(IpSetting record);

    int updateByPrimaryKey(IpSetting record);

    int updateById(int id);

    List<IpSetting> selectByOther(@Param("keyWord") String keyWord);
}