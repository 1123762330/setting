package com.xnpool.setting.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xnpool.logaop.service.exception.DataExistException;
import com.xnpool.logaop.service.exception.InsertException;
import com.xnpool.setting.common.BaseController;
import com.xnpool.setting.config.ApiContext;
import com.xnpool.setting.domain.mapper.IpAssignMapper;
import com.xnpool.setting.domain.model.IpSettingExample;
import com.xnpool.setting.domain.pojo.FactoryHouse;
import com.xnpool.setting.domain.redismodel.IpSettingRedisModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import com.xnpool.setting.domain.pojo.IpSetting;
import com.xnpool.setting.domain.mapper.IpSettingMapper;
import com.xnpool.setting.service.IpSettingService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @author zly
 * @version 1.0
 * @date 2020/2/5 12:38
 */
@Service
public class IpSettingServiceImpl extends BaseController implements IpSettingService {

    @Autowired
    private IpSettingMapper ipSettingMapper;
    @Autowired
    private IpAssignMapper ipAssignMapper;

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return ipSettingMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(IpSetting record) {
        return ipSettingMapper.insert(record);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertSelective(IpSetting record) {
        List<String> list = ipSettingMapper.selectNameList(record.getId());
        if (list.contains(record.getStartIp())) {
            throw new DataExistException("数据已存在,请勿重复添加!");
        }
        int rows = ipSettingMapper.insertSelective(record);
        record.setCreateTime(new Date());
        IpSettingRedisModel ipSettingRedisModel = getIpSettingRedisModel(record);
        redisToInsert(rows, "ip_setting", ipSettingRedisModel, record.getMineId());
    }

    @Override
    public IpSetting selectByPrimaryKey(Integer id) {
        return ipSettingMapper.selectByPrimaryKey(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateByPrimaryKeySelective(IpSetting record) {
        List<String> list = ipSettingMapper.selectNameList(record.getId());
        if (list.contains(record.getStartIp())) {
            throw new DataExistException("数据已存在,请勿重复添加!");
        }
        int rows = ipSettingMapper.updateByPrimaryKeySelective(record);
        IpSetting ipSetting = ipSettingMapper.selectByPrimaryKey(record.getId());
        IpSettingRedisModel ipSettingRedisModel = getIpSettingRedisModel(ipSetting);
        redisToUpdate(rows, "ip_setting", ipSettingRedisModel, ipSetting.getMineId());
    }

    @Override
    public int updateByPrimaryKey(IpSetting record) {
        return ipSettingMapper.updateByPrimaryKey(record);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateById(int id) {
        int rows = ipSettingMapper.updateById(id);
        IpSetting ipSetting = ipSettingMapper.selectByPrimaryKey(id);
        IpSettingRedisModel ipSettingRedisModel = getIpSettingRedisModel(ipSetting);
        redisToDelete(rows, "ip_setting", ipSettingRedisModel, ipSetting.getMineId());
    }

    @Override
    public PageInfo<IpSettingExample> selectByOther(String keyWord, int pageNum, int pageSize) {
        if (!StringUtils.isEmpty(keyWord)) {
            keyWord = "%" + keyWord + "%";
        }
        PageHelper.startPage(pageNum, pageSize);
        List<IpSettingExample> ipSettings = ipSettingMapper.selectByOther(keyWord,null);
        PageInfo<IpSettingExample> pageInfo = new PageInfo<>(ipSettings);
        return pageInfo;
    }

    @Override
    public HashMap<Integer, String> selectByIPStart() {
        List<IpSettingExample> ipSettings = ipSettingMapper.selectByOther(null,null);
        if (ipSettings != null) {
            HashMap<Integer, String> resultMap = new HashMap<>();
            ipSettings.forEach(ipSetting -> {
                Integer id = ipSetting.getId();
                String startIp = ipSetting.getStartIp();
                String endIp = ipSetting.getEndIp();
                String ipName = startIp + "-" + endIp;
                resultMap.put(id, ipName);
            });
            return resultMap;
        } else {
            return null;
        }
    }


    public HashMap<String, String> selectIpQuJian() {
        List<IpSettingExample> ipSettings = ipSettingMapper.selectByOther(null,null);
        if (ipSettings != null) {
            HashMap<String, String> resultMap = new HashMap<>();
            ipSettings.forEach(ipSetting -> {
                String startIp = ipSetting.getStartIp();
                String endIp = ipSetting.getEndIp();
                String ipName = startIp + "-" + endIp;
                int lastIndexOf = startIp.lastIndexOf(".");
                String startIp_tmp = startIp.substring(0, lastIndexOf);
                resultMap.put(startIp_tmp, ipName);
            });
            return resultMap;
        } else {
            return null;
        }
    }

    @Override
    public HashMap<Integer, String> selectByIpStartByMineId(String mineName,Integer mineId) {
        List<Integer> ipIdList = ipAssignMapper.selectIpIdList();
        List<IpSettingExample> ipSettings = ipSettingMapper.selectByOther(null,mineId);
        if (ipSettings != null) {
            HashMap<Integer, String> resultMap = new HashMap<>();
            ipSettings.forEach(ipSetting -> {
                Integer id = ipSetting.getId();
                //过滤已经分配了的IP区间
                if (!ipIdList.contains(id)){
                    String startIp = ipSetting.getStartIp();
                    String endIp = ipSetting.getEndIp();
                    String ipName = mineName+" "+startIp + "-" + endIp;
                    resultMap.put(id, ipName);
                }
            });
            return resultMap;
        } else {
            return null;
        }
    }

}


