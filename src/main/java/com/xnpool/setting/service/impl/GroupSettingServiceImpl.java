package com.xnpool.setting.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xnpool.setting.domain.mapper.FactoryHouseMapper;
import com.xnpool.setting.domain.mapper.FrameSettingMapper;
import com.xnpool.setting.domain.mapper.IpSettingMapper;
import com.xnpool.setting.domain.pojo.*;
import com.xnpool.setting.service.FrameSettingService;
import com.xnpool.setting.service.IpSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import com.xnpool.setting.domain.mapper.GroupSettingMapper;
import com.xnpool.setting.service.GroupSettingService;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * @author zly
 * @version 1.0
 * @date 2020/2/5 15:36
 */
@Service
public class GroupSettingServiceImpl implements GroupSettingService {

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
    public int insertSelective(GroupSetting record) {
        return groupSettingMapper.insertSelective(record);
    }

    @Override
    public GroupSetting selectByPrimaryKey(Integer groupid) {
        return groupSettingMapper.selectByPrimaryKey(groupid);
    }

    @Override
    public int updateByPrimaryKeySelective(GroupSetting record) {
        return groupSettingMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(GroupSetting record) {
        return groupSettingMapper.updateByPrimaryKey(record);
    }

    @Override
    public int updateById(int id) {
        //需要去查询当前分组内是否有矿机IP,首先查到IP字段Map集合
        HashMap<Integer, String> ipStart = ipSettingService.selectByIPStart();
        Set<Integer> keySet = ipStart.keySet();
        GroupSetting groupSetting = groupSettingMapper.selectByPrimaryKey(id);
        String ipid = groupSetting.getIpid();
        if (ipid.contains(",")) {
            //多个IP
            String[] split = ipid.split(",");
            for (int i = 0; i < split.length; i++) {
                if (keySet.contains(split[i])) {
                    return -1;
                }
            }
        } else {
            //单个IP
            if (keySet.contains(ipStart)) {
                return -1;
            }
        }
        return groupSettingMapper.updateById(id);
    }

    @Override
    public PageInfo<GroupSettingExample> selectByOther(String keyWord, int pageNum, int pageSize) {
        if (!StringUtils.isEmpty(keyWord)) {
            keyWord = "%" + keyWord + "%";
        }

        PageHelper.startPage(pageNum, pageSize);
        List<GroupSettingExample> groupSettings = groupSettingMapper.selectByOther(keyWord);
        //查询IP字段Map
        HashMap<Integer, String> ipStartMap = ipSettingService.selectByIPStart();
        //解決包含多个属性集合
        groupSettings.forEach(groupSettingExample -> {
            //包含多个IP
            String ipid = groupSettingExample.getIpid();
            if (ipid.contains(",")) {
                //如果包含多个IP
                String[] split = ipid.split(",");
                for (int i = 0; i < split.length; i++) {
                    String ipStr = ipStartMap.get(split[i]);
                    if (groupSettingExample.getIpStr() != null) {
                        String manyIPStart = groupSettingExample.getIpStr() + "," + ipStr;
                        groupSettingExample.setIpStr(manyIPStart);
                    } else {
                        groupSettingExample.setIpStr(ipStr);
                    }
                }
            } else {
                //只有一个IP字段
                String ipStr = groupSettingExample.getStartip() + "-" + groupSettingExample.getEndip();
                groupSettingExample.setIpStr(ipStr);
            }

            //包含多个矿机架
            String frameid = groupSettingExample.getFrameid();
            if (frameid.contains(",")) {
                //如果包含多个矿机架Id
                groupSettingExample.setFramename(null);
                String[] split = frameid.split(",");
                for (int i = 0; i < split.length; i++) {
                    FrameSetting frameSetting = frameSettingMapper.selectByPrimaryKey(Integer.valueOf(split[i]));
                    String framename = frameSetting.getFramename();
                    Integer number = frameSetting.getNumber();
                    framename = framename + " 1-" + number + "层";
                    if (groupSettingExample.getFramename() != null) {
                        String manyFramename = groupSettingExample.getFramename() + "," + framename;
                        groupSettingExample.setFramename(manyFramename);
                    } else {
                        groupSettingExample.setFramename(framename);
                    }
                }
            } else {
                String framename = groupSettingExample.getFramename() + " 1-" + groupSettingExample.getNumber() + "层";
                groupSettingExample.setFramename(framename);
            }

            //包含多个厂房Id
            String factoryid = groupSettingExample.getFactoryid();
            if (factoryid.contains(",")) {
                //如果包含多个厂房ID
                groupSettingExample.setFactoryname(null);
                String[] split = factoryid.split(",");
                for (int i = 0; i < split.length; i++) {
                    FactoryHouse factoryHouse = factoryHouseMapper.selectByPrimaryKey(Integer.valueOf(split[i]));
                    String factoryname = factoryHouse.getFactoryname();
                    if (groupSettingExample.getFactoryname() != null) {
                        String manyFrame = groupSettingExample.getFactoryname() + "," + factoryname;
                        groupSettingExample.setFactoryname(manyFrame);
                    } else {
                        groupSettingExample.setFactoryname(factoryname);
                    }
                }
            }
        });
        PageInfo<GroupSettingExample> pageInfo = new PageInfo<>(groupSettings);
        return pageInfo;
    }

}



