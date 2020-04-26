package com.xnpool.setting.service.impl;

import com.alibaba.fastjson.JSONPath;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xnpool.logaop.service.exception.CheckException;
import com.xnpool.logaop.service.exception.DataExistException;
import com.xnpool.logaop.service.exception.InsertException;
import com.xnpool.logaop.util.JwtUtil;
import com.xnpool.setting.common.BaseController;
import com.xnpool.setting.config.ApiContext;
import com.xnpool.setting.domain.mapper.WorkerAssignMapper;
import com.xnpool.setting.domain.model.CustomerSettingExample;
import com.xnpool.setting.domain.pojo.UserRoleVO;
import com.xnpool.setting.fegin.UserCenterAPI;
import com.xnpool.setting.service.AgreementSettingService;
import com.xnpool.setting.service.GroupSettingService;
import com.xnpool.setting.utils.PageUtil;
import com.xnpool.setting.utils.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import com.xnpool.setting.domain.mapper.CustomerSettingMapper;
import com.xnpool.setting.domain.pojo.CustomerSetting;
import com.xnpool.setting.service.CustomerSettingService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

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

    @Autowired
    private ApiContext apiContext;

    @Autowired
    private UserCenterAPI userCenterAPI;

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
    public int insertSelective(CustomerSetting record, String token) {
        Integer userId = getUserId(token);
        record.setUserId(userId);
        List<HashMap> hashMapList = customerSettingMapper.selectTenantList(userId);
        List<Long> list = new ArrayList<>();
        hashMapList.forEach(hashMap -> {
            Long tenantId = Long.valueOf(hashMap.get("tenant_id").toString());
            list.add(tenantId);
        });
        if (list.contains(record.getTenantId())) {
            throw new DataExistException("该企业已经申请授权,请勿重复授权!");
        }
        return customerSettingMapper.insertSelective(record);
    }

    @Override
    public CustomerSetting selectByPrimaryKey(Integer id) {
        return customerSettingMapper.selectByPrimaryKey(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateByPrimaryKeySelective(CustomerSetting record) {
        int rows = customerSettingMapper.updateByPrimaryKeySelective(record);
        //record.setUpdateTime(new Date());
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
        //CustomerSetting record = new CustomerSetting();
        //record.setUpdateTime(new Date());
        //record.setId(id);
        //redisToDelete(rows,"customer_setting",record,null);
    }

    @Override
    public Object selectByOther(String username, String agreementName, String groupName, int pageNum, int pageSize, Integer authorize) {
        //这里需要解析token,然后拿到当前管理的Id,查询属于他的客户列表
        //这里后面合并需要做关联查询,查询客户的一些基本信息
        List<CustomerSettingExample> customerSettingExamples = new ArrayList<>();
        if (StringUtils.isEmpty(username) & StringUtils.isEmpty(agreementName) & StringUtils.isEmpty(groupName)) {
            PageHelper.startPage(pageNum, pageSize);
            customerSettingExamples = customerSettingMapper.selectByOther();
        } else {
            customerSettingExamples = customerSettingMapper.selectByOther();
        }


        if (authorize == 1) {
            customerSettingExamples = customerSettingExamples.stream().filter(a -> a.getAuthentication() == authorize).collect(Collectors.toList());
        }
        //解决多个协议ID问题和多个菜单栏ID问题,先去查出相应的map集合,然后遍历该实体类进行拼接封装
        //log.info("客户设置列表" + customerSettingExamples);
        customerSettingExamples.forEach(customerSettingExample -> {
            String agreementid = customerSettingExample.getAgreementId();
            if (!StringUtils.isEmpty(agreementid)) {
                HashMap<Integer, String> agreementMap = agreementSettingService.selectAgreementMap();
                if (agreementid.contains(",")) {
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
                } else {
                    String agreemmentName = agreementMap.get(Integer.valueOf(agreementid));
                    customerSettingExample.setAgreementName(agreemmentName);
                }
            }
            String groupId = customerSettingExample.getGroupId();
            if (!StringUtils.isEmpty(groupId)) {
                HashMap<Integer, String> groupNameHashMap = groupSettingService.selectGroupMap();
                if (groupId.contains(",")) {
                    //多个分组ID,从分组IDMap集合里面取出相应的值,重新set进属性里面
                    String[] split = groupId.split(",");
                    StringBuffer groupNameNameStr = null;
                    for (int i = 0; i < split.length; i++) {
                        String groupName_db = groupNameHashMap.get(Integer.valueOf(split[i]));
                        if (groupNameNameStr == null) {
                            groupNameNameStr = new StringBuffer().append(groupName_db);
                        } else {
                            groupNameNameStr = groupNameNameStr.append(",").append(groupName_db);
                        }
                    }
                    customerSettingExample.setGroupName(groupNameNameStr.toString());
                } else {
                    String groupName_db = groupNameHashMap.get(Integer.valueOf(groupId));
                    customerSettingExample.setGroupName(groupName_db);
                }
            }
        });
        //用户名不为空
        if (!StringUtils.isEmpty(username)) {
            customerSettingExamples = customerSettingExamples.stream().filter(a -> a.getUserName().contains(username)).collect(Collectors.toList());
            if (!StringUtils.isEmpty(agreementName)) {
                customerSettingExamples = customerSettingExamples.stream().filter(a -> agreementName.equals(a.getAgreementName())).collect(Collectors.toList());
            }
            if (!StringUtils.isEmpty(groupName)) {
                customerSettingExamples = customerSettingExamples.stream().filter(a -> groupName.equals(a.getGroupName())).collect(Collectors.toList());
            }
        }
        //协议名不为空
        if (!StringUtils.isEmpty(agreementName)) {
            customerSettingExamples = customerSettingExamples.stream().filter(a -> agreementName.equals(a.getAgreementName())).collect(Collectors.toList());
            if (!StringUtils.isEmpty(groupName)) {
                customerSettingExamples = customerSettingExamples.stream().filter(a -> groupName.equals(a.getGroupName())).collect(Collectors.toList());
            }
        }
        //分组名不为空
        if (!StringUtils.isEmpty(groupName)) {
            customerSettingExamples = customerSettingExamples.stream().filter(a -> groupName.equals(a.getGroupName())).collect(Collectors.toList());
        }
        if (StringUtils.isEmpty(username) & StringUtils.isEmpty(agreementName) & StringUtils.isEmpty(groupName)) {
            PageInfo<CustomerSettingExample> pageInfo = new PageInfo<>(customerSettingExamples);
            return pageInfo;
        } else {
            HashMap<String, Object> page = PageUtil.startPage(customerSettingExamples, pageNum, pageSize);
            return page;
        }

    }

    @Override
    public void updateAttestationById(String cusId, int isPass) {
        if (cusId.contains(",")) {
            String[] split = cusId.split(",");
            List<String> cusIdlist = Arrays.asList(split);
            customerSettingMapper.updateAttestationById(cusIdlist, isPass);
        } else {
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
            resultMap.put(id, username);
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
            resultMap.put(id, username);
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
            resultMap.put(id, username);
        });
        return resultMap;
    }

    @Override
    public HashMap<Long, HashMap> selectTenantList(String token) {
        HashMap<Long, HashMap> resultMap = new HashMap<>();
        Integer userId = getUserId(token);
        List<HashMap> hashMapList = customerSettingMapper.selectTenantList(userId);
        hashMapList.forEach(hashMap -> {
            String tenantName = String.valueOf(hashMap.get("tenant_name"));
            Long tenantId = Long.valueOf(hashMap.get("tenant_id").toString());
            Integer authentication = Integer.valueOf(hashMap.get("authentication").toString());
            HashMap<Object, Object> map = new HashMap<>();
            map.put(tenantName, authentication);
            resultMap.put(tenantId, map);
        });
        return resultMap;
    }

    @Override
    public void deleteAuthority(String tenantId, String token) {
        apiContext.setTenantId(Long.valueOf(tenantId));
        Integer userId = getUserId(token);
        List<Long> list = new ArrayList<>();
        if (tenantId.contains(",")) {
            //多个企业id
            String[] split = tenantId.split(",");
            for (int i = 0; i < split.length; i++) {
                Long tenantIdToLong = Long.valueOf(split[i]);
                list.add(tenantIdToLong);
            }
        } else {
            list.add(Long.valueOf(tenantId));
        }

        customerSettingMapper.deleteAuthority(list, userId);
    }

    @Override
    public int insertSelective(CustomerSetting record) {
        return customerSettingMapper.insertSelective(record);
    }

    @Override
    public String authorizeToken(String userId) {
        Integer userId_db = customerSettingMapper.selectAuthorizedToYes(Integer.valueOf(userId));
        log.info("userId_db是:" + userId_db);
        String access_token = "";
        if (Integer.valueOf(userId) == userId_db) {
            //用户一致,可以发送授权token
            JSONObject jsonObject = userCenterAPI.authorizeToken(userId);
            log.info("fegin请求返回的数据:" + jsonObject);
            if (!StringUtils.isEmpty(jsonObject)) {
                access_token = JSONPath.eval(jsonObject, "$.datas.access_token").toString();

            }
        } else {
            throw new CheckException("该用户和你不属于同一个企业");
        }
        return access_token;
    }

    @Override
    public HashMap<String, HashSet<String>> selectCustomDropList(Integer authorize) {
        //这里需要解析token,然后拿到当前管理的Id,查询属于他的客户列表
        //这里后面合并需要做关联查询,查询客户的一些基本信息
        HashSet<String> agreementSet = new HashSet<>();
        HashSet<String> groupSet = new HashSet<>();
        List<CustomerSettingExample> customerSettingExamples = customerSettingMapper.selectByOther();
        if (authorize == 1) {
            customerSettingExamples = customerSettingExamples.stream().filter(a -> a.getAuthentication() == authorize).collect(Collectors.toList());
        }
        //解决多个协议ID问题和多个菜单栏ID问题,先去查出相应的map集合,然后遍历该实体类进行拼接封装
        //log.info("客户设置列表" + customerSettingExamples);
        customerSettingExamples.forEach(customerSettingExample -> {
            String agreementid = customerSettingExample.getAgreementId();
            //协议名称
            if (!StringUtils.isEmpty(agreementid)) {
                HashMap<Integer, String> agreementMap = agreementSettingService.selectAgreementMap();
                if (agreementid.contains(",")) {
                    //多个协议ID,从协议IDMap集合里面取出相应的值,重新set进属性里面
                    String[] split = agreementid.split(",");
                    for (int i = 0; i < split.length; i++) {
                        String agreemmentName = agreementMap.get(Integer.valueOf(split[i]));
                        agreementSet.add(agreemmentName);
                    }
                } else {
                    String agreemmentName = agreementMap.get(Integer.valueOf(agreementid));
                    agreementSet.add(agreemmentName);
                }
            }

            //分组名称
            String groupId = customerSettingExample.getGroupId();
            if (!StringUtils.isEmpty(groupId)) {
                HashMap<Integer, String> groupNameHashMap = groupSettingService.selectGroupMap();
                if (groupId.contains(",")) {
                    //多个分组ID,从分组IDMap集合里面取出相应的值,重新set进属性里面
                    String[] split = groupId.split(",");
                    for (int i = 0; i < split.length; i++) {
                        String groupName_db = groupNameHashMap.get(Integer.valueOf(split[i]));
                        groupSet.add(groupName_db);
                    }
                } else {
                    String groupName_db = groupNameHashMap.get(Integer.valueOf(groupId));
                    groupSet.add(groupName_db);
                }
            }
        });
        HashMap<String, HashSet<String>> resultMap = new HashMap<>();
        resultMap.put("groupList",groupSet);
        resultMap.put("agreementList",agreementSet);
        return resultMap;
    }
}











