package com.xnpool.setting.controller;

import com.github.pagehelper.PageInfo;
import com.xnpool.setting.common.BaseController;
import com.xnpool.setting.domain.pojo.Worker;
import com.xnpool.setting.domain.pojo.WorkerExample;
import com.xnpool.setting.domain.pojo.WorkerbrandSetting;
import com.xnpool.setting.service.WorkerService;
import com.xnpool.setting.utils.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/selectComeInWorkerList")
    public ResponseResult selectWorkerbrand(String keyWord, @RequestParam(value = "pageNum", required = false, defaultValue = "1") int pageNum,
                                            @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize) {
        PageInfo<WorkerExample> workerExamplePageInfo = workerService.selectByOther(keyWord, pageNum, pageSize);
        return new ResponseResult(SUCCESS, workerExamplePageInfo);
    }
}
