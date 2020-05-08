package com.xnpool.setting.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xnpool.logaop.service.exception.CheckException;
import com.xnpool.logaop.service.exception.DataExistException;
import com.xnpool.logaop.service.exception.DataNotFoundException;
import com.xnpool.logaop.service.exception.IOException;
import com.xnpool.setting.common.BaseController;
import com.xnpool.setting.domain.model.ElectricityMeterSettingExample;
import com.xnpool.setting.domain.pojo.ElectricityMeterSetting;
import com.xnpool.setting.domain.mapper.ElectricityMeterSettingMapper;
import com.xnpool.setting.service.ElectricityMeterSettingService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xnpool.setting.utils.QRCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zly
 * @since 2020-05-04
 */
@Service
public class ElectricityMeterSettingServiceImpl extends ServiceImpl<ElectricityMeterSettingMapper, ElectricityMeterSetting> implements ElectricityMeterSettingService {
    @Resource
    private ElectricityMeterSettingMapper electricityMeterSettingMapper;

    //文件存储路径
    @Value("${config.filePath}")
    private String filePath;

    @Value("${config.prifix_2}")
    private String prifix_2;

    @Autowired
    private BaseController baseController;
    @Override
    public int insertSelective(ElectricityMeterSetting record) {
        List<String> list = electricityMeterSettingMapper.selectNameList(record.getId());
        if (list.contains(record.getElectricityMeterName())) {
            throw new DataExistException("数据已存在,请勿重复添加!");
        }
       return electricityMeterSettingMapper.insert(record);
    }

    @Override
    public int updateByPrimaryKeySelective(ElectricityMeterSetting record) {
        List<String> list = electricityMeterSettingMapper.selectNameList(record.getId());
        if (list.contains(record.getElectricityMeterName())) {
            throw new DataExistException("数据已存在,请勿重复添加!");
        }
        return electricityMeterSettingMapper.updateById(record);
    }

    @Override
    public int deleteById(int id) {
        return electricityMeterSettingMapper.deleteByKeyId(id);
    }

    @Override
    public Page<ElectricityMeterSettingExample> selectByOther(String keyWord, int pageNum, int pageSize) {
        if (!StringUtils.isEmpty(keyWord)) {
            keyWord = "%" + keyWord + "%";
        }
        Page<ElectricityMeterSettingExample> page = new Page<>(pageNum, pageSize);
        List<ElectricityMeterSettingExample> electricityList = electricityMeterSettingMapper.selectByOther(keyWord,page);
        electricityList.forEach(electricityMeterSettingExample -> {
            electricityMeterSettingExample.setLineLoss(electricityMeterSettingExample.getLineLoss()+"%");
        });
        page.setRecords(electricityList);
        return page;
    }

    @Override
    public String generateQRCodeImage(Integer id) {
        HashMap hashMap = electricityMeterSettingMapper.selectOtherById(id);
        if (!hashMap.isEmpty()){
            String id1 = String.valueOf(hashMap.get("id"));
            String electricityName = String.valueOf(hashMap.get("electricity_meter_Name"));
            String mineName = String.valueOf(hashMap.get("mine_name"));
            String factoryName = String.valueOf(hashMap.get("factory_name"));
            String tenantName = String.valueOf(hashMap.get("tenant_name"));
            StringBuffer stringBuffer = new StringBuffer("所属编号:").append(id1).append("\n")
                    .append("所属矿场:").append(mineName).append("\n")
                    .append("所属厂房:").append(factoryName).append("\n")
                    .append("所属企业:").append(tenantName);
            try {
                String path = System.getProperty("user.dir") + filePath;
                path=path+id1+"-MyQRCode.png";
                QRCodeUtils.generateQRCodeImage(stringBuffer.toString(),300,300,path);
                return prifix_2+id1+"-MyQRCode.png";
            } catch (Exception e) {
                throw new IOException("生成二维码失败,请重试");
            }
        }else {
            throw new DataNotFoundException("未读取到有效的数据,请重试");
        }
    }
}
