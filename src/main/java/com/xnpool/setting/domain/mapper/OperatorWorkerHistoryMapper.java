package com.xnpool.setting.domain.mapper;

import com.xnpool.setting.domain.pojo.OperatorWorkerHistory;import com.xnpool.setting.domain.pojo.OperatorWorkerHistoryExample;import org.apache.ibatis.annotations.Param;import java.util.List;

/**
 * @author zly
 * @version 1.0
 * @date 2020/2/27 14:16
 */
public interface OperatorWorkerHistoryMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OperatorWorkerHistory record);

    int insertSelective(OperatorWorkerHistory record);

    OperatorWorkerHistory selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OperatorWorkerHistory record);

    int updateByPrimaryKey(OperatorWorkerHistory record);

    int updateComeInTimeById(@Param("list") List<Integer> list);

    int insertTobatch(@Param("list")List<Integer> list,@Param("reason") String reason,@Param("mineid")  Integer mineid,@Param("operatorId")  Integer operatorId);

    List<OperatorWorkerHistoryExample> selectWorkerHistoryList(@Param("keyWord") String keyWord);
}