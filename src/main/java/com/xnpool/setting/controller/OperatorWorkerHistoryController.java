package com.xnpool.setting.controller;

import com.github.pagehelper.PageInfo;
import com.xnpool.setting.common.BaseController;
import com.xnpool.setting.domain.model.OperatorWorkerHistoryExample;
import com.xnpool.setting.service.OperatorWorkerHistoryService;
import com.xnpool.setting.utils.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 矿机出库记录历史表
 * @author zly
 * @version 1.0
 * @date 2020/2/19 12:53
 */
@RestController
@Slf4j
@RequestMapping("/api/v1/OperatorWorkerHistory")
public class OperatorWorkerHistoryController extends BaseController {
    @Autowired
    private OperatorWorkerHistoryService operatorWorkerHistoryService;

    /**
     * @Description 历史出入库查询
     * @Author zly
     * @Date 13:29 2020/2/19
     * @Param
     * @return
     */
    @GetMapping("/selectWorkerHistoryList")
    public ResponseResult selectWorkerHistoryList(@RequestParam(value = "startTime", required=false)String startTime,
                                                  @RequestParam(value = "endTime", required=false)String endTime,
                                                  @RequestParam(value = "keyWord", required=false)String keyWord,
                                                  @RequestParam(value = "pageNum", required = false, defaultValue = "1") int pageNum,
                                          @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize) {
        PageInfo<OperatorWorkerHistoryExample> pageInfo = operatorWorkerHistoryService.selectWorkerHistoryList(startTime,endTime,keyWord, pageNum, pageSize);
        return new ResponseResult(SUCCESS, pageInfo);
    }
}
