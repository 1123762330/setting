package com.xnpool.setting.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xnpool.logaop.service.exception.DataExistException;
import com.xnpool.logaop.service.exception.InsertException;
import com.xnpool.logaop.service.exception.UpdateException;
import com.xnpool.setting.common.BaseController;
import com.xnpool.setting.domain.mapper.MineSettingMapper;
import com.xnpool.setting.domain.mapper.WorkerDetailedMapper;
import com.xnpool.setting.domain.model.FactoryHouseExample;
import com.xnpool.setting.domain.model.FrameSettingExample;
import com.xnpool.setting.domain.pojo.MineSetting;
import com.xnpool.setting.domain.pojo.WorkerDetailed;
import com.xnpool.setting.domain.redismodel.FrameSettingRedisModel;
import com.xnpool.setting.domain.redismodel.WorkerDetailedRedisModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import com.xnpool.setting.domain.mapper.FrameSettingMapper;
import com.xnpool.setting.domain.pojo.FrameSetting;
import com.xnpool.setting.service.FrameSettingService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
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
@Slf4j
public class FrameSettingServiceImpl extends BaseController implements FrameSettingService {

    @Resource
    private FrameSettingMapper frameSettingMapper;

    @Autowired
    private WorkerDetailedMapper workerDetailedMapper;

    @Autowired
    private MineSettingMapper mineSettingMapper;

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
    public Integer insertSelective(FrameSetting record) {
        Integer mineId = record.getMineId();
        MineSetting mineSetting = mineSettingMapper.selectByPrimaryKey(mineId);
        Integer number = mineSetting.getFrameNum();
        List<String> frameNameList = frameSettingMapper.selectFrameNameList(record.getFactoryId(), record.getId());
        if (frameNameList.contains(record.getFrameName())) {
            throw new DataExistException("数据已存在,请勿重复添加!");
        }
        String framename = record.getFrameName();
        record.setNumber(number);
        String detailed = framename + " 1-" + number + "层";
        record.setDetailed(detailed);
        record.setCreateTime(new Date());
        int rows = frameSettingMapper.insertSelective(record);

        //机架数据同步入缓存
        FrameSettingRedisModel frameSettingRedisModel = getFrameSettingRedisModel(record);
        redisToInsert(rows, "frame_setting", frameSettingRedisModel, record.getMineId());

        //同时在矿机详情表生成数据
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
        //批量入缓存
        for (WorkerDetailed workerDetailed : list) {
            WorkerDetailedRedisModel workerDetailedRedisModel = getWorkerDetailedRedisModel(workerDetailed);
            redisToInsert(rows2, "worker_detailed", workerDetailedRedisModel, record.getMineId());
        }
        return rows;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer insertSelectiveToBatch(FrameSetting record) {
        String framename = record.getFrameName();
        Integer number = record.getNumber();
        String detailed = framename + " 1-" + number + "层";
        record.setDetailed(detailed);
        record.setCreateTime(new Date());
        int rows = frameSettingMapper.insertSelective(record);

        //执行入缓存操作
        FrameSettingRedisModel frameSettingRedisModel = getFrameSettingRedisModel(record);
        redisToInsert(rows, "frame_setting", frameSettingRedisModel, record.getMineId());

        //同时在矿机详情表生成数据
        List<WorkerDetailed> list = new ArrayList<>();
        for (int i = 1; i <= number; i++) {
            WorkerDetailed workerDetailed = new WorkerDetailed();
            workerDetailed.setFactoryId(record.getFactoryId());
            workerDetailed.setFrameId(record.getId());
            workerDetailed.setFrameNumber(i);
            workerDetailed.setMineId(record.getMineId());
            workerDetailed.setIsComeIn(0);
            workerDetailed.setIsDelete(0);
            workerDetailed.setCreateTime(new Date());
            workerDetailed.setUpdateTime(new Date());
            list.add(workerDetailed);
        }
        int rows2 = workerDetailedMapper.batchInsert(list);
        //详情表同步入缓存
        //System.out.println("批量查询详情表返回的自增id:"+list);
        //List<WorkerDetailed> workerDetailedRedisModels = workerDetailedMapper.selectModelToRedis(list);
        for (WorkerDetailed workerDetailed : list) {
            WorkerDetailedRedisModel workerDetailedRedisModel = getWorkerDetailedRedisModel(workerDetailed);
            redisToInsert(rows2, "worker_detailed", workerDetailedRedisModel, workerDetailed.getMineId());
        }
        return record.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer insertByNotExits(FrameSetting record) {
        List<String> frameNameList = frameSettingMapper.selectFrameNameList(record.getFactoryId(), record.getId());
        if (frameNameList.contains(record.getFrameName())) {
            throw new DataExistException("数据已存在,请勿重复添加!");
        }
        String framename = record.getFrameName();
        Integer number = record.getNumber();
        String detailed = framename + " 1-" + number + "层";
        record.setDetailed(detailed);
        record.setCreateTime(new Date());
        int rows = frameSettingMapper.insertSelective(record);
        //同时在矿机详情表生成数据
        List<WorkerDetailed> list = new ArrayList<>();
        List<WorkerDetailedRedisModel> redisList = new ArrayList<>();
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

        FrameSettingRedisModel frameSettingRedisModel = getFrameSettingRedisModel(record);
        redisToInsert(rows, "frame_setting", frameSettingRedisModel, record.getMineId());
        return rows;
    }

    @Override
    public FrameSetting selectByPrimaryKey(Integer id) {
        return frameSettingMapper.selectByPrimaryKey(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateByPrimaryKeySelective(FrameSetting record) {
        //首先去详情表查询该机架是否在使用
        List<String> frameNameList = frameSettingMapper.selectFrameNameList(record.getFactoryId(), record.getId());
        if (frameNameList.contains(record.getFrameName())) {
            throw new DataExistException("数据已存在,请勿重复添加!");
        }

        Integer mineId = record.getMineId();
        if (mineId!=null){
            FrameSetting frameSetting_db = frameSettingMapper.selectByPrimaryKey(record.getId());
            Integer mineId_db = frameSetting_db.getMineId();
            if (mineId!=mineId_db){
                throw new UpdateException("如需更换矿场,请先删除该机架!");
            }
        }

        String framename = record.getFrameName();
        Integer number = record.getNumber();
        String detailed = framename + " 1-" + number + "层";
        record.setDetailed(detailed);
        int rows = frameSettingMapper.updateByPrimaryKeySelective(record);

        FrameSetting frameSetting = frameSettingMapper.selectByPrimaryKey(record.getId());
        FrameSettingRedisModel frameSettingRedisModel = getFrameSettingRedisModel(frameSetting);
        redisToUpdate(rows, "frame_setting", frameSettingRedisModel, frameSetting.getMineId());
    }

    @Override
    public int updateByPrimaryKey(FrameSetting record) {
        return frameSettingMapper.updateByPrimaryKey(record);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateById(int id) {
        List<Integer> list = workerDetailedMapper.selectNotExistWorkerFrame(id);
        List<Integer> existList = workerDetailedMapper.selectExistWorkerFrame(id);
        if (existList.size()!=0){
            throw new DataExistException("该机架上存在已上架的矿机,请先下架再删除!");
        }else {
            int rows2 = workerDetailedMapper.updateById(list);
            log.info("成功删除机架条数:"+rows2);
            int rows = frameSettingMapper.updateById(id);
            FrameSetting frameSetting = frameSettingMapper.selectByPrimaryKey(id);
            FrameSettingRedisModel frameSettingRedisModel = getFrameSettingRedisModel(frameSetting);
            redisToDelete(rows, "frame_setting", frameSettingRedisModel, frameSetting.getMineId());
        }

    }

    @Override
    public PageInfo<FrameSettingExample> selectByOther(String keyWord, int pageNum, int pageSize,String token) {
        List<Integer> mineIds = getMineId(token);
        ArrayList<FrameSettingExample> resultList = new ArrayList<>();
        if (!StringUtils.isEmpty(keyWord)) {
            keyWord = "%" + keyWord + "%";
        }
        PageHelper.startPage(pageNum, pageSize);
        List<FrameSettingExample> frameSettingExamples = frameSettingMapper.selectByOther(keyWord);
        for (FrameSettingExample frameSettingExample : frameSettingExamples) {
            Integer id = frameSettingExample.getMineId();
            if (mineIds.contains(id)){
                resultList.add(frameSettingExample);
            }
        }
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
            frameMap.put(frameId, nameBuffer.toString());
        }
        return frameMap;
    }

    @Override
    public Integer equalsFrameName(String frameStr, Integer factoryId, Integer mineId) {
        return frameSettingMapper.equalsFrameName(frameStr, factoryId, mineId);
    }

}



