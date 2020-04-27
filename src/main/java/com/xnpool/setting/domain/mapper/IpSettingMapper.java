package com.xnpool.setting.domain.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xnpool.setting.domain.model.IpSettingExample;
import com.xnpool.setting.domain.pojo.IpSetting;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xnpool.setting.domain.pojo.MineSetting;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zly
 * @since 2020-04-27
 */
public interface IpSettingMapper extends BaseMapper<IpSetting> {

    Integer deleteByKeyId(int id);

    List<IpSettingExample> selectByOther(@Param("keyWord") String keyWord,@Param("mineId") Integer mineId, Page<IpSettingExample> page);

    List<String> selectNameList(@Param("id")Integer id);
}
