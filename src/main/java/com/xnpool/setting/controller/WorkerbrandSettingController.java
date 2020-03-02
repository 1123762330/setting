package com.xnpool.setting.controller;

import com.github.pagehelper.PageInfo;
import com.xnpool.setting.common.BaseController;
import com.xnpool.setting.domain.pojo.WorkerbrandSetting;
import com.xnpool.setting.service.WorkerbrandSettingService;
import com.xnpool.setting.utils.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

/**
 * 矿机品牌设置
 *
 * @author zly
 * @version 1.0
 * @date 2020/2/6 13:24
 */
@RestController
@Slf4j
@RequestMapping("/api/v1/workerbrand")
public class WorkerbrandSettingController extends BaseController {
    @Autowired
    private WorkerbrandSettingService workerbrandSettingService;

    /**
     * @return
     * @Description 添加矿机品牌
     * @Author zly
     * @Date 13:28 2020/2/6
     * @Param
     */
    @PostMapping("/addWorkerbrand")
    public ResponseResult addWorkerbrand(WorkerbrandSetting workerbrandSetting) {
        workerbrandSettingService.insertSelective(workerbrandSetting);
        return new ResponseResult(SUCCESS);
    }

    /**
     * @return
     * @Description 修改矿机品牌
     * @Author zly
     * @Date 13:33 2020/2/6
     * @Param
     */
    @PutMapping("/updateWorkerbrand")
    public ResponseResult updateWorkerbrand(WorkerbrandSetting workerbrandSetting) {
        workerbrandSettingService.updateByPrimaryKeySelective(workerbrandSetting);
        return new ResponseResult(SUCCESS);
    }

    /**
     * @return
     * @Description 软删除矿机品牌
     * @Author zly
     * @Date 13:50 2020/2/6
     * @Param
     */
    @DeleteMapping("/deleteWorkerbrand")
    public ResponseResult deleteWorkerbrand(int id) {
        workerbrandSettingService.updateById(id);
        return new ResponseResult(SUCCESS);
    }

    /**
     * @return
     * @Description 查询矿机品牌列表
     * @Author zly
     * @Date 13:49 2020/2/6
     * @Param
     */
    @GetMapping("/selectWorkerbrand")
    public ResponseResult selectWorkerbrand(String keyWord, @RequestParam(value = "pageNum", required = false, defaultValue = "1") int pageNum,
                                            @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize) {
        PageInfo<WorkerbrandSetting> pageInfo = workerbrandSettingService.selectByOther(keyWord, pageNum, pageSize);
        return new ResponseResult(SUCCESS, pageInfo);
    }

    /**
     * @Description
     * @Author zly
     * @Date 16:34 2020/3/2
     * @Param
     * @return
     */
    @GetMapping("/selectWorkerbrandMap")
    public ResponseResult selectWorkerbrand(){
        HashMap<Integer, String> workerbrandMap = workerbrandSettingService.selectWorkerbrandMap();
        return new ResponseResult(SUCCESS, workerbrandMap);
    }
}
