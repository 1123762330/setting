package com.xnpool.setting.controller;

import com.github.pagehelper.PageInfo;
import com.xnpool.setting.common.BaseController;
import com.xnpool.setting.domain.pojo.GroupSetting;
import com.xnpool.setting.domain.pojo.GroupSettingExample;
import com.xnpool.setting.service.GroupSettingService;
import com.xnpool.setting.utils.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


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
    @DeleteMapping("/deleteGroup")
    public ResponseResult deleteGroup(int id) {
        //需要去查询当前分组内是否有矿机IP
        int rows = groupSettingService.updateById(id);
        if (rows == -1) {
            return new ResponseResult(fail, "该分组下存在有效IP,不允许删除!");
        }
        return new ResponseResult(SUCCESS);
    }

    /**
     * @return
     * @Description 查询分组列表
     * @Author zly
     * @Date 14:58 2020/2/4
     * @Param
     */
    @GetMapping("/selectGroupList")
    public ResponseResult selectGroupList(String keyWord, @RequestParam(value = "pageNum", required = false, defaultValue = "1") int pageNum,
                                          @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize) {
        PageInfo<GroupSettingExample> groupSettingExamplePageInfo = groupSettingService.selectByOther(keyWord, pageNum, pageSize);
        return new ResponseResult(SUCCESS, groupSettingExamplePageInfo);
    }
}
