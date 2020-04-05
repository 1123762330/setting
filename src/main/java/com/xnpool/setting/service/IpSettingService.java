package com.xnpool.setting.service;

import com.github.pagehelper.PageInfo;
import com.xnpool.setting.domain.model.IpSettingExample;
import com.xnpool.setting.domain.pojo.IpSetting;

import java.util.HashMap;
import java.util.List;

/**
 * @author zly
 * @version 1.0
 * @date 2020/2/5 12:38
 */
public interface IpSettingService {


    int deleteByPrimaryKey(Integer id);

    int insert(IpSetting record);

    void insertSelective(IpSetting record);

    IpSetting selectByPrimaryKey(Integer id);

    void updateByPrimaryKeySelective(IpSetting record);

    int updateByPrimaryKey(IpSetting record);

    void updateById(int id);

    PageInfo<IpSettingExample> selectByOther(String keyWord, int pageNum, int pageSize);

    HashMap<Integer, String> selectByIPStart();

    HashMap<String, String> selectIpQuJian();

    HashMap<Integer, String> selectByIpStartByMineId(String mineName,Integer mineId);
}


