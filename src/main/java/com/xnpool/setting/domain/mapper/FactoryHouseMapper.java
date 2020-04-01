package com.xnpool.setting.domain.mapper;

import com.xnpool.setting.domain.pojo.FactoryHouse;import com.xnpool.setting.domain.model.FactoryHouseExample;import org.apache.ibatis.annotations.Param;import java.util.HashMap;import java.util.List;

/**
 * @author zly
 * @version 1.0
 * @date 2020/3/5 12:59
 */
public interface FactoryHouseMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FactoryHouse record);

    int insertSelective(FactoryHouse record);

    FactoryHouse selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(FactoryHouse record);

    int updateByPrimaryKey(FactoryHouse record);

    int updateById(int id);

    List<FactoryHouseExample> selectByOther(@Param("keyWord") String keyWord);

    List<FactoryHouse> selectByMineId(@Param("mineId") Integer mineId);

    List<HashMap> selectFactoryNameByMineId(@Param("mineId") Integer mineId);

    Integer selectMineId(@Param("id") Integer id);
}