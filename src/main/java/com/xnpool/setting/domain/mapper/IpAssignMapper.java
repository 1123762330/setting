package com.xnpool.setting.domain.mapper;

import com.xnpool.setting.domain.pojo.IpAssign;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xnpool.setting.domain.pojo.MineIdAndIP;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zly
 * @since 2020-04-03
 */
public interface IpAssignMapper extends BaseMapper<IpAssign> {

    Integer batchInsert(ArrayList<MineIdAndIP> ip_list);
    List<Integer> selectIpIdList();
}
