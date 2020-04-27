package com.xnpool.setting.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageInfo;
import com.xnpool.logaop.service.exception.DataExistException;
import com.xnpool.logaop.service.exception.InsertException;
import com.xnpool.setting.common.BaseController;
import com.xnpool.setting.domain.model.FrameSettingExample;
import com.xnpool.setting.domain.pojo.FrameSetting;
import com.xnpool.setting.domain.pojo.MineSetting;
import com.xnpool.setting.domain.mapper.MineSettingMapper;
import com.xnpool.setting.domain.redismodel.FrameSettingRedisModel;
import com.xnpool.setting.domain.redismodel.MineSettingRedisModel;
import com.xnpool.setting.service.MineSettingService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author zly
 * @since 2020-04-22
 */
@Service
@Slf4j
public class MineSettingServiceImpl extends ServiceImpl<MineSettingMapper, MineSetting> implements MineSettingService {
    @Resource
    private MineSettingMapper minesettingMapper;

    @Autowired
    private BaseController baseController;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(MineSetting mineSetting, String token) {
        Integer superMineId = baseController.getSuperMineId(token);
        if (superMineId != null && superMineId == -1) {
            List<String> list = minesettingMapper.selectMineNameList(mineSetting.getId());
            if (list.contains(mineSetting.getMineName())) {
                throw new DataExistException("数据已存在,请勿重复添加!");
            }
            int rows = minesettingMapper.insert(mineSetting);
            mineSetting.setCreateTime(LocalDateTime.now());
            MineSettingRedisModel mineSettingRedisModel = baseController.getMineSettingRedisModel(mineSetting);
            baseController.redisToInsert(rows, "mine_setting", mineSettingRedisModel, mineSetting.getId());
        } else {
            throw new InsertException("只有管理员才能添加矿场");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(int id) {
        int rows = minesettingMapper.deleteByKeyId(id);
        MineSetting mineSetting = minesettingMapper.selectById(id);
        MineSettingRedisModel mineSettingRedisModel = baseController.getMineSettingRedisModel(mineSetting);
        baseController.redisToDelete(rows, "mine_setting", mineSettingRedisModel, mineSetting.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateByKeyId(MineSetting mineSetting) {
        List<String> list = minesettingMapper.selectMineNameList(mineSetting.getId());
        if (list.contains(mineSetting.getMineName())) {
            throw new DataExistException("数据已存在,请勿重复添加!");
        }
        int rows = 0;
        if (StringUtils.isEmpty(mineSetting.getDescription())) {
            UpdateWrapper updateWrapper = new UpdateWrapper();
            updateWrapper.eq("id",mineSetting.getId());
            updateWrapper.set("Description", null);
            rows = minesettingMapper.update(mineSetting, updateWrapper);
        } else {
            rows = minesettingMapper.updateById(mineSetting);
        }

        MineSetting mineSetting_db = minesettingMapper.selectById(mineSetting.getId());
        MineSettingRedisModel mineSettingRedisModel = baseController.getMineSettingRedisModel(mineSetting_db);
        baseController.redisToUpdate(rows, "mine_setting", mineSettingRedisModel, mineSetting.getId());
    }

    @Override
    public Page<MineSetting> selectByOther(String keyWord, int pageNum, int pageSize, String token) {
        List<Integer> mineIds = baseController.getMineId(token);
        List<MineSetting> resultList = new ArrayList<>();
        if (!StringUtils.isEmpty(keyWord)) {
            keyWord = "%" + keyWord + "%";
        }
        Page<MineSetting> page = new Page<>(pageNum, pageSize);
        List<MineSetting> mineSettingList = minesettingMapper.selectByOther(keyWord, page);
        for (MineSetting mineSetting : mineSettingList) {
            Integer id = mineSetting.getId();
            if (mineIds.contains(id)) {
                resultList.add(mineSetting);
            }
        }
        page.setRecords(resultList);
        return page;
    }

    @Override
    public HashMap<Integer, String> selectMineNameByOther(String token) {
        List<Integer> mineIds = baseController.getMineId(token);
        log.info("获取到的数据权限矿场id为:" + mineIds);
        List<MineSetting> mineSettingList = minesettingMapper.selectByOther(null, null);
        HashMap<Integer, String> resultMap = new HashMap<>();
        mineSettingList.forEach(mineSetting -> {
            Integer id = mineSetting.getId();
            if (mineIds.contains(id)) {
                String mineName = mineSetting.getMineName();
                resultMap.put(id, mineName);
            }

        });
        return resultMap;
    }
}
