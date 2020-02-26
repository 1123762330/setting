package com.xnpool.setting.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xnpool.setting.common.BaseController;
import com.xnpool.setting.domain.pojo.CustomerSettingExample;
import com.xnpool.setting.domain.pojo.FactoryHouseExample;
import com.xnpool.setting.domain.pojo.MineSetting;
import com.xnpool.setting.service.AgreementSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import com.xnpool.setting.domain.mapper.CustomerSettingMapper;
import com.xnpool.setting.domain.pojo.CustomerSetting;
import com.xnpool.setting.service.CustomerSettingService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @author zly
 * @version 1.0
 * @date 2020/2/10 10:35
 */
@Service
public class CustomerSettingServiceImpl extends BaseController implements CustomerSettingService {

    @Resource
    private CustomerSettingMapper customerSettingMapper;

    @Autowired
    private AgreementSettingService agreementSettingService;

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
    public void insertSelective(CustomerSetting record) {
        int rows = customerSettingMapper.insertSelective(record);
        record.setCreatetime(new Date());
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
        record.setUpdatetime(new Date());
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
        record.setUpdatetime(new Date());
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
        HashMap<Integer, String> agreementMap = agreementSettingService.selectAgreementMap();
        customerSettingExamples.forEach(customerSettingExample -> {
            String agreementid = customerSettingExample.getAgreementid();
            if (agreementid.contains(",")){
                //多个协议ID,从协议IDMap集合里面取出相应的值,重新set进属性里面
                String[] split = agreementid.split(",");
                StringBuffer agreemmentNameStr=null;
                for (int i = 0; i < split.length; i++) {
                     String agreemmentName = agreementMap.get(split[i]);
                     if (agreemmentNameStr==null){
                         agreemmentNameStr=agreemmentNameStr.append(agreemmentName);
                     }else {
                         agreemmentNameStr=agreemmentNameStr.append(",").append(agreemmentName);
                     }
                }
                customerSettingExample.setAgreementName(agreemmentNameStr.toString());

            }
            String menuid = customerSettingExample.getMenuid();
            if (menuid.contains(",")){
                //多个菜单栏ID,从菜单栏IDMap集合里面取出相应的值,重新set进属性里面
            }
        });
        PageInfo<CustomerSettingExample> pageInfo = new PageInfo<>(customerSettingExamples);
        return pageInfo;
    }

    @Override
    public void updateAttestationById(int userid, int isPass) {
        customerSettingMapper.updateAttestationById(userid, isPass);
    }

}





