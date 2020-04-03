package com.xnpool.setting.service.impl;

import com.xnpool.setting.domain.mapper.WorkerDetailedMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import com.xnpool.setting.domain.mapper.WorkerInfoMapper;
import com.xnpool.setting.domain.pojo.WorkerInfo;
import com.xnpool.setting.service.WorkerInfoService;

import java.util.HashMap;
import java.util.List;

/**
 * @author zly
 * @version 1.0
 * @date 2020/3/5 14:28
 */
@Service
public class WorkerInfoServiceImpl implements WorkerInfoService {

    @Resource
    private WorkerInfoMapper workerInfoMapper;

    @Autowired
    private WorkerDetailedMapper workerDetailedMapper;

    @Override
    public int deleteByPrimaryKey(Long id) {
        return workerInfoMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(WorkerInfo record) {
        return workerInfoMapper.insert(record);
    }

    @Override
    public int insertSelective(WorkerInfo record) {
        return workerInfoMapper.insertSelective(record);
    }

    @Override
    public WorkerInfo selectByPrimaryKey(Long id) {
        return workerInfoMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(WorkerInfo record) {
        return workerInfoMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(WorkerInfo record) {
        return workerInfoMapper.updateByPrimaryKey(record);
    }

    /**
     * @return
     * @Description 矿机列表
     * @Author zly
     * @Date 12:07 2020/2/23
     * @Param
     */
    @Override
    public HashMap<Integer, String> selectWorkerList() {
        List<WorkerInfo> workers = workerInfoMapper.selectByOther(null,null,null);
        //已经入库的矿机Id
        List<Integer> comeInlist = workerDetailedMapper.selectWorkerIdlist(1);
        HashMap<Integer, String> resultMap = new HashMap<>();
        for (WorkerInfo worker : workers) {
            Integer id = Integer.valueOf(String.valueOf(worker.getId()));
            if (!comeInlist.contains(id)){
                String workername = worker.getWorker1();
                String workerip = worker.getIp();
                String workernames =  workerip+ " " + workername;
                resultMap.put(id, workernames);
            }

        }
        return resultMap;
    }
}


