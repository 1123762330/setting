package com.xnpool.setting.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import com.xnpool.setting.domain.mapper.FeeSettingMapper;
import com.xnpool.setting.domain.pojo.FeeSetting;
import com.xnpool.setting.service.FeeSettingService;
import org.springframework.util.StringUtils;

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

}

