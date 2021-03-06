package com.xnpool.setting.controller;


import com.xnpool.logaop.annotation.SystemLog;
import com.xnpool.logaop.util.LogType;
import com.xnpool.logaop.util.ResponseResult;
import com.xnpool.logaop.util.WriteLogUtil;
import com.xnpool.setting.common.BaseController;
import com.xnpool.setting.domain.pojo.CustomerSetting;
import com.xnpool.setting.service.CustomerSettingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.HashSet;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zly
 * @since 2020-04-27
 */
@RestController
@Slf4j
@RequestMapping("/api/v1/customer")
public class CustomerSettingController extends BaseController {
    @Autowired
    private CustomerSettingService customerSettingService;

    @Autowired
    private WriteLogUtil writeLogUtil;

    /**
     * @Description 添加客户设置
     * @Author zly
     * @Date 10:40 2020/2/10
     * @Param
     * @return
     */
    @SystemLog(value = "添加客户",type = LogType.SYSTEM)
    @PostMapping("/addCustomer")
    public ResponseResult addCustomer(CustomerSetting customerSetting, HttpServletRequest request) {
        String token = writeLogUtil.getToken(request);
        customerSettingService.insertSelective(customerSetting,token);
        return new ResponseResult(SUCCESS);
    }

    /**
     * @Description 修改客户设置
     * @Author zly
     * @Date 10:41 2020/2/10
     * @Param
     * @return
     */
    @SystemLog(value = "修改客户设置",type = LogType.SYSTEM)
    @PutMapping("/updateCustomer")
    public ResponseResult updateCustomer(CustomerSetting customerSetting) {
        customerSettingService.updateByPrimaryKeySelective(customerSetting);
        return new ResponseResult(SUCCESS);
    }

    /**
     * @Description 删除客户设置
     * @Author zly
     * @Date 10:44 2020/2/10
     * @Param
     * @return
     */
    @SystemLog(value = "删除客户",type = LogType.SYSTEM)
    @DeleteMapping("/deleteCustomer")
    public ResponseResult deleteCustomer(int id) {
        customerSettingService.deleteById(id);
        return new ResponseResult(SUCCESS);
    }

    /**
     * @Description 查询客户列表
     * 每个管理员只能查询自己权限内的客户
     * @Author zly
     * @Date 11:37 2020/2/10
     * @Param
     * @return
     */
    @SystemLog(value = "查询客户列表",type = LogType.SYSTEM)
    @GetMapping("/selectCustomerList")
    public ResponseResult selectCustomerList (@RequestParam(value = "username", required = false, defaultValue = "") String username,
                                              @RequestParam(value = "agreementName", required = false, defaultValue = "") String agreementName,
                                              @RequestParam(value = "groupName", required = false, defaultValue = "") String groupName,
                                              @RequestParam(value = "pageNum", required = false, defaultValue = "1") int pageNum,
                                              @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
                                              @RequestParam(value = "authorize", required = false, defaultValue = "0") Integer authorize){
        Object pageInfo = customerSettingService.selectByOther(username,agreementName,groupName, pageNum, pageSize,authorize);
        return new ResponseResult(SUCCESS,pageInfo);
    }

    /**
     * 认证模式还需要用户表才能实现,需要企业ID,管理员登录,根据管理员的企业ID来查看申请列表.然后选择是否同意认证
     * @Description 认证用户
     * @Author zly
     * @Date 11:57 2020/2/10
     * @Param
     * @return
     */
    @SystemLog(value = "客户认证",type = LogType.SYSTEM)
    @PutMapping("/attestationCustomer")
    public ResponseResult attestationCustomer(String cusId,Integer isPass) {
        //首先根据根据管理者的企业ID去查询向企业请求认证的用户列表,如果authentication=0就是用户申请认证
        //这个时候管理者可以选择通过认证,也可以选择拒绝认证,通过认证就是1,拒绝认证就是-1.
        customerSettingService.updateAttestationById(cusId,isPass);
        return new ResponseResult(SUCCESS);
    }

    /**
     * @Description 查询客户名称,在矿机入库时绑定userid
     * @Author zly
     * @Date 13:50 2020/3/8
     * @Param
     * @return
     */
    @GetMapping("/selectUserList")
    public ResponseResult selectUserList (){
        HashMap<Integer, String> userNameList = customerSettingService.selectUserList();
        return new ResponseResult(SUCCESS,userNameList);
    }

    /**
     * @Description 查询客户姓名集合,添加电表时需要
     * @Author zly
     * @Date 9:51 2020/3/23
     * @Param
     * @return
     */
    @GetMapping("/selectCustomerMap")
    public ResponseResult selectCustomerMap (){
        HashMap<Integer, String> userNameList = customerSettingService.selectCustomerMap();
        return new ResponseResult(SUCCESS,userNameList);
    }

    /**
     * @Description 查询用户表所有的用户
     * @Author zly
     * @Date 10:21 2020/3/23
     * @Param
     * @return
     */
    @GetMapping("/selectAllUser")
    public ResponseResult selectAllUser (){
        HashMap<Integer, String> userNameList = customerSettingService.selectAllUser();
        return new ResponseResult(SUCCESS,userNameList);
    }

    /**
     * @Description 授权管理员用户token
     * @Author zly
     * @Date 21:05 2020/3/30
     * @Param
     * @return
     */
    @GetMapping("/authorizeToken")
    public ResponseResult authorizeToken (String userId){
        String authorizeToken = customerSettingService.authorizeToken(userId);
        return new ResponseResult(SUCCESS,"",authorizeToken);
    }

    @SystemLog(value = "客户列表下拉列表",type = LogType.SYSTEM)
    @GetMapping("/selectCustomDropList")
    public ResponseResult selectCustomDropList (@RequestParam(value = "authorize", required = false, defaultValue = "0") Integer authorize){
        HashMap<String, HashSet<String>> dropList = customerSettingService.selectCustomDropList(authorize);
        return new ResponseResult(SUCCESS,dropList);
    }
}
