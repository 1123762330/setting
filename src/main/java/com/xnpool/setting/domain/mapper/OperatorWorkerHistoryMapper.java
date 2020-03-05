package com.xnpool.setting.domain.mapper;

import com.xnpool.setting.domain.pojo.OperatorWorkerHistory;import com.xnpool.setting.domain.pojo.OperatorWorkerHistoryExample;import org.apache.ibatis.annotations.Param;import java.util.List;

/**
 * @author zly
 * @version 1.0
 * @date 2020/3/5 13:32
 */
public interface OperatorWorkerHistoryMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OperatorWorkerHistory record);

    int insertSelective(OperatorWorkerHistory record);

    OperatorWorkerHistory selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OperatorWorkerHistory record);

    int updateByPrimaryKey(OperatorWorkerHistory record);

    int updateComeInTimeById(@Param("list") List<Integer> list);

    int insertTobatch(@Param("list") List<Integer> list, @Param("reason") String reason, @Param("mineId") Integer mineid, @Param("operatorId") Integer operatorId);

    List<OperatorWorkerHistoryExample> selectWorkerHistoryList(@Param("startTime") String startTime, @Param("endTime") String endTime, @Param("keyword") String keyword);
}