package com.xnpool.setting.service.impl;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xnpool.setting.common.BaseController;
import com.xnpool.setting.domain.mapper.MineSettingMapper;
import com.xnpool.setting.domain.pojo.MineSetting;
import com.xnpool.setting.domain.redismodel.WorkerbrandSettingRedisModel;
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
public class WorkerbrandSettingServiceImpl extends BaseController implements WorkerbrandSettingService {

    @Resource
    private WorkerbrandSettingMapper workerbrandSettingMapper;

    @Autowired
    private MineSettingMapper mineSettingMapper;

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return workerbrandSettingMapper.deleteById(id);
    }

    @Override
    public int insert(WorkerbrandSetting record) {
        int rows = workerbrandSettingMapper.insert(record);
        WorkerbrandSettingRedisModel workerbrandSettingRedisModel = getWorkerbrandSettingRedisModel(record);
        List<MineSetting> mineSettingList = mineSettingMapper.selectByOther(null);
        for (MineSetting mineSetting : mineSettingList) {
            Integer mineId = mineSetting.getId();
            redisToInsert(rows, "workerbrand_setting", workerbrandSettingRedisModel, mineId);
        }
        return rows;
    }

    @Override
    public WorkerbrandSetting selectByPrimaryKey(Integer id) {
        return workerbrandSettingMapper.selectById(id);
    }

    @Override
    public int updateByPrimaryKeySelective(WorkerbrandSetting record) {
        int rows = workerbrandSettingMapper.updateById(record);
        WorkerbrandSetting workerbrandSetting = workerbrandSettingMapper.selectById(record.getId());
        WorkerbrandSettingRedisModel workerbrandSettingRedisModel = getWorkerbrandSettingRedisModel(workerbrandSetting);
        List<MineSetting> mineSettingList = mineSettingMapper.selectByOther(null);
        for (MineSetting mineSetting : mineSettingList) {
            Integer mineId = mineSetting.getId();
            redisToUpdate(rows, "workerbrand_setting", workerbrandSettingRedisModel, mineId);
        }
        return rows;
    }

    @Override
    public int updateByPrimaryKey(WorkerbrandSetting record) {
        return workerbrandSettingMapper.updateById(record);
    }

    @Override
    public int updateById(int id) {
        int rows = workerbrandSettingMapper.deleteByIdKey(id);
        WorkerbrandSetting workerbrandSetting = workerbrandSettingMapper.selectById(id);
        WorkerbrandSettingRedisModel workerbrandSettingRedisModel = getWorkerbrandSettingRedisModel(workerbrandSetting);
        List<MineSetting> mineSettingList = mineSettingMapper.selectByOther(null);
        for (MineSetting mineSetting : mineSettingList) {
            Integer mineId = mineSetting.getId();
            redisToDelete(rows, "workerbrand_setting", workerbrandSettingRedisModel, mineId);
        }
        return rows;
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
            String workerType = workerbrandSetting.getWorkerType();
            String workerBrand = brandName + " " + workerType;
            Integer id = workerbrandSetting.getId();
            workerbrandMap.put(id, workerBrand);
        });
        return workerbrandMap;
    }

    @Override
    public HashMap<String,Integer> selectMapByWorkerType() {
        HashMap<String,Integer> workerbrandMap = new HashMap<>();
        List<WorkerbrandSetting> workerbrandSettingList = workerbrandSettingMapper.selectByOther(null);
        workerbrandSettingList.forEach(workerbrandSetting -> {
            String workerType = workerbrandSetting.getWorkerType();
            Integer id = workerbrandSetting.getId();
            workerbrandMap.put(workerType, id);
        });
        return workerbrandMap;
    }

}




