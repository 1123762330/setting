package com.xnpool.setting.service.impl;
import java.util.Date;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xnpool.setting.common.BaseController;
import com.xnpool.setting.domain.pojo.Worker;
import com.xnpool.setting.domain.pojo.WorkerDetailedExample;
import com.xnpool.setting.domain.pojo.WorkerDetailedParam;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import com.xnpool.setting.domain.pojo.WorkerDetailed;
import com.xnpool.setting.domain.mapper.WorkerDetailedMapper;
import com.xnpool.setting.service.WorkerDetailedService;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author zly
 * @version 1.0
 * @date 2020/2/23 12:52
 */
@Service
public class WorkerDetailedServiceImpl extends BaseController implements WorkerDetailedService {

    @Resource
    private WorkerDetailedMapper workerDetailedMapper;

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
        redisToBatchInsert(rows,"worker_detailed",list,mineId);
    }

}


