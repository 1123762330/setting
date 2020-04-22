package com.xnpool.setting.domain.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xnpool.setting.domain.model.FactoryHouseExample;
import com.xnpool.setting.domain.pojo.FactoryHouse;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zly
 * @since 2020-04-22
 */
public interface FactoryHouseMapper extends BaseMapper<FactoryHouse> {

    List<FactoryHouse> selectFactoryByMineId(@Param("mineId") Integer mineId,@Param("id")  Integer id);

    int deleteByKeyId(int id);

    List<FactoryHouseExample> selectByOther(@Param("keyWord") String keyWord, Page<FactoryHouseExample> page);

    List<HashMap> selectFactoryNameByMineId(Integer mineId);

    Integer equalsFactoryNum(@Param("factoryNum")String factoryNum,@Param("mineId") Integer mineId);

    List<FactoryHouse> selectByMineId(int id);
}
