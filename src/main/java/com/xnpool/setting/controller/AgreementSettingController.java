package com.xnpool.setting.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xnpool.setting.common.BaseController;
import com.xnpool.setting.domain.pojo.AgreementSetting;
import com.xnpool.setting.service.AgreementSettingService;
import com.xnpool.setting.utils.ResponseResult;
import com.xnpool.setting.utils.UploadUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

/**
 * 协议设置
 * @author zly
 * @version 1.0
 * @date 2020/2/7 9:43
 */
@RestController
@Slf4j
@RequestMapping("/api/v1/agreement")
public class AgreementSettingController extends BaseController {
    @Autowired
    private AgreementSettingService agreementSettingService;
    //访问URL
    @Value("${config.prifix}")
    private String prifix;

    /**
     * @return
     * @Description 添加协议
     * @Author zly
     * @Date 9:48 2020/2/7
     * @Param
     */
    @PostMapping("/addAgreement")
    public ResponseResult addAgreement(AgreementSetting agreementSetting, @RequestParam("file") MultipartFile file) {
        //上传文件到服务器上
        ResponseResult result = UploadUtils.getFileToUpload(file);
        if (result != null) return result;
        //添加记录到数据库
        agreementSetting.setPath(prifix + file.getOriginalFilename());
        agreementSettingService.insertSelective(agreementSetting);
        return new ResponseResult(SUCCESS);
    }

    /**
     * @return
     * @Description 修改协议(其实就是重新上传文档)
     * @Author zly
     * @Date 9:49 2020/2/7
     * @Param
     */
    @PutMapping("/updateAgreement")
    public ResponseResult updateAgreement(AgreementSetting agreementSetting, @RequestParam("file") MultipartFile file) {
        //上传文件到服务器上
        ResponseResult result = UploadUtils.getFileToUpload(file);
        if (result != null) return result;
        //添加记录到数据库
        agreementSetting.setPath(prifix + file.getOriginalFilename());
        agreementSettingService.updateByPrimaryKeySelective(agreementSetting);
        return new ResponseResult(SUCCESS);

    }


    /**
     * @return
     * @Description 删除协议
     * @Author zly
     * @Date 9:52 2020/2/7
     * @Param
     */
    @DeleteMapping("/deleteAgreement")
    public ResponseResult deleteAgreement(int id) {
        agreementSettingService.updateById(id);
        return new ResponseResult(SUCCESS);
    }

    /**
     * @return
     * @Description 查询协议列表
     * @Author zly
     * @Date 9:55 2020/2/7
     * @Param
     */
    @GetMapping("/selectAgreement")
    public ResponseResult selectAgreement(String keyWord, @RequestParam(value = "pageNum", required = false, defaultValue = "1") int pageNum,
                                @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize) {
        PageInfo<AgreementSetting> pageInfo = agreementSettingService.selectByOther(keyWord, pageNum, pageSize);
        return new ResponseResult(SUCCESS,pageInfo);
    }

    @GetMapping("/selectAgreementMap")
    public ResponseResult selectAgreementMap(String keyWord, @RequestParam(value = "pageNum", required = false, defaultValue = "1") int pageNum,
                                          @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize) {
        PageInfo<AgreementSetting> pageInfo = agreementSettingService.selectByOther(keyWord, pageNum, pageSize);
        return new ResponseResult(SUCCESS,pageInfo);
    }


}
