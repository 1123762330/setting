package com.xnpool.setting.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xnpool.logaop.service.exception.CheckException;
import com.xnpool.setting.common.BaseController;
import com.xnpool.setting.domain.model.CustomerSettingExample;
import com.xnpool.setting.service.AgreementSettingService;
import com.xnpool.setting.service.GroupSettingService;
import com.xnpool.setting.utils.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import com.xnpool.setting.domain.mapper.CustomerSettingMapper;
import com.xnpool.setting.domain.pojo.CustomerSetting;
import com.xnpool.setting.service.CustomerSettingService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * @author zly
 * @version 1.0
 * @date 2020/2/10 10:35
 */
@Service
@Slf4j
public class CustomerSettingServiceImpl extends BaseController implements CustomerSettingService {

    @Resource
    private CustomerSettingMapper customerSettingMapper;

    @Autowired
    private AgreementSettingService agreementSettingService;

    @Autowired
    private GroupSettingService groupSettingService;

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return customerSettingMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(CustomerSetting record) {
        return customerSettingMapper.insert(record);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertSelective(CustomerSetting record,String token) {
        JSONObject jsonObject = TokenUtil.verify(token);
        log.info("解析的token:"+jsonObject);
        Integer success = jsonObject.getInteger("success");
        if (success==200) {
            JSONObject data = jsonObject.getJSONObject("data");
            Integer userId = data.getInteger("id");
            record.setUserId(userId);
        }
        int rows = customerSettingMapper.insertSelective(record);
        //record.setCreateTime(new Date());
        //redisToInsert(rows,"customer_setting",record,null);
    }

    @Override
    public CustomerSetting selectByPrimaryKey(Integer id) {
        return customerSettingMapper.selectByPrimaryKey(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateByPrimaryKeySelective(CustomerSetting record) {
        int rows = customerSettingMapper.updateByPrimaryKeySelective(record);
        record.setUpdateTime(new Date());
        //redisToUpdate(rows,"customer_setting",record,null);
    }

    @Override
    public int updateByPrimaryKey(CustomerSetting record) {
        return customerSettingMapper.updateByPrimaryKey(record);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateById(int id) {
        int rows = customerSettingMapper.updateById(id);
        CustomerSetting record = new CustomerSetting();
        record.setUpdateTime(new Date());
        record.setId(id);
        //redisToDelete(rows,"customer_setting",record,null);
    }

    @Override
    public PageInfo<CustomerSettingExample> selectByOther(String keyWord, int pageNum, int pageSize) {
        if (!StringUtils.isEmpty(keyWord)) {
            keyWord = "%" + keyWord + "%";
        }
        //这里需要解析token,然后拿到当前管理的Id,查询属于他的客户列表
        int managerUserId = 1;
        PageHelper.startPage(pageNum, pageSize);
        //这里后面合并需要做关联查询,查询客户的一些基本信息
        List<CustomerSettingExample> customerSettingExamples = customerSettingMapper.selectByOther(keyWord, managerUserId);
        //解决多个协议ID问题和多个菜单栏ID问题,先去查出相应的map集合,然后遍历该实体类进行拼接封装
        //log.info("客户设置列表" + customerSettingExamples);
        customerSettingExamples.forEach(customerSettingExample -> {
            String agreementid = customerSettingExample.getAgreementId();
            if (agreementid.contains(",")) {
                HashMap<Integer, String> agreementMap = agreementSettingService.selectAgreementMap();
                //多个协议ID,从协议IDMap集合里面取出相应的值,重新set进属性里面
                String[] split = agreementid.split(",");
                StringBuffer agreemmentNameStr = null;
                for (int i = 0; i < split.length; i++) {
                    String agreemmentName = agreementMap.get(Integer.valueOf(split[i]));
                    if (agreemmentNameStr == null) {
                        agreemmentNameStr = new StringBuffer().append(agreemmentName);
                    } else {
                        agreemmentNameStr = agreemmentNameStr.append(",").append(agreemmentName);
                    }
                }
                customerSettingExample.setAgreementName(agreemmentNameStr.toString());

            }
            String groupId = customerSettingExample.getGroupId();
            if (groupId.contains(",")) {
                //多个分组ID,从分组IDMap集合里面取出相应的值,重新set进属性里面
                HashMap<Integer, String> groupNameHashMap = groupSettingService.selectGroupMap();
                String[] split = agreementid.split(",");
                StringBuffer groupNameNameStr = null;
                for (int i = 0; i < split.length; i++) {
                    String groupName = groupNameHashMap.get(Integer.valueOf(split[i]));
                    if (groupNameNameStr == null) {
                        groupNameNameStr = new StringBuffer().append(groupName);
                    } else {
                        groupNameNameStr = groupNameNameStr.append(",").append(groupName);
                    }
                }
                customerSettingExample.setGroupName(groupNameNameStr.toString());
            }
        });
        PageInfo<CustomerSettingExample> pageInfo = new PageInfo<>(customerSettingExamples);
        return pageInfo;
    }

    @Override
    public void updateAttestationById(String cusId, int isPass) {
        if (cusId.contains(",")){
            String[] split = cusId.split(",");
            List<String> cusIdlist = Arrays.asList(split);
            customerSettingMapper.updateAttestationById(cusIdlist, isPass);
        }else {
            List<String> cusIdlist = new ArrayList<>();
            cusIdlist.add(cusId);
            customerSettingMapper.updateAttestationById(cusIdlist, isPass);
        }

    }

    @Override
    public HashMap<Integer, String> selectUserList() {
        HashMap<Integer, String> resultMap = new HashMap<>();
        List<HashMap> hashMapList = customerSettingMapper.selectUserList();
        hashMapList.forEach(hashMap -> {
            Integer id = Integer.valueOf(hashMap.get("id").toString());
            String username = hashMap.get("username").toString();
            resultMap.put(id,username);
        });
        return resultMap;
    }

    @Override
    public HashMap<Integer, String> selectCustomerMap() {
        HashMap<Integer, String> resultMap = new HashMap<>();
        List<HashMap> hashMapList = customerSettingMapper.selectUserList();
        hashMapList.forEach(hashMap -> {
            Integer id = Integer.valueOf(hashMap.get("customerId").toString());
            String username = hashMap.get("username").toString();
            resultMap.put(id,username);
        });
        return resultMap;
    }

    @Override
    public HashMap<Integer, String> selectAllUser() {
        HashMap<Integer, String> resultMap = new HashMap<>();
        List<HashMap> hashMapList = customerSettingMapper.selectUserMap();
        hashMapList.forEach(hashMap -> {
            Integer id = Integer.valueOf(hashMap.get("id").toString());
            String username = hashMap.get("username").toString();
            resultMap.put(id,username);
        });
        return resultMap;
    }

    @Override
    public HashMap<Long, String> selectTenantList(String token) {
        HashMap<Long, String> resultMap = new HashMap<>();
        //后期从token中获取用户Id
        HashMap<String, Object> tokenData = getTokenData(token);
        Integer userId=null;
        if (tokenData!=null){
            userId = Integer.valueOf(tokenData.get("userId").toString());
        }else {
            throw new CheckException("校验token失败!");
        }
        List<HashMap> hashMapList = customerSettingMapper.selectTenantList(userId);
        hashMapList.forEach(hashMap -> {
            String enterpriseName = String.valueOf(hashMap.get("enterprise_name"));
            Long tenantId = Long.valueOf(hashMap.get("tenant_id").toString());
            resultMap.put(tenantId,enterpriseName);
        });
        return resultMap;
    }

}










