package com.xnpool.setting.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xnpool.setting.common.BaseController;
import com.xnpool.setting.common.exception.InsertException;
import com.xnpool.setting.common.exception.UpdateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import com.xnpool.setting.domain.pojo.MineSetting;
import com.xnpool.setting.domain.mapper.MineSettingMapper;
import com.xnpool.setting.service.MineSettingService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @author zly
 * @version 1.0
 * @date 2020/2/3 12:46
 */
@Service
@Slf4j
public class MinesettingServiceImpl extends BaseController implements MineSettingService {

    @Resource
    private MineSettingMapper minesettingMapper;

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return minesettingMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(MineSetting record) {
        return minesettingMapper.insert(record);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertSelective(MineSetting record) {
        int rows = minesettingMapper.insertSelective(record);
        if (rows == 1) {
            //入库成功,写缓存
            record.setCreatetime(new Date());
            insertRedis("mine_setting", INSERT, record.toString());
        } else {
            throw new InsertException("添加失败");
        }
    }

    @Override
    public MineSetting selectByPrimaryKey(Integer id) {
        return minesettingMapper.selectByPrimaryKey(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateByPrimaryKeySelective(MineSetting record) {
        int rows = minesettingMapper.updateByPrimaryKeySelective(record);
        if (rows == 1) {
            //修改成功,写缓存
            record.setUpdatetime(new Date());
            insertRedis("mine_setting", UPDATE, record.toString());
        } else {
            throw new UpdateException("修改失败");
        }
    }

    @Override
    public int updateByPrimaryKey(MineSetting record) {
        return minesettingMapper.updateByPrimaryKey(record);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateById(int id) {
        int rows = minesettingMapper.updateById(id);
        if (rows == 1) {
            //修改成功,写缓存
            MineSetting record = new MineSetting();
            record.setUpdatetime(new Date());
            record.setId(id);
            insertRedis("mine_setting", DELETE, record.toString());
        } else {
            throw new UpdateException("删除失败");
        }
    }

    @Override
    public HashMap<Integer, String> selectPoolNameByOther() {
        List<MineSetting> mineSettingList = minesettingMapper.selectByOther(null);
        HashMap<Integer, String> resultMap = new HashMap<>();
        mineSettingList.forEach(mineSetting -> {
            Integer id = mineSetting.getId();
            String mineName = mineSetting.getMinename();
            resultMap.put(id, mineName);
        });
        return resultMap;
    }

    @Override
    public PageInfo<MineSetting> selectByOther(String keyWord, int pageNum, int pageSize) {
        if (!StringUtils.isEmpty(keyWord)) {
            keyWord = "%" + keyWord + "%";
        }
        PageHelper.startPage(pageNum, pageSize);
        List<MineSetting> mineSettingList = minesettingMapper.selectByOther(keyWord);
        PageInfo<MineSetting> pageInfo = new PageInfo<>(mineSettingList);
        return pageInfo;
    }

}

