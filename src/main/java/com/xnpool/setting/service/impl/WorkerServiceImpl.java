package com.xnpool.setting.service.impl;

import java.util.Date;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xnpool.setting.domain.pojo.WorkerExample;
import com.xnpool.setting.domain.pojo.WorkerbrandSetting;
import com.xnpool.setting.service.IpSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import com.xnpool.setting.domain.mapper.WorkerMapper;
import com.xnpool.setting.domain.pojo.Worker;
import com.xnpool.setting.service.WorkerService;
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
public class WorkerServiceImpl implements WorkerService {

    @Resource
    private WorkerMapper workerMapper;

    @Autowired
    private IpSettingService ipSettingService;

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
    public PageInfo<WorkerExample> selectByOther(String keyWord, int pageNum, int pageSize) {
        if (!StringUtils.isEmpty(keyWord)) {
            keyWord = "%" + keyWord + "%";
        }

        //取IP字段区间Map,便于后面取值
        HashMap<Integer, String> selectByIPStart = ipSettingService.selectByIPStart();
        List<WorkerExample> result = new ArrayList<>();
        HashMap<String, String> ipMap = new HashMap<>();
        for (Map.Entry<Integer, String> entry : selectByIPStart.entrySet()) {
            String value = entry.getValue();
            String[] split = value.split("-");
            String ipStart = split[0];
            int lastIndexOf = ipStart.lastIndexOf(".");
            String key = ipStart.substring(0, lastIndexOf);
            ipMap.put(key, value);
        }
        PageHelper.startPage(pageNum, pageSize);
        List<Worker> workers = workerMapper.selectByOther(keyWord);
        //遍历list集合,setIP所属区间进去
        for (Worker worker : workers) {
            int lastIndexOf = worker.getIp().lastIndexOf(".");
            String substring = worker.getIp().substring(0, lastIndexOf);
            String ip_quJian = ipMap.get(substring);
            WorkerExample workerExample = new WorkerExample(worker.getId(), worker.getIp(), ip_quJian, worker.getIsOnline(), worker.getIsComeIn());
            result.add(workerExample);
        }
        PageInfo<WorkerExample> pageInfo = new PageInfo<>(result);
        return pageInfo;
    }

}
