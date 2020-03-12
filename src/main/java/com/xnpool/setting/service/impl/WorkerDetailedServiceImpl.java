package com.xnpool.setting.service.impl;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xnpool.setting.common.BaseController;
import com.xnpool.setting.common.exception.DeleteException;
import com.xnpool.setting.common.exception.InsertException;
import com.xnpool.setting.domain.mapper.WorkerInfoMapper;
import com.xnpool.setting.domain.pojo.*;
import com.xnpool.setting.domain.redismodel.WorkerDetailedRedisModel;
import com.xnpool.setting.service.IpSettingService;
import com.xnpool.setting.service.OperatorWorkerHistoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import com.xnpool.setting.domain.mapper.WorkerDetailedMapper;
import com.xnpool.setting.service.WorkerDetailedService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * @author zly
 * @version 1.0
 * @date 2020/2/23 12:52
 */
@Service
@Slf4j
public class WorkerDetailedServiceImpl extends BaseController implements WorkerDetailedService {

    @Resource
    private WorkerDetailedMapper workerDetailedMapper;

    @Resource
    private WorkerInfoMapper workerInfoMapper;

    @Autowired
    private IpSettingService ipSettingService;

    @Autowired
    private OperatorWorkerHistoryService operatorWorkerHistoryService;

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return workerDetailedMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(WorkerDetailed record) {
        return workerDetailedMapper.insert(record);
    }

    @Override
    public int insertSelective(WorkerDetailed record) {
        return workerDetailedMapper.insertSelective(record);
    }

    @Override
    public WorkerDetailed selectByPrimaryKey(Integer id) {
        return workerDetailedMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(WorkerDetailed record) {
        return workerDetailedMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(WorkerDetailed record) {
        return workerDetailedMapper.updateByPrimaryKey(record);
    }

    @Override
    public PageInfo<WorkerDetailedExample> selectMoveOutList(String keyWord, int pageNum, int pageSize) {
        if (!StringUtils.isEmpty(keyWord)) {
            keyWord = "%" + keyWord + "%";
        }
        PageHelper.startPage(pageNum, pageSize);
        List<WorkerDetailedExample> WorkerDetailedExampleList = workerDetailedMapper.selectMoveOutList(keyWord);
        WorkerDetailedExampleList.forEach(workerDetailedExample -> {
            String workerName = workerDetailedExample.getWorkerName();
            int lastIndexOf = workerName.lastIndexOf(".");
            String minerName = workerName.substring(0, lastIndexOf);
            String workerNameStr = workerName.substring(lastIndexOf + 1);
            String frameName = workerDetailedExample.getFrameName();
            Integer frameNumber = workerDetailedExample.getFrameNumber();
            workerDetailedExample.setMiner(minerName);
            workerDetailedExample.setWorkerName(workerNameStr);
            StringBuffer frameNameBuffer = new StringBuffer(frameName).append(" ").append(frameNumber).append("层");
            workerDetailedExample.setFrameName(frameNameBuffer.toString());
        });
        PageInfo<WorkerDetailedExample> pageInfo = new PageInfo<>(WorkerDetailedExampleList);
        return pageInfo;
    }

    /**
     * @return
     * @Description 添加矿机到出入库管理表中
     * @Author zly
     * @Date 13:38 2020/2/26
     * @Param
     */
    @Override
    public void addWorkerToLibrary(WorkerDetailedParam workerDetailedParam) {
        String workerid = workerDetailedParam.getWorkerid();
        Integer userId = workerDetailedParam.getUserId();
        Integer factoryid = workerDetailedParam.getFactoryid();
        Integer workerbrandId = workerDetailedParam.getWorkerbrandId();
        Integer frameid = workerDetailedParam.getFrameid();
        Integer framenumber = workerDetailedParam.getFramenumber();
        Integer groupid = workerDetailedParam.getGroupid();
        Integer mineId = workerDetailedParam.getMineId();
        String workerIp = workerDetailedParam.getWorkerIp();
        String remarks = workerDetailedParam.getRemarks();
        List<Integer> workerIdList = workerDetailedMapper.selectWorkerIdlist(null);
        WorkerDetailedServiceImpl.log.info("库中已经存在的矿机ID:" + workerIdList);
        ArrayList<WorkerDetailed> list = new ArrayList<>();
        ArrayList<WorkerDetailedRedisModel> redisModelList = new ArrayList<>();
        if (workerid.contains(",")) {
            //批量添加
            String[] split = workerid.split(",");
            String[] split_ip = workerIp.split(",");
            for (int i = 0; i < split.length; i++) {
                if (workerIdList.contains(Integer.valueOf(split[i]))) {
                    throw new InsertException("ID为" + split[i] + "的矿机已经添加过!");
                } else {
                    String workerId = split[i];
                    WorkerDetailed workerDetailed = new WorkerDetailed();
                    workerDetailed.setWorkerId(Integer.valueOf(workerId));
                    workerDetailed.setWorkerIp(split_ip[i]);
                    workerDetailed.setUserId(userId);
                    workerDetailed.setWorkerbrandId(workerbrandId);
                    workerDetailed.setFactoryId(factoryid);
                    workerDetailed.setFrameId(frameid);
                    workerDetailed.setFrameNumber(framenumber);
                    workerDetailed.setGroupId(groupid);
                    workerDetailed.setMineId(mineId);
                    workerDetailed.setCreateTime(new Date());
                    workerDetailed.setRemarks(remarks);
                    list.add(workerDetailed);

                    WorkerDetailedRedisModel workerDetailedRedisModel = new WorkerDetailedRedisModel();
                    workerDetailedRedisModel.setWorker_id(Integer.valueOf(workerId));
                    workerDetailedRedisModel.setUser_id(userId);
                    workerDetailedRedisModel.setWorkerbrand_id(workerbrandId);
                    workerDetailedRedisModel.setFactory_id(factoryid);
                    workerDetailedRedisModel.setFrame_id(frameid);
                    workerDetailedRedisModel.setFrame_number(framenumber);
                    workerDetailedRedisModel.setGroup_id(groupid);
                    workerDetailedRedisModel.setMine_id(mineId);
                    workerDetailedRedisModel.setRemarks(remarks);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    workerDetailedRedisModel.setCreate_time(sdf.format(new Date()));
                    workerDetailedRedisModel.setWorker_ip(split_ip[i]);
                    redisModelList.add(workerDetailedRedisModel);
                }
            }
        } else {
            if (workerIdList.contains(workerid)) {
                throw new InsertException("该矿机已经添加过!");
            } else {
                WorkerDetailed workerDetailed = new WorkerDetailed();
                workerDetailed.setWorkerId(Integer.valueOf(workerid));
                workerDetailed.setWorkerIp(workerIp);
                workerDetailed.setUserId(userId);
                workerDetailed.setFactoryId(factoryid);
                workerDetailed.setFrameId(frameid);
                workerDetailed.setFrameNumber(framenumber);
                workerDetailed.setGroupId(groupid);
                workerDetailed.setMineId(mineId);
                workerDetailed.setCreateTime(new Date());
                workerDetailed.setWorkerIp(workerIp);
                list.add(workerDetailed);

                WorkerDetailedRedisModel workerDetailedRedisModel = new WorkerDetailedRedisModel();
                workerDetailedRedisModel.setWorker_id(Integer.valueOf(workerid));
                workerDetailedRedisModel.setUser_id(userId);
                workerDetailedRedisModel.setWorkerbrand_id(workerbrandId);
                workerDetailedRedisModel.setFactory_id(factoryid);
                workerDetailedRedisModel.setFrame_id(frameid);
                workerDetailedRedisModel.setFrame_number(framenumber);
                workerDetailedRedisModel.setGroup_id(groupid);
                workerDetailedRedisModel.setMine_id(mineId);
                workerDetailedRedisModel.setRemarks(remarks);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                workerDetailedRedisModel.setCreate_time(sdf.format(new Date()));
                workerDetailedRedisModel.setWorker_ip(workerIp);

                redisModelList.add(workerDetailedRedisModel);
            }
        }

        //批量入管理仓库
        int rows = workerDetailedMapper.batchInsert(list);
        //批量入缓存
        redisToBatchInsert(rows, "worker_detailed", redisModelList, mineId);
    }

    //入库列表
    @Override
    public PageInfo<WorkerExample> selectComeInWorkerList(String keyWord, int pageNum, int pageSize) {
        if (!StringUtils.isEmpty(keyWord)) {
            keyWord = "%" + keyWord + "%";
        }
        //取IP字段区间Map,便于后面取值
        HashMap<Integer, String> selectByIPStart = ipSettingService.selectByIPStart();
        List<WorkerExample> result = new ArrayList<>();
        HashMap<String, String> ipMap = new HashMap<>();
        for (Map.Entry<Integer, String> entry : selectByIPStart.entrySet()) {
            String value = entry.getValue();
            String[] split = value.split("-");
            String ipStart = split[0];
            int lastIndexOf = ipStart.lastIndexOf(".");
            String key = ipStart.substring(0, lastIndexOf);
            ipMap.put(key, value);
        }
        PageHelper.startPage(pageNum, pageSize);
        List<WorkerInfo> workers = workerInfoMapper.selectByOther(keyWord);
        //已经入库的矿机Id
        List<Integer> comeInlist = workerDetailedMapper.selectWorkerIdlist(1);

        //遍历list集合,setIP所属区间进去
        for (WorkerInfo worker : workers) {
            int lastIndexOf = worker.getIp().lastIndexOf(".");
            String substring = worker.getIp().substring(0, lastIndexOf);
            String ip_quJian = ipMap.get(substring);
            //这里需要做个判断,判断这个矿机有没有入库,如果入库列表里有那就是1,如果没有就是0
            if (comeInlist.contains(worker.getId())) {
                WorkerExample workerExample = new WorkerExample(Integer.valueOf(worker.getId().toString()), worker.getIp(), ip_quJian, Integer.valueOf(worker.getState()), 1);
                result.add(workerExample);
            } else {
                WorkerExample workerExample = new WorkerExample(Integer.valueOf(worker.getId().toString()), worker.getIp(), ip_quJian, Integer.valueOf(worker.getState()), 0);
                result.add(workerExample);
            }
        }
        PageInfo<WorkerExample> pageInfo = new PageInfo<>(result);
        return pageInfo;
    }

    //出库操作
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateMoveOutByid(String ids, String reason, String token) {
        ArrayList<Integer> list = new ArrayList<>();
        //从token中取出userid
        int userId = 12;
        if (ids.contains(",")) {
            //全部出库
            String[] split = ids.split(",");
            for (int i = 0; i < split.length; i++) {
                list.add(Integer.valueOf(split[i]));
            }
        } else {
            //单个出库
            list.add(Integer.valueOf(ids));
        }
        workerDetailedMapper.updateMoveOutByid(list);

        List<WorkerMineVO> workerMineVOS = workerDetailedMapper.selectByWorkerId(list);
        Map<Integer, List<WorkerMineVO>> groupByMineId = workerMineVOS.stream().collect(Collectors.groupingBy(WorkerMineVO::getMineId));
        for (Map.Entry<Integer, List<WorkerMineVO>> entry : groupByMineId.entrySet()) {
            //同时需要记录到历史表中
            List<Integer> workerIdList = new ArrayList<>();
            //将该矿场下的矿机id提取到集合中
            List<WorkerMineVO> WorkerMineVOList = entry.getValue();
            for (WorkerMineVO workerMineVO : WorkerMineVOList) {
                Integer workerId = workerMineVO.getWorkerId();
                workerIdList.add(workerId);
            }
            int rows = operatorWorkerHistoryService.insertTobatch(workerIdList, reason, entry.getKey(), userId);
            //出库数据同步到缓存里
            HashMap<String, Object> operatorWorkerData = new HashMap<>();
            operatorWorkerData.put("workerIdList", workerIdList);
            operatorWorkerData.put("reason", reason);
            operatorWorkerData.put("operatorId", userId);
            redisToBatchInsert(rows, "operator_worker_histkory", operatorWorkerData, entry.getKey());
            batchMoveOut(rows, "worker_detailed", workerIdList, entry.getKey());
        }
    }

    //入库操作
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateComeInByid(String ids) {
        ArrayList<Integer> list = new ArrayList<>();
        if (ids.contains(",")) {
            //全部入库
            String[] split = ids.split(",");
            for (int i = 0; i < split.length; i++) {
                list.add(Integer.valueOf(split[i]));
            }
        } else {
            //单个入库
            list.add(Integer.valueOf(ids));
        }
        int rows = workerDetailedMapper.updateComeInByid(list);
        //修改记录表里的入库时间
        int rows2 = operatorWorkerHistoryService.updateComeInTimeById(list);
        if (rows == 0 || rows2 == 0) {
            throw new InsertException("矿机入库失败");
        }
        //根据批量更新的id去库里面查询矿场id
        List<WorkerMineVO> workerMineVOS = workerDetailedMapper.selectByWorkerId(list);
        Map<Integer, List<WorkerMineVO>> groupByMineId = workerMineVOS.stream().collect(Collectors.groupingBy(WorkerMineVO::getMineId));
        for (Map.Entry<Integer, List<WorkerMineVO>> entry : groupByMineId.entrySet()) {
            //同时需要记录到历史表中
            List<Integer> workerIdList = new ArrayList<>();
            //将该矿场下的矿机id提取到集合中
            List<WorkerMineVO> WorkerMineVOList = entry.getValue();
            for (WorkerMineVO workerMineVO : WorkerMineVOList) {
                Integer workerId = workerMineVO.getWorkerId();
                workerIdList.add(workerId);
            }
            //批量入库数据同步到缓存里
            redisToBatchUpdate(rows, "operator_worker_histkory", workerIdList, entry.getKey());
            batchComeIn(rows, "worker_detailed", workerIdList, entry.getKey());
        }
    }

    //软删除
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateById(String ids) {
        ArrayList<Integer> list = new ArrayList<>();
        if (ids.contains(",")) {
            //全部出库
            String[] split = ids.split(",");
            for (int i = 0; i < split.length; i++) {
                list.add(Integer.valueOf(split[i]));
            }
        } else {
            //单个出库
            list.add(Integer.valueOf(ids));
        }

        //根据批量更新的id去库里面查询矿场id
        List<WorkerMineVO> workerMineVOS = workerDetailedMapper.selectByWorkerId(list);
        Map<Integer, List<WorkerMineVO>> groupByMineId = workerMineVOS.stream().collect(Collectors.groupingBy(WorkerMineVO::getMineId));
        log.info("矿机矿场ID列表:" + groupByMineId);

        int rows = workerDetailedMapper.updateById(list);
        if (rows == 0) {
            throw new DeleteException("矿机删除失败");
        }
        for (Map.Entry<Integer, List<WorkerMineVO>> entry : groupByMineId.entrySet()) {
            //同时需要记录到历史表中
            List<Integer> workerIdList = new ArrayList<>();
            //将该矿场下的矿机id提取到集合中
            List<WorkerMineVO> WorkerMineVOList = entry.getValue();
            for (WorkerMineVO workerMineVO : WorkerMineVOList) {
                Integer workerId = workerMineVO.getWorkerId();
                workerIdList.add(workerId);
            }
            //批量入库数据同步到缓存里
            redisToBatchDelete(rows, "worker_info", workerIdList, entry.getKey());
            redisToBatchDelete(rows, "worker_detailed", workerIdList, entry.getKey());
        }

    }

    /**
     * @return
     * @Description 用户网站的矿机详情列表
     * @Author zly
     * @Date 18:00 2020/3/10
     * @Param
     */
    public PageInfo<WorkerDetailedModel> selectAllWorkerDetailed(String workerName, String startIp,
                                                                 String endIp, Integer pageNum,
                                                                 Integer pageSize, String token) {
        int userId = 0;
        Long startIpToLong = null;
        Long endIpToLong = null;
        if (!StringUtils.isEmpty(startIp)) {
            startIpToLong = getStringIpToLong(startIp);
        }
        if (!StringUtils.isEmpty(endIp)) {
            endIpToLong = getStringIpToLong(endIp);
        }
        PageHelper.startPage(pageNum, pageSize);
        List<WorkerDetailedModel> workerDetailedModels = workerDetailedMapper.selectAllWorkerDetailed(workerName, startIpToLong, endIpToLong, userId);
        for (WorkerDetailedModel workerDetailedModel : workerDetailedModels) {
            String frameName = workerDetailedModel.getFrameName();
            String frameNumber = workerDetailedModel.getFrameNumber();
            StringBuffer frameDetailsBuffer = new StringBuffer(frameName).append(" ").append(frameNumber);
            workerDetailedModel.setFrameDetails(frameDetailsBuffer.toString());
            String workerNameStr = workerDetailedModel.getWorkerName();
            int lastIndexOf = workerNameStr.lastIndexOf(".");
            workerDetailedModel.setWorkerName(workerNameStr.substring(lastIndexOf + 1));
            if (workerDetailedModel.getOnTime() != null) {
                String onTimeStr = calculTime(Long.valueOf(workerDetailedModel.getOnTime()));
                workerDetailedModel.setOnTime(onTimeStr);
            }
            if (workerDetailedModel.getRunTime() != null) {
                String runTimeStr = calculTime(Long.valueOf(workerDetailedModel.getRunTime()));
                workerDetailedModel.setRunTime(runTimeStr);
            }
        }
        PageInfo<WorkerDetailedModel> pageInfo = new PageInfo<>(workerDetailedModels);
        return pageInfo;
    }

}






