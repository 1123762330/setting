package com.xnpool.setting.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageInfo;
import com.xnpool.setting.domain.model.FactoryHouseExample;
import com.xnpool.setting.domain.pojo.FactoryHouse;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.HashMap;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zly
 * @since 2020-04-22
 */
public interface FactoryHouseService extends IService<FactoryHouse> {

    Integer addFactoryHouse(FactoryHouse factoryHouse);

    void updateByPrimaryKeySelective(FactoryHouse factoryHouse);

    void deleteById(int id);

    Page<FactoryHouseExample> selectByOther(String keyWord, int pageNum, int pageSize, String token);

    HashMap<Integer, String> selectFactoryNameByMineId(Integer mineId);

    Integer equalsFactoryNum(String factoryNum, Integer mineId);

    List<FactoryHouse> selectByMineId(int id);

    Integer insertSelectiveToBatch(FactoryHouse factoryHouse);
}
