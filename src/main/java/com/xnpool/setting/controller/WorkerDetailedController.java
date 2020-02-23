package com.xnpool.setting.controller;

import com.github.pagehelper.PageInfo;
import com.xnpool.setting.common.BaseController;
import com.xnpool.setting.domain.pojo.Worker;
import com.xnpool.setting.domain.pojo.WorkerDetailedExample;
import com.xnpool.setting.service.WorkerDetailedService;
import com.xnpool.setting.utils.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
     * @Description 矿机出入库列表
     * @Author zly
     * @Date 12:55 2020/2/23
     * @Param
     * @return
     */
    @GetMapping("/selectMoveOutList")
    public ResponseResult selectMoveOutList(String keyWord, @RequestParam(value = "pageNum", required = false, defaultValue = "1") int pageNum,
                                            @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize) {
        PageInfo<WorkerDetailedExample> workerExamplePageInfo = workerDetailedService.selectMoveOutList(keyWord, pageNum, pageSize);
        return new ResponseResult(SUCCESS, workerExamplePageInfo);
    }
}
