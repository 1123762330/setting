package com.xnpool.setting.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageInfo;
import com.xnpool.setting.domain.model.FrameSettingExample;
import com.xnpool.setting.domain.pojo.FrameSetting;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.HashMap;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zly
 * @since 2020-04-17
 */
public interface FrameSettingService extends IService<FrameSetting> {

    void deleteById(int id);

    Integer addFrame(FrameSetting entity);

    Page<FrameSettingExample> selectByOther(String keyWord, int pageNum, int pageSize, String token);

    List<HashMap> selectExistFrameByFactoryId(Integer factoryId);

    HashMap<Integer, String> selectFrameNameByFactoryId(Integer factoryId);

    HashMap<Integer, String> selectFrameNameToGruop(Integer factoryId);

    HashMap<Integer, String> selectMineFactoryAndFrame(Integer factoryId);

    Integer equalsFrameName(Integer frameStr,Integer paiNumber, Integer factoryId, Integer mineId);

    Integer insertSelectiveToBatch(FrameSetting frameSetting);

    void updateFrame(FrameSetting frameSetting);
}
