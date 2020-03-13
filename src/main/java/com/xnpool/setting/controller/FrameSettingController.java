package com.xnpool.setting.controller;

import com.github.pagehelper.PageInfo;


import com.xnpool.logaop.annotation.SystemLog;
import com.xnpool.logaop.util.LogType;
import com.xnpool.setting.common.BaseController;
import com.xnpool.setting.domain.pojo.FrameSetting;
import com.xnpool.setting.domain.model.FrameSettingExample;
import com.xnpool.setting.service.FrameSettingService;
import com.xnpool.setting.utils.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

/**
 * 矿机架设置
 *
 * @author zly
 * @version 1.0
 * @date 2020/2/4 20:06
 */
@RestController
@Slf4j
@RequestMapping("/api/v1/frameSetting")
public class FrameSettingController extends BaseController {
    @Autowired
    private FrameSettingService frameSettingService;

    /**
     * @return
     * @Description 添加矿机架
     * @Author zly
     * @Date 12:59 2020/2/3
     * @Param
     */
    @SystemLog(value = "添加矿机架",type = LogType.SYSTEM)
    @PostMapping("/addFrame")
    public ResponseResult addFrame(FrameSetting frameSetting) {
        frameSettingService.insertSelective(frameSetting);
        return new ResponseResult(SUCCESS);
    }

    /**
     * @return
     * @Description 修改矿机架
     * @Author zly
     * @Date 11:26 2020/2/4
     * @Param
     */
    @SystemLog(value = "修改矿机架",type = LogType.SYSTEM)
    @PutMapping("/updateFrame")
    public ResponseResult updateFrame(FrameSetting frameSetting) {
        frameSettingService.updateByPrimaryKeySelective(frameSetting);
        return new ResponseResult(SUCCESS);
    }

    /**
     * @return
     * @Description 根据ID对某条数据进行软删除
     * @Author zly
     * @Date 11:45 2020/2/4
     * @Param
     */
    @SystemLog(value = "删除矿机架",type = LogType.SYSTEM)
    @DeleteMapping("/deleteFrameById")
    public ResponseResult deleteFrameById(int id) {
        frameSettingService.updateById(id);
        return new ResponseResult(SUCCESS);
    }

    /**
     * @return
     * @Description 查询矿机架列表
     * @Author zly
     * @Date 14:58 2020/2/4
     * @Param
     */
    @SystemLog(value = "查询矿机架列表",type = LogType.SYSTEM)
    @GetMapping("/selectFrameList")
    public ResponseResult selectFrameList(String keyWord, @RequestParam(value = "pageNum", required = false, defaultValue = "1") int pageNum,
                                          @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize) {
        PageInfo<FrameSettingExample> frameSettingExamplePageInfo = frameSettingService.selectByOther(keyWord, pageNum, pageSize);
        return new ResponseResult(SUCCESS, frameSettingExamplePageInfo);
    }

    /**
     * @return
     * @Description 根据厂房ID查询框架名称列表
     * @Author zly
     * @Date 14:25 2020/2/5
     * @Param
     */
    @GetMapping("/selectFrameNameList")
    public ResponseResult selectFrameNameList(Integer factoryId) {
        HashMap<Integer, String> resultMap = frameSettingService.selectFrameNameByFactoryId(factoryId);
        return new ResponseResult(SUCCESS, resultMap);
    }

    /**
     * @Description 添加分组处的矿机架列表
     * @Author zly
     * @Date 15:15 2020/2/10
     * @Param
     * @return
     */
    @GetMapping("/selectFrameNameToGroup")
    public ResponseResult selectFrameNameToGroup(Integer factoryId) {
        HashMap<Integer, String> resultMap = frameSettingService.selectFrameNameToGruop(factoryId);
        return new ResponseResult(SUCCESS, resultMap);
    }


}
