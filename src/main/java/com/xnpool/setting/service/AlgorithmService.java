package com.xnpool.setting.service;

import com.github.pagehelper.PageInfo;
import com.xnpool.setting.domain.pojo.Algorithm;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * @author zly
 * @version 1.0
 * @date 2020/3/19 16:16
 */
@Service
public interface AlgorithmService {
    int deleteByPrimaryKey(Integer id);

    int insert(Algorithm record);


    PageInfo<Algorithm> selectAlgorithm(String keyWord, int pageNum, int pageSize);

    HashMap<Integer, String>  selectAlgorithmMap(String token,Long tenantId);


    int updateByPrimaryKey(Algorithm record);

    Algorithm selectByPrimaryKey(Integer id);

    int deleteById(int id);
}

