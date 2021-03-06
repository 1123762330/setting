package com.xnpool.setting.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xnpool.logaop.service.exception.DataExistException;
import com.xnpool.logaop.service.exception.InsertException;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import com.xnpool.setting.domain.mapper.AgreementSettingMapper;
import com.xnpool.setting.domain.pojo.AgreementSetting;
import com.xnpool.setting.service.AgreementSettingService;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;

/**
 * @author zly
 * @version 1.0
 * @date 2020/2/7 9:42
 */
@Service
public class AgreementSettingServiceImpl implements AgreementSettingService {

    @Resource
    private AgreementSettingMapper agreementSettingMapper;

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return agreementSettingMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(AgreementSetting record) {
        return agreementSettingMapper.insert(record);
    }

    @Override
    public int insertSelective(AgreementSetting record) {
        List<String> list = agreementSettingMapper.selectNameList(record.getId());
        if (list.contains(record.getAgreementName())) {
            throw new DataExistException("数据已存在,请勿重复添加!");
        }
        return agreementSettingMapper.insertSelective(record);
    }

    @Override
    public AgreementSetting selectByPrimaryKey(Integer id) {
        return agreementSettingMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(AgreementSetting record) {
        List<String> list = agreementSettingMapper.selectNameList(record.getId());
        if (list.contains(record.getAgreementName())) {
            throw new DataExistException("数据已存在,请勿重复添加!");
        }
        return agreementSettingMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(AgreementSetting record) {
        return agreementSettingMapper.updateByPrimaryKey(record);
    }

    @Override
    public int updateById(int id) {
        return agreementSettingMapper.updateById(id);
    }

    @Override
    public PageInfo<AgreementSetting> selectByOther(String keyWord, int pageNum, int pageSize) {
        if (!StringUtils.isEmpty(keyWord)) {
            keyWord = "%" + keyWord + "%";
        }
        PageHelper.startPage(pageNum, pageSize);
        List<AgreementSetting> agreementSettingList = agreementSettingMapper.selectByOther(keyWord);
        PageInfo<AgreementSetting> pageInfo = new PageInfo<>(agreementSettingList);
        return pageInfo;
    }

    public HashMap<Integer, String> selectAgreementMap() {
        List<HashMap> hashMaps = agreementSettingMapper.selectAgreementMap();
        HashMap<Integer, String> resultMap = new HashMap<>();
        hashMaps.forEach(hashMap -> {
            Integer id = Integer.valueOf(hashMap.get("id").toString());
            String agreementName = hashMap.get("agreement_name").toString();
            resultMap.put(id, agreementName);
        });
        return resultMap;
    }


}



