package com.xnpool.setting.service.impl;

import java.util.*;
import java.util.stream.Collectors;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xnpool.setting.common.BaseController;
import com.xnpool.setting.domain.mapper.WorkerMapper;
import com.xnpool.setting.domain.pojo.*;
import com.xnpool.setting.service.IpSettingService;
import com.xnpool.setting.service.OperatorWorkerHistoryService;
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
public class WorkerDetailedServiceImpl extends BaseController implements WorkerDetailedService {

    @Resource
    private WorkerDetailedMapper workerDetailedMapper;

    @Resource
    private WorkerMapper workerMapper;

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
        Integer factoryid = workerDetailedParam.getFactoryid();
        Integer frameid = workerDetailedParam.getFrameid();
        Integer framenumber = workerDetailedParam.getFramenumber();
        Integer groupid = workerDetailedParam.getGroupid();
        Integer mineId = workerDetailedParam.getMineId();
        ArrayList<WorkerDetailed> list = new ArrayList<>();
        if (workerid.contains(",")) {
            //批量添加
            String[] split = workerid.split(",");
            for (int i = 0; i < split.length; i++) {
                String workerId = split[i];
                WorkerDetailed workerDetailed = new WorkerDetailed();
                workerDetailed.setWorkerid(Integer.valueOf(workerId));
                workerDetailed.setFactoryid(factoryid);
                workerDetailed.setFrameid(frameid);
                workerDetailed.setFramenumber(framenumber);
                workerDetailed.setGroupid(groupid);
                workerDetailed.setMineid(mineId);
                workerDetailed.setCreatetime(new Date());
                list.add(workerDetailed);
            }
        } else {
            WorkerDetailed workerDetailed = new WorkerDetailed();
            workerDetailed.setWorkerid(Integer.valueOf(workerid));
            workerDetailed.setFactoryid(factoryid);
            workerDetailed.setFrameid(frameid);
            workerDetailed.setFramenumber(framenumber);
            workerDetailed.setGroupid(groupid);
            workerDetailed.setMineid(mineId);
            workerDetailed.setCreatetime(new Date());
            list.add(workerDetailed);
        }
        //批量入管理仓库
        int rows = workerDetailedMapper.batchInsert(list);
        //批量入缓存
        //Map<Integer, List<WorkerDetailed>> groupBy = list.stream().collect(Collectors.groupingBy(WorkerDetailed::getMineid));
        //String jsonString = JSONArray.toJSONString(groupBy);
        //System.out.println(jsonString);
        redisToBatchInsert(rows, "worker_detailed", list, mineId);
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
        List<Worker> workers = workerMapper.selectByOther(keyWord);

        //已经入库的矿机Id
        List<Integer> comeInlist = workerDetailedMapper.selectComeInlist();

        //遍历list集合,setIP所属区间进去
        for (Worker worker : workers) {
            int lastIndexOf = worker.getWorkerip().lastIndexOf(".");
            String substring = worker.getWorkerip().substring(0, lastIndexOf);
            String ip_quJian = ipMap.get(substring);
            //这里需要做个判断,判断这个矿机有没有入库,如果入库列表里有那就是1,如果没有就是0
            if (comeInlist.contains(worker.getId())) {
                WorkerExample workerExample = new WorkerExample(worker.getId(), worker.getWorkerip(), ip_quJian, worker.getIsonline(), 1);
                result.add(workerExample);
            } else {
                WorkerExample workerExample = new WorkerExample(worker.getId(), worker.getWorkerip(), ip_quJian, worker.getIsonline(), 0);
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
        ArrayList<OperatorWorkerHistory> operatorWorkerHistoryList = new ArrayList<>();
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
        System.out.println(groupByMineId);
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
            operatorWorkerData.put("reason", userId);
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
        operatorWorkerHistoryService.updateComeInTimeById(list);

        //根据批量更新的id去库里面查询矿场id
        List<WorkerMineVO> workerMineVOS = workerDetailedMapper.selectByWorkerId(list);
        Map<Integer, List<WorkerMineVO>> groupByMineId = workerMineVOS.stream().collect(Collectors.groupingBy(WorkerMineVO::getMineId));
        System.out.println(groupByMineId);
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
        int rows = workerMapper.updateById(list);

        //根据批量更新的id去库里面查询矿场id
        List<WorkerMineVO> workerMineVOS = workerDetailedMapper.selectByWorkerId(list);
        Map<Integer, List<WorkerMineVO>> groupByMineId = workerMineVOS.stream().collect(Collectors.groupingBy(WorkerMineVO::getMineId));
        System.out.println(groupByMineId);
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
            redisToBatchDelete(rows, "worker_detailed", workerIdList, entry.getKey());
        }

    }

}


