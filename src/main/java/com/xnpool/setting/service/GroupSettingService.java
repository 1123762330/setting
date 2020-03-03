package com.xnpool.setting.service;

import com.github.pagehelper.PageInfo;
import com.xnpool.setting.domain.pojo.GroupSetting;
import com.xnpool.setting.domain.pojo.GroupSettingExample;

import java.util.HashMap;

/**
 * @author zly
 * @version 1.0
 * @date 2020/2/5 15:36
 */
public interface GroupSettingService {


    int deleteByPrimaryKey(Integer groupid);

    int insert(GroupSetting record);

    void insertSelective(GroupSetting record);

    GroupSetting selectByPrimaryKey(Integer groupid);

    void updateByPrimaryKeySelective(GroupSetting record);

    int updateByPrimaryKey(GroupSetting record);

    void updateById(int id);

    PageInfo<GroupSettingExample> selectByOther(String keyWord, int pageNum, int pageSize);

    HashMap<Integer,String> selectGroupMap();
}



