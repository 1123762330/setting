package com.xnpool.setting.domain.mapper;

import com.github.pagehelper.PageInfo;
import com.xnpool.setting.domain.pojo.Worker;
import com.xnpool.setting.domain.pojo.WorkerExample;
import org.apache.ibatis.annotations.Param;import java.util.List;

/**
 * @author zly
 * @version 1.0
 * @date 2020/2/23 11:28
 */
public interface WorkerMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Worker record);

    int insertSelective(Worker record);

    Worker selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Worker record);

    int updateByPrimaryKey(Worker record);

    List<Worker> selectByOther(@Param("keyWord") String keyWord);

    int updateComeInByid(@Param("list") List<Integer> list);

    int updateById(@Param("list") List<Integer> list);

    int updateMoveOutByid(@Param("list") List<Integer> list);

    PageInfo<WorkerExample> selectWorkerList(String keyWord);
}