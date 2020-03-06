package com.xnpool.setting.domain.mapper;

import com.xnpool.setting.domain.pojo.MineFactoryAndFraneId;
import com.xnpool.setting.domain.pojo.UserRoleVO;
import com.xnpool.setting.domain.pojo.WorkerAssign;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.List;

/**
 * @author  zly
 * @date  2020/3/6 14:57
 * @version 1.0
 */
public interface WorkerAssignMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(WorkerAssign record);

    int insertSelective(WorkerAssign record);

    WorkerAssign selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(WorkerAssign record);

    int updateByPrimaryKey(WorkerAssign record);

    List<UserRoleVO> selectByOther(@Param("keyWord") String keyWord);

    int batchInsert(ArrayList<MineFactoryAndFraneId> list);
}