package com.xnpool.setting.controller;

import com.github.pagehelper.PageInfo;


import com.xnpool.logaop.annotation.SystemLog;
import com.xnpool.logaop.util.LogType;
import com.xnpool.logaop.util.ResponseResult;
import com.xnpool.logaop.util.WriteLogUtil;
import com.xnpool.setting.common.BaseController;
import com.xnpool.setting.domain.model.WorkerDetailedExample;
import com.xnpool.setting.domain.pojo.MoveOutParam;
import com.xnpool.setting.domain.pojo.WorkerDetailedParam;
import com.xnpool.setting.domain.model.WorkerExample;
import com.xnpool.setting.service.WorkerDetailedService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.HashSet;

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

    @Autowired
    private WriteLogUtil writeLogUtil;

    /**
     * @Description 矿机出库列表
     * @Author zly
     * @Date 12:55 2020/2/23
     * @Param
     * @return
     */
    @SystemLog(value = "查询矿机出库列表",type = LogType.MINE)
    @GetMapping("/selectMoveOutList")
    public ResponseResult selectMoveOutList(MoveOutParam moveOutParam,
                                            @RequestParam(value = "pageNum", required = false, defaultValue = "1") int pageNum,
                                            @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
                                            HttpServletRequest request) {
        String token = writeLogUtil.getToken(request);
        PageInfo<WorkerDetailedExample> moveOutList = workerDetailedService.selectMoveOutList(moveOutParam, pageNum, pageSize, token);
        return new ResponseResult(SUCCESS, moveOutList);
    }

    /**
     * @Description 过滤下拉列表
     * @Author zly
     * @Date 17:06 2020/4/14
     * @Param
     * @return
     */
    @SystemLog(value = "查询下拉列表",type = LogType.MINE)
    @GetMapping("/selectDropownList")
    public ResponseResult selectDropownList(HttpServletRequest request) {
        String token = writeLogUtil.getToken(request);
        HashMap<String, HashSet> resultMap = workerDetailedService.selectDropownList(token);
        return new ResponseResult(SUCCESS, resultMap);
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
    public ResponseResult selectComeInWorkerList(@RequestParam(value = "workerType", required = false,defaultValue = "")String workerType,
                                                 @RequestParam(value = "state", required = false,defaultValue = "")Integer state,
                                                 @RequestParam(value = "ip", required = false,defaultValue = "")String ip,
                                                 @RequestParam(value = "pageNum", required = false, defaultValue = "1") int pageNum,
                                                 @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
                                                 HttpServletRequest request) {
        String token = writeLogUtil.getToken(request);
        HashMap<String, Object>  workerPageInfo = workerDetailedService.selectComeInWorkerList(workerType,state,ip, pageNum, pageSize,token);
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
    @PutMapping("/addWorkerToLibrary")
    public ResponseResult addWorkerToLibrary(WorkerDetailedParam workerDetailedParam,HttpServletRequest request) {
        String token = request.getHeader("token");
        workerDetailedService.addWorkerToLibrary(workerDetailedParam,token);
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
        String token = writeLogUtil.getToken(request);
        workerDetailedService.updateMoveOutByid(ids,reason,token);
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

    /**
     * @Description 查询空闲矿机架
     * @Author zly
     * @Date 14:38 2020/3/27
     * @Param
     * @return
     */
    @GetMapping("/selectNullFrame")
    public ResponseResult selectNullFrame(Integer frameId) {
        HashMap<Integer,String> resultMap = workerDetailedService.selectNullFrame(frameId);
        return new ResponseResult(SUCCESS, resultMap);
    }

    /**
     * @Description 全部上架
     * @Author zly
     * @Date 17:05 2020/4/7
     * @Param
     * @return
     */
    @SystemLog(value = "矿机全部上架",type = LogType.MINE)
    @PostMapping("/batchIntoFrame")
    public ResponseResult batchIntoFrame(HttpServletRequest request) {
        String token = writeLogUtil.getToken(request);
        workerDetailedService.batchIntoFrame(token);
        return new ResponseResult(SUCCESS);
    }

    /**
     * @Description 批量分配用户
     * @Author zly
     * @Date 9:38 2020/4/4
     * @Param
     * @return
     */
    @SystemLog(value = "矿机分配所属用户",type = LogType.MINE)
    @PutMapping("/batchUpdateToUser")
    public ResponseResult batchUpdateToUser(String ids,Integer userId,Integer groupId,HttpServletRequest request) {
        //String token = writeLogUtil.getToken(request);
        workerDetailedService.batchUpdateToUser(ids,userId,groupId);
        return new ResponseResult(SUCCESS);
    }



}
