package com.xnpool.setting.domain.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xnpool.setting.domain.model.ElectricityMeterSettingExample;
import com.xnpool.setting.domain.pojo.ElectricityMeterSetting;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zly
 * @since 2020-05-04
 */
public interface ElectricityMeterSettingMapper extends BaseMapper<ElectricityMeterSetting> {

    List<String> selectNameList(@Param("id") Integer id);

    int deleteByKeyId(int id);

    List<ElectricityMeterSettingExample> selectByOther(@Param("keyWord") String keyWord, Page<ElectricityMeterSettingExample> page);

    HashMap selectOtherById(Integer id);
}
