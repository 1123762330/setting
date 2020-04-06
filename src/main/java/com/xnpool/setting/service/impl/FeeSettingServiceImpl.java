package com.xnpool.setting.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xnpool.logaop.service.exception.DataExistException;
import com.xnpool.logaop.service.exception.InsertException;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import com.xnpool.setting.domain.mapper.FeeSettingMapper;
import com.xnpool.setting.domain.pojo.FeeSetting;
import com.xnpool.setting.service.FeeSettingService;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;

/**
 * @author zly
 * @version 1.0
 * @date 2020/2/6 16:05
 */
@Service
public class FeeSettingServiceImpl implements FeeSettingService {

    @Resource
    private FeeSettingMapper feeSettingMapper;

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return feeSettingMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(FeeSetting record) {
        return feeSettingMapper.insert(record);
    }

    @Override
    public int insertSelective(FeeSetting record) {
        List<String> list = feeSettingMapper.selectFeeNameList(record.getId());
        if (list.contains(record.getFeeName())) {
            throw new DataExistException("数据已存在,请勿重复添加!");
        }
        return feeSettingMapper.insertSelective(record);
    }

    @Override
    public FeeSetting selectByPrimaryKey(Integer id) {
        return feeSettingMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(FeeSetting record) {
        return feeSettingMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(FeeSetting record) {
        return feeSettingMapper.updateByPrimaryKey(record);
    }

    @Override
    public int updateById(int id) {
        return feeSettingMapper.updateById(id);
    }

    @Override
    public PageInfo<FeeSetting> selectByOther(String keyWord, int pageNum, int pageSize) {
        if (!StringUtils.isEmpty(keyWord)) {
            keyWord = "%" + keyWord + "%";
        }
        PageHelper.startPage(pageNum, pageSize);
        List<FeeSetting> feeSettingList = feeSettingMapper.selectByOther(keyWord);
        PageInfo<FeeSetting> pageInfo = new PageInfo<>(feeSettingList);
        return pageInfo;
    }

    @Override
    public HashMap<Integer, String> selectFeeMap() {
        HashMap<Integer, String> feeMap = new HashMap<>();
        List<FeeSetting> feeSettingList = feeSettingMapper.selectByOther(null);
        for (FeeSetting feeSetting : feeSettingList) {
            String feeName = feeSetting.getFeeName();
            Integer id = feeSetting.getId();
            feeMap.put(id, feeName);
        }
        return feeMap;
    }

}



