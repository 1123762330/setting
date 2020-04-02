package com.xnpool.setting.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xnpool.logaop.service.exception.DataExistException;
import com.xnpool.logaop.service.exception.InsertException;
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

import java.util.ArrayList;
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
        List<String> list = minesettingMapper.selectMineNameList(record.getId());
        if (list.contains(record.getMineName())) {
            throw new DataExistException("数据已存在,请勿重复添加!");
        }
        int rows = minesettingMapper.insertSelective(record);
        record.setCreateTime(new Date());
        MineSettingRedisModel mineSettingRedisModel = getMineSettingRedisModel(record);
        redisToInsert(rows, "mine_setting", mineSettingRedisModel, record.getId());

    }

    @Override
    public MineSetting selectByPrimaryKey(Integer id) {
        return minesettingMapper.selectByPrimaryKey(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateByPrimaryKeySelective(MineSetting record) {
        List<String> list = minesettingMapper.selectMineNameList(record.getId());
        if (list.contains(record.getMineName())) {
            throw new DataExistException("数据已存在,请勿重复添加!");
        }

        int rows = minesettingMapper.updateByPrimaryKeySelective(record);
        record.setUpdateTime(new Date());
        MineSettingRedisModel mineSettingRedisModel = getMineSettingRedisModel(record);
        redisToUpdate(rows, "mine_setting", mineSettingRedisModel, record.getId());

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
        redisToDelete(rows, "mine_setting", mineSettingRedisModel, record.getId());
    }

    @Override
    public HashMap<Integer, String> selectMineNameByOther() {
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


}

