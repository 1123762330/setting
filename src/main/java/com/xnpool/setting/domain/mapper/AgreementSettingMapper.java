package com.xnpool.setting.domain.mapper;

import com.xnpool.setting.domain.pojo.AgreementSetting;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;

/**
 * @author  zly
 * @date  2020/2/7 9:42
 * @version 1.0
 */
public interface AgreementSettingMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(AgreementSetting record);

    int insertSelective(AgreementSetting record);

    AgreementSetting selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AgreementSetting record);

    int updateByPrimaryKey(AgreementSetting record);

    int updateById(int id);

    List<AgreementSetting> selectByOther(@Param("keyWord") String keyWord);

   List<HashMap> selectAgreementMap();
}