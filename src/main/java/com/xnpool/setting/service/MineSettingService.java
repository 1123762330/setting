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

    void insertSelective(MineSetting record);

    MineSetting selectByPrimaryKey(Integer id);

    void updateByPrimaryKeySelective(MineSetting record);

    int updateByPrimaryKey(MineSetting record);

    void updateById(int id);

    HashMap<Integer, String> selectMineNameByOther();

    PageInfo<MineSetting> selectByOther(String keyWord, int pageNum, int pageSize,String token);


}




