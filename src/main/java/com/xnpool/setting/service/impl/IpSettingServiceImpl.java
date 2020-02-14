package com.xnpool.setting.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xnpool.setting.config.ApiContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import com.xnpool.setting.domain.pojo.IpSetting;
import com.xnpool.setting.domain.mapper.IpSettingMapper;
import com.xnpool.setting.service.IpSettingService;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;

/**
 * @author zly
 * @version 1.0
 * @date 2020/2/5 12:38
 */
@Service
public class IpSettingServiceImpl implements IpSettingService {

    @Autowired
    private IpSettingMapper ipSettingMapper;

    @Autowired
    private ApiContext apiContext;

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return ipSettingMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(IpSetting record) {
        return ipSettingMapper.insert(record);
    }

    @Override
    public int insertSelective(IpSetting record) {
        return ipSettingMapper.insertSelective(record);
    }

    @Override
    public IpSetting selectByPrimaryKey(Integer id) {
        return ipSettingMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(IpSetting record) {
        return ipSettingMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(IpSetting record) {
        return ipSettingMapper.updateByPrimaryKey(record);
    }

    @Override
    public int updateById(int id) {
        return ipSettingMapper.updateById(id);
    }

    @Override
    public PageInfo<IpSetting> selectByOther(String keyWord,int pageNum,int pageSize) {
        if (!StringUtils.isEmpty(keyWord)) {
            keyWord = "%" + keyWord + "%";
        }
        PageHelper.startPage(pageNum,pageSize);
        apiContext.setTenantId(112233L);
        List<IpSetting> ipSettings = ipSettingMapper.selectByOther(keyWord);
        PageInfo<IpSetting> pageInfo = new PageInfo<>(ipSettings);
        return pageInfo;
    }

    @Override
    public HashMap<Integer, String> selectByIPStart() {
        List<IpSetting> ipSettings = ipSettingMapper.selectByOther(null);
        if (ipSettings != null) {
            HashMap<Integer, String> resultMap = new HashMap<>();
            ipSettings.forEach(ipSetting -> {
                Integer id = ipSetting.getId();
                String startip = ipSetting.getStartip();
                String endip = ipSetting.getEndip();
                String ipName = startip + "-" + endip;
                resultMap.put(id, ipName);
            });
            return resultMap;
        } else {
            return null;
        }
    }

}

