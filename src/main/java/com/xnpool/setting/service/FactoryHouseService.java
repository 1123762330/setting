package com.xnpool.setting.service;

import com.github.pagehelper.PageInfo;
import com.xnpool.setting.domain.pojo.FactoryHouse;
import com.xnpool.setting.domain.model.FactoryHouseExample;

import java.util.HashMap;
import java.util.List;

/**
 * @author zly
 * @version 1.0
 * @date 2020/2/4 15:29
 */
public interface FactoryHouseService {


    int deleteByPrimaryKey(Integer id);

    int insert(FactoryHouse record);

    void insertSelective(FactoryHouse record);

    FactoryHouse selectByPrimaryKey(Integer id);

    void updateByPrimaryKeySelective(FactoryHouse record);

    int updateByPrimaryKey(FactoryHouse record);

    void updateById(int id);

    PageInfo<FactoryHouseExample> selectByOther(String keyWord, int pageNum, int pageSize);

    List<FactoryHouse> selectByMineId(Integer mineId);

    HashMap<Integer, String> selectFactoryNameByMineId(Integer mineId);
}



