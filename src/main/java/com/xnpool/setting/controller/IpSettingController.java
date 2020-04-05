package com.xnpool.setting.controller;

import com.github.pagehelper.PageInfo;


import com.xnpool.logaop.annotation.SystemLog;
import com.xnpool.logaop.util.LogType;
import com.xnpool.logaop.util.ResponseResult;
import com.xnpool.setting.common.BaseController;
import com.xnpool.setting.domain.model.IpSettingExample;
import com.xnpool.setting.domain.pojo.IpSetting;
import com.xnpool.setting.service.IpSettingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
/**
 * ip字段设置
 *
 * @author zly
 * @version 1.0
 * @date 2020/2/5 12:45
 */
@RestController
@Slf4j
@RequestMapping("/api/v1/ipSetting")
public class IpSettingController extends BaseController {
    @Autowired
    private IpSettingService ipSettingService;

    /**
     * @return
     * @Description 添加IP字段设置
     * @Author zly
     * @Date 12:59 2020/2/3
     * @Param
     */
    @SystemLog(value = "添加ip区间",type = LogType.SYSTEM)
    @PostMapping("/addIp")
    public ResponseResult addIP(IpSetting ipSetting) {
        ipSettingService.insertSelective(ipSetting);
        return new ResponseResult(SUCCESS);
    }

    /**
     * @return
     * @Description 修改IP字段
     * @Author zly
     * @Date 11:26 2020/2/4
     * @Param
     */
    @SystemLog(value = "修改IP区间",type = LogType.SYSTEM)
    @PutMapping("/updateIp")
    public ResponseResult updateIP(IpSetting ipSetting) {
        ipSettingService.updateByPrimaryKeySelective(ipSetting);
        return new ResponseResult(SUCCESS);
    }

    /**
     * @return
     * @Description 根据ID对某条数据进行软删除
     * @Author zly
     * @Date 11:45 2020/2/4
     * @Param
     */
    @SystemLog(value = "删除ip区间段",type = LogType.SYSTEM)
    @DeleteMapping("/deleteIpById")
    public ResponseResult deleteIPById(int id) {
        ipSettingService.updateById(id);
        return new ResponseResult(SUCCESS);
    }

    /**
     * @return
     * @Description 查询IP列表
     * @Author zly
     * @Date 14:58 2020/2/4
     * @Param
     */
    @SystemLog(value = "查询IP区间列表",type = LogType.SYSTEM)
    @GetMapping("/selectIpList")
    public ResponseResult selectIPList(@RequestParam(value = "keyWord", required = false, defaultValue = "") String keyWord,
                                       @RequestParam(value = "pageNum", required = false, defaultValue = "1") int pageNum,
                                       @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize) {
        PageInfo<IpSettingExample> ipSettingPageInfo = ipSettingService.selectByOther(keyWord, pageNum, pageSize);
        return new ResponseResult(SUCCESS, ipSettingPageInfo);
    }

    /**
     * @return
     * @Description 查询IP字段列表
     * @Author zly
     * @Date 14:58 2020/2/4
     * @Param
     */
    @GetMapping("/selectByIpStart")
    public ResponseResult selectByIPStart() {
        HashMap<Integer, String> resultMap = ipSettingService.selectByIPStart();
        return new ResponseResult(SUCCESS, resultMap);
    }

    /**
     * @Description 运维权限矿机Ip区间分配
     * @Author zly
     * @Date 13:39 2020/4/5
     * @Param
     * @return
     */
    @GetMapping("/selectByIpStartByMineId")
    public ResponseResult selectByIpStartByMineId(String mineName,Integer mineId) {
        HashMap<Integer, String> resultMap = ipSettingService.selectByIpStartByMineId(mineName,mineId);
        return new ResponseResult(SUCCESS, resultMap);
    }
}
