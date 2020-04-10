package com.xnpool.setting.controller;

import com.github.pagehelper.PageInfo;


import com.xnpool.logaop.annotation.SystemLog;
import com.xnpool.logaop.util.LogType;
import com.xnpool.logaop.util.ResponseResult;
import com.xnpool.logaop.util.WriteLogUtil;
import com.xnpool.setting.common.BaseController;
import com.xnpool.setting.config.ApiContext;
import com.xnpool.setting.domain.pojo.SysUser;
import com.xnpool.setting.domain.pojo.UserRoleVO;
import com.xnpool.setting.domain.redismodel.SysUserRedisModel;
import com.xnpool.setting.service.WorkerAssignService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
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

    @Autowired
    private ApiContext apiContext;

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
    public ResponseResult addAssignWorker(String ids,String deleteIds,String deleteIps,String ipId, Integer userId) {
        workerAssignService.addAssignWorker(ids,deleteIds,deleteIps,ipId,userId);
        return new ResponseResult(SUCCESS);
    }

    //判断添加的矿机架是否已经分配
    //@PostMapping("/selectWorkerAssign")
    //public ResponseResult selectWorkerAssign() {
    //    List<Integer> list = workerAssignService.selectWorkerAssign();
    //    return new ResponseResult(SUCCESS,list);
    //}

    @SystemLog(value = "运维设置查询矿场列表",type = LogType.MINE)
    @GetMapping("/selectAssignMineMap")
    public ResponseResult selectAssignMineMap(Integer userId) {
        HashMap<Integer, HashMap<String, Object>> mineMap = workerAssignService.selectAssignMineMap(userId);
        return new ResponseResult(SUCCESS,mineMap);
    }

    @SystemLog(value = "运维设置查询厂房列表",type = LogType.MINE)
    @GetMapping("/selectAssignFactoryMap")
    public ResponseResult selectAssignFactoryMap(Integer userId,Integer mineId) {
        HashMap<Integer, HashMap<String, Object>> mineMap = workerAssignService.selectAssignFactoryMap(userId,mineId);
        return new ResponseResult(SUCCESS,mineMap);
    }

    @SystemLog(value = "运维设置查询机架列表",type = LogType.MINE)
    @GetMapping("/selectAssignFrameMap")
    public ResponseResult selectAssignFrameMap(Integer userId,Integer factoryId) {
        HashMap<Integer, HashMap<String, Object>> mineMap = workerAssignService.selectAssignFrameMap(userId,factoryId);
        return new ResponseResult(SUCCESS,mineMap);
    }

    @SystemLog(value = "运维设置查询IP区间列表",type = LogType.MINE)
    @GetMapping("/selectAssignIPMap")
    public ResponseResult selectAssignIPMap(Integer userId,String mineName,Integer mineId) {
        HashMap<Integer, HashMap<String, Object>> mineMap = workerAssignService.selectAssignIPMap(userId, mineName,mineId);
        return new ResponseResult(SUCCESS,mineMap);
    }

    /**
     * @Description 修改用户时同步用户信息
     * @Author zly
     * @Date 12:18 2020/4/10
     * @Param
     * @return
     */
    @PutMapping("/syncingUpdateUser")
    public ResponseResult syncingUpdateUser (SysUser sysUser, String token){
        ResponseResult responseResult = syncinUser(sysUser, token);
        return responseResult;
    }

    /**
     * @Description 删除用户时同步用户信息
     * @Author zly
     * @Date 12:26 2020/4/10
     * @Param
     * @return
     */
    @DeleteMapping("/syncinDeleteUser")
    public ResponseResult syncinDeleteUser (SysUser sysUser, String token){
        ResponseResult responseResult = syncinUser(sysUser, token);
        return responseResult;
    }
}
