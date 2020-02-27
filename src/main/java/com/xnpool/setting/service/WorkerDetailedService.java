package com.xnpool.setting.service;

import com.github.pagehelper.PageInfo;
import com.xnpool.setting.domain.pojo.*;

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

    PageInfo<WorkerDetailedExample> selectMoveOutList(String keyWord, int pageNum, int pageSize);

    void addWorkerToLibrary(WorkerDetailedParam workerDetailedParam);

    PageInfo<WorkerExample> selectComeInWorkerList(String keyWord, int pageNum, int pageSize);

    void updateMoveOutByid(String ids, String reason, String token);

    void updateComeInByid(String ids);

    void updateById(String ids);
}


