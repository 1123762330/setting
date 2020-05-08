package com.xnpool.setting.controller;
import java.time.LocalDateTime;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xnpool.logaop.annotation.SystemLog;
import com.xnpool.logaop.util.LogType;
import com.xnpool.logaop.util.ResponseResult;
import com.xnpool.setting.common.BaseController;
import com.xnpool.setting.domain.model.MeterReviewExample;
import com.xnpool.setting.domain.pojo.FactoryHouse;
import com.xnpool.setting.domain.pojo.MeterReview;
import com.xnpool.setting.domain.pojo.MeterReviewParam;
import com.xnpool.setting.domain.pojo.MineSetting;
import com.xnpool.setting.service.MeterReviewService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *抄表审核
 * @author zly
 * @since 2020-05-04
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/meter_review")
public class MeterReviewController extends BaseController {
    @Autowired
    private MeterReviewService meterReviewService;

    /**
     * @Description 抄表
     * @Author zly
     * @Date 14:29 2020/5/5
     * @Param
     * @return
     */
    //@SystemLog(value = "抄表",type = LogType.SYSTEM)
    @PostMapping("/add")
    public ResponseResult addMeterReview(@RequestBody String json) {
        meterReviewService.batchSave(json);
        return new ResponseResult(SUCCESS);
    }

    /**
     * @Description 删除抄表记录
     * @Author zly
     * @Date 14:52 2020/5/5
     * @Param
     * @return
     */
    @SystemLog(value = "删除抄表记录",type = LogType.SYSTEM)
    @DeleteMapping("/delete")
    public ResponseResult deleteMineSetting(int id) {
            meterReviewService.deleteById(id);
            return new ResponseResult(SUCCESS);
    }

    /**
     * @Description 查询抄表审核列表
     * @Author zly
     * @Date 14:24 2020/5/5
     * @Param
     * @return
     */
    @SystemLog(value = "查询抄表审核列表",type = LogType.SYSTEM)
    @GetMapping("/list")
    public ResponseResult meterReviewList(@RequestParam(value = "keyWord", required = false, defaultValue = "") String keyWord,
                                            @RequestParam(value = "pageNum", required = false, defaultValue = "1") int pageNum,
                                            @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize) {
        Page<MeterReviewExample> meterReviewPage = meterReviewService.selectByOther(keyWord, pageNum, pageSize);
        return new ResponseResult(SUCCESS, meterReviewPage);
    }

}
