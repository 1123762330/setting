package com.xnpool.setting.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xnpool.setting.common.BaseController;
import com.xnpool.setting.domain.model.FrameSettingExample;
import com.xnpool.setting.domain.redismodel.FrameSettingRedisModel;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import com.xnpool.setting.domain.mapper.FrameSettingMapper;
import com.xnpool.setting.domain.pojo.FrameSetting;
import com.xnpool.setting.service.FrameSettingService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @author zly
 * @version 1.0
 * @date 2020/2/4 20:04
 */
@Service
public class FrameSettingServiceImpl extends BaseController implements FrameSettingService {

    @Resource
    private FrameSettingMapper frameSettingMapper;

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return frameSettingMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(FrameSetting record) {
        return frameSettingMapper.insert(record);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertSelective(FrameSetting record) {
        String framename = record.getFrameName();
        Integer number = record.getNumber();
        String detailed = framename + " 1-" + number + "层";
        record.setDetailed(detailed);
        int rows = frameSettingMapper.insertSelective(record);
        record.setCreateTime(new Date());
        FrameSettingRedisModel factoryHouseRedisModel = getFactoryHouseRedisModel(record);
        redisToInsert(rows, "frame_setting", factoryHouseRedisModel, record.getMineId());
    }

    @Override
    public FrameSetting selectByPrimaryKey(Integer id) {
        return frameSettingMapper.selectByPrimaryKey(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateByPrimaryKeySelective(FrameSetting record) {
        String framename = record.getFrameName();
        Integer number = record.getNumber();
        String detailed = framename + " 1-" + number + "层";
        record.setDetailed(detailed);
        int rows = frameSettingMapper.updateByPrimaryKeySelective(record);
        record.setUpdateTime(new Date());
        FrameSettingRedisModel factoryHouseRedisModel = getFactoryHouseRedisModel(record);
        redisToUpdate(rows, "frame_setting", factoryHouseRedisModel, record.getMineId());
    }

    @Override
    public int updateByPrimaryKey(FrameSetting record) {
        return frameSettingMapper.updateByPrimaryKey(record);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateById(int id) {
        int rows = frameSettingMapper.updateById(id);
        FrameSetting record = new FrameSetting();
        record.setUpdateTime(new Date());
        record.setId(id);
        FrameSettingRedisModel factoryHouseRedisModel = getFactoryHouseRedisModel(record);
        redisToDelete(rows, "frame_setting", factoryHouseRedisModel, record.getMineId());
    }

    @Override
    public PageInfo<FrameSettingExample> selectByOther(String keyWord, int pageNum, int pageSize) {
        if (!StringUtils.isEmpty(keyWord)) {
            keyWord = "%" + keyWord + "%";
        }
        PageHelper.startPage(pageNum, pageSize);
        List<FrameSettingExample> frameSettingExamples = frameSettingMapper.selectByOther(keyWord);
        PageInfo<FrameSettingExample> pageInfo = new PageInfo<>(frameSettingExamples);
        return pageInfo;
    }

    @Override
    public HashMap<Integer, String> selectFrameNameByFactoryId(Integer factoryId) {
        List<HashMap> hashMaps = frameSettingMapper.selectFrameNameByFactoryId(factoryId);
        if (hashMaps.isEmpty()) {
            return null;
        } else {
            HashMap<Integer, String> resultMap = new HashMap<>();
            hashMaps.forEach(hashMap -> {
                Integer id = Integer.valueOf(hashMap.get("id").toString());
                String factoryName = hashMap.get("frame_name").toString();
                resultMap.put(id, factoryName);
            });
            return resultMap;
        }
    }

    /**
     * @return
     * @Description 添加分组时需要用到的矿机架列表
     * @Author zly
     * @Date 15:29 2020/2/10
     * @Param
     */
    public HashMap<Integer, String> selectFrameNameToGruop(Integer factoryId) {
        List<HashMap> hashMaps = frameSettingMapper.selectFrameNameByFactoryId(factoryId);
        if (hashMaps.isEmpty()) {
            return null;
        } else {
            HashMap<Integer, String> resultMap = new HashMap<>();
            hashMaps.forEach(hashMap -> {
                Integer id = Integer.valueOf(hashMap.get("id").toString());
                StringBuffer frameName = new StringBuffer(hashMap.get("frame_name").toString());
                StringBuffer number = new StringBuffer(hashMap.get("number").toString());
                StringBuffer resultStr = frameName.append("  1-" + number + "层");
                resultMap.put(id, resultStr.toString());
            });
            return resultMap;
        }
    }

    @Override
    public HashMap<String, HashMap> selectMineFactoryAndFrame() {
        List<HashMap> mineFactoryAndFrameList = frameSettingMapper.selectMineFactoryAndFrame();
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



