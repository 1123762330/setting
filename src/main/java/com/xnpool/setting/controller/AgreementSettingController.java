package com.xnpool.setting.controller;

import com.github.pagehelper.PageInfo;
import com.xnpool.logaop.annotation.SystemLog;
import com.xnpool.logaop.util.LogType;
import com.xnpool.logaop.util.ResponseResult;
import com.xnpool.setting.common.BaseController;
import com.xnpool.setting.domain.pojo.AgreementSetting;
import com.xnpool.setting.service.AgreementSettingService;
import com.xnpool.setting.utils.UploadUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 协议设置
 *
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

    //文件存储路径
    @Value("${config.filePath}")
    private String filePath;

    @Value("${config.prifix_2}")
    private String prifix_2;
    /**
     * @return
     * @Description 添加协议
     * @Author zly
     * @Date 9:48 2020/2/7
     * @Param
     */
    @SystemLog(value = "添加协议",type = LogType.SYSTEM)
    @PostMapping("/addAgreement")
    public ResponseResult addAgreement(AgreementSetting agreementSetting, @RequestParam("file") MultipartFile file) {
        //上传文件到服务器上
        long fileSize = file.getSize();
        long time = System.currentTimeMillis() / 1000;
        String originalFilename = time + "-" + file.getOriginalFilename();
        if (fileSize > 10485760) {
            return new ResponseResult(FAIL, "上传文件不能超过10M");
        } else {
            ResponseResult result = UploadUtils.getFileToUpload(file, filePath, originalFilename);
            if (200 != result.getStatus()) return result;
            //添加记录到数据库
            agreementSetting.setPath(prifix_2 + originalFilename);
            agreementSetting.setFileName(file.getOriginalFilename());
            agreementSettingService.insertSelective(agreementSetting);
            return new ResponseResult(SUCCESS);
        }

    }

    /**
     * @return
     * @Description 修改协议(其实就是重新上传文档)
     * @Author zly
     * @Date 9:49 2020/2/7
     * @Param
     */
    //@SystemLog(value = "修改协议", type = LogType.SYSTEM)
    @PutMapping("/updateAgreement")
    public ResponseResult updateAgreement(AgreementSetting agreementSetting, @RequestParam(value = "file", required = false) MultipartFile file) {
        //上传文件到服务器上
        if (file!=null&&!file.isEmpty()){
            long time = System.currentTimeMillis() / 1000;
            String originalFilename = time + "-" + file.getOriginalFilename();
            ResponseResult result = UploadUtils.getFileToUpload(file, filePath, originalFilename);
            if (result != null) return result;
            //添加记录到数据库
            agreementSetting.setPath(prifix_2 + file.getOriginalFilename());
            agreementSetting.setFileName(originalFilename);
            agreementSettingService.updateByPrimaryKeySelective(agreementSetting);
        }else {
            agreementSettingService.updateByPrimaryKeySelective(agreementSetting);
        }

        return new ResponseResult(SUCCESS);

    }


    /**
     * @return
     * @Description 删除协议
     * @Author zly
     * @Date 9:52 2020/2/7
     * @Param
     */
    @SystemLog(value = "删除协议", type = LogType.SYSTEM)
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
    @SystemLog(value = "查询协议", type = LogType.SYSTEM)
    @GetMapping("/selectAgreement")
    public ResponseResult selectAgreement(@RequestParam(value = "keyWord", required = false, defaultValue = "") String keyWord,
                                          @RequestParam(value = "pageNum", required = false, defaultValue = "1") int pageNum,
                                          @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize) {
        PageInfo<AgreementSetting> pageInfo = agreementSettingService.selectByOther(keyWord, pageNum, pageSize);
        return new ResponseResult(SUCCESS, pageInfo);
    }

    @GetMapping("/selectAgreementMap")
    public ResponseResult selectAgreementMap(String keyWord, @RequestParam(value = "pageNum", required = false, defaultValue = "1") int pageNum,
                                             @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize) {
        PageInfo<AgreementSetting> pageInfo = agreementSettingService.selectByOther(keyWord, pageNum, pageSize);
        return new ResponseResult(SUCCESS, pageInfo);
    }


}
