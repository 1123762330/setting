package com.xnpool.setting.domain.mapper;

import com.xnpool.setting.domain.model.CustomerSettingExample;import com.xnpool.setting.domain.pojo.CustomerSetting;import org.apache.ibatis.annotations.Param;import java.util.HashMap;import java.util.List;

/**
 * @author zly
 * @version 1.0
 * @date 2020/3/26 23:01
 */
public interface CustomerSettingMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CustomerSetting record);

    int insertSelective(CustomerSetting record);

    CustomerSetting selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CustomerSetting record);

    int updateByPrimaryKey(CustomerSetting record);

    int updateById(int id);

    List<CustomerSettingExample> selectByOther(@Param("keyWord") String keyWord, @Param("managerUserId") Integer managerUserId);

    void updateAttestationById(@Param("list") List<String> list, @Param("isPass") int isPass);

    List<HashMap> selectUserList(@Param("tenantId")Long tenantId);

    List<HashMap> selectUserMap();

    List<HashMap> selectTenantList(Integer userId);

    List<HashMap> selectUserRole();

    void deleteAuthority(@Param("list") List<Long> list, @Param("userId") Integer userId);

    Integer selectAuthorizedToYes(@Param("userId") Integer userId);
}