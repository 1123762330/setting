package com.xnpool.setting.service;

import com.github.pagehelper.PageInfo;
import com.xnpool.setting.domain.pojo.OperatorWorkerHistory;
import com.xnpool.setting.domain.pojo.OperatorWorkerHistoryExample;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author  zly
 * @date  2020/2/19 11:34
 * @version 1.0
 */
public interface OperatorWorkerHistoryService{


    int deleteByPrimaryKey(Integer id);

    int insert(OperatorWorkerHistory record);

    int insertSelective(OperatorWorkerHistory record);

    OperatorWorkerHistory selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OperatorWorkerHistory record);

    int updateByPrimaryKey(OperatorWorkerHistory record);

    int  updateComeInTimeById(List<Integer> list);

    void insertTobatch(List<OperatorWorkerHistory> operatorWorkerHistoryList);

    PageInfo<OperatorWorkerHistoryExample> selectWorkerHistoryList(String keyWord, int pageNum, int pageSize);
}
