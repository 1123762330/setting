package com.xnpool.setting.domain.mapper;

import com.xnpool.setting.domain.pojo.IpAssign;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xnpool.setting.domain.pojo.MineIdAndIP;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zly
 * @since 2020-04-09
 */
public interface IpAssignMapper extends BaseMapper<IpAssign> {

    Integer batchInsert(ArrayList<MineIdAndIP> ip_list);

    Integer deleteByBatch(ArrayList<MineIdAndIP> delete_ips);

    List<IpAssign> selectByOther(@Param("list") List<MineIdAndIP> ip_list, @Param("isDel")Integer isDel);
}
