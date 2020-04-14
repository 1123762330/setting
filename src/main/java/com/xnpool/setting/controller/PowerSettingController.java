package com.xnpool.setting.controller;

import com.github.pagehelper.PageInfo;


import com.xnpool.logaop.annotation.SystemLog;
import com.xnpool.logaop.util.LogType;
import com.xnpool.logaop.util.ResponseResult;
import com.xnpool.logaop.util.WriteLogUtil;
import com.xnpool.setting.common.BaseController;
import com.xnpool.setting.domain.model.PowerSettingExample;
import com.xnpool.setting.domain.pojo.PowerSetting;
import com.xnpool.setting.service.PowerSettingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


/**
 * 电费设置
 *
 * @author zly
 * @version 1.0
 * @date 2020/2/6 14:15
 */
@RestController
@Slf4j
@RequestMapping("/api/v1/powerSetting")
public class PowerSettingController extends BaseController {
    @Autowired
    private PowerSettingService powerSettingService;

    @Autowired
    private WriteLogUtil writeLogUtil;

    /**
     * @return
     * @Description 添加电费
     * @Author zly
     * @Date 15:28 2020/2/6
     * @Param
     */
    @SystemLog(value = "添加电费设置",type = LogType.SYSTEM)
    @PostMapping("/addPowerRate")
    public ResponseResult addPowerRate(PowerSetting powerSetting) {
        powerSettingService.insertSelective(powerSetting);
        return new ResponseResult(SUCCESS);
    }

    /**
     * @return
     * @Description 修改电费
     * @Author zly
     * @Date 15:33 2020/2/6
     * @Param
     */
    @SystemLog(value = "修改电费设置",type = LogType.SYSTEM)
    @PutMapping("/updatePowerRate")
    public ResponseResult updatePowerRate(PowerSetting powerSetting) {
        powerSettingService.updateByPrimaryKeySelective(powerSetting);
        return new ResponseResult(SUCCESS);
    }

    /**
     * @return
     * @Description 删除电费
     * @Author zly
     * @Date 15:34 2020/2/6
     * @Param
     */
    //@SystemLog(value = "删除电费设置",type = LogType.SYSTEM)
    @DeleteMapping("/deletePowerRateById")
    public ResponseResult deletePowerRateById(int id) {
        //删除电费之前应该先查查有没有应用到这个电费的地方
        powerSettingService.updateById(id);
        return new ResponseResult(SUCCESS);
    }

    /**
     * @return
     * @Description 查询电费列表
     * @Author zly
     * @Date 15:52 2020/2/6
     * @Param
     */
    @SystemLog(value = "查询电费设置列表",type = LogType.SYSTEM)
    @GetMapping("/selectPowerRateList")
    public ResponseResult selectFrameList(@RequestParam(value = "keyWord", required=false,defaultValue = "")String keyWord,
                                          @RequestParam(value = "pageNum", required = false, defaultValue = "1") int pageNum,
                                          @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
                                          HttpServletRequest request) {
        String token = writeLogUtil.getToken(request);
        PageInfo<PowerSettingExample> pageInfo = powerSettingService.selectByOther(keyWord, pageNum, pageSize,token);
        return new ResponseResult(SUCCESS, pageInfo);
    }
}
