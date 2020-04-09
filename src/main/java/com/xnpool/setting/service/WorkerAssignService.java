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

    void addAssignWorker(String ids, String ipId, String token);

    HashMap<Integer, HashMap<String, Integer>> selectAssignMineMap(String token);

    HashMap<Integer, HashMap<String, Integer>> selectAssignFactoryMap(String token,Integer mineId);

    HashMap<Integer, HashMap<String, Integer>> selectAssignFrameMap(String token, Integer factoryId);

    HashMap<Integer, HashMap<String, Integer>> selectAssignIPMap(String token,String mineName, Integer mineId);
}

