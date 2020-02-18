package com.xnpool.setting.domain.mapper;

import com.github.pagehelper.PageInfo;
import com.xnpool.setting.domain.pojo.Worker;
import com.xnpool.setting.domain.pojo.WorkerbrandSetting;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zly
 * @version 1.0
 * @date 2020/2/18 12:02
 */
public interface WorkerMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Worker record);

    int insertSelective(Worker record);

    Worker selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Worker record);

    int updateByPrimaryKey(Worker record);

    List<Worker> selectByOther(@Param("keyWord") String keyWord);

    void updateComeInByid(@Param("list") List<Integer> list);

    void updateById(@Param("list") List<Integer> list);

    void updateMoveOutByid(@Param("list") List<Integer> list);
}