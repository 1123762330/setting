package com.xnpool.setting.service;

import com.github.pagehelper.PageInfo;
import com.xnpool.setting.domain.pojo.CustomerSetting;
import com.xnpool.setting.domain.model.CustomerSettingExample;

import java.util.HashMap;
import java.util.HashSet;

/**
 * @author zly
 * @version 1.0
 * @date 2020/2/10 10:35
 */
public interface CustomerSettingService {


    int deleteByPrimaryKey(Integer id);

    int insert(CustomerSetting record);

    int insertSelective(CustomerSetting record,String token);

    CustomerSetting selectByPrimaryKey(Integer id);

    void updateByPrimaryKeySelective(CustomerSetting record);

    int updateByPrimaryKey(CustomerSetting record);

    void updateById(int id);

    Object selectByOther(String username,String agreementName,String groupName, int pageNum, int pageSize,Integer authorize);

    void updateAttestationById(String cusId, int isPass);

    HashMap<Integer, String> selectUserList();

    HashMap<Integer, String> selectCustomerMap();

    HashMap<Integer, String> selectAllUser();

    HashMap<Long, HashMap> selectTenantList(String token);

    void deleteAuthority(String tenantId, String token);

    int insertSelective(CustomerSetting record);

    String authorizeToken(String userId);

    HashMap<String, HashSet<String>> selectCustomDropList(Integer authorize);
}











