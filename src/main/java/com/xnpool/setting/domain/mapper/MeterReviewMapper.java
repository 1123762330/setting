package com.xnpool.setting.domain.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xnpool.setting.domain.model.MeterReviewExample;
import com.xnpool.setting.domain.pojo.MeterReview;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zly
 * @since 2020-05-04
 */
public interface MeterReviewMapper extends BaseMapper<MeterReview> {

    List<MeterReviewExample> selectByOther(@Param("keyWord") String keyWord, Page<MeterReviewExample> page);

    int deleteByKeyId(int id);

    int batchSave(List<MeterReview> meterReviews);
}
