package com.xnpool.setting.controller;

import com.github.pagehelper.PageInfo;
import com.xnpool.setting.common.BaseController;
import com.xnpool.setting.domain.pojo.AgreementSetting;
import com.xnpool.setting.domain.pojo.ElectricityMeterSetting;
import com.xnpool.setting.domain.pojo.FactoryHouse;
import com.xnpool.setting.service.ElectricityMeterSettingService;
import com.xnpool.setting.utils.ResponseResult;
import com.xnpool.setting.utils.UploadUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 电表设置
 * @author zly
 * @version 1.0
 * @date 2020/2/7 15:39
 */
@RestController
@Slf4j
@RequestMapping("/api/v1/electricityMeter")
public class ElectricityMeterController extends BaseController {
    @Autowired
    private ElectricityMeterSettingService electricityMeterSettingService;

    /**
     * @Description 添加电表设置
     * @Author zly
     * @Date 15:46 2020/2/7
     * @Param
     * @return
     */
    @PostMapping("/addElectricityMeter")
    public ResponseResult addElectricityMeter(ElectricityMeterSetting electricityMeterSetting) {
        electricityMeterSettingService.insertSelective(electricityMeterSetting);
        return new ResponseResult(SUCCESS);
    }

    /**
     * @Description 修改电表设置
     * @Author zly
     * @Date 15:47 2020/2/7
     * @Param
     * @return
     */
    @PutMapping("/updateElectricityMeter")
    public ResponseResult updateElectricityMeter(ElectricityMeterSetting electricityMeterSetting) {
        electricityMeterSettingService.updateByPrimaryKeySelective(electricityMeterSetting);
        return new ResponseResult(SUCCESS);
    }

    /**
     * @Description 删除电表
     * @Author zly
     * @Date 10:46 2020/2/10
     * @Param
     * @return
     */
    @DeleteMapping("/deleteElectricityMeter")
    public ResponseResult deleteElectricityMeter(int id) {
        electricityMeterSettingService.updateById(id);
        return new ResponseResult(SUCCESS);
    }

    /**
     * @Description 电表设置列表
     * @Author zly
     * @Date 16:25 2020/2/10
     * @Param
     * @return
     */
    @GetMapping("/selectElectricityMeter")
    public ResponseResult selectElectricityMeter(String keyWord, @RequestParam(value = "pageNum", required = false, defaultValue = "1") int pageNum,
                                          @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize) {
        PageInfo<ElectricityMeterSetting> pageInfo = electricityMeterSettingService.selectByOther(keyWord, pageNum, pageSize);
        return new ResponseResult(SUCCESS,pageInfo);
    }
}
