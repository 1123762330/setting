package com.xnpool.setting.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xnpool.logaop.service.exception.DataExistException;
import com.xnpool.setting.common.BaseController;
import com.xnpool.setting.domain.mapper.FactoryHouseMapper;
import com.xnpool.setting.domain.mapper.FrameSettingMapper;
import com.xnpool.setting.domain.model.GroupSettingExample;
import com.xnpool.setting.domain.pojo.*;
import com.xnpool.setting.domain.redismodel.GroupSettingRedisModel;
import com.xnpool.setting.service.IpSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import com.xnpool.setting.domain.mapper.GroupSettingMapper;
import com.xnpool.setting.service.GroupSettingService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @author zly
 * @version 1.0
 * @date 2020/2/5 15:36
 */
@Service
public class GroupSettingServiceImpl extends BaseController implements GroupSettingService {

    @Resource
    private GroupSettingMapper groupSettingMapper;
    @Resource
    private FrameSettingMapper frameSettingMapper;
    @Resource
    private FactoryHouseMapper factoryHouseMapper;
    @Autowired
    private IpSettingService ipSettingService;


    @Override
    public int deleteByPrimaryKey(Integer groupid) {
        return groupSettingMapper.deleteByPrimaryKey(groupid);
    }

    @Override
    public int insert(GroupSetting record) {
        return groupSettingMapper.insert(record);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertSelective(GroupSetting record) {
        List<String> list = groupSettingMapper.selectGroupNameList(record.getId());
        if (list.contains(record.getGroupName())) {
            throw new DataExistException("数据已存在,请勿重复添加!");
        }
        int rows = groupSettingMapper.insertSelective(record);
        record.setCreateTime(new Date());
        GroupSettingRedisModel groupSettingRedisModel = getGroupSettingRedisModel(record);
        redisToInsert(rows, "group_setting", groupSettingRedisModel, record.getMineId());
    }

    @Override
    public GroupSetting selectByPrimaryKey(Integer groupid) {
        return groupSettingMapper.selectByPrimaryKey(groupid);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateByPrimaryKeySelective(GroupSetting record) {
        List<String> list = groupSettingMapper.selectGroupNameList(record.getId());
        if (list.contains(record.getGroupName())) {
            throw new DataExistException("数据已存在,请勿重复添加!");
        }
        int rows = groupSettingMapper.updateByPrimaryKeySelective(record);
        GroupSetting groupSetting = groupSettingMapper.selectByPrimaryKey(record.getId());
        GroupSettingRedisModel groupSettingRedisModel = getGroupSettingRedisModel(groupSetting);
        redisToUpdate(rows, "group_setting", groupSettingRedisModel, groupSetting.getMineId());
    }

    @Override
    public int updateByPrimaryKey(GroupSetting record) {
        return groupSettingMapper.updateByPrimaryKey(record);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateById(int id) {
        //需要去查询当前分组内是否有矿机IP,首先查到IP字段Map集合
        //HashMap<Integer, String> ipStart = ipSettingService.selectByIPStart();
        //Set<Integer> keySet = ipStart.keySet();
        //GroupSetting groupSetting = groupSettingMapper.selectByPrimaryKey(id);
        //String ipid = groupSetting.getIpId();
        //if (ipid.contains(",")) {
        //    //多个IP
        //    String[] split = ipid.split(",");
        //    for (int i = 0; i < split.length; i++) {
        //        if (keySet.contains(split[i])) {
        //            throw new DeleteException("该分组下存在有效IP,不允许删除!");
        //        }
        //    }
        //} else {
        //    //单个IP
        //    if (keySet.contains(ipStart)) {
        //        throw new DeleteException("该分组下存在有效IP,不允许删除!");
        //    }
        //}
        int rows = groupSettingMapper.updateById(id);
        GroupSetting groupSetting = groupSettingMapper.selectByPrimaryKey(id);
        GroupSettingRedisModel groupSettingRedisModel = getGroupSettingRedisModel(groupSetting);
        redisToDelete(rows, "group_setting", groupSettingRedisModel, groupSetting.getMineId());
    }

    @Override
    public PageInfo<GroupSettingExample> selectByOther(String keyWord, int pageNum, int pageSize) {
        if (!StringUtils.isEmpty(keyWord)) {
            keyWord = "%" + keyWord + "%";
        }

        PageHelper.startPage(pageNum, pageSize);
        List<GroupSettingExample> groupSettings = groupSettingMapper.selectByOther(keyWord);
        //System.out.println(groupSettings);
        //查询IP字段Map
        HashMap<Integer, String> ipStartMap = ipSettingService.selectByIPStart();
        //System.out.println(ipStartMap);
        //解決包含多个属性集合
        groupSettings.forEach(groupSettingExample -> {
            //包含多个IP
            String ipid = groupSettingExample.getIpId();
            if (ipid.contains(",")) {
                //如果包含多个IP
                String[] split = ipid.split(",");
                for (int i = 0; i < split.length; i++) {
                    String ipStr = ipStartMap.get(Integer.valueOf(split[i]));
                    if (groupSettingExample.getIpStr() != null) {
                        String manyIPStart = groupSettingExample.getIpStr() + "," + ipStr;
                        groupSettingExample.setIpStr(manyIPStart);
                    } else {
                        groupSettingExample.setIpStr(ipStr);
                    }
                }
            } else {
                //只有一个IP字段
                String ipStr = groupSettingExample.getStartIp() + "-" + groupSettingExample.getEndIp();
                groupSettingExample.setIpStr(ipStr);
            }

            //包含多个矿机架
            String frameid = groupSettingExample.getFrameId();
            if (frameid.contains(",")) {
                //如果包含多个矿机架Id
                groupSettingExample.setFramenameDetailed(null);
                String[] split = frameid.split(",");
                for (int i = 0; i < split.length; i++) {
                    FrameSetting frameSetting = frameSettingMapper.selectById(Integer.valueOf(split[i]));
                    String detailed = frameSetting.getDetailed();
                    if (groupSettingExample.getFramenameDetailed() != null) {
                        String manyFramename = groupSettingExample.getFramenameDetailed() + "," + detailed;
                        groupSettingExample.setFramenameDetailed(manyFramename);
                    } else {
                        groupSettingExample.setFramenameDetailed(detailed);
                    }
                }
            }

            //包含多个厂房Id
            String factoryid = groupSettingExample.getFactoryId();
            if (factoryid.contains(",")) {
                //如果包含多个厂房ID
                groupSettingExample.setFactoryName(null);
                String[] split = factoryid.split(",");
                for (int i = 0; i < split.length; i++) {
                    FactoryHouse factoryHouse = factoryHouseMapper.selectById(Integer.valueOf(split[i]));
                    String factoryname = factoryHouse.getFactoryName();
                    if (groupSettingExample.getFactoryName() != null) {
                        String manyFrame = groupSettingExample.getFactoryName() + "," + factoryname;
                        groupSettingExample.setFactoryName(manyFrame);
                    } else {
                        groupSettingExample.setFactoryName(factoryname);
                    }
                }
            }
        });
        PageInfo<GroupSettingExample> pageInfo = new PageInfo<>(groupSettings);
        return pageInfo;
    }

    /**
     * @return
     * @Description 分组名集合
     * @Author zly
     * @Date 11:15 2020/3/3
     * @Param
     */
    @Override
    public HashMap<Integer, String> selectGroupMap() {
        HashMap<Integer, String> groupNameMap = new HashMap<>();
        List<GroupSetting> groupSettings = groupSettingMapper.selectGroupMap();
        groupSettings.forEach(groupSettingExample -> {
            Integer groupid = groupSettingExample.getId();
            String groupname = groupSettingExample.getGroupName();
            groupNameMap.put(groupid, groupname);
        });
        return groupNameMap;
    }

}




