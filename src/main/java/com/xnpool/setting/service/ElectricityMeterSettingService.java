package com.xnpool.setting.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageInfo;
import com.xnpool.setting.domain.model.ElectricityMeterSettingExample;
import com.xnpool.setting.domain.pojo.ElectricityMeterSetting;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zly
 * @since 2020-05-04
 */
public interface ElectricityMeterSettingService extends IService<ElectricityMeterSetting> {

    int insertSelective(ElectricityMeterSetting electricityMeterSetting);

    int updateByPrimaryKeySelective(ElectricityMeterSetting electricityMeterSetting);

    int deleteById(int id);

    Page<ElectricityMeterSettingExample> selectByOther(String keyWord, int pageNum, int pageSize);

    String generateQRCodeImage(Integer id);
}
