package com.xnpool.setting.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xnpool.setting.common.BaseController;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
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
        record.setCreatetime(new Date());
        redisToInsert(rows,"mine_setting",record,record.getId());
    }

    @Override
    public MineSetting selectByPrimaryKey(Integer id) {
        return minesettingMapper.selectByPrimaryKey(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateByPrimaryKeySelective(MineSetting record) {
        int rows = minesettingMapper.updateByPrimaryKeySelective(record);
        record.setUpdatetime(new Date());
        redisToUpdate(rows,"mine_setting",record,record.getId());
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
        record.setUpdatetime(new Date());
        record.setId(id);
        String jsonString = JSON.toJSONString(record, true);
        redisToDelete(rows,"mine_setting",record,record.getId());
    }

    @Override
    public HashMap<Integer, String> selectPoolNameByOther() {
        List<MineSetting> mineSettingList = minesettingMapper.selectByOther(null);
        HashMap<Integer, String> resultMap = new HashMap<>();
        mineSettingList.forEach(mineSetting -> {
            Integer id = mineSetting.getId();
            String mineName = mineSetting.getMinename();
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
        System.out.println(mineSettingList);
        PageInfo<MineSetting> pageInfo = new PageInfo<>(mineSettingList);
        return pageInfo;
    }

    @Override
    public HashMap<String, HashMap<Integer, String>> selectMineFactoryAndFrame() {
        List<HashMap> mineFactoryAndFrameList = minesettingMapper.selectMineFactoryAndFrame();
        HashMap<Integer, String> mineMap = new HashMap<>();
        HashMap<Integer, String> factroyMap = new HashMap<>();
        HashMap<Integer, String> frameMap = new HashMap<>();
        HashMap<String, HashMap<Integer, String>> resultMap = new HashMap<>();
        for (HashMap hashMap : mineFactoryAndFrameList) {
            Integer frameId = (Integer) hashMap.get("frameId");
            String frameName = String.valueOf(hashMap.get("frameName"));
            Integer factoryId = (Integer) hashMap.get("factoryId");
            String factoryName = String.valueOf(hashMap.get("factoryName"));
            Integer mineId = (Integer) hashMap.get("mineId");
            String mineName = String.valueOf(hashMap.get("mineName"));
            mineMap.put(mineId,mineName);
            factroyMap.put(factoryId,factoryName+"-"+mineName);
            frameMap.put(frameId,frameName+"-"+factoryName+"-"+mineName);
        }
        resultMap.put("mine",mineMap);
        resultMap.put("factory",factroyMap);
        resultMap.put("frame",frameMap);
        return resultMap;
    }

}

