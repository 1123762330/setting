package com.xnpool.setting.domain.mapper;

import com.xnpool.setting.domain.pojo.WorkerDetailed;
import com.xnpool.setting.domain.pojo.WorkerDetailedExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author  zly
 * @date  2020/2/23 12:52
 * @version 1.0
 */
public interface WorkerDetailedMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(WorkerDetailed record);

    int insertSelective(WorkerDetailed record);

    WorkerDetailed selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(WorkerDetailed record);

    int updateByPrimaryKey(WorkerDetailed record);

    List<WorkerDetailedExample> selectMoveOutList(@Param("keyWord") String keyWord);
}