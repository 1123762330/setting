package com.xnpool.setting.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xnpool.setting.common.BaseController;
import com.xnpool.setting.domain.mapper.WorkerDetailedMapper;
import com.xnpool.setting.domain.model.FrameSettingExample;
import com.xnpool.setting.domain.pojo.WorkerDetailed;
import com.xnpool.setting.domain.redismodel.FrameSettingRedisModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import com.xnpool.setting.domain.mapper.FrameSettingMapper;
import com.xnpool.setting.domain.pojo.FrameSetting;
import com.xnpool.setting.service.FrameSettingService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
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

    @Autowired
    private WorkerDetailedMapper workerDetailedMapper;

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
        List<WorkerDetailed> list = new ArrayList<>();
        for (int i = 1; i <= number; i++) {
            WorkerDetailed workerDetailed = new WorkerDetailed();
            workerDetailed.setFactoryId(record.getFactoryId());
            workerDetailed.setFrameId(record.getId());
            workerDetailed.setFrameNumber(i);
            workerDetailed.setMineId(record.getMineId());
            workerDetailed.setCreateTime(new Date());
            workerDetailed.setUpdateTime(new Date());
            list.add(workerDetailed);
        }
        int rows2 = workerDetailedMapper.batchInsert(list);
        record.setCreateTime(new Date());
        FrameSettingRedisModel factoryHouseRedisModel = getFactoryHouseRedisModel(record);
        //批量入缓存
        redisToInsert(rows, "frame_setting", factoryHouseRedisModel, record.getMineId());
        redisToBatchInsert(rows2, "worker_detailed", list, record.getMineId());
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
    public HashMap<Integer, String> selectMineFactoryAndFrame(Integer factoryId) {
        List<HashMap> mineFactoryAndFrameList = frameSettingMapper.selectMineFactoryAndFrame(factoryId);
        HashMap<Integer, String> frameMap = new HashMap<>();
        for (HashMap hashMap : mineFactoryAndFrameList) {
            Integer frameId = (Integer) hashMap.get("frameId");
            String frameName = String.valueOf(hashMap.get("frame_name"));
            String factoryName = String.valueOf(hashMap.get("factory_name"));
            String mineName = String.valueOf(hashMap.get("mine_name"));
            StringBuffer nameBuffer = new StringBuffer(frameName).append("-").append(factoryName).append("-").append(mineName);
            frameMap.put(frameId,nameBuffer.toString());
        }
        return frameMap;
    }

}



