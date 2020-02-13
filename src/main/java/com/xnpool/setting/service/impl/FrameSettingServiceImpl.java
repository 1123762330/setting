package com.xnpool.setting.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xnpool.setting.domain.pojo.FactoryHouse;
import com.xnpool.setting.domain.pojo.FrameSettingExample;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import com.xnpool.setting.domain.mapper.FrameSettingMapper;
import com.xnpool.setting.domain.pojo.FrameSetting;
import com.xnpool.setting.service.FrameSettingService;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;

/**
 * @author zly
 * @version 1.0
 * @date 2020/2/4 20:04
 */
@Service
public class FrameSettingServiceImpl implements FrameSettingService {

    @Resource
    private FrameSettingMapper frameSettingMapper;

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return frameSettingMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(FrameSetting record) {
        return frameSettingMapper.insert(record);
    }

    @Override
    public int insertSelective(FrameSetting record) {
        return frameSettingMapper.insertSelective(record);
    }

    @Override
    public FrameSetting selectByPrimaryKey(Integer id) {
        return frameSettingMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(FrameSetting record) {
        return frameSettingMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(FrameSetting record) {
        return frameSettingMapper.updateByPrimaryKey(record);
    }

    @Override
    public int updateById(int id) {

        return frameSettingMapper.updateById(id);
    }

    @Override
    public PageInfo<FrameSettingExample> selectByOther(String keyWord,int pageNum,int pageSize) {
        if (!StringUtils.isEmpty(keyWord)) {
            keyWord = "%" + keyWord + "%";
        }
        PageHelper.startPage(pageNum, pageSize);
        List<FrameSettingExample> frameSettingExamples = frameSettingMapper.selectByOther(keyWord);
        PageInfo<FrameSettingExample> pageInfo = new PageInfo<>(frameSettingExamples);
        return pageInfo;
    }

    @Override
    public HashMap<Integer, String> selectFrameNameByFactoryId(Integer factoryId) {
        List<HashMap> hashMaps = frameSettingMapper.selectFrameNameByFactoryId(factoryId);
        if (hashMaps.isEmpty()) {
            return null;
        } else {
            HashMap<Integer, String> resultMap = new HashMap<>();
            hashMaps.forEach(hashMap -> {
                Integer id = Integer.valueOf(hashMap.get("id").toString());
                String factoryName = hashMap.get("frameName").toString();
                resultMap.put(id, factoryName);
            });
            return resultMap;
        }
    }

    /**
     * @Description 添加分组时需要用到的矿机架列表
     * @Author zly
     * @Date 15:29 2020/2/10
     * @Param
     * @return
     */
    public HashMap<Integer, String> selectFrameNameToGruop(Integer factoryId) {
        List<HashMap> hashMaps = frameSettingMapper.selectFrameNameByFactoryId(factoryId);
        if (hashMaps.isEmpty()) {
            return null;
        } else {
            HashMap<Integer, String> resultMap = new HashMap<>();
            hashMaps.forEach(hashMap -> {
                Integer id = Integer.valueOf(hashMap.get("id").toString());
                StringBuffer  frameName = new StringBuffer(hashMap.get("frameName").toString());
                StringBuffer number = new StringBuffer(hashMap.get("number").toString());
                StringBuffer resultStr=frameName.append(" 1-"+number+"层");
                resultMap.put(id, resultStr.toString());
            });
            return resultMap;
        }
    }

}

