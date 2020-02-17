package com.xnpool.setting.service;

import com.github.pagehelper.PageInfo;
import com.xnpool.setting.domain.pojo.FactoryHouse;
import com.xnpool.setting.domain.pojo.FrameSetting;
import com.xnpool.setting.domain.pojo.FrameSettingExample;

import java.util.HashMap;
import java.util.List;

/**
 * @author zly
 * @version 1.0
 * @date 2020/2/4 20:04
 */
public interface FrameSettingService {

    int deleteByPrimaryKey(Integer id);

    int insert(FrameSetting record);

    int insertSelective(FrameSetting record);

    FrameSetting selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(FrameSetting record);

    int updateByPrimaryKey(FrameSetting record);

    int updateById(int id);

    PageInfo<FrameSettingExample> selectByOther(String keyWord, int pageNum, int pageSize);

    HashMap<Integer, String> selectFrameNameByFactoryId(Integer factoryId);

    HashMap<Integer, String> selectFrameNameToGruop(Integer factoryId);
}


