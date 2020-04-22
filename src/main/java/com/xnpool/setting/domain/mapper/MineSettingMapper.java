package com.xnpool.setting.domain.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xnpool.setting.domain.model.FrameSettingExample;
import com.xnpool.setting.domain.pojo.MineSetting;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zly
 * @since 2020-04-22
 */
public interface MineSettingMapper extends BaseMapper<MineSetting> {

    List<String> selectMineNameList(@Param("id") Integer id);

    int deleteByKeyId(int id);

    List<MineSetting> selectByOther(@Param("keyWord") String keyWord, Page<MineSetting> page);

    List<Integer> selectMineId();
}
