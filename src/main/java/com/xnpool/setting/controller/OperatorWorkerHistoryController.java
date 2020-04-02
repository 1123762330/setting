package com.xnpool.setting.controller;

import com.github.pagehelper.PageInfo;


import com.xnpool.logaop.annotation.SystemLog;
import com.xnpool.logaop.util.LogType;
import com.xnpool.logaop.util.ResponseResult;
import com.xnpool.logaop.util.WriteLogUtil;
import com.xnpool.setting.common.BaseController;
import com.xnpool.setting.domain.model.OperatorWorkerHistoryExample;
import com.xnpool.setting.service.OperatorWorkerHistoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

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

    @Autowired
    private WriteLogUtil writeLogUtil;
    /**
     * @Description 历史出入库查询
     * @Author zly
     * @Date 13:29 2020/2/19
     * @Param
     * @return
     */
    @SystemLog(value = "查询矿机出库历史记录",type = LogType.SYSTEM)
    @GetMapping("/selectWorkerHistoryList")
    public ResponseResult selectWorkerHistoryList(@RequestParam(value = "startTime", required=false,defaultValue = "")String startTime,
                                                  @RequestParam(value = "endTime", required=false,defaultValue = "")String endTime,
                                                  @RequestParam(value = "keyWord", required=false,defaultValue = "")String keyWord,
                                                  @RequestParam(value = "pageNum", required = false, defaultValue = "1") int pageNum,
                                                  @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
                                                  HttpServletRequest request) {
        String token = writeLogUtil.getToken(request);
        PageInfo<OperatorWorkerHistoryExample> pageInfo = operatorWorkerHistoryService.selectWorkerHistoryList(startTime,endTime,keyWord, pageNum, pageSize,token);
        return new ResponseResult(SUCCESS, pageInfo);
    }
}
