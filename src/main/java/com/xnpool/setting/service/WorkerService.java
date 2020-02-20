package com.xnpool.setting.service;

import com.github.pagehelper.PageInfo;
import com.xnpool.setting.domain.pojo.Worker;
import com.xnpool.setting.domain.pojo.WorkerExample;
import com.xnpool.setting.domain.pojo.WorkerbrandSetting;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author zly
 * @version 1.0
 * @date 2020/2/18 12:02
 */
public interface WorkerService {


    int deleteByPrimaryKey(Integer id);

    int insert(Worker record);

    int insertSelective(Worker record);

    Worker selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Worker record);

    int updateByPrimaryKey(Worker record);

    PageInfo<WorkerExample>  selectComeInWorkerList(String keyWord, int pageNum, int pageSize);

    void updateComeInByid(String ids);

    PageInfo<Worker> selectMoveOutList(String keyWord, int pageNum, int pageSize);

    void updateById(String ids);

    void updateMoveOutByid(String ids, String reason, String token);
}