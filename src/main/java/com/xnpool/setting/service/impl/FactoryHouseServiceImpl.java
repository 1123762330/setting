package com.xnpool.setting.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xnpool.logaop.service.exception.DataExistException;
import com.xnpool.setting.common.BaseController;
import com.xnpool.setting.domain.model.FactoryHouseExample;
import com.xnpool.setting.domain.pojo.FactoryHouse;
import com.xnpool.setting.domain.mapper.FactoryHouseMapper;
import com.xnpool.setting.domain.redismodel.FactoryHouseRedisModel;
import com.xnpool.setting.service.FactoryHouseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zly
 * @since 2020-04-22
 */
@Service
public class FactoryHouseServiceImpl extends ServiceImpl<FactoryHouseMapper, FactoryHouse> implements FactoryHouseService {
    @Resource
    private FactoryHouseMapper factoryHouseMapper;

    @Autowired
    private BaseController baseController;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer addFactoryHouse(FactoryHouse record) {
        List<FactoryHouse> factoryHouses = factoryHouseMapper.selectFactoryByMineId(record.getMineId(), record.getId());
        List<String> factoryNameList = new ArrayList<>();
        List<Integer> factoryNumList = new ArrayList<>();
        factoryHouses.forEach(factoryHouse -> {
            Integer factoryNum = factoryHouse.getFactoryNum();
            String factoryName = factoryHouse.getFactoryName();
            factoryNameList.add(factoryName);
            factoryNumList.add(factoryNum);
        });
        if (factoryNameList.contains(record.getFactoryName())) {
            throw new DataExistException("厂房名称已存在,请勿重复添加!");
        }
        if (factoryNumList.contains(record.getFactoryNum())) {
            throw new DataExistException("厂房编号已存在,请勿重复添加!");
        }

        int rows = factoryHouseMapper.insert(record);
        FactoryHouse factoryHouse = factoryHouseMapper.selectById(record.getId());
        FactoryHouseRedisModel factoryHouseRedisModel = baseController.getFactoryHouseRedisModel(factoryHouse);
        baseController.redisToInsert(rows, "factory_house", factoryHouseRedisModel, record.getMineId());
        return record.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateByPrimaryKeySelective(FactoryHouse record) {
        List<FactoryHouse> factoryHouses = factoryHouseMapper.selectFactoryByMineId(record.getMineId(), record.getId());
        List<String> factoryNameList = new ArrayList<>();
        List<Integer> factoryNumList = new ArrayList<>();
        factoryHouses.forEach(factoryHouse -> {
            Integer factoryNum = factoryHouse.getFactoryNum();
            String factoryName = factoryHouse.getFactoryName();
            factoryNameList.add(factoryName);
            factoryNumList.add(factoryNum);
        });
        if (factoryNameList.contains(record.getFactoryName())) {
            throw new DataExistException("厂房名称已存在,请勿重复添加!");
        }
        if (factoryNumList.contains(record.getFactoryNum())) {
            throw new DataExistException("厂房编号已存在,请勿重复添加!");
        }
        int rows = factoryHouseMapper.updateById(record);
        FactoryHouse factoryHouse = factoryHouseMapper.selectById(record.getId());
        FactoryHouseRedisModel factoryHouseRedisModel = baseController.getFactoryHouseRedisModel(factoryHouse);
        baseController.redisToUpdate(rows, "factory_house", factoryHouseRedisModel, factoryHouse.getMineId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(int id) {
        int rows = factoryHouseMapper.deleteByKeyId(id);
        FactoryHouse factoryHouse = factoryHouseMapper.selectById(id);
        FactoryHouseRedisModel factoryHouseRedisModel = baseController.getFactoryHouseRedisModel(factoryHouse);
        baseController.redisToDelete(rows, "factory_house", factoryHouseRedisModel, factoryHouse.getMineId());
    }

    @Override
    public Page<FactoryHouseExample> selectByOther(String keyWord, int pageNum, int pageSize, String token) {
        List<Integer> mineIds = baseController.getMineId(token);
        if (!StringUtils.isEmpty(keyWord)) {
            keyWord = "%" + keyWord + "%";
        }
        ArrayList<FactoryHouseExample> resultList = new ArrayList<>();
        Page<FactoryHouseExample> page = new Page<>(pageNum, pageSize);
        List<FactoryHouseExample> factoryHouses = factoryHouseMapper.selectByOther(keyWord,page);
        for (FactoryHouseExample factoryHouse : factoryHouses) {
            Integer id = factoryHouse.getMineId();
            if (mineIds.contains(id)){
                resultList.add(factoryHouse);
            }
        }
        page.setRecords(resultList);
        return page;
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
    public Integer equalsFactoryNum(String factoryNum, Integer mineId) {
        return factoryHouseMapper.equalsFactoryNum(factoryNum,mineId);
    }

    @Override
    public List<FactoryHouse> selectByMineId(int id) {
        List<FactoryHouse> factoryHouses = factoryHouseMapper.selectByMineId(id);
        return factoryHouses;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer insertSelectiveToBatch(FactoryHouse record) {
        int rows = factoryHouseMapper.insert(record);
        FactoryHouse factoryHouse = factoryHouseMapper.selectById(record.getId());
        FactoryHouseRedisModel factoryHouseRedisModel = baseController.getFactoryHouseRedisModel(factoryHouse);
        baseController.redisToInsert(rows, "factory_house", factoryHouseRedisModel, record.getMineId());
        return record.getId();
    }
}
