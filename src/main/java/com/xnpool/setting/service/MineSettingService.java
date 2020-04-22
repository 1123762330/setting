package com.xnpool.setting.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageInfo;
import com.xnpool.setting.domain.pojo.MineSetting;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.HashMap;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zly
 * @since 2020-04-22
 */
public interface MineSettingService extends IService<MineSetting> {

    void save(MineSetting mineSetting, String token);

    void deleteById(int id);

    void updateByKeyId(MineSetting mineSetting);

    Page<MineSetting> selectByOther(String keyWord, int pageNum, int pageSize, String token);

    HashMap<Integer, String> selectMineNameByOther(String token);
}
