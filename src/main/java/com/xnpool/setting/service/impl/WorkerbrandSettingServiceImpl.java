package com.xnpool.setting.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import com.xnpool.setting.domain.pojo.WorkerbrandSetting;
import com.xnpool.setting.domain.mapper.WorkerbrandSettingMapper;
import com.xnpool.setting.service.WorkerbrandSettingService;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @author  zly
 * @date  2020/2/6 13:22
 * @version 1.0
 */
@Service
public class WorkerbrandSettingServiceImpl implements WorkerbrandSettingService{

    @Resource
    private WorkerbrandSettingMapper workerbrandSettingMapper;

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return workerbrandSettingMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(WorkerbrandSetting record) {
        return workerbrandSettingMapper.insert(record);
    }

    @Override
    public int insertSelective(WorkerbrandSetting record) {
        return workerbrandSettingMapper.insertSelective(record);
    }

    @Override
    public WorkerbrandSetting selectByPrimaryKey(Integer id) {
        return workerbrandSettingMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(WorkerbrandSetting record) {
        return workerbrandSettingMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(WorkerbrandSetting record) {
        return workerbrandSettingMapper.updateByPrimaryKey(record);
    }

    @Override
    public int updateById(int id) {
        return workerbrandSettingMapper.updateById(id);
    }

    @Override
    public PageInfo<WorkerbrandSetting> selectByOther(String keyWord,int pageNum,int pageSize) {
        if (!StringUtils.isEmpty(keyWord)) {
            keyWord = "%" + keyWord + "%";
        }
        PageHelper.startPage(pageNum,pageSize);
        List<WorkerbrandSetting> workerbrandSettingList = workerbrandSettingMapper.selectByOther(keyWord);
        PageInfo<WorkerbrandSetting> pageInfo = new PageInfo<>(workerbrandSettingList);
        return pageInfo;
    }

}
