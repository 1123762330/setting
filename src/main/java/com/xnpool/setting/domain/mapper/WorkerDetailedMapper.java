package com.xnpool.setting.domain.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xnpool.setting.domain.model.GroupModel;
import com.xnpool.setting.domain.pojo.WorkerDetailed;import com.xnpool.setting.domain.model.WorkerDetailedExample;import com.xnpool.setting.domain.model.WorkerDetailedModel;
import com.xnpool.setting.domain.pojo.WorkerDetailedParam;
import com.xnpool.setting.domain.pojo.WorkerMineVO;
import com.xnpool.setting.domain.redismodel.WorkerDetailedRedisModel;
import org.apache.ibatis.annotations.Param;import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author zly
 * @version 1.0
 * @date 2020/3/12 12:02
 */
public interface WorkerDetailedMapper{
    int deleteByPrimaryKey(Integer id);

    int insert(WorkerDetailed record);

    int insertSelective(WorkerDetailed record);

    WorkerDetailed selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(WorkerDetailed record);

    int updateByPrimaryKey(WorkerDetailed record);

    List<WorkerDetailedExample> selectMoveOutList(@Param("keyWord") String keyWord,@Param("tenantId")Long tenantId);

    int batchInsert(List<WorkerDetailed> list);

    int update(WorkerDetailed workerDetailed);

    int updateMoveOutByid(@Param("list") List<Integer> list);

    List<Integer> selectWorkerIdlist(@Param("is_come_in") Integer is_come_in);

    List<WorkerMineVO> selectByWorkerId(List<Integer> list);

    int updateById(ArrayList<Integer> list);

    List<WorkerDetailedModel> selectAllWorkerDetailed(@Param("startIp") Long startIp,
                                                      @Param("endIp") Long endIp,
                                                      @Param("userId") Integer userId,
                                                      @Param("tenantId")Long  tenantId);

    List<GroupModel> selectGroupModel(@Param("userId")Integer userId,@Param("tenantId")Long tenantId);

    List<HashMap> selectNullFrame(@Param("frameId")Integer frameId);

    List<Integer> selectIdByWorkerId(@Param("list") List<Integer> list);

    Integer selectMineId(@Param("id") Integer id);

    int updateBatch(@Param("list")List<WorkerDetailed> list);

    List<WorkerDetailed> selectModelToRedis(@Param("list")List<WorkerDetailed> list);

    int batchUpdateToUser(@Param("list")List<Integer> list,@Param("userId") Integer userId,@Param("groupId")  Integer groupId);

    List<WorkerDetailed> selectWorkerDetailedList(@Param("list")List<Integer> list);
}