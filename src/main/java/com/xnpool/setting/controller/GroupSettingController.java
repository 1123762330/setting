package com.xnpool.setting.controller;

import com.github.pagehelper.PageInfo;
import com.xnpool.logaop.annotation.SystemLog;
import com.xnpool.logaop.util.LogType;
import com.xnpool.setting.common.BaseController;
import com.xnpool.setting.domain.pojo.GroupSetting;
import com.xnpool.setting.domain.model.GroupSettingExample;
import com.xnpool.setting.service.GroupSettingService;
import com.xnpool.setting.utils.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;


/**
 * 分组设置
 *
 * @author zly
 * @version 1.0
 * @date 2020/2/5 15:37
 */
@RestController
@Slf4j
@RequestMapping("/api/v1/groupSetting")
public class GroupSettingController extends BaseController {
    @Autowired
    private GroupSettingService groupSettingService;

    /**
     * @return
     * @Description 添加分组
     * @Author zly
     * @Date 12:59 2020/2/3
     * @Param
     */
    @SystemLog(value = "添加分组设置",type = LogType.SYSTEM)
    @PostMapping("/addGroup")
    public ResponseResult addGroup(GroupSetting groupSetting) {
        groupSettingService.insertSelective(groupSetting);
        return new ResponseResult(SUCCESS);
    }

    /**
     * @return
     * @Description 修改分组
     * @Author zly
     * @Date 11:26 2020/2/4
     * @Param
     */
    @SystemLog(value = "修改分组设置",type = LogType.SYSTEM)
    @PutMapping("/updateGroup")
    public ResponseResult updateGroup(GroupSetting groupSetting) {
        groupSettingService.updateByPrimaryKeySelective(groupSetting);
        return new ResponseResult(SUCCESS);
    }

    /**
     * @return
     * @Description 根据ID对某条数据进行软删除
     * @Author zly
     * @Date 11:45 2020/2/4
     * @Param
     */
    @SystemLog(value = "删除分组设置",type = LogType.SYSTEM)
    @DeleteMapping("/deleteGroup")
    public ResponseResult deleteGroup(int id) {
        //需要去查询当前分组内是否有矿机IP
        groupSettingService.updateById(id);
        return new ResponseResult(SUCCESS);
    }

    /**
     * @return
     * @Description 查询分组列表
     * @Author zly
     * @Date 14:58 2020/2/4
     * @Param
     */
    @SystemLog(value = "查询分组设置列表",type = LogType.SYSTEM)
    @GetMapping("/selectGroupList")
    public ResponseResult selectGroupList(String keyWord, @RequestParam(value = "pageNum", required = false, defaultValue = "1") int pageNum,
                                          @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize) {
        PageInfo<GroupSettingExample> groupSettingExamplePageInfo = groupSettingService.selectByOther(keyWord, pageNum, pageSize);
        return new ResponseResult(SUCCESS, groupSettingExamplePageInfo);
    }

    /**
     * @return
     * @Description 查询分组列表
     * @Author zly
     * @Date 14:58 2020/2/4
     * @Param
     */
    @GetMapping("/selectGroupMap")
    public ResponseResult selectGroupMap() {
        HashMap<Integer, String> groupMap = groupSettingService.selectGroupMap();
        return new ResponseResult(SUCCESS,groupMap);
    }
}
