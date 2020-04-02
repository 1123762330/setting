package com.xnpool.setting.controller;

import com.github.pagehelper.PageInfo;


import com.xnpool.logaop.annotation.SystemLog;
import com.xnpool.logaop.util.LogType;
import com.xnpool.logaop.util.ResponseResult;
import com.xnpool.logaop.util.WriteLogUtil;
import com.xnpool.setting.common.BaseController;
import com.xnpool.setting.domain.pojo.UserRoleVO;
import com.xnpool.setting.service.WorkerAssignService;
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
    @Autowired
    private WriteLogUtil writeLogUtil;

    /**
     * @Description 用户角色列表
     * @Author zly
     * @Date 15:00 2020/3/6
     * @Param
     * @return
     */
    @SystemLog(value = "查询矿机权限列表",type = LogType.MINE)
    @GetMapping("/selectUserAndRole")
    public ResponseResult selectUserAndRole( @RequestParam(value = "keyWord", required=false,defaultValue = "")String keyWord,
                                             @RequestParam(value = "pageNum", required = false, defaultValue = "1") int pageNum,
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
        String token = writeLogUtil.getToken(request);
        workerAssignService.addAssignWorker(ids,token);
        return new ResponseResult(SUCCESS);
    }


    //判断添加的矿机架是否已经分配
    @PostMapping("/selectWorkerAssign")
    public ResponseResult selectWorkerAssign() {
        List<Integer> list = workerAssignService.selectWorkerAssign();
        return new ResponseResult(SUCCESS,list);
    }
}
