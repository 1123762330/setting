package com.xnpool.setting.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xnpool.setting.common.BaseController;
import com.xnpool.setting.domain.pojo.FactoryHouseExample;
import com.xnpool.setting.domain.pojo.FrameSetting;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import com.xnpool.setting.domain.pojo.FactoryHouse;
import com.xnpool.setting.domain.mapper.FactoryHouseMapper;
import com.xnpool.setting.service.FactoryHouseService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @author zly
 * @version 1.0
 * @date 2020/2/4 15:29
 */
@Service
public class FactoryHouseServiceImpl extends BaseController implements FactoryHouseService {

    @Resource
    private FactoryHouseMapper factoryHouseMapper;

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return factoryHouseMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(FactoryHouse record) {
        return factoryHouseMapper.insert(record);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertSelective(FactoryHouse record) {
        int rows = factoryHouseMapper.insertSelective(record);
        record.setCreateTime(new Date());
        redisToInsert(rows, "factory_house", record, record.getMineId());
    }

    @Override
    public FactoryHouse selectByPrimaryKey(Integer id) {
        return factoryHouseMapper.selectByPrimaryKey(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateByPrimaryKeySelective(FactoryHouse record) {
        int rows = factoryHouseMapper.updateByPrimaryKeySelective(record);
        record.setUpdateTime(new Date());
        redisToUpdate(rows, "factory_house", record, record.getMineId());
    }

    @Override
    public int updateByPrimaryKey(FactoryHouse record) {
        return factoryHouseMapper.updateByPrimaryKey(record);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateById(int id) {
        int rows = factoryHouseMapper.updateById(id);
        FactoryHouse record = new FactoryHouse();
        record.setUpdateTime(new Date());
        record.setId(id);
        redisToDelete(rows, "factory_house", record, record.getMineId());
    }

    @Override
    public PageInfo<FactoryHouseExample> selectByOther(String keyWord, int pageNum, int pageSize) {
        if (!StringUtils.isEmpty(keyWord)) {
            keyWord = "%" + keyWord + "%";
        }
        PageHelper.startPage(pageNum, pageSize);
        List<FactoryHouseExample> factoryHouses = factoryHouseMapper.selectByOther(keyWord);
        PageInfo<FactoryHouseExample> pageInfo = new PageInfo<>(factoryHouses);
        return pageInfo;
    }

    @Override
    public List<FactoryHouse> selectByMineId(Integer mineId) {
        return factoryHouseMapper.selectByMineId(mineId);
    }

    @Override
    public HashMap<Integer, String> selectFactoryNameByMineId(Integer mineId) {
        List<HashMap> hashMaps = factoryHouseMapper.selectFactoryNameByMineId(mineId);
        if (hashMaps.isEmpty()) {
            return null;
        } else {
            HashMap<Integer, String> resultMap = new HashMap<>();
            hashMaps.forEach(hashMap -> {
                Integer id = Integer.valueOf(hashMap.get("id").toString());
                String factoryName = hashMap.get("factoryName").toString();
                resultMap.put(id, factoryName);
            });
            return resultMap;
        }
    }

}



