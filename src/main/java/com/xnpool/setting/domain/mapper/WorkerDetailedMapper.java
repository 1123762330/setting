package com.xnpool.setting.domain.mapper;

import com.xnpool.setting.domain.pojo.WorkerDetailed;import com.xnpool.setting.domain.pojo.WorkerDetailedExample;import com.xnpool.setting.domain.pojo.WorkerDetailedModel;import com.xnpool.setting.domain.pojo.WorkerMineVO;import org.apache.ibatis.annotations.Param;import java.util.ArrayList;import java.util.List;

/**
 * @author zly
 * @version 1.0
 * @date 2020/3/12 12:02
 */
public interface WorkerDetailedMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(WorkerDetailed record);

    int insertSelective(WorkerDetailed record);

    WorkerDetailed selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(WorkerDetailed record);

    int updateByPrimaryKey(WorkerDetailed record);

    List<WorkerDetailedExample> selectMoveOutList(@Param("keyWord") String keyWord);

    int batchInsert(List<WorkerDetailed> list);

    int updateMoveOutByid(@Param("list") List<Integer> list);

    int updateComeInByid(ArrayList<Integer> list);

    List<Integer> selectWorkerIdlist(@Param("is_come_in") Integer is_come_in);

    List<WorkerMineVO> selectByWorkerId(List<Integer> list);

    int updateById(ArrayList<Integer> list);

    List<WorkerDetailedModel> selectAllWorkerDetailed(@Param("workerName") String workerName,
                                                      @Param("startIp") Long startIp,
                                                      @Param("endIp") Long endIp,
                                                      @Param("userId") Integer userId);
}