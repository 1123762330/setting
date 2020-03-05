package com.xnpool.setting.domain.mapper;

import com.xnpool.setting.domain.pojo.WorkerInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author  zly
 * @date  2020/3/5 14:28
 * @version 1.0
 */
public interface WorkerInfoMapper {
    int deleteByPrimaryKey(Long id);

    int insert(WorkerInfo record);

    int insertSelective(WorkerInfo record);

    WorkerInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(WorkerInfo record);

    int updateByPrimaryKey(WorkerInfo record);

    List<WorkerInfo> selectByOther(@Param("keyWord") String keyWord);
}