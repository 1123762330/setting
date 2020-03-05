package com.xnpool.setting.service;

import com.github.pagehelper.PageInfo;
import com.xnpool.setting.domain.pojo.AgreementSetting;

import java.util.HashMap;
import java.util.List;

/**
 * @author zly
 * @version 1.0
 * @date 2020/2/7 9:42
 */
public interface AgreementSettingService {


    int deleteByPrimaryKey(Integer id);

    int insert(AgreementSetting record);

    int insertSelective(AgreementSetting record);

    AgreementSetting selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AgreementSetting record);

    int updateByPrimaryKey(AgreementSetting record);

    int updateById(int id);

    PageInfo<AgreementSetting> selectByOther(String keyWord, int pageNum, int pageSize);

    HashMap<Integer, String> selectAgreementMap();
}

