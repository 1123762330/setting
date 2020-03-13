package com.xnpool.setting.controller;

import com.github.pagehelper.PageInfo;


import com.xnpool.logaop.annotation.SystemLog;
import com.xnpool.logaop.util.LogType;
import com.xnpool.setting.common.BaseController;
import com.xnpool.setting.domain.pojo.PowerSetting;
import com.xnpool.setting.domain.pojo.UserRoleVO;
import com.xnpool.setting.domain.pojo.WorkerAssign;
import com.xnpool.setting.service.WorkerAssignService;
import com.xnpool.setting.utils.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 矿机分配
 * @author zly
 * @version 1.0
 * @date 2020/3/6 14:58
 */
@RestController
@Slf4j
@RequestMapping("/api/v1/WorkerAssign/")
public class WorkerAssignController extends BaseController {
    @Autowired
    private WorkerAssignService workerAssignService;

    /**
     * @Description 用户角色列表
     * @Author zly
     * @Date 15:00 2020/3/6
     * @Param
     * @return
     */
    @SystemLog(value = "查询矿机权限列表",type = LogType.MINE)
    @GetMapping("/selectUserAndRole")
    public ResponseResult selectFrameList(String keyWord, @RequestParam(value = "pageNum", required = false, defaultValue = "1") int pageNum,
                                          @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize) {
        PageInfo<UserRoleVO> pageInfo = workerAssignService.selectByOther(keyWord, pageNum, pageSize);
        return new ResponseResult(SUCCESS, pageInfo);
    }

    /**
     * @return
     * @Description 分配矿机
     * @Author zly
     * @Date 15:28 2020/2/6
     * @Param
     */
    @SystemLog(value = "矿机权限设置",type = LogType.MINE)
    @PostMapping("/addAssignWorker")
    public ResponseResult addAssignWorker(String ids, HttpServletRequest request) {
        String token = request.getHeader("token");
        workerAssignService.addAssignWorker(ids,token);
        return new ResponseResult(SUCCESS);
    }

}
