package com.xnpool.setting.domain.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xnpool.setting.domain.model.CustomerSettingExample;
import com.xnpool.setting.domain.pojo.CustomerSetting;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zly
 * @since 2020-04-27
 */
public interface CustomerSettingMapper extends BaseMapper<CustomerSetting> {

    List<HashMap> selectTenantList(Integer userId);

    Integer deleteByKeyId(int id);

    List<CustomerSettingExample> selectByOther(Page<CustomerSettingExample> page);

    void updateAttestationById(@Param("list") List<String> cusIdlist,@Param("isPass") int isPass);

    List<HashMap> selectUserList();

    List<HashMap> selectUserMap();

    void deleteAuthority(@Param("list") List<Long> list, @Param("userId") Integer userId);

    Integer selectAuthorizedToYes(Integer valueOf);
}
