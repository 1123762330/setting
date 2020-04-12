package com.xnpool.setting.service;

import com.github.pagehelper.PageInfo;
import com.xnpool.setting.domain.pojo.PowerSetting;
import com.xnpool.setting.domain.pojo.UserRoleVO;
import com.xnpool.setting.domain.pojo.WorkerAssign;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
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

    void addAssignWorker(String ids,String delIds,String delIps,String ipId, Integer userId);

    HashMap<Integer, HashMap<String, Object>> selectAssignMineMap(Integer userId);

    HashMap<Integer, HashMap<String, Object>> selectAssignFactoryMap(Integer userId,Integer mineId);

    HashMap<Integer, HashMap<String, Object>> selectAssignFrameMap(Integer userId, Integer factoryId);

    HashMap<Integer, HashMap<String, Object>> selectAssignIPMap(Integer userId,String mineName, Integer mineId);
}

