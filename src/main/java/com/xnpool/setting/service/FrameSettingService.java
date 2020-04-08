package com.xnpool.setting.service;

import com.github.pagehelper.PageInfo;
import com.xnpool.setting.domain.pojo.FrameSetting;
import com.xnpool.setting.domain.model.FrameSettingExample;

import java.util.HashMap;

/**
 * @author zly
 * @version 1.0
 * @date 2020/2/4 20:04
 */
public interface FrameSettingService {

    int deleteByPrimaryKey(Integer id);

    int insert(FrameSetting record);

    Integer insertSelective(FrameSetting record);

    Integer insertSelectiveToBatch(FrameSetting record);

    Integer insertByNotExits(FrameSetting record);

    FrameSetting selectByPrimaryKey(Integer id);

    void updateByPrimaryKeySelective(FrameSetting record);

    int updateByPrimaryKey(FrameSetting record);

    void updateById(int id);

    PageInfo<FrameSettingExample> selectByOther(String keyWord, int pageNum, int pageSize);

    HashMap<Integer, String> selectFrameNameByFactoryId(Integer factoryId);

    HashMap<Integer, String> selectFrameNameToGruop(Integer factoryId);

    HashMap<Integer, String> selectMineFactoryAndFrame(Integer factoryId);

    Integer equalsFrameName(String frameStr, Integer factoryId, Integer mineId);
}



