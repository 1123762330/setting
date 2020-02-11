package com.xnpool.setting.service;

import com.github.pagehelper.PageInfo;
import com.xnpool.setting.domain.pojo.MineSetting;

import java.util.HashMap;
import java.util.List;

/**
 * @author zly
 * @version 1.0
 * @date 2020/2/3 12:46
 */
public interface MineSettingService {


    int deleteByPrimaryKey(Integer id);

    int insert(MineSetting record);

    int insertSelective(MineSetting record);

    MineSetting selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MineSetting record);

    int updateByPrimaryKey(MineSetting record);

    int updateById(int id);

    HashMap<Integer, String> selectPoolNameByOther();

    PageInfo<MineSetting> selectByOther(String keyWord, int pageNum, int pageSize);
}



