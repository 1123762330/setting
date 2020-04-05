package com.xnpool.setting.domain.mapper;

import com.xnpool.setting.domain.model.IpSettingExample;
import com.xnpool.setting.domain.pojo.IpSetting;import org.apache.ibatis.annotations.Param;import java.util.List;

/**
 * @author zly
 * @version 1.0
 * @date 2020/3/5 13:30
 */
public interface IpSettingMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(IpSetting record);

    int insertSelective(IpSetting record);

    IpSetting selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(IpSetting record);

    int updateByPrimaryKey(IpSetting record);

    int updateById(int id);

    //开启这个注解,就相当于这个方法不增加多租户信息
    //@SqlParser(filter = true)
    List<IpSettingExample> selectByOther(@Param("keyWord") String keyWord,@Param("mineId")Integer mineId);

    Integer selectMineId(@Param("id")Integer id);
    List<String> selectNameList(@Param("id")Integer id);
}