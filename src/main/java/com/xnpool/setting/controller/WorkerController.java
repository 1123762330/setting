package com.xnpool.setting.controller;

import com.github.pagehelper.PageInfo;
import com.xnpool.setting.common.BaseController;
import com.xnpool.setting.domain.pojo.Worker;
import com.xnpool.setting.domain.pojo.WorkerExample;
import com.xnpool.setting.service.WorkerService;
import com.xnpool.setting.utils.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 矿机出入库操作
 * @author zly
 * @version 1.0
 * @date 2020/2/18 12:03
 */
@RestController
@Slf4j
@RequestMapping("/api/v1/Worker")
public class WorkerController extends BaseController {
    @Autowired
    private WorkerService workerService;

    /**
     * @Description 矿机入库列表
     * @Author zly
     * @Date 15:42 2020/2/18
     * @Param
     * @return
     */
    @GetMapping("/selectComeInWorkerList")
    public ResponseResult selectComeInWorkerList(String keyWord, @RequestParam(value = "pageNum", required = false, defaultValue = "1") int pageNum,
                                            @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize) {
        PageInfo<WorkerExample> workerExamplePageInfo = workerService.selectComeInWorkerList(keyWord, pageNum, pageSize);
        return new ResponseResult(SUCCESS, workerExamplePageInfo);
    }

    /**
     * @Description 矿机出库列表
     * @Author zly
     * @Date 16:09 2020/2/18
     * @Param
     * @return
     */
    @GetMapping("/selectMoveOutList")
    public ResponseResult selectMoveOutList(String keyWord, @RequestParam(value = "pageNum", required = false, defaultValue = "1") int pageNum,
                                            @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize) {
        PageInfo<Worker> workerExamplePageInfo = workerService.selectMoveOutList(keyWord, pageNum, pageSize);
        return new ResponseResult(SUCCESS, workerExamplePageInfo);
    }

    /**
     * @Description 出库操作
     * @Author zly
     * @Date 15:43 2020/2/18
     * @Param
     * @return
     */
    @PutMapping("/moveOut")
    public ResponseResult moveOut(String ids) {
        workerService.updateMoveOutByid(ids);
        return new ResponseResult(SUCCESS);
    }

    /**
     * @Description 入库操作
     * @Author zly
     * @Date 16:48 2020/2/18
     * @Param
     * @return
     */
    @PutMapping("/comeIn")
    public ResponseResult comeIn(String ids) {
        workerService.updateComeInByid(ids);
        return new ResponseResult(SUCCESS);
    }

    /**
     * @Description 删除矿机
     * @Author zly
     * @Date 16:36 2020/2/18
     * @Param
     * @return
     */
    @DeleteMapping("/deleteWorker")
    public ResponseResult deleteWorker(String ids) {
        workerService.updateById(ids);
        return new ResponseResult(SUCCESS);
    }
}
