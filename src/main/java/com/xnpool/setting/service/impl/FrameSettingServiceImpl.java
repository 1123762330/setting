package com.xnpool.setting.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xnpool.logaop.service.exception.DataExistException;
import com.xnpool.logaop.service.exception.UpdateException;
import com.xnpool.setting.common.BaseController;
import com.xnpool.setting.domain.mapper.MineSettingMapper;
import com.xnpool.setting.domain.mapper.WorkerDetailedMapper;
import com.xnpool.setting.domain.model.FrameSettingExample;
import com.xnpool.setting.domain.pojo.FrameSetting;
import com.xnpool.setting.domain.mapper.FrameSettingMapper;
import com.xnpool.setting.domain.pojo.MineSetting;
import com.xnpool.setting.domain.pojo.WorkerDetailed;
import com.xnpool.setting.domain.redismodel.FrameSettingRedisModel;
import com.xnpool.setting.domain.redismodel.WorkerDetailedRedisModel;
import com.xnpool.setting.service.FrameSettingService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xnpool.setting.utils.PageUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author zly
 * @since 2020-04-17
 */
@Service
@Slf4j
public class FrameSettingServiceImpl extends ServiceImpl<FrameSettingMapper, FrameSetting> implements FrameSettingService {
    @Autowired
    private BaseController baseController;
    @Autowired
    private WorkerDetailedMapper workerDetailedMapper;

    @Autowired
    private FrameSettingMapper frameSettingMapper;

    @Autowired
    private MineSettingMapper mineSettingMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer addFrame(FrameSetting record) {
        Integer mineId = record.getMineId();
        MineSetting mineSetting = mineSettingMapper.selectById(mineId);
        Integer number = mineSetting.getFrameNum();
        Integer exist = frameSettingMapper.isExist(record);
        if (exist != null) {
            throw new DataExistException("数据已存在,请勿重复添加!");
        }
        String framename = record.getFrameName();
        if (StringUtils.isEmpty(framename)) {
            Integer storageRacksNum = record.getStorageRacksNum();
            Integer rowNum = record.getRowNum();
            framename = storageRacksNum + "#" + rowNum;
            record.setFrameName(framename);
        }
        record.setNumber(number);
        String detailed = framename + " 1-" + number + "层";
        record.setDetailed(detailed);
        record.setCreateTime(LocalDateTime.now());
        int rows = frameSettingMapper.insert(record);

        //机架数据同步入缓存
        FrameSettingRedisModel frameSettingRedisModel = baseController.getFrameSettingRedisModel(record);
        baseController.redisToInsert(rows, "frame_setting", frameSettingRedisModel, record.getMineId());

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
            WorkerDetailedRedisModel workerDetailedRedisModel = baseController.getWorkerDetailedRedisModel(workerDetailed);
            baseController.redisToInsert(rows2, "worker_detailed", workerDetailedRedisModel, record.getMineId());
        }
        return rows;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(int id) {
        List<Integer> list = workerDetailedMapper.selectNotExistWorkerFrame(id);
        List<Integer> existList = workerDetailedMapper.selectExistWorkerFrame(id);
        if (existList.size() != 0) {
            throw new DataExistException("该机架上存在已上架的矿机,请先下架再删除!");
        } else {
            int rows2 = workerDetailedMapper.updateById(list);
            log.info("成功删除机架条数:" + rows2);
            int rows = frameSettingMapper.deleteByKeyId(id);
            FrameSetting frameSetting = frameSettingMapper.selectById(id);
            FrameSettingRedisModel frameSettingRedisModel = baseController.getFrameSettingRedisModel(frameSetting);
            baseController.redisToDelete(rows, "frame_setting", frameSettingRedisModel, frameSetting.getMineId());
        }
    }

    @Override
    public Page<FrameSettingExample> selectByOther(String keyWord, int pageNum, int pageSize, String token) {
        List<Integer> mineIds = baseController.getMineId(token);
        List<FrameSettingExample> resultList = new ArrayList<>();
        if (!StringUtils.isEmpty(keyWord)) {
            keyWord = "%" + keyWord + "%";
        }
        Page<FrameSettingExample> page = new Page<>(pageNum, pageSize);
        List<FrameSettingExample> frameSettingExamples = frameSettingMapper.selectByOther(keyWord, page);

        for (FrameSettingExample frameSettingExample : frameSettingExamples) {
            Integer id = frameSettingExample.getMineId();
            if (mineIds.contains(id)) {
                String frameName = frameSettingExample.getFrameName();
                if (StringUtils.isEmpty(frameName)) {
                    Integer storageRacksNum = frameSettingExample.getStorageRacksNum();
                    Integer rowNum = frameSettingExample.getRowNum();
                    frameName = storageRacksNum + "#" + rowNum;
                    frameSettingExample.setFrameName(frameName);
                }
                resultList.add(frameSettingExample);
            }
        }
        page.setRecords(resultList);
        return page;
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
                //在这里需要去查询该矿机架是否还有空位
                List<HashMap> hashMapList = workerDetailedMapper.selectNullFrame(id);
                if (!hashMapList.isEmpty()) {
                    String frame_name = hashMap.get("frame_name").toString();
                    if (StringUtils.isEmpty(frame_name)) {
                        String storage_racks_num = hashMap.get("storage_racks_num").toString();
                        String row_num = hashMap.get("row_num").toString();
                        frame_name = storage_racks_num + "#" + row_num;
                    }
                    resultMap.put(id, frame_name);
                }

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
                if (StringUtils.isEmpty(frameName.toString())) {
                    String storage_racks_num = hashMap.get("storage_racks_num").toString();
                    String row_num = hashMap.get("row_num").toString();
                    frameName = new StringBuffer(storage_racks_num).append("#").append(row_num);
                }
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
            StringBuffer frameName = new StringBuffer(String.valueOf(hashMap.get("frame_name")));
            if (StringUtils.isEmpty(frameName.toString())) {
                String storage_racks_num = hashMap.get("storage_racks_num").toString();
                String row_num = hashMap.get("row_num").toString();
                frameName = new StringBuffer(storage_racks_num).append("#").append(row_num);
            }
            String factoryName = String.valueOf(hashMap.get("factory_name"));
            String mineName = String.valueOf(hashMap.get("mine_name"));
            StringBuffer nameBuffer = frameName.append("-").append(factoryName).append("-").append(mineName);
            frameMap.put(frameId, nameBuffer.toString());
        }
        return frameMap;
    }

    @Override
    public Integer equalsFrameName(Integer frameStr, Integer paiNumber, Integer factoryId, Integer mineId) {
        return frameSettingMapper.equalsFrameName(frameStr, paiNumber, factoryId, mineId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer insertSelectiveToBatch(FrameSetting record) {
        String framename = record.getFrameName();
        Integer number = record.getNumber();
        String detailed = framename + " 1-" + number + "层";
        record.setDetailed(detailed);
        record.setCreateTime(LocalDateTime.now());
        int rows = frameSettingMapper.insert(record);

        //执行入缓存操作
        FrameSettingRedisModel frameSettingRedisModel = baseController.getFrameSettingRedisModel(record);
        baseController.redisToInsert(rows, "frame_setting", frameSettingRedisModel, record.getMineId());

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
            WorkerDetailedRedisModel workerDetailedRedisModel = baseController.getWorkerDetailedRedisModel(workerDetailed);
            baseController.redisToInsert(rows2, "worker_detailed", workerDetailedRedisModel, workerDetailed.getMineId());
        }
        return record.getId();
    }

    @Override
    public void updateFrame(FrameSetting record) {
        //首先去详情表查询该机架是否在使用
        Integer exist = frameSettingMapper.isExist(record);
        if (exist != null) {
            throw new DataExistException("数据已存在,请勿重复添加!");
        }

        Integer mineId = record.getMineId();
        if (mineId != null) {
            FrameSetting frameSetting_db = frameSettingMapper.selectById(record.getId());
            Integer mineId_db = frameSetting_db.getMineId();
            if (mineId != mineId_db) {
                throw new UpdateException("如需更换矿场,请先删除该机架!");
            }
        }

        String framename = record.getFrameName();
        Integer number = record.getNumber();
        String detailed = framename + " 1-" + number + "层";
        record.setDetailed(detailed);
        int rows = frameSettingMapper.updateById(record);

        FrameSetting frameSetting = frameSettingMapper.selectById(record.getId());
        FrameSettingRedisModel frameSettingRedisModel = baseController.getFrameSettingRedisModel(frameSetting);
        baseController.redisToUpdate(rows, "frame_setting", frameSettingRedisModel, frameSetting.getMineId());
    }
}
