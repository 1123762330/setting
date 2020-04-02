package com.xnpool.setting.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xnpool.logaop.service.exception.InsertException;
import com.xnpool.setting.domain.model.ElectricityMeterSettingExample;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import com.xnpool.setting.domain.mapper.ElectricityMeterSettingMapper;
import com.xnpool.setting.domain.pojo.ElectricityMeterSetting;
import com.xnpool.setting.service.ElectricityMeterSettingService;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @author zly
 * @version 1.0
 * @date 2020/2/7 15:38
 */
@Service
public class ElectricityMeterSettingServiceImpl implements ElectricityMeterSettingService {

    @Resource
    private ElectricityMeterSettingMapper electricityMeterSettingMapper;

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return electricityMeterSettingMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(ElectricityMeterSetting record) {
        return electricityMeterSettingMapper.insert(record);
    }

    @Override
    public int insertSelective(ElectricityMeterSetting record) {
        List<String> list = electricityMeterSettingMapper.selectNameList(record.getId());
        if (list.contains(record.getId())) {
            throw new InsertException("数据已存在,请勿重复添加!");
        }
        return electricityMeterSettingMapper.insertSelective(record);
    }

    @Override
    public ElectricityMeterSetting selectByPrimaryKey(Integer id) {
        return electricityMeterSettingMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(ElectricityMeterSetting record) {
        List<String> list = electricityMeterSettingMapper.selectNameList(record.getId());
        if (list.contains(record.getId())) {
            throw new InsertException("数据已存在,请勿重复添加!");
        }
        return electricityMeterSettingMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(ElectricityMeterSetting record) {
        return electricityMeterSettingMapper.updateByPrimaryKey(record);
    }

    @Override
    public void updateById(int id) {
        electricityMeterSettingMapper.updateById(id);
    }

    @Override
    public PageInfo<ElectricityMeterSettingExample> selectByOther(String keyWord, int pageNum, int pageSize) {
        if (!StringUtils.isEmpty(keyWord)) {
            keyWord = "%" + keyWord + "%";
        }
        PageHelper.startPage(pageNum, pageSize);
        List<ElectricityMeterSettingExample> agreementSettingList = electricityMeterSettingMapper.selectByOther(keyWord);
        PageInfo<ElectricityMeterSettingExample> pageInfo = new PageInfo<>(agreementSettingList);
        return pageInfo;
    }

}






