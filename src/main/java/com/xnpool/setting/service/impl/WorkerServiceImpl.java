package com.xnpool.setting.service.impl;

import java.util.Date;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xnpool.setting.common.BaseController;
import com.xnpool.setting.domain.pojo.OperatorWorkerHistory;
import com.xnpool.setting.domain.pojo.WorkerExample;
import com.xnpool.setting.domain.pojo.WorkerbrandSetting;
import com.xnpool.setting.service.IpSettingService;
import com.xnpool.setting.service.OperatorWorkerHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import com.xnpool.setting.domain.mapper.WorkerMapper;
import com.xnpool.setting.domain.pojo.Worker;
import com.xnpool.setting.service.WorkerService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zly
 * @version 1.0
 * @date 2020/2/18 12:02
 */
@Service
public class WorkerServiceImpl extends BaseController implements WorkerService {

    @Resource
    private WorkerMapper workerMapper;

    @Autowired
    private IpSettingService ipSettingService;

    @Autowired
    private OperatorWorkerHistoryService operatorWorkerHistoryService;

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return workerMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(Worker record) {
        return workerMapper.insert(record);
    }

    @Override
    public int insertSelective(Worker record) {
        return workerMapper.insertSelective(record);
    }

    @Override
    public Worker selectByPrimaryKey(Integer id) {
        return workerMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(Worker record) {
        return workerMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(Worker record) {
        return workerMapper.updateByPrimaryKey(record);
    }

    @Override
    public void updateComeInByid(String ids) {

    }

    @Override
    public PageInfo<Worker> selectMoveOutList(String keyWord, int pageNum, int pageSize) {
        return null;
    }

    //入库操作
//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public void updateComeInByid(String ids) {
//        ArrayList<Integer> list = new ArrayList<>();
//        if (ids.contains(",")) {
//            //全部入库
//            String[] split = ids.split(",");
//            for (int i = 0; i < split.length; i++) {
//                list.add(Integer.valueOf(split[i]));
//            }
//        } else {
//            //单个入库
//            list.add(Integer.valueOf(ids));
//        }
//        int rows = workerMapper.updateComeInByid(list);
//        //修改记录表里的入库时间
//        operatorWorkerHistoryService.updateComeInTimeById(list);
//        //批量入库数据同步到缓存里
//        batchComeIn(rows, "worker", list.toString());
//        batchComeIn(rows, "workerbrand_setting", list.toString());
//    }


    /**
     * @return
     * @Description 矿机列表
     * @Author zly
     * @Date 12:07 2020/2/23
     * @Param
     */
    @Override
    public HashMap<Integer, String> selectWorkerList(String keyWord) {
        List<Worker> workers = workerMapper.selectByOther(keyWord);
        HashMap<Integer, String> resultMap = new HashMap<>();
        for (Worker worker : workers) {
            Integer id = worker.getId();
            String workername = worker.getWorkername();
            resultMap.put(id, workername);
        }
        return resultMap;
    }

}


