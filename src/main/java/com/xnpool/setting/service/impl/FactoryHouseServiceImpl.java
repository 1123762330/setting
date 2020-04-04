package com.xnpool.setting.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xnpool.logaop.service.exception.DataExistException;
import com.xnpool.logaop.service.exception.InsertException;
import com.xnpool.setting.common.BaseController;
import com.xnpool.setting.domain.model.FactoryHouseExample;
import com.xnpool.setting.domain.redismodel.FactoryHouseRedisModel;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import com.xnpool.setting.domain.pojo.FactoryHouse;
import com.xnpool.setting.domain.mapper.FactoryHouseMapper;
import com.xnpool.setting.service.FactoryHouseService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;

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
    public Integer insertSelective(FactoryHouse record) {
        List<String> list = factoryHouseMapper.selectFactoryNameList(record.getMineId(),record.getId());
        if (list.contains(record.getFactoryName())) {
            throw new DataExistException("数据已存在,请勿重复添加!");
        }

        int rows = factoryHouseMapper.insertSelective(record);
        record.setCreateTime(new Date());
        FactoryHouseRedisModel factoryHouseRedisModel = getFactoryHouseRedisModel(record);
        redisToInsert(rows, "factory_house", factoryHouseRedisModel, record.getMineId());
        return rows;
    }

    @Override
    public FactoryHouse selectByPrimaryKey(Integer id) {
        return factoryHouseMapper.selectByPrimaryKey(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateByPrimaryKeySelective(FactoryHouse record) {
        List<String> list = factoryHouseMapper.selectFactoryNameList(record.getMineId(),record.getId());
        if (list.contains(record.getFactoryName())) {
            throw new DataExistException("数据已存在,请勿重复添加!");
        }
        int rows = factoryHouseMapper.updateByPrimaryKeySelective(record);
        record.setUpdateTime(new Date());
        FactoryHouseRedisModel factoryHouseRedisModel = getFactoryHouseRedisModel(record);
        redisToUpdate(rows, "factory_house", factoryHouseRedisModel, record.getMineId());
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
        FactoryHouseRedisModel factoryHouseRedisModel = getFactoryHouseRedisModel(record);
        Integer mineId = factoryHouseMapper.selectMineId(id);
        redisToDelete(rows, "factory_house", factoryHouseRedisModel, mineId);
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
                String factoryName = hashMap.get("factory_name").toString();
                resultMap.put(id, factoryName);
            });
            return resultMap;
        }
    }

    @Override
    public HashMap<String, Integer> selectMapByFactoryName(Integer mineId) {
        List<HashMap> hashMaps = factoryHouseMapper.selectFactoryNameByMineId(mineId);
        if (hashMaps.isEmpty()) {
            return null;
        } else {
            HashMap<String, Integer> resultMap = new HashMap<>();
            hashMaps.forEach(hashMap -> {
                Integer id = Integer.valueOf(hashMap.get("id").toString());
                String factoryName = hashMap.get("factory_name").toString();
                resultMap.put(factoryName,id);
            });
            return resultMap;
        }
    }

    @Override
    public Integer equalsFactoryName(String factoryStr, Integer mineId) {
        return factoryHouseMapper.equalsFactoryName(factoryStr,mineId);
    }

}



