package com.xnpool.setting.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xnpool.setting.domain.pojo.FactoryHouseExample;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import com.xnpool.setting.domain.pojo.FactoryHouse;
import com.xnpool.setting.domain.mapper.FactoryHouseMapper;
import com.xnpool.setting.service.FactoryHouseService;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;

/**
 * @author zly
 * @version 1.0
 * @date 2020/2/4 15:29
 */
@Service
public class FactoryHouseServiceImpl implements FactoryHouseService {

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
    public int insertSelective(FactoryHouse record) {
        return factoryHouseMapper.insertSelective(record);
    }

    @Override
    public FactoryHouse selectByPrimaryKey(Integer id) {
        return factoryHouseMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(FactoryHouse record) {
        return factoryHouseMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(FactoryHouse record) {
        return factoryHouseMapper.updateByPrimaryKey(record);
    }

    @Override
    public int updateById(int id) {
        return factoryHouseMapper.updateById(id);
    }

    @Override
    public PageInfo<FactoryHouseExample> selectByOther(String keyWord,int pageNum,int pageSize) {
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


