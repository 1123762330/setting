package com.xnpool.setting.controller;

import com.github.pagehelper.PageInfo;
import com.xnpool.setting.common.BaseController;
import com.xnpool.setting.domain.pojo.Worker;
import com.xnpool.setting.domain.pojo.WorkerExample;
import com.xnpool.setting.service.WorkerService;
import com.xnpool.setting.utils.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
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
public class WorkerController extends BaseController {
    @Autowired
    private WorkerService workerService;

    @GetMapping("/selectWorkerList")
    public ResponseResult selectWorkerList(String keyWord, @RequestParam(value = "pageNum", required = false, defaultValue = "1") int pageNum,
                                                 @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize) {
        HashMap<Integer, String> workerList = workerService.selectWorkerList(keyWord);
        return new ResponseResult(SUCCESS,workerList);
    }
}
