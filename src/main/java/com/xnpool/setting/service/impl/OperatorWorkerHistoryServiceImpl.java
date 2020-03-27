package com.xnpool.setting.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xnpool.setting.common.BaseController;
import com.xnpool.setting.domain.model.OperatorWorkerHistoryExample;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import com.xnpool.setting.domain.pojo.OperatorWorkerHistory;
import com.xnpool.setting.domain.mapper.OperatorWorkerHistoryMapper;
import com.xnpool.setting.service.OperatorWorkerHistoryService;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author zly
 * @version 1.0
 * @date 2020/2/19 11:34
 */
@Service
@Slf4j
public class OperatorWorkerHistoryServiceImpl extends BaseController implements OperatorWorkerHistoryService {

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
    public int updateMoveOutTimeById(List<Integer> list) {
        return operatorWorkerHistoryMapper.updateMoveOutTimeById(list);
    }

    @Override
    public int insertTobatch(List<Integer> list, String reason, Integer mineid, Integer operatorId) {
        return operatorWorkerHistoryMapper.insertTobatch(list, reason, mineid, operatorId);
    }

    @Override
    public PageInfo<OperatorWorkerHistoryExample> selectWorkerHistoryList(String startTime, String endTime, String keyWord, int pageNum, int pageSize) {
        if (!StringUtils.isEmpty(keyWord)) {
            keyWord = "%" + keyWord + "%";
        }
        PageHelper.startPage(pageNum, pageSize);
        List<OperatorWorkerHistoryExample> operatorWorkerHistoryExamples = operatorWorkerHistoryMapper.selectWorkerHistoryList(startTime, endTime, keyWord);
        for (OperatorWorkerHistoryExample operatorWorkerHistoryExample : operatorWorkerHistoryExamples) {
            SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//转换年月日
            try {
                String moveOutTimeStr = operatorWorkerHistoryExample.getMoveOutTime();
                String comeInTimeStr = operatorWorkerHistoryExample.getComeInTime();
                Date moveouttime=null;
                Date comeintime=null;
                if (!StringUtils.isEmpty(moveOutTimeStr)){
                     moveouttime = simpleDate.parse(moveOutTimeStr);
                }
                if (!StringUtils.isEmpty(comeInTimeStr)){
                    comeintime = simpleDate.parse(comeInTimeStr);

                }
                if (moveouttime != null) {
                    long totalTime = (comeintime.getTime() - moveouttime.getTime()) / 1000;
                    String DateTimes = calculTime(totalTime);
                    operatorWorkerHistoryExample.setTotalTime(DateTimes);
                } else {
                    operatorWorkerHistoryExample.setTotalTime("--");
                }
                String workerName = operatorWorkerHistoryExample.getWorkerName();
                int lastIndexOf = workerName.lastIndexOf(".");
                String minerName = workerName.substring(0, lastIndexOf);
                String workerNameStr = workerName.substring(lastIndexOf+1);
                operatorWorkerHistoryExample.setMiner(minerName);
                operatorWorkerHistoryExample.setWorkerName(workerNameStr);
            } catch (ParseException e) {
                log.error("时间转换异常!" + e.getMessage());
            }

        }
        PageInfo<OperatorWorkerHistoryExample> pageInfo = new PageInfo<>(operatorWorkerHistoryExamples);
        return pageInfo;
    }

}



