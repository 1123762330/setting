package com.xnpool.setting.domain.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xnpool.setting.domain.pojo.Algorithm;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;

/**
 * @author zly
 * @version 1.0
 * @date 2020/3/19 16:21
 */
public interface AlgorithmMapper extends BaseMapper<Algorithm> {

    List<Algorithm> selectAlgorithm(@Param("keyWord") String keyWord);

    List<HashMap> selectAlgorithmMap();

    int deleteByAlgorithmId(Integer id);
}