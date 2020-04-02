package com.xnpool.setting.controller;

import com.github.pagehelper.PageInfo;


import com.xnpool.logaop.annotation.SystemLog;
import com.xnpool.logaop.util.LogType;
import com.xnpool.logaop.util.ResponseResult;
import com.xnpool.setting.common.BaseController;
import com.xnpool.setting.domain.pojo.FeeSetting;
import com.xnpool.setting.service.FeeSettingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

/**
 * 费用设置
 *
 * @author zly
 * @version 1.0
 * @date 2020/2/6 16:05
 */
@RestController
@Slf4j
@RequestMapping("/api/v1/fee")
public class FeeSettingController extends BaseController {
    @Autowired
    private FeeSettingService feeSettingService;

    /**
     * @return
     * @Description 添加费用
     * @Author zly
     * @Date 16:08 2020/2/6
     * @Param
     */
    @SystemLog(value = "添加费用设置",type = LogType.SYSTEM)
    @PostMapping("/addFee")
    public ResponseResult addFee(FeeSetting feeSetting) {
        feeSettingService.insertSelective(feeSetting);
        return new ResponseResult(SUCCESS);
    }

    /**
     * @return
     * @Description 修改费用设置
     * @Author zly
     * @Date 16:13 2020/2/6
     * @Param
     */
    @SystemLog(value = "修改费用设置",type = LogType.SYSTEM)
    @PutMapping("/updateFee")
    public ResponseResult updateFee(FeeSetting feeSetting) {
        feeSettingService.updateByPrimaryKeySelective(feeSetting);
        return new ResponseResult(SUCCESS);
    }

    /**
     * @return
     * @Description 删除费用设置(软删除)
     * @Author zly
     * @Date 16:14 2020/2/6
     * @Param
     */
    @SystemLog(value = "删除费用",type = LogType.SYSTEM)
    @DeleteMapping("/deleteFee")
    public ResponseResult deleteFee(int id) {
        //这里删除之前应该查询是否有相关的地方调用了此设置
        feeSettingService.updateById(id);
        return new ResponseResult(SUCCESS);
    }

    /**
     * @return
     * @Description 查询费用列表
     * @Author zly
     * @Date 16:17 2020/2/6
     * @Param
     */
    @SystemLog(value = "查询费用列表",type = LogType.SYSTEM)
    @GetMapping("/selectFeeList")
    public ResponseResult selectFeeList(@RequestParam(value = "keyWord", required = false, defaultValue = "") String keyWord,
                                        @RequestParam(value = "pageNum", required = false, defaultValue = "1") int pageNum,
                                        @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize) {
        PageInfo<FeeSetting> feeSettingPageInfo = feeSettingService.selectByOther(keyWord, pageNum, pageSize);
        return new ResponseResult(SUCCESS, feeSettingPageInfo);
    }

    /**
     * @Description 用户网站的费用列表
     * @Author zly
     *
     * @Date 17:41 2020/3/20
     * @Param
     * @return
     */
    @GetMapping("/selectFeeMap")
    public ResponseResult selectFeeMap() {
        HashMap<Integer, String> feeMap = feeSettingService.selectFeeMap();
        return new ResponseResult(SUCCESS,feeMap);
    }
}
