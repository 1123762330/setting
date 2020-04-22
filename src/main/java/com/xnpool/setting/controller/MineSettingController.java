package com.xnpool.setting.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xnpool.logaop.annotation.SystemLog;
import com.xnpool.logaop.util.LogType;
import com.xnpool.logaop.util.ResponseResult;
import com.xnpool.logaop.util.WriteLogUtil;
import com.xnpool.setting.common.BaseController;
import com.xnpool.setting.domain.pojo.FactoryHouse;
import com.xnpool.setting.domain.pojo.MineSetting;
import com.xnpool.setting.service.FactoryHouseService;
import com.xnpool.setting.service.MineSettingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 *  矿场设置
 * </p>
 *
 * @author zly
 * @since 2020-04-22
 */
@RestController
@Slf4j
@RequestMapping("/api/v1/mineSetting")
public class MineSettingController extends BaseController {
    @Autowired
    private MineSettingService mineSettingService;

    @Autowired
    private FactoryHouseService factoryHouseService;

    @Autowired
    private WriteLogUtil writeLogUtil;

    /**
     * @return
     * @Description 添加矿场设置
     * @Author zly
     * @Date 12:59 2020/2/3
     * @Param
     */
    @SystemLog(value = "添加矿场",type = LogType.SYSTEM)
    @PostMapping("/addMineSetting")
    public ResponseResult addMineSetting(MineSetting mineSetting, HttpServletRequest request) {
        String token = writeLogUtil.getToken(request);
        mineSettingService.save(mineSetting,token);
        return new ResponseResult(SUCCESS);
    }

    /**
     * @return
     * @Description 修改矿场设置
     * @Author zly
     * @Date 11:26 2020/2/4
     * @Param
     */
    @SystemLog(value = "修改矿场",type = LogType.SYSTEM)
    @PutMapping("/updateMineSetting")
    public ResponseResult updateMineSetting(MineSetting mineSetting) {
        mineSettingService.updateByKeyId(mineSetting);
        return new ResponseResult(SUCCESS);
    }

    /**
     * @return
     * @Description 根据ID对某条数据进行软删除
     * @Author zly
     * @Date 11:45 2020/2/4
     * @Param
     */
    @SystemLog(value = "删除矿场",type = LogType.SYSTEM)
    @DeleteMapping("/deleteMineSetting")
    public ResponseResult deleteMineSetting(int id) {
        List<FactoryHouse> factoryHouse2s = factoryHouseService.selectByMineId(id);
        if (factoryHouse2s.isEmpty()) {
            mineSettingService.deleteById(id);
            return new ResponseResult(SUCCESS);
        } else {
            return new ResponseResult(FAIL, "该矿场下面有厂房,请先删除该矿场下的所有厂房");
        }
    }

    /**
     * @return
     * @Description 查询矿场设置列表
     * @Author zly
     * @Date 14:58 2020/2/4
     * @Param
     */
    @SystemLog(value = "查询矿场列表",type = LogType.SYSTEM)
    @GetMapping("/selectMineSetting")
    public ResponseResult selectMineSetting(@RequestParam(value = "keyWord", required = false, defaultValue = "") String keyWord,
                                            @RequestParam(value = "pageNum", required = false, defaultValue = "1") int pageNum,
                                            @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
                                            HttpServletRequest request) {
        String token = writeLogUtil.getToken(request);
        Page<MineSetting> mineSettingPage = mineSettingService.selectByOther(keyWord, pageNum, pageSize, token);
        return new ResponseResult(SUCCESS, mineSettingPage);
    }

    /**
     * @return
     * @Description 查询矿场名称列表
     * @Author zly
     * @Date 13:13 2020/2/5
     * @Param
     */
    @GetMapping("/selectMineName")
    public ResponseResult selectMineSetting(HttpServletRequest request) {
        String token = writeLogUtil.getToken(request);
        HashMap<Integer, String> resultMap = mineSettingService.selectMineNameByOther(token);
        return new ResponseResult(SUCCESS, resultMap);
    }
}
