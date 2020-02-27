package com.xnpool.setting.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xnpool.setting.domain.pojo.OperatorWorkerHistoryExample;
import com.xnpool.setting.domain.pojo.PowerSetting;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import com.xnpool.setting.domain.pojo.OperatorWorkerHistory;
import com.xnpool.setting.domain.mapper.OperatorWorkerHistoryMapper;
import com.xnpool.setting.service.OperatorWorkerHistoryService;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author zly
 * @version 1.0
 * @date 2020/2/19 11:34
 */
@Service
public class OperatorWorkerHistoryServiceImpl implements OperatorWorkerHistoryService {

    @Resource
    private OperatorWorkerHistoryMapper operatorWorkerHistoryMapper;

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return operatorWorkerHistoryMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(OperatorWorkerHistory record) {
        return operatorWorkerHistoryMapper.insert(record);
    }

    @Override
    public int insertSelective(OperatorWorkerHistory record) {
        return operatorWorkerHistoryMapper.insertSelective(record);
    }

    @Override
    public OperatorWorkerHistory selectByPrimaryKey(Integer id) {
        return operatorWorkerHistoryMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(OperatorWorkerHistory record) {
        return operatorWorkerHistoryMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(OperatorWorkerHistory record) {
        return operatorWorkerHistoryMapper.updateByPrimaryKey(record);
    }

    @Override
    public int updateComeInTimeById(List<Integer> list) {
        return operatorWorkerHistoryMapper.updateComeInTimeById(list);
    }

    @Override
    public int insertTobatch(List<Integer> list,String reason,Integer mineid,Integer operatorId) {
        return operatorWorkerHistoryMapper.insertTobatch(list,reason,mineid,operatorId);
    }

    @Override
    public PageInfo<OperatorWorkerHistoryExample> selectWorkerHistoryList(String keyWord, int pageNum, int pageSize) {
        if (!StringUtils.isEmpty(keyWord)) {
            keyWord = "%" + keyWord + "%";
        }
        PageHelper.startPage(pageNum, pageSize);
        List<OperatorWorkerHistoryExample> operatorWorkerHistoryExamples = operatorWorkerHistoryMapper.selectWorkerHistoryList(keyWord);
        for (OperatorWorkerHistoryExample operatorWorkerHistoryExample : operatorWorkerHistoryExamples) {
            Date moveouttime = operatorWorkerHistoryExample.getMoveouttime();
            Date comeintime = operatorWorkerHistoryExample.getComeintime();
            if (comeintime != null) {
                long totalTime = (comeintime.getTime() - moveouttime.getTime()) / 1000;
                String DateTimes = null;
                long days = totalTime / (60 * 60 * 24);
                long hours = (totalTime % (60 * 60 * 24)) / (60 * 60);
                long minutes = (totalTime % (60 * 60)) / 60;
                long seconds = totalTime % 60;
                if (days > 0) {
                    DateTimes = days + "天" + hours + "小时" + minutes + "分钟" + seconds + "秒";
                } else if (hours > 0) {
                    DateTimes = hours + "小时" + minutes + "分钟" + seconds + "秒";
                } else if (minutes > 0) {
                    DateTimes = minutes + "分钟" + seconds + "秒";
                } else {
                    DateTimes = seconds + "秒";
                }
                operatorWorkerHistoryExample.setTotalTime(DateTimes);
            } else {
                operatorWorkerHistoryExample.setTotalTime("--");
            }
        }
        PageInfo<OperatorWorkerHistoryExample> pageInfo = new PageInfo<>(operatorWorkerHistoryExamples);
        return pageInfo;
    }

}


