package com.xnpool.setting.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageInfo;
import com.xnpool.logaop.annotation.SystemLog;
import com.xnpool.logaop.util.LogType;
import com.xnpool.logaop.util.ResponseResult;
import com.xnpool.logaop.util.WriteLogUtil;
import com.xnpool.setting.common.BaseController;
import com.xnpool.setting.domain.model.FactoryHouseExample;
import com.xnpool.setting.domain.pojo.FactoryHouse;
import com.xnpool.setting.service.FactoryHouseService;
import com.xnpool.setting.service.FrameSettingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 *  厂房设置
 * </p>
 *
 * @author zly
 * @since 2020-04-22
 */
@RestController
@Slf4j
@RequestMapping("/api/v1/factoryHouse")
public class FactoryHouseController extends BaseController {
    @Autowired
    private FactoryHouseService factoryHouseService;
    @Autowired
    private FrameSettingService frameSettingService;
    @Autowired
    private WriteLogUtil writeLogUtil;

    /**
     * @return
     * @Description 添加厂房设置
     * @Author zly
     * @Date 12:59 2020/2/3
     * @Param
     */
    @SystemLog(value = "添加厂房设置", type = LogType.SYSTEM)
    @PostMapping("/addFactoryHouse")
    public ResponseResult addFactoryHouse(FactoryHouse factoryHouse) {
        factoryHouseService.addFactoryHouse(factoryHouse);
        return new ResponseResult(SUCCESS);
    }

    /**
     * @return
     * @Description 修改厂房设置
     * @Author zly
     * @Date 11:26 2020/2/4
     * @Param
     */
    @SystemLog(value = "修改厂房设置", type = LogType.SYSTEM)
    @PutMapping("/updateFactoryHouse")
    public ResponseResult updateFactoryHouse(FactoryHouse factoryHouse) {
        factoryHouseService.updateByPrimaryKeySelective(factoryHouse);
        return new ResponseResult(SUCCESS);
    }

    /**
     * @return
     * @Description 根据ID对某条数据进行软删除
     * @Author zly
     * @Date 11:45 2020/2/4
     * @Param
     */
    @SystemLog(value = "删除厂房", type = LogType.SYSTEM)
    @DeleteMapping("/deleteFactoryHouse")
    public ResponseResult deleteFactoryHouse(int id) {
        List<HashMap> hashMapList = frameSettingService.selectExistFrameByFactoryId(id);
        if (hashMapList != null && !hashMapList.isEmpty()) {
            return new ResponseResult(FAIL, "该厂房下存在矿机架,请先移除该厂房下的矿机架");
        } else {
            factoryHouseService.deleteById(id);
            return new ResponseResult(SUCCESS);

        }
    }

    /**
     * @return
     * @Description 查询厂房列表
     * @Author zly
     * @Date 14:58 2020/2/4
     * @Param
     */
    @SystemLog(value = "查询厂房列表", type = LogType.SYSTEM)
    @GetMapping("/selectFactoryHouse")
    public ResponseResult selectFactoryHouse(@RequestParam(value = "keyWord", required = false, defaultValue = "") String keyWord,
                                             @RequestParam(value = "pageNum", required = false, defaultValue = "1") int pageNum,
                                             @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
                                             HttpServletRequest request) {
        String token = writeLogUtil.getToken(request);
        Page<FactoryHouseExample> factoryHouseExamplePage = factoryHouseService.selectByOther(keyWord, pageNum, pageSize, token);
        return new ResponseResult(SUCCESS, factoryHouseExamplePage);
    }

    /**
     * @return
     * @Description 查询厂房列表
     * @Author zly
     * @Date 14:58 2020/2/4
     * @Param
     */
    @GetMapping("/selectFactoryNameList")
    public ResponseResult selectFactoryNameList(Integer mineId) {
        HashMap<Integer, String> resultMap = factoryHouseService.selectFactoryNameByMineId(mineId);
        return new ResponseResult(SUCCESS, resultMap);
    }
}
