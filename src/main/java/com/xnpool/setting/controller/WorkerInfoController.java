package com.xnpool.setting.controller;



import com.xnpool.logaop.annotation.SystemLog;
import com.xnpool.logaop.util.LogType;
import com.xnpool.logaop.util.ResponseResult;
import com.xnpool.setting.common.BaseController;
import com.xnpool.setting.service.WorkerInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

/**
 * 矿机出入库操作
 * @author zly
 * @version 1.0
 * @date 2020/2/18 12:03
 */
@RestController
@Slf4j
@RequestMapping("/api/v1/Worker")
public class WorkerInfoController extends BaseController {
    @Autowired
    private WorkerInfoService workerInfoService;

    @SystemLog(value = "查询矿机列表",type = LogType.MINE)
    @GetMapping("/selectWorkerList")
    public ResponseResult selectWorkerList(String keyWord, @RequestParam(value = "pageNum", required = false, defaultValue = "1") int pageNum,
                                           @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize) {
        HashMap<Integer, String> workerList = workerInfoService.selectWorkerList(keyWord);
        return new ResponseResult(SUCCESS,workerList);
    }
}
