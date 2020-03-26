package com.xnpool.setting.service;

import com.github.pagehelper.PageInfo;
import com.xnpool.setting.domain.pojo.CustomerSetting;
import com.xnpool.setting.domain.model.CustomerSettingExample;

import java.util.HashMap;

/**
 * @author zly
 * @version 1.0
 * @date 2020/2/10 10:35
 */
public interface CustomerSettingService {


    int deleteByPrimaryKey(Integer id);

    int insert(CustomerSetting record);

    void insertSelective(CustomerSetting record,String token);

    CustomerSetting selectByPrimaryKey(Integer id);

    void updateByPrimaryKeySelective(CustomerSetting record);

    int updateByPrimaryKey(CustomerSetting record);

    void updateById(int id);

    PageInfo<CustomerSettingExample> selectByOther(String keyWord, int pageNum, int pageSize);

    void updateAttestationById(String cusId, int isPass);

    HashMap<Integer,String> selectUserList();

    HashMap<Integer, String> selectCustomerMap();

    HashMap<Integer, String> selectAllUser();

    HashMap<Long, String> selectTenantList(String token);
}










