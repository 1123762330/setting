package com.xnpool.setting.controller;

import com.github.pagehelper.PageInfo;
import com.xnpool.logaop.annotation.SystemLog;
import com.xnpool.logaop.util.LogType;
import com.xnpool.logaop.util.ResponseResult;
import com.xnpool.logaop.util.WriteLogUtil;
import com.xnpool.setting.common.BaseController;
import com.xnpool.setting.domain.pojo.Algorithm;
import com.xnpool.setting.service.AlgorithmService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

/**
 * 算法设置
 * @author zly
 * @version 1.0
 * @date 2020/3/19 16:30
 */
@RestController
@Slf4j
@RequestMapping("/api/v1/Algorithm")
public class AlgorithmController extends BaseController {
    @Autowired
    private AlgorithmService algorithmService;

    @Autowired
    private WriteLogUtil writeLogUtil;

    /**
     * @return
     * @Description 添加算法
     * @Author zly
     * @Date 12:59 2020/3/19
     * @Param
     */
    @SystemLog(value = "添加算法",type = LogType.SYSTEM)
    @PostMapping("/addAlgorithm")
    public ResponseResult addFactoryHouse(Algorithm algorithm) {
        algorithmService.insert(algorithm);
        return new ResponseResult(SUCCESS);
    }

    /**
     * @return
     * @Description 修改算法
     * @Author zly
     * @Date 11:26 2020/2/4
     * @Param
     */
    @SystemLog(value = "修改算法",type = LogType.SYSTEM)
    @PutMapping("/updateAlgorithm")
    public ResponseResult updateAlgorithm(Algorithm algorithm) {
        algorithmService.updateByPrimaryKey(algorithm);
        return new ResponseResult(SUCCESS);
    }

    /**
     * @return
     * @Description 根据ID对某条数据进行软删除
     * @Author zly
     * @Date 11:45 2020/2/4
     * @Param
     */
    @SystemLog(value = "删除算法",type = LogType.SYSTEM)
    @DeleteMapping("/deleteAlgorithm")
    public ResponseResult deleteAlgorithm(int id) {
            algorithmService.deleteById(id);
            return new ResponseResult(SUCCESS);
    }

    /**
     * @return
     * @Description 查询算法列表
     * @Author zly
     * @Date 14:58 2020/2/4
     * @Param
     */
    @SystemLog(value = "查询算法列表",type = LogType.SYSTEM)
    @GetMapping("/selectAlgorithm")
    public ResponseResult selectFactoryHouse (@RequestParam(value = "keyWord", required = false, defaultValue = "") String keyWord,
                                              @RequestParam(value = "pageNum", required = false, defaultValue = "1") int pageNum,
                                              @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize){
        PageInfo<Algorithm> pageInfo = algorithmService.selectAlgorithm(keyWord, pageNum, pageSize);
        return new ResponseResult(SUCCESS,pageInfo);
    }

    /**
     * @Description 算法列表集合
     * @Author zly
     * @Date 16:56 2020/3/19
     * @Param
     * @return
     */
    @GetMapping("/selectAlgorithmMap")
    public ResponseResult selectAlgorithmMap (HttpServletRequest request){
        String token = writeLogUtil.getToken(request);
        Long tenantId=Long.valueOf(request.getHeader("tenantId"));
        HashMap<Integer, String> algorithmMap = algorithmService.selectAlgorithmMap(token,tenantId);
        return new ResponseResult(SUCCESS,algorithmMap);
    }
}
