package com.xnpool.setting.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xnpool.setting.common.BaseController;
import com.xnpool.setting.domain.redismodel.MineSettingRedisModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import com.xnpool.setting.domain.pojo.MineSetting;
import com.xnpool.setting.domain.mapper.MineSettingMapper;
import com.xnpool.setting.service.MineSettingService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @author zly
 * @version 1.0
 * @date 2020/2/3 12:46
 */
@Service
@Slf4j
public class MinesettingServiceImpl extends BaseController implements MineSettingService {

    @Resource
    private MineSettingMapper minesettingMapper;

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return minesettingMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(MineSetting record) {
        return minesettingMapper.insert(record);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertSelective(MineSetting record) {
        int rows = minesettingMapper.insertSelective(record);
        record.setCreateTime(new Date());
        MineSettingRedisModel mineSettingRedisModel = getMineSettingRedisModel(record);
        redisToInsert(rows,"mine_setting",mineSettingRedisModel,record.getId());
    }

    @Override
    public MineSetting selectByPrimaryKey(Integer id) {
        return minesettingMapper.selectByPrimaryKey(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateByPrimaryKeySelective(MineSetting record) {
        int rows = minesettingMapper.updateByPrimaryKeySelective(record);
        record.setUpdateTime(new Date());
        MineSettingRedisModel mineSettingRedisModel = getMineSettingRedisModel(record);
        redisToUpdate(rows,"mine_setting",mineSettingRedisModel,record.getId());
    }

    @Override
    public int updateByPrimaryKey(MineSetting record) {
        return minesettingMapper.updateByPrimaryKey(record);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateById(int id) {
        int rows = minesettingMapper.updateById(id);
        MineSetting record = new MineSetting();
        record.setUpdateTime(new Date());
        record.setId(id);
        MineSettingRedisModel mineSettingRedisModel = getMineSettingRedisModel(record);
        redisToDelete(rows,"mine_setting",mineSettingRedisModel,record.getId());
    }

    @Override
    public HashMap<Integer, String> selectPoolNameByOther() {
        List<MineSetting> mineSettingList = minesettingMapper.selectByOther(null);
        HashMap<Integer, String> resultMap = new HashMap<>();
        mineSettingList.forEach(mineSetting -> {
            Integer id = mineSetting.getId();
            String mineName = mineSetting.getMineName();
            resultMap.put(id, mineName);
        });
        return resultMap;
    }

    @Override
    public PageInfo<MineSetting> selectByOther(String keyWord, int pageNum, int pageSize) {
        if (!StringUtils.isEmpty(keyWord)) {
            keyWord = "%" + keyWord + "%";
        }
        PageHelper.startPage(pageNum, pageSize);
        List<MineSetting> mineSettingList = minesettingMapper.selectByOther(keyWord);
        PageInfo<MineSetting> pageInfo = new PageInfo<>(mineSettingList);
        return pageInfo;
    }

    @Override
    public HashMap<String, HashMap> selectMineFactoryAndFrame() {
        List<HashMap> mineFactoryAndFrameList = minesettingMapper.selectMineFactoryAndFrame();
        HashMap<Integer, String> mineMap = new HashMap<>();
        HashMap<Integer, String> factroyMap = new HashMap<>();
        HashMap<String, String> frameMap = new HashMap<>();
        HashMap<String, HashMap> resultMap = new HashMap<>();
        for (HashMap hashMap : mineFactoryAndFrameList) {
            Integer frameId = (Integer) hashMap.get("frameId");
            String frameName = String.valueOf(hashMap.get("frame_name"));
            Integer factoryId = (Integer) hashMap.get("factoryId");
            String factoryName = String.valueOf(hashMap.get("factory_name"));
            Integer mineId = (Integer) hashMap.get("mineId");
            String mineName = String.valueOf(hashMap.get("mine_name"));
            mineMap.put(mineId,mineName);
            factroyMap.put(factoryId,factoryName+"-"+mineName);
            StringBuffer idBuffer = new StringBuffer(String.valueOf(frameId)).append("-").append(factoryId).append("-").append(mineId);
            StringBuffer nameBuffer = new StringBuffer(frameName).append("-").append(factoryName).append("-").append(mineName);
            frameMap.put(idBuffer.toString(),nameBuffer.toString());
        }
        resultMap.put("mine",mineMap);
        resultMap.put("factory",factroyMap);
        resultMap.put("frame",frameMap);
        return resultMap;
    }

}

