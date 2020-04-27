package com.xnpool.setting.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageInfo;
import com.xnpool.setting.domain.model.IpSettingExample;
import com.xnpool.setting.domain.pojo.IpParam;
import com.xnpool.setting.domain.pojo.IpSetting;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.HashMap;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zly
 * @since 2020-04-27
 */
public interface IpSettingService extends IService<IpSetting> {

    Integer insertSelective(IpSetting ipSetting);

    Integer deleteById(int id);

    Page<IpSettingExample> selectByOther(String keyWord, int pageNum, int pageSize);

    void updateByPrimaryKeySelective(IpSetting ipSetting);

    HashMap<Integer, String> selectByIPStart();

    HashMap<Integer, String> selectByIpStartByMineId(String mineName, Integer mineId);

    void batchSaveIp(IpParam ipParam);

    HashMap<String, String> selectIpQuJian();
}
