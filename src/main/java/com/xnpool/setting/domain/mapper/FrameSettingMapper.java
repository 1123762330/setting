package com.xnpool.setting.domain.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xnpool.setting.domain.model.FrameSettingExample;
import com.xnpool.setting.domain.pojo.FeeSetting;
import com.xnpool.setting.domain.pojo.FrameSetting;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import javafx.scene.control.Pagination;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zly
 * @since 2020-04-17
 */
public interface FrameSettingMapper extends BaseMapper<FrameSetting> {

    int deleteByKeyId(Integer id);

    List<FrameSettingExample> selectByOther(@Param("keyWord") String keyWord, Page<FrameSettingExample> page);

    List<HashMap> selectFrameNameByFactoryId(Integer factoryId);

    Integer equalsFrameName(@Param("storageRacksNum")Integer storageRacksNum,
                            @Param("rowNum")Integer rowNum,
                            @Param("factoryId") Integer factoryId,@Param("mineId")  Integer mineId);

    List<HashMap> selectMineFactoryAndFrame(@Param("factoryId") Integer factoryId);

    Integer isExist(FrameSetting record);

    Integer selectByOtherCOUNT();
}
