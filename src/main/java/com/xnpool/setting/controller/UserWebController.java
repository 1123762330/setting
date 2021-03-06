package com.xnpool.setting.controller;

import com.github.pagehelper.PageInfo;


import com.xnpool.logaop.annotation.SystemLog;
import com.xnpool.logaop.util.LogType;
import com.xnpool.logaop.util.ResponseResult;
import com.xnpool.logaop.util.WriteLogUtil;
import com.xnpool.setting.common.BaseController;
import com.xnpool.setting.config.ApiContext;
import com.xnpool.setting.domain.model.WorkerDetailedModel;
import com.xnpool.setting.domain.pojo.CustomerSetting;
import com.xnpool.setting.service.CustomerSettingService;
import com.xnpool.setting.service.WorkerDetailedService;
import com.xnpool.setting.service.impl.UserWebService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @Autowired
    private CustomerSettingService customerSettingService;

    @Autowired
    private WriteLogUtil writeLogUtil;

    @Autowired
    private ApiContext apiContext;

    /**
     * @Description 用户网站查询矿机详情列表
     * @Author zly
     * @Date 18:02 2020/3/10
     * @Param
     * @return
     */
    @SystemLog(value = "查询用户网站矿机详情列表",type = LogType.SURVER)
    @GetMapping("/selectAllWorkerDetailed")
    public ResponseResult selectAllWorkerDetailed( @RequestParam(value = "workerName", required=false,defaultValue = "")String workerName,
                                                   @RequestParam(value = "startIp", required=false,defaultValue = "")String startIp,
                                                   @RequestParam(value = "endIp", required=false,defaultValue = "")String endIp,
                                                  @RequestParam(value = "onLine", required = false, defaultValue = "0") String onLine,
                                                  @RequestParam(value = "offLine", required = false, defaultValue = "0") String offLine,
                                                  @RequestParam(value = "pageNum", required = false, defaultValue = "1") int pageNum,
                                                  @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
                                                  HttpServletRequest request) {
        String token = writeLogUtil.getToken(request);
        Long tenantId=Long.valueOf(request.getHeader("tenantId"));
        apiContext.setTenantId(Long.valueOf(tenantId));
        PageInfo<WorkerDetailedModel> workerPageInfo = workerDetailedService.selectAllWorkerDetailed(onLine,offLine,workerName,startIp,endIp, pageNum, pageSize,token,tenantId);
        return new ResponseResult(SUCCESS, workerPageInfo);
    }

    /**
     * @Description 用户网站的分组列表
     * @Author zly
     * @Date 17:52 2020/3/12
     * @Param
     * @return
     */
    @SystemLog(value = "查询用户网站分组列表",type = LogType.SURVER)
    @GetMapping("/selectGroupModel")
    public ResponseResult selectGroupModel(@RequestParam(value = "groupName", required=false,defaultValue = "")String groupName,
                                           @RequestParam(value = "startIp", required=false,defaultValue = "")String startIp,
                                           @RequestParam(value = "endIp", required=false,defaultValue = "")String endIp,
                                           @RequestParam(value = "pageNum", required = false, defaultValue = "1") int pageNum,
                                          @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
                                           HttpServletRequest request) {
        String token = writeLogUtil.getToken(request);
        Long tenantId=Long.valueOf(request.getHeader("tenantId"));
        apiContext.setTenantId(Long.valueOf(tenantId));
        HashMap<String, Object> groupModel = workerDetailedService.selectGroupModel(groupName,startIp,endIp,token, pageNum, pageSize,tenantId);
        return new ResponseResult(SUCCESS,groupModel);
    }

    /**
     * @Description 用户矿机算力图
     * @Author zly
     * @Date 15:00 2020/3/13
     * @Param
     * @return
     */
    @SystemLog(value = "查询用户矿机算力曲线图",type = LogType.SURVER)
    @GetMapping("/getWorkerHashByDay")
    public ResponseResult getPoolWorkerHashByDay(Integer algorithmId,HttpServletRequest request){
        String token = writeLogUtil.getToken(request);
        Long tenantId=Long.valueOf(request.getHeader("tenantId"));
        apiContext.setTenantId(Long.valueOf(tenantId));
        Map<Object, Object>  workerHashByDay = userWebService.getWorkerHashByDay(algorithmId,token,tenantId);
        return new ResponseResult(SUCCESS,workerHashByDay);
    }

    /**
     * @Description 用户矿机在线数量图
     * @Author zly
     * @Date 15:00 2020/3/13
     * @Param
     * @return
     */
    @SystemLog(value = "查询用户矿机在线数量图",type = LogType.SURVER)
    @GetMapping("/getWorkerTotalByDay")
    public ResponseResult getWorkerTotalByDay(Integer algorithmId,HttpServletRequest request){
        String token = writeLogUtil.getToken(request);
        Long tenantId=Long.valueOf(request.getHeader("tenantId"));
        apiContext.setTenantId(Long.valueOf(tenantId));
        Map<Object, Object>  workerTotalByDay = userWebService.getWorkerTotalByDay(token,algorithmId,tenantId);
        return new ResponseResult(SUCCESS,workerTotalByDay);
    }

    @SystemLog(value = "用户饼状图",type = LogType.SURVER)
    @GetMapping("/getWorkerTotal")
    public ResponseResult getWorkerTotal(HttpServletRequest request){
        String token = writeLogUtil.getToken(request);
        Long tenantId=Long.valueOf(request.getHeader("tenantId"));
        apiContext.setTenantId(Long.valueOf(tenantId));
        HashMap<String, Integer> hashMap = userWebService.getWorkerTotal(token,tenantId);
        return new ResponseResult(SUCCESS,hashMap);
    }

    @SystemLog(value = "用户申请企业授权",type = LogType.SYSTEM)
    @PostMapping("/userApplyAuthority")
    public ResponseResult userApplyAuthority(CustomerSetting customerSetting,HttpServletRequest request){
        String token = writeLogUtil.getToken(request);
        Long tenantId=Long.valueOf(request.getHeader("tenantId"));
        apiContext.setTenantId(Long.valueOf(tenantId));
        customerSettingService.insertSelective(customerSetting,token);
        return new ResponseResult(SUCCESS);
    }

    /**
     * @Description 查询已授权企业列表
     * @Author zly
     * @Date 12:47 2020/3/26
     * @Param
     * @return
     */
    @SystemLog(value = "查询企业列表",type = LogType.SYSTEM)
    @GetMapping("/selectTenantList")
    public ResponseResult selectTenantList (HttpServletRequest request){
        String token = writeLogUtil.getToken(request);
        HashMap<Long, HashMap> tenantList = customerSettingService.selectTenantList(token);
        return new ResponseResult(SUCCESS,tenantList);
    }

    /**
     * @Description 删除权限
     * @Author zly
     * @Date 20:50 2020/3/26
     * @Param
     * @return
     */
    @SystemLog(value = "用户删除企业授权",type = LogType.SYSTEM)
    @PutMapping("/deleteAuthority")
    public ResponseResult deleteAuthority (String tenantId, HttpServletRequest request){
        String token = writeLogUtil.getToken(request);
        customerSettingService.deleteAuthority(tenantId,token);
        return new ResponseResult(SUCCESS);
    }
}
