package com.xnpool.setting.domain.mapper;

import com.xnpool.setting.domain.pojo.CustomerSetting;import com.xnpool.setting.domain.pojo.CustomerSettingExample;import org.apache.ibatis.annotations.Param;import java.util.List;

/**
 * @author zly
 * @version 1.0
 * @date 2020/2/11 10:56
 */
public interface CustomerSettingMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CustomerSetting record);

    int insertSelective(CustomerSetting record);

    CustomerSetting selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CustomerSetting record);

    int updateByPrimaryKey(CustomerSetting record);

    int updateById(int id);

    List<CustomerSettingExample> selectByOther(@Param("keyWord") String keyWord, @Param("managerUserId") int managerUserId);

    void updateAttestationById(@Param("userId") int userId, @Param("isPass") int isPass);
}