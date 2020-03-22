package com.xnpool.setting.controller;

import com.github.pagehelper.PageInfo;


import com.xnpool.logaop.annotation.SystemLog;
import com.xnpool.logaop.util.LogType;
import com.xnpool.logaop.util.ResponseResult;
import com.xnpool.setting.common.BaseController;
import com.xnpool.setting.domain.model.WorkerDetailedExample;
import com.xnpool.setting.domain.pojo.WorkerDetailedParam;
import com.xnpool.setting.domain.model.WorkerExample;
import com.xnpool.setting.service.WorkerDetailedService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 矿机出入库管理
 * @author zly
 * @version 1.0
 * @date 2020/2/23 12:53
 */
@RestController
@Slf4j
@RequestMapping("/api/v1/WorkerDetailed")
public class WorkerDetailedController extends BaseController {
    @Autowired
    private WorkerDetailedService workerDetailedService;

    /**
     * @Description 矿机出库列表
     * @Author zly
     * @Date 12:55 2020/2/23
     * @Param
     * @return
     */
    @SystemLog(value = "查询矿机出库列表",type = LogType.MINE)
    @GetMapping("/selectMoveOutList")
    public ResponseResult selectMoveOutList(String keyWord, @RequestParam(value = "pageNum", required = false, defaultValue = "1") int pageNum,
                                            @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize) {
        PageInfo<WorkerDetailedExample> workerExamplePageInfo = workerDetailedService.selectMoveOutList(keyWord, pageNum, pageSize);
        return new ResponseResult(SUCCESS, workerExamplePageInfo);
    }

    /**
     * @Description 矿机入库列表
     * @Author zly
     * @Date 15:42 2020/2/18
     * @Param
     * @return
     */
    @SystemLog(value = "查询矿机入库列表",type = LogType.MINE)
    @GetMapping("/selectComeInWorkerList")
    public ResponseResult selectComeInWorkerList(String keyWord, @RequestParam(value = "pageNum", required = false, defaultValue = "1") int pageNum,
                                                 @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize) {
        PageInfo<WorkerExample> workerPageInfo = workerDetailedService.selectComeInWorkerList(keyWord, pageNum, pageSize);
        return new ResponseResult(SUCCESS, workerPageInfo);
    }

    /**
     * @Description 添加矿机到出入库管理表里
     * @Author zly
     * @Date 13:36 2020/2/26
     * @Param
     * @return
     */
    @SystemLog(value = "矿机上架",type = LogType.MINE)
    @PostMapping("/addWorkerToLibrary")
    public ResponseResult addWorkerToLibrary(WorkerDetailedParam workerDetailedParam) {
        workerDetailedService.addWorkerToLibrary(workerDetailedParam);
        return new ResponseResult(SUCCESS);
    }

    /**
     * @Description 出库操作
     * @Author zly
     * @Date 15:43 2020/2/18
     * @Param
     * @return
     */
    @SystemLog(value = "矿机下架",type = LogType.MINE)
    @PutMapping("/moveOut")
    public ResponseResult moveOut(String ids, @RequestParam(value = "reason",required = false) String reason, HttpServletRequest request) {
        String token = request.getHeader("token");
        workerDetailedService.updateMoveOutByid(ids,reason,token);
        return new ResponseResult(SUCCESS);
    }

    /**
     * @Description 入库操作
     * @Author zly
     * @Date 16:48 2020/2/18
     * @Param
     * @return
     */
    @SystemLog(value = "矿机入库",type = LogType.MINE)
    @PutMapping("/comeIn")
    public ResponseResult comeIn(String ids) {
        workerDetailedService.updateComeInByid(ids);
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
        workerDetailedService.updateById(ids);
        return new ResponseResult(SUCCESS);
    }

}
