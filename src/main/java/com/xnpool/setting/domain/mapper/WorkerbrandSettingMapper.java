package com.xnpool.setting.domain.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xnpool.setting.domain.pojo.WorkerbrandSetting;import org.apache.ibatis.annotations.Param;import java.util.List;

/**
 * @author zly
 * @version 1.0
 * @date 2020/3/20 15:06
 */
public interface WorkerbrandSettingMapper extends BaseMapper<WorkerbrandSetting> {

    int deleteByIdKey(int id);

    List<WorkerbrandSetting> selectByOther(@Param("keyWord") String keyWord);
    List<String> selectBrandNameByAlgorithmId(@Param("algorithmId") Integer algorithmId);
}