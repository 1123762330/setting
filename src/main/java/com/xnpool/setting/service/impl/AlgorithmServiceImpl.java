package com.xnpool.setting.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xnpool.logaop.service.exception.DataNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import com.xnpool.setting.domain.pojo.Algorithm;
import com.xnpool.setting.domain.mapper.AlgorithmMapper;
import com.xnpool.setting.service.AlgorithmService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;

/**
 * @author zly
 * @version 1.0
 * @date 2020/3/19 16:21
 */
@Service
public class AlgorithmServiceImpl implements AlgorithmService {

    @Resource
    private AlgorithmMapper algorithmMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteByPrimaryKey(Integer id) {
        return algorithmMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(Algorithm record) {
        return algorithmMapper.insert(record);
    }

    @Override
    public PageInfo<Algorithm> selectAlgorithm(String keyWord, int pageNum, int pageSize) {
        if (!StringUtils.isEmpty(keyWord)) {
            keyWord = "%" + keyWord + "%";
        }
        PageHelper.startPage(pageNum, pageSize);
        List<Algorithm> algorithmList = algorithmMapper.selectAlgorithm(keyWord);
        PageInfo<Algorithm> pageInfo = new PageInfo<>(algorithmList);
        return pageInfo;
    }

    @Override
    public HashMap<Integer, String> selectAlgorithmMap() {
        List<HashMap> hashMapList = algorithmMapper.selectAlgorithmMap();
        HashMap<Integer, String> resultMap = new HashMap<>();
        for (HashMap hashMap : hashMapList) {
            String algorithm_name = hashMap.get("algorithm_name").toString();
            Integer id = Integer.valueOf(hashMap.get("id").toString());
            resultMap.put(id, algorithm_name);
        }
        return resultMap;
    }

    @Override
    public Algorithm selectByPrimaryKey(Integer id) {
        return algorithmMapper.selectById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteById(int id) {
        return algorithmMapper.deleteByAlgorithmId(id);
    }

    @Override
    public int updateByPrimaryKey(Algorithm record) {
        return algorithmMapper.updateById(record);
    }

}
