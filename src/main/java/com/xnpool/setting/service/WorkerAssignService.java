package com.xnpool.setting.service;

import com.github.pagehelper.PageInfo;
import com.xnpool.setting.domain.pojo.PowerSetting;
import com.xnpool.setting.domain.pojo.UserRoleVO;
import com.xnpool.setting.domain.pojo.WorkerAssign;

import java.util.List;

/**
 * @author zly
 * @version 1.0
 * @date 2020/3/6 14:57
 */
public interface WorkerAssignService {


    int deleteByPrimaryKey(Integer id);

    int insert(WorkerAssign record);

    int insertSelective(WorkerAssign record);

    WorkerAssign selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(WorkerAssign record);

    int updateByPrimaryKey(WorkerAssign record);

    PageInfo<UserRoleVO> selectByOther(String keyWord, int pageNum, int pageSize);

    void addAssignWorker(String ids,String token);

    List<Integer> selectWorkerAssign();
}
