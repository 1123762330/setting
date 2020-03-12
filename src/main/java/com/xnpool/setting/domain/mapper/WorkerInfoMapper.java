package com.xnpool.setting.domain.mapper;

import com.xnpool.setting.domain.pojo.WorkerInfo;import org.apache.ibatis.annotations.Param;import java.util.List;

/**
 * @author zly
 * @version 1.0
 * @date 2020/3/12 10:46
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