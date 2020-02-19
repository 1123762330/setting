package com.xnpool.setting.domain.mapper;

import com.xnpool.setting.domain.pojo.OperatorWorkerHistory;
import com.xnpool.setting.domain.pojo.OperatorWorkerHistoryExample;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * @author  zly
 * @date  2020/2/19 11:34
 * @version 1.0
 */
public interface OperatorWorkerHistoryMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OperatorWorkerHistory record);

    int insertSelective(OperatorWorkerHistory record);

    OperatorWorkerHistory selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OperatorWorkerHistory record);

    int updateByPrimaryKey(OperatorWorkerHistory record);

    int updateComeInTimeById(@Param("list") List<Integer> list);

    void insertTobatch(@Param("list")List<OperatorWorkerHistory> operatorWorkerHistoryList);

    List<OperatorWorkerHistoryExample> selectWorkerHistoryList(@Param("keyWord") String keyWord);
}