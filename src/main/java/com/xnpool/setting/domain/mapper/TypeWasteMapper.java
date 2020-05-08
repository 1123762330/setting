package com.xnpool.setting.domain.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xnpool.setting.domain.model.TypeWasteExample;
import com.xnpool.setting.domain.pojo.TypeWaste;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zly
 * @since 2020-05-05
 */
public interface TypeWasteMapper extends BaseMapper<TypeWaste> {

    List<TypeWasteExample> selectByOther(Page<TypeWasteExample> page);
}
