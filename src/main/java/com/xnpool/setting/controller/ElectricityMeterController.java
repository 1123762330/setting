package com.xnpool.setting.controller;

import com.github.pagehelper.PageInfo;


import com.xnpool.logaop.annotation.SystemLog;
import com.xnpool.logaop.util.LogType;
import com.xnpool.setting.common.BaseController;
import com.xnpool.setting.domain.pojo.ElectricityMeterSetting;
import com.xnpool.setting.domain.model.ElectricityMeterSettingExample;
import com.xnpool.setting.service.ElectricityMeterSettingService;
import com.xnpool.setting.utils.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    @SystemLog(value = "添加电表设置",type = LogType.SYSTEM)
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
    @SystemLog(value = "修改电表设置",type = LogType.SYSTEM)
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
    @SystemLog(value = "删除电表设置",type = LogType.SYSTEM)
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
    @SystemLog(value = "查询电表设置列表",type = LogType.SYSTEM)
    @GetMapping("/selectElectricityMeter")
    public ResponseResult selectElectricityMeter(String keyWord, @RequestParam(value = "pageNum", required = false, defaultValue = "1") int pageNum,
                                          @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize) {
        PageInfo<ElectricityMeterSettingExample> pageInfo = electricityMeterSettingService.selectByOther(keyWord, pageNum, pageSize);
        return new ResponseResult(SUCCESS,pageInfo);
    }
}
