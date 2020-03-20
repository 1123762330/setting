package com.xnpool.setting.service.impl;
import java.util.Date;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xnpool.setting.common.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.xnpool.setting.domain.pojo.WorkerbrandSetting;
import com.xnpool.setting.domain.mapper.WorkerbrandSettingMapper;
import com.xnpool.setting.service.WorkerbrandSettingService;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

/**
 * @author zly
 * @version 1.0
 * @date 2020/2/6 13:22
 */
@Service
public class WorkerbrandSettingServiceImpl  implements WorkerbrandSettingService {

    @Resource
    private WorkerbrandSettingMapper workerbrandSettingMapper;

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return workerbrandSettingMapper.deleteById(id);
    }

    @Override
    public int insert(WorkerbrandSetting record) {
        return workerbrandSettingMapper.insert(record);
    }

    @Override
    public WorkerbrandSetting selectByPrimaryKey(Integer id) {
        return workerbrandSettingMapper.selectById(id);
    }

    @Override
    public int updateByPrimaryKeySelective(WorkerbrandSetting record) {
        return workerbrandSettingMapper.update(record,new UpdateWrapper<>());
    }

    @Override
    public int updateByPrimaryKey(WorkerbrandSetting record) {
        return workerbrandSettingMapper.updateById(record);
    }

    @Override
    public int updateById(int id) {
        return workerbrandSettingMapper.deleteById(id);
    }

    @Override
    public PageInfo<WorkerbrandSetting> selectByOther(String keyWord, int pageNum, int pageSize) {
        if (!StringUtils.isEmpty(keyWord)) {
            keyWord = "%" + keyWord + "%";
        }
        PageHelper.startPage(pageNum, pageSize);
        List<WorkerbrandSetting> workerbrandSettingList = workerbrandSettingMapper.selectByOther(keyWord);
        PageInfo<WorkerbrandSetting> pageInfo = new PageInfo<>(workerbrandSettingList);
        return pageInfo;
    }

    @Override
    public HashMap<Integer, String> selectWorkerbrandMap() {
        HashMap<Integer, String> workerbrandMap = new HashMap<>();
        List<WorkerbrandSetting> workerbrandSettingList = workerbrandSettingMapper.selectByOther(null);
        workerbrandSettingList.forEach(workerbrandSetting -> {
            String brandName = workerbrandSetting.getBrandName();
            String difficulty = workerbrandSetting.getDifficulty();
            String workerBrand = brandName + " " + difficulty;
            Integer id = workerbrandSetting.getId();
            workerbrandMap.put(id, workerBrand);
        });
        return workerbrandMap;
    }

}




