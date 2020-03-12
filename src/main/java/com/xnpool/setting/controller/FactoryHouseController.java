package com.xnpool.setting.controller;

import com.github.pagehelper.PageInfo;
import com.xnpool.setting.common.BaseController;
import com.xnpool.setting.domain.pojo.FactoryHouse;
import com.xnpool.setting.domain.model.FactoryHouseExample;
import com.xnpool.setting.service.FactoryHouseService;
import com.xnpool.setting.service.FrameSettingService;
import com.xnpool.setting.utils.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

/**
 * 厂房设置
 *
 * @author zly
 * @version 1.0
 * @date 2020/2/4 15:30
 */
@RestController
@Slf4j
@RequestMapping("/api/v1/factoryHouse")
public class FactoryHouseController extends BaseController {
    @Autowired
    private FactoryHouseService factoryHouseService;
    @Autowired
    private FrameSettingService frameSettingService;

    /**
     * @return
     * @Description 添加矿场设置
     * @Author zly
     * @Date 12:59 2020/2/3
     * @Param
     */
    @PostMapping("/addFactoryHouse")
    public ResponseResult addFactoryHouse(FactoryHouse factoryHouse) {
        factoryHouseService.insertSelective(factoryHouse);
        return new ResponseResult(SUCCESS);
    }

    /**
     * @return
     * @Description 修改厂房设置
     * @Author zly
     * @Date 11:26 2020/2/4
     * @Param
     */
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
    @DeleteMapping("/deleteFactoryHouse")
    public ResponseResult deleteFactoryHouse(int id) {

        HashMap<Integer, String> frameNameMap = frameSettingService.selectFrameNameByFactoryId(id);
        if (frameNameMap != null && !frameNameMap.isEmpty()) {
            return new ResponseResult(FAIL, "该厂房下存在矿机架,请先移除该厂房下的矿机架");
        } else {
            factoryHouseService.updateById(id);
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
        @GetMapping("/selectFactoryHouse")
        public ResponseResult selectFactoryHouse (String keyWord,
        @RequestParam(value = "pageNum", required = false, defaultValue = "1") int pageNum,
        @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize){
            PageInfo<FactoryHouseExample> pageInfo = factoryHouseService.selectByOther(keyWord, pageNum, pageSize);
            return new ResponseResult(SUCCESS,pageInfo);
        }

        /**
         * @return
         * @Description 查询厂房列表
         * @Author zly
         * @Date 14:58 2020/2/4
         * @Param
         */
        @GetMapping("/selectFactoryNameList")
        public ResponseResult selectFactoryNameList (Integer mineId){
            HashMap<Integer, String> resultMap = factoryHouseService.selectFactoryNameByMineId(mineId);
            return new ResponseResult(SUCCESS,resultMap);
        }
    }
