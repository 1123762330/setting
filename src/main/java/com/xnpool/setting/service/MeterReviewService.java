package com.xnpool.setting.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xnpool.setting.domain.model.MeterReviewExample;
import com.xnpool.setting.domain.pojo.MeterReview;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xnpool.setting.domain.pojo.MineSetting;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zly
 * @since 2020-05-04
 */
public interface MeterReviewService extends IService<MeterReview> {

    Page<MeterReviewExample> selectByOther(String keyWord, int pageNum, int pageSize);

    void insertSelective(MeterReview meterReview);

    void deleteById(int id);

    int batchSave(String json);
}
