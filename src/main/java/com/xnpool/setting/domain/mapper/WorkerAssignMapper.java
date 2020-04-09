package com.xnpool.setting.domain.mapper;

import com.xnpool.setting.domain.pojo.MineFactoryAndFraneId;import com.xnpool.setting.domain.pojo.UserRoleVO;import com.xnpool.setting.domain.pojo.WorkerAssign;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;import java.util.HashMap;import java.util.List;

/**
 * @author zly
 * @version 1.0
 * @date 2020/4/9 14:34
 */
public interface WorkerAssignMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(WorkerAssign record);

    int insertSelective(WorkerAssign record);

    WorkerAssign selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(WorkerAssign record);

    int updateByPrimaryKey(WorkerAssign record);

    List<UserRoleVO> selectByOther();

    int batchInsert(ArrayList<MineFactoryAndFraneId> list);

    List<HashMap> selectCountGroupByUserId();

    List<WorkerAssign> selectWorkerAssignList(@Param("userId") Integer userId);

    int batchToDelete(ArrayList<MineFactoryAndFraneId> deleteList);
}