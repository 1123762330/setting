package com.xnpool.setting.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sun.org.apache.xerces.internal.util.EntityResolverWrapper;
import com.xnpool.setting.domain.mapper.ElectricityMeterSettingMapper;
import com.xnpool.setting.domain.model.MeterReviewExample;
import com.xnpool.setting.domain.pojo.ElectricityMeterSetting;
import com.xnpool.setting.domain.pojo.MeterReview;
import com.xnpool.setting.domain.mapper.MeterReviewMapper;
import com.xnpool.setting.domain.pojo.MeterReviewParam;
import com.xnpool.setting.domain.pojo.MineSetting;
import com.xnpool.setting.service.MeterReviewService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author zly
 * @since 2020-05-04
 */
@Service
public class MeterReviewServiceImpl extends ServiceImpl<MeterReviewMapper, MeterReview> implements MeterReviewService {
    @Autowired
    private MeterReviewMapper meterReviewMapper;

    @Autowired
    private ElectricityMeterSettingMapper electricityMeterSettingMapper;

    @Override
    public Page<MeterReviewExample> selectByOther(String keyWord, int pageNum, int pageSize) {
        if (!StringUtils.isEmpty(keyWord)) {
            keyWord = "%" + keyWord + "%";
        }
        Page<MeterReviewExample> page = new Page<>(pageNum, pageSize);
        List<MeterReviewExample> MeterReviewList = meterReviewMapper.selectByOther(keyWord, page);
        page.setRecords(MeterReviewList);
        return page;
    }

    @Override
    public void insertSelective(MeterReview meterReview) {
        QueryWrapper<MeterReview> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_delete", 0);
        queryWrapper.eq("electricity_id", meterReview.getElectricityId());
        MeterReview meterReview_db = meterReviewMapper.selectOne(queryWrapper);
        if (meterReview_db==null){
            //表中没有记录,那就是首次新增
            meterReviewMapper.insert(meterReview);
        }else {
            //表中已经有记录了,那就是修改
            meterReviewMapper.update(meterReview,queryWrapper);
        }
    }

    @Override
    public void deleteById(int id) {
        meterReviewMapper.deleteByKeyId(id);
    }

    @Override
    public int batchSave(String json) {
        JSONObject jsonObject = JSONObject.parseObject(json);
        List<MeterReviewParam> MeterReviewParamList = JSONObject.parseArray(jsonObject.getString("json"),MeterReviewParam.class);
        List<MeterReview> meterReviews = new ArrayList<>();
        MeterReviewParamList.forEach(meterReviewParam -> {
            MeterReview meterReview = new MeterReview();
            meterReview.setElectricityId(meterReviewParam.getElectricityId());
            //起始码从数据库查询
            //终止码是次记录
            //使用量是(终止码-起始码)*倍率
            ElectricityMeterSetting electricityMeterSetting = electricityMeterSettingMapper.selectById(meterReviewParam.getElectricityId());
            String lastCode = electricityMeterSetting.getLastCode();
            String nowCode = electricityMeterSetting.getNowCode();
            String electricityStart = electricityMeterSetting.getElectricityStart();
            Integer multiple = electricityMeterSetting.getMultiple();
            if (StringUtils.isEmpty(nowCode)){
                meterReview.setStart(Double.valueOf(electricityStart));
                double usePower = (meterReviewParam.getEnd() - Double.valueOf(electricityStart)) * multiple;
                meterReview.setUsePower(usePower);
            }else if( "0".equals(lastCode) && !StringUtils.isEmpty(nowCode)){
                meterReview.setStart(Double.valueOf(nowCode));
                double usePower = (meterReviewParam.getEnd() - Double.valueOf(nowCode)) * multiple;
                meterReview.setUsePower(usePower);
            }else {
                meterReview.setStart(Double.valueOf(lastCode));
                double usePower = (meterReviewParam.getEnd() - Double.valueOf(lastCode)) * multiple;
                meterReview.setUsePower(usePower);
            }
            meterReview.setEnd(meterReviewParam.getEnd());
            meterReview.setPath(meterReviewParam.getPath());
            meterReview.setCreateTime(meterReviewParam.getCreateTime());
            meterReviews.add(meterReview);
        });
        return meterReviewMapper.batchSave(meterReviews);
    }
}
