package com.xnpool.setting.controller;


import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xnpool.logaop.annotation.SystemLog;
import com.xnpool.logaop.util.LogType;
import com.xnpool.logaop.util.ResponseResult;
import com.xnpool.setting.common.BaseController;
import com.xnpool.setting.domain.model.MeterReviewExample;
import com.xnpool.setting.domain.model.TypeWasteExample;
import com.xnpool.setting.domain.pojo.MeterReview;
import com.xnpool.setting.domain.pojo.MineSetting;
import com.xnpool.setting.domain.pojo.TypeWaste;
import com.xnpool.setting.service.TypeWasteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 * 矿场私有功耗表
 *
 * @author zly
 * @since 2020-05-05
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/meter/type_waste")
public class TypeWasteController extends BaseController {
    @Autowired
    private TypeWasteService typeWasteService;

    /**
     * @Description 添加
     * @Author zly
     * @Date 17:10 2020/5/5
     * @Param
     * @return
     */
    @SystemLog(value = "添加矿场型号功耗",type = LogType.SYSTEM)
    @PostMapping("/add")
    public ResponseResult addTypeWaste(TypeWaste typeWaste) {
        typeWasteService.save(typeWaste);
        return new ResponseResult(SUCCESS);
    }

    /**
     * @Description 修改
     * @Author zly
     * @Date 17:12 2020/5/5
     * @Param
     * @return
     */
    @SystemLog(value = "修改矿场型号功耗",type = LogType.SYSTEM)
    @PutMapping("/update")
    public ResponseResult updateTypeWaste(TypeWaste typeWaste) {
        typeWasteService.updateById(typeWaste);
        return new ResponseResult(SUCCESS);
    }

    /**
     * @Description 删除
     * @Author zly
     * @Date 17:17 2020/5/5
     * @Param
     * @return
     */
    @SystemLog(value = "删除矿场型号功耗",type = LogType.SYSTEM)
    @PutMapping("/delete")
    public ResponseResult deleteTypeWaste(TypeWaste typeWaste) {
        UpdateWrapper updateWrapper = new UpdateWrapper();
        updateWrapper.eq("id",typeWaste.getId());
        updateWrapper.set("is_delete",1);
        typeWasteService.update(typeWaste,updateWrapper);
        return new ResponseResult(SUCCESS);
    }

    /**
     * @Description 矿场型号功耗列表
     * @Author zly
     * @Date 18:06 2020/5/5
     * @Param
     * @return
     */
    @SystemLog(value = "矿场型号功耗列表",type = LogType.SYSTEM)
    @GetMapping("/list")
    public ResponseResult selectList(@RequestParam(value = "mineName", required = false, defaultValue = "") String mineName,
                                     @RequestParam(value = "isOpen", required = false, defaultValue = "1") String isOpen,
                                     @RequestParam(value = "brand", required = false, defaultValue = "") String brand,
                                          @RequestParam(value = "pageNum", required = false, defaultValue = "1") int pageNum,
                                          @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize) {
        Object typeWastePage = typeWasteService.selectByOther(mineName,isOpen,brand, pageNum, pageSize);
        return new ResponseResult(SUCCESS, typeWastePage);
    }
}
