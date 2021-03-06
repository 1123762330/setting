package com.xnpool.setting.service.impl;

import com.alibaba.fastjson.JSONPath;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xnpool.logaop.service.exception.CheckException;
import com.xnpool.logaop.service.exception.DataExistException;
import com.xnpool.setting.common.BaseController;
import com.xnpool.setting.config.ApiContext;
import com.xnpool.setting.domain.mapper.ElectricityManagerMapper;
import com.xnpool.setting.domain.model.CustomerSettingExample;
import com.xnpool.setting.domain.pojo.CustomerSetting;
import com.xnpool.setting.domain.mapper.CustomerSettingMapper;
import com.xnpool.setting.domain.pojo.ElectricityManager;
import com.xnpool.setting.fegin.UserCenterAPI;
import com.xnpool.setting.service.AgreementSettingService;
import com.xnpool.setting.service.CustomerSettingService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xnpool.setting.service.GroupSettingService;
import com.xnpool.setting.utils.PageUtil;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author zly
 * @since 2020-04-27
 */
@Service
@Slf4j
public class CustomerSettingServiceImpl extends ServiceImpl<CustomerSettingMapper, CustomerSetting> implements CustomerSettingService {

    @Autowired
    private CustomerSettingMapper customerSettingMapper;

    @Autowired
    private BaseController baseController;

    @Autowired
    private AgreementSettingService agreementSettingService;

    @Autowired
    private GroupSettingService groupSettingService;

    @Autowired
    private ApiContext apiContext;

    @Autowired
    private UserCenterAPI userCenterAPI;

    @Autowired
    private ElectricityManagerMapper electricityManagerMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer insertSelective(CustomerSetting record, String token) {
        Integer userId = baseController.getUserId(token);
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
        return customerSettingMapper.insert(record);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer updateByPrimaryKeySelective(CustomerSetting record) {
        return customerSettingMapper.updateById(record);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer deleteById(int id) {
        return customerSettingMapper.deleteByKeyId(id);
    }

    @Override
    public Object selectByOther(String username, String agreementName, String groupName, int pageNum, int pageSize, Integer authorize) {
        //这里需要解析token,然后拿到当前管理的Id,查询属于他的客户列表
        //这里后面合并需要做关联查询,查询客户的一些基本信息
        List<CustomerSettingExample> customerSettingExamples = new ArrayList<>();
        Page<CustomerSettingExample> page = new Page<>(pageNum, pageSize);
        if (StringUtils.isEmpty(username) & StringUtils.isEmpty(agreementName) & StringUtils.isEmpty(groupName)) {
            customerSettingExamples = customerSettingMapper.selectByOther(page);
        } else {
            customerSettingExamples = customerSettingMapper.selectByOther(null);
        }

        //获取客户级别map
        HashMap<Integer, String> cusLevelMap = new HashMap<>();
        List<HashMap> cusLevelList = customerSettingMapper.selectCusLevelList();
        HashMap<Integer, String> agreementMap = agreementSettingService.selectAgreementMap();
        HashMap<Integer, String> groupNameHashMap = groupSettingService.selectGroupMap();
        cusLevelList.forEach(hashMap -> {
            Integer id = Integer.valueOf(hashMap.get("id").toString());
            String level = hashMap.get("level").toString();
            cusLevelMap.put(id, level);
        });

        if (authorize == 1) {
            customerSettingExamples = customerSettingExamples.stream().filter(a -> a.getAuthentication() == authorize).collect(Collectors.toList());
        }
        //解决多个协议ID问题和多个菜单栏ID问题,先去查出相应的map集合,然后遍历该实体类进行拼接封装
        //log.info("客户设置列表" + customerSettingExamples);
        customerSettingExamples.forEach(customerSettingExample -> {
            String agreementid = customerSettingExample.getAgreementId();
            if (!StringUtils.isEmpty(agreementid)) {
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

            Integer cusLevelId = customerSettingExample.getCusLevelId();
            if (!StringUtils.isEmpty(cusLevelId)) {
                String level = cusLevelMap.get(cusLevelId);
                customerSettingExample.setLevel(level);
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
            page.setRecords(customerSettingExamples);
            return page;
        } else {
            HashMap<String, Object> pageInfo = PageUtil.startPage(customerSettingExamples, pageNum, pageSize);
            return pageInfo;
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateAttestationById(String cusId, int isPass) {
        List<String> cusIdlist = new ArrayList<>();
        if (cusId.contains(",")) {
            String[] split = cusId.split(",");
            cusIdlist = Arrays.asList(split);
            customerSettingMapper.updateAttestationById(cusIdlist, isPass);
        } else {
            cusIdlist.add(cusId);
            customerSettingMapper.updateAttestationById(cusIdlist, isPass);
        }

        //只有授权为1时,才会添加数据到t_electricity_manager表中
        if (isPass==1){
            List<CustomerSetting> customerSettingList = customerSettingMapper.selectCustomerById(cusIdlist);
            customerSettingList.forEach(customerSetting -> {
                Integer userId = customerSetting.getUserId();
                Integer id = customerSetting.getId();
                ElectricityManager electricityManager = new ElectricityManager();
                electricityManager.setCustomerId(Long.valueOf(id));
                electricityManager.setUserId(Long.valueOf(userId));
                electricityManager.setCreateTime(LocalDateTime.now());
                electricityManagerMapper.insert(electricityManager);
            });
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
        Integer userId = baseController.getUserId(token);
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
        Integer userId = baseController.getUserId(token);
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
    public String authorizeToken(String userId) {
        Integer userId_db = customerSettingMapper.selectAuthorizedToYes(Integer.valueOf(userId));
        log.info("userId_db是:" + userId_db);
        String access_token = "";
        if (userId_db == null) {
            throw new CheckException("该用户还未通过审核认证,请先认证");
        } else {
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
        }
        return access_token;
    }

    @Override
    public HashMap<String, HashSet<String>> selectCustomDropList(Integer authorize) {
        //这里需要解析token,然后拿到当前管理的Id,查询属于他的客户列表
        //这里后面合并需要做关联查询,查询客户的一些基本信息
        HashSet<String> agreementSet = new HashSet<>();
        HashSet<String> groupSet = new HashSet<>();
        List<CustomerSettingExample> customerSettingExamples = customerSettingMapper.selectByOther(null);
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
        resultMap.put("groupList", groupSet);
        resultMap.put("agreementList", agreementSet);
        return resultMap;
    }

}
