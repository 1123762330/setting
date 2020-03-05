package com.xnpool.setting.service;

import com.xnpool.setting.domain.pojo.WorkerInfo;

import java.util.HashMap;

/**
 * @author zly
 * @version 1.0
 * @date 2020/3/5 14:28
 */
public interface WorkerInfoService {


    int deleteByPrimaryKey(Long id);

    int insert(WorkerInfo record);

    int insertSelective(WorkerInfo record);

    WorkerInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(WorkerInfo record);

    int updateByPrimaryKey(WorkerInfo record);

    HashMap<Integer, String> selectWorkerList(String keyWord);
}
