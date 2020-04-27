package com.xnpool.setting.service;

import com.xnpool.setting.domain.pojo.CustomerSetting;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.HashMap;
import java.util.HashSet;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zly
 * @since 2020-04-27
 */
public interface CustomerSettingService extends IService<CustomerSetting> {

    Integer insertSelective(CustomerSetting customerSetting, String token);

    Integer updateByPrimaryKeySelective(CustomerSetting customerSetting);

    Integer deleteById(int id);

    Object selectByOther(String username, String agreementName, String groupName, int pageNum, int pageSize, Integer authorize);

    void updateAttestationById(String cusId, int isPass);

    HashMap<Integer, String> selectUserList();

    HashMap<Integer, String> selectCustomerMap();

    HashMap<Integer, String> selectAllUser();

    HashMap<Long, HashMap> selectTenantList(String token);

    void deleteAuthority(String tenantId, String token);

    String authorizeToken(String userId);

    HashMap<String, HashSet<String>> selectCustomDropList(Integer authorize);
}
