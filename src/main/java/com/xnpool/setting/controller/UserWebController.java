package com.xnpool.setting.controller;

import com.github.pagehelper.PageInfo;
import com.xnpool.setting.common.BaseController;
import com.xnpool.setting.domain.model.WorkerDetailedModel;
import com.xnpool.setting.service.WorkerDetailedService;
import com.xnpool.setting.service.impl.UserWebService;
import com.xnpool.setting.utils.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zly
 * @version 1.0
 * @date 2020/3/12 17:49
 */
@RestController
@Slf4j
@RequestMapping("/api/v1/userweb/")
public class UserWebController extends BaseController {
    @Autowired
    private WorkerDetailedService workerDetailedService;

    @Autowired
    private UserWebService userWebService;

    /**
     * @Description 用户网站查询矿机详情列表
     * @Author zly
     * @Date 18:02 2020/3/10
     * @Param
     * @return
     */
    @GetMapping("/selectAllWorkerDetailed")
    public ResponseResult selectAllWorkerDetailed(String workerName,String startIp,String endIp,
                                                  @RequestParam(value = "pageNum", required = false, defaultValue = "1") int pageNum,
                                                  @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
                                                  HttpServletRequest request) {
        String token = request.getHeader("token");
        PageInfo<WorkerDetailedModel> workerPageInfo = workerDetailedService.selectAllWorkerDetailed(workerName,startIp,endIp, pageNum, pageSize,token);
        return new ResponseResult(SUCCESS, workerPageInfo);
    }

    /**
     * @Description 用户网站的分组列表
     * @Author zly
     * @Date 17:52 2020/3/12
     * @Param
     * @return
     */
    @GetMapping("/selectGroupModel")
    public ResponseResult selectGroupModel(String keyWord, @RequestParam(value = "pageNum", required = false, defaultValue = "1") int pageNum,
                                          @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
                                           HttpServletRequest request) {
        String token = request.getHeader("token");
        HashMap<String, Object> groupModel = workerDetailedService.selectGroupModel(token, pageNum, pageSize);
        return new ResponseResult(SUCCESS,groupModel);
    }

    /**
     * @Description 用户矿机算力图
     * @Author zly
     * @Date 15:00 2020/3/13
     * @Param
     * @return
     */
    @GetMapping("/getWorkerHashByDay")
    public ResponseResult getPoolWorkerHashByDay(HttpServletRequest request){
        String token = request.getHeader("token");
        Map<Object, Object>  workerHashByDay = userWebService.getWorkerHashByDay(token);
        return new ResponseResult(SUCCESS,workerHashByDay);
    }

    /**
     * @Description 用户矿机在线数量图
     * @Author zly
     * @Date 15:00 2020/3/13
     * @Param
     * @return
     */
    @GetMapping("/getWorkerTotalByDay")
    public ResponseResult getWorkerTotalByDay(HttpServletRequest request){
        String token = request.getHeader("token");
        Map<Object, Object>  workerTotalByDay = userWebService.getWorkerTotalByDay(token);
        return new ResponseResult(SUCCESS,workerTotalByDay);
    }
}
