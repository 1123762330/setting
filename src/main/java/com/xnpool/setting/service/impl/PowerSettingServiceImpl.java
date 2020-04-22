package com.xnpool.setting.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xnpool.logaop.service.exception.DataExistException;
import com.xnpool.setting.domain.model.PowerSettingExample;
import com.xnpool.setting.service.MineSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import com.xnpool.setting.domain.mapper.PowerSettingMapper;
import com.xnpool.setting.domain.pojo.PowerSetting;
import com.xnpool.setting.service.PowerSettingService;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author zly
 * @version 1.0
 * @date 2020/2/6 14:13
 */
@Service
public class PowerSettingServiceImpl implements PowerSettingService {

    @Resource
    private PowerSettingMapper powerSettingMapper;

    @Autowired
    private MineSettingService mineSettingService;

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return powerSettingMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(PowerSetting record) {
        return powerSettingMapper.insert(record);
    }

    @Override
    public int insertSelective(PowerSetting record) {
        List<String> list = powerSettingMapper.selectNameList(record.getId());
        if (list.contains(record.getDescription())) {
            throw new DataExistException("数据已存在,请勿重复添加!");
        }
        return powerSettingMapper.insertSelective(record);
    }

    @Override
    public PowerSetting selectByPrimaryKey(Integer id) {
        return powerSettingMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(PowerSetting record) {
        List<String> list = powerSettingMapper.selectNameList(record.getId());
        if (list.contains(record.getDescription())) {
            throw new DataExistException("数据已存在,请勿重复添加!");
        }
        return powerSettingMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(PowerSetting record) {
        return powerSettingMapper.updateByPrimaryKey(record);
    }

    @Override
    public int updateById(int id) {
        return powerSettingMapper.updateById(id);
    }

    @Override
    public PageInfo<PowerSettingExample> selectByOther(String keyWord, int pageNum, int pageSize,String token) {
        if (!StringUtils.isEmpty(keyWord)) {
            keyWord = "%" + keyWord + "%";
        }
        HashMap<Integer, String> mineNameMap = mineSettingService.selectMineNameByOther(token);
        List<PowerSettingExample> list = new ArrayList<>();
        PageHelper.startPage(pageNum, pageSize);
        List<PowerSetting> powerSettings = powerSettingMapper.selectByOther(keyWord);
        for (PowerSetting powerSetting : powerSettings) {
            PowerSettingExample powerSettingExample = new PowerSettingExample();
            powerSettingExample.setId(powerSetting.getId());
            Integer mineId = powerSetting.getMineId();
            powerSettingExample.setMineId(mineId);
            String mineName = mineNameMap.get(mineId);
            powerSettingExample.setMineName(mineName);
            powerSettingExample.setPrice(powerSetting.getPrice());
            powerSettingExample.setBasePrice(powerSetting.getBasePrice());
            powerSettingExample.setDescription(powerSetting.getDescription());
            list.add(powerSettingExample);
        }
        PageInfo<PowerSettingExample> pageInfo = new PageInfo<>(list);
        return pageInfo;
    }

}



