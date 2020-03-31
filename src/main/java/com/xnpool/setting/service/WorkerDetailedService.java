package com.xnpool.setting.service;

import com.github.pagehelper.PageInfo;
import com.xnpool.setting.domain.model.GroupModel;
import com.xnpool.setting.domain.model.WorkerDetailedExample;
import com.xnpool.setting.domain.model.WorkerDetailedModel;
import com.xnpool.setting.domain.model.WorkerExample;
import com.xnpool.setting.domain.pojo.*;

import java.util.HashMap;
import java.util.List;

/**
 * @author zly
 * @version 1.0
 * @date 2020/2/23 12:52
 */
public interface WorkerDetailedService {


    int deleteByPrimaryKey(Integer id);

    int insert(WorkerDetailed record);

    int insertSelective(WorkerDetailed record);

    WorkerDetailed selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(WorkerDetailed record);

    int updateByPrimaryKey(WorkerDetailed record);

    PageInfo<WorkerDetailedExample> selectMoveOutList(String keyWord, int pageNum, int pageSize,String token);

    void addWorkerToLibrary(WorkerDetailedParam workerDetailedParam,String token);

    HashMap<String, Object>  selectComeInWorkerList(String workerType, Integer state, String ip, int pageNum, int pageSize);

    void updateMoveOutByid(String ids, String reason, String token);

    void updateById(String ids);

    PageInfo<WorkerDetailedModel> selectAllWorkerDetailed(String workerName, String startIp, String endIp, Integer pageNum, Integer pageSize, String token,Long tenantId);

    HashMap<String, Object> selectGroupModel(String token, Integer pageNum, Integer pageSize,Long tenantId);

    HashMap<Integer, String> selectNullFrame(Integer frameId);
}






