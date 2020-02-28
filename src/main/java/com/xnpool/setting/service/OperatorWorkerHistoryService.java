package com.xnpool.setting.service;

import com.github.pagehelper.PageInfo;
import com.xnpool.setting.domain.pojo.OperatorWorkerHistory;
import com.xnpool.setting.domain.pojo.OperatorWorkerHistoryExample;
import org.apache.ibatis.annotations.Param;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author zly
 * @version 1.0
 * @date 2020/2/19 11:34
 */
public interface OperatorWorkerHistoryService {


    int deleteByPrimaryKey(Integer id);

    int insert(OperatorWorkerHistory record);

    int insertSelective(OperatorWorkerHistory record);

    OperatorWorkerHistory selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OperatorWorkerHistory record);

    int updateByPrimaryKey(OperatorWorkerHistory record);

    int updateComeInTimeById(List<Integer> list);

    int insertTobatch(List<Integer> list,String reason,Integer mineid,Integer operatorId);

    PageInfo<OperatorWorkerHistoryExample> selectWorkerHistoryList(String startTime,String endTime,String keyWord, int pageNum, int pageSize);
}


