package com.xnpool.setting.service.impl;

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
    public HashMap<Integer, String> selectWorkerList(String keyWord) {
        List<WorkerInfo> workers = workerInfoMapper.selectByOther(keyWord);
        HashMap<Integer, String> resultMap = new HashMap<>();
        for (WorkerInfo worker : workers) {
            Integer id = Integer.valueOf(String.valueOf(worker.getId()));
            String workername = worker.getWorker1();
            String workerip = worker.getIp();
            String workernames = workername + " " + workerip;
            resultMap.put(id, workernames);
        }
        return resultMap;
    }
}

