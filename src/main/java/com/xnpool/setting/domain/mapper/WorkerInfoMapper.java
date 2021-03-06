package com.xnpool.setting.domain.mapper;

import com.xnpool.setting.domain.pojo.WorkerInfo;import org.apache.ibatis.annotations.Param;import java.util.List;

/**
 * @author zly
 * @version 1.0
 * @date 2020/3/12 20:36
 */
public interface WorkerInfoMapper {
    int deleteByPrimaryKey(Long id);

    int insert(WorkerInfo record);

    int insertSelective(WorkerInfo record);

    WorkerInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(WorkerInfo record);

    int updateByPrimaryKey(WorkerInfo record);

    List<WorkerInfo> selectByOther(@Param("workerType")String workerType,@Param("state") Integer state, @Param("ip")String ip);

    Integer selectWorkerIdByIp(@Param("workerIp")String workerIp,@Param("mineId") Integer mineId);
}