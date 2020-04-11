package com.xnpool.setting.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xnpool.logaop.service.exception.DataExistException;
import com.xnpool.logaop.service.exception.InsertException;
import com.xnpool.logaop.service.exception.UnAuthorisedException;
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
    public void insertSelective(MineSetting record,String token) {
        List<Integer> mineId = getMineId(token);
        if (mineId.contains(-1)){
            List<String> list = minesettingMapper.selectMineNameList(record.getId());
            if (list.contains(record.getMineName())) {
                throw new DataExistException("数据已存在,请勿重复添加!");
            }
            int rows = minesettingMapper.insertSelective(record);
            record.setCreateTime(new Date());
            MineSettingRedisModel mineSettingRedisModel = getMineSettingRedisModel(record);
            redisToInsert(rows, "mine_setting", mineSettingRedisModel, record.getId());
        }else {
            throw new UnAuthorisedException("只有管理员才能添加矿场");
        }


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
        MineSetting mineSetting = minesettingMapper.selectByPrimaryKey(record.getId());
        MineSettingRedisModel mineSettingRedisModel = getMineSettingRedisModel(mineSetting);
        redisToUpdate(rows, "mine_setting", mineSettingRedisModel, mineSetting.getId());

    }

    @Override
    public int updateByPrimaryKey(MineSetting record) {
        return minesettingMapper.updateByPrimaryKey(record);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateById(int id) {
        int rows = minesettingMapper.updateById(id);
        MineSetting mineSetting = minesettingMapper.selectByPrimaryKey(id);
        MineSettingRedisModel mineSettingRedisModel = getMineSettingRedisModel(mineSetting);
        redisToDelete(rows, "mine_setting", mineSettingRedisModel, mineSetting.getId());
    }

    @Override
    public HashMap<Integer, String> selectMineNameByOther(String token) {
        List<Integer> mineIds = getMineId(token);
        List<MineSetting> mineSettingList = minesettingMapper.selectByOther(null);
        HashMap<Integer, String> resultMap = new HashMap<>();
        mineSettingList.forEach(mineSetting -> {
            Integer id = mineSetting.getId();
            if (mineIds.contains(id)){
                String mineName = mineSetting.getMineName();
                resultMap.put(id, mineName);
            }

        });
        return resultMap;
    }

    @Override
    public PageInfo<MineSetting> selectByOther(String keyWord, int pageNum, int pageSize,String token) {
        List<Integer> mineIds = getMineId(token);
        ArrayList<MineSetting> resultList = new ArrayList<>();
        if (!StringUtils.isEmpty(keyWord)) {
            keyWord = "%" + keyWord + "%";
        }
        PageHelper.startPage(pageNum, pageSize);
        List<MineSetting> mineSettingList = minesettingMapper.selectByOther(keyWord);
        for (MineSetting mineSetting : mineSettingList) {
            Integer id = mineSetting.getId();
            if (mineIds.contains(id)){
                resultList.add(mineSetting);
            }
        }
        PageInfo<MineSetting> pageInfo = new PageInfo<>(resultList);
        return pageInfo;
    }


}

