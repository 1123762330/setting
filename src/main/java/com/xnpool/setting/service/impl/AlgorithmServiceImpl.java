package com.xnpool.setting.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xnpool.logaop.service.exception.DataNotFoundException;
import com.xnpool.setting.common.BaseController;
import com.xnpool.setting.domain.mapper.WorkerbrandSettingMapper;
import com.xnpool.setting.domain.pojo.WorkerbrandSetting;
import com.xnpool.setting.utils.JedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import com.xnpool.setting.domain.pojo.Algorithm;
import com.xnpool.setting.domain.mapper.AlgorithmMapper;
import com.xnpool.setting.service.AlgorithmService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.net.URLEncoder;
import java.util.*;

import static com.xnpool.setting.common.BaseController.HASHRATE_DATA;

/**
 * @author zly
 * @version 1.0
 * @date 2020/3/19 16:21
 */
@Service
public class AlgorithmServiceImpl extends BaseController implements AlgorithmService {

    @Resource
    private AlgorithmMapper algorithmMapper;
    @Autowired
    private JedisUtil jedisUtil;
    @Autowired
    private WorkerbrandSettingMapper workerbrandSettingMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteByPrimaryKey(Integer id) {
        return algorithmMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(Algorithm record) {
        return algorithmMapper.insert(record);
    }

    @Override
    public PageInfo<Algorithm> selectAlgorithm(String keyWord, int pageNum, int pageSize) {
        if (!StringUtils.isEmpty(keyWord)) {
            keyWord = "%" + keyWord + "%";
        }
        PageHelper.startPage(pageNum, pageSize);
        List<Algorithm> algorithmList = algorithmMapper.selectAlgorithm(keyWord);
        PageInfo<Algorithm> pageInfo = new PageInfo<>(algorithmList);
        return pageInfo;
    }

    @Override
    public HashMap<Integer, String> selectExistAlgorithm(String token,Long tenantId) {
        //首先去缓存里进行查询这个用户名下的所有的矿机类型
        //存到一个list的集合,然后遍历,同数据库查询的矿机品牌做对比,然后取算法id
        Integer userId = getUserId(token);
        //System.out.println(userId);
        String startTimekey = HASHRATE_DATA +userId+":"+tenantId.toString()+ ":*";
        Set<String> hashKeySet = jedisUtil.scan(startTimekey);
        HashSet<String> workertypeSet = new HashSet<>();
        for (String key : hashKeySet) {
            int lastIndexOf = key.lastIndexOf(":");
            String workerType = key.substring(lastIndexOf+1);
            workertypeSet.add(workerType);
        }
        String startTimekeyNum = ON_LINE_DATA + userId + ":" + tenantId + ":*";
        Set<String> numKeySet = jedisUtil.scan(startTimekeyNum);
        for (String key : numKeySet) {
            int lastIndexOf = key.lastIndexOf(":");
            String workerType = key.substring(lastIndexOf+1);
            workertypeSet.add(workerType);
        }

        HashMap<String, Integer> workerbrandMap = new HashMap<>();
        List<WorkerbrandSetting> workerbrandSettingList = workerbrandSettingMapper.selectByOther(null);
        workerbrandSettingList.forEach(workerbrandSetting -> {
            String workerType = workerbrandSetting.getWorkerType();
            String workerType_encoder = URLEncoder.encode(workerType);
            Integer algorithmId = workerbrandSetting.getAlgorithmId();
            workerbrandMap.put(workerType_encoder, algorithmId);
        });

        List<HashMap> hashMapList = algorithmMapper.selectAlgorithmMap();

        HashMap<Integer, String> hashMap_db = new HashMap<>();
        for (HashMap hashMap : hashMapList) {
            String algorithm_name = hashMap.get("algorithm_name").toString();
            Integer id = Integer.valueOf(hashMap.get("id").toString());
            hashMap_db.put(id, algorithm_name);
        }

        HashMap<Integer, String> resultMap = new HashMap<>();
        for (String workerType : workertypeSet) {
            Integer algorithmId = workerbrandMap.get(workerType);
            String algorithmName = hashMap_db.get(algorithmId);
            resultMap.put(algorithmId,algorithmName);
        }
        return resultMap;
    }

    @Override
    public HashMap<Integer, String> selectAlgorithmMap() {
        List<HashMap> hashMapList = algorithmMapper.selectAlgorithmMap();
        HashMap<Integer, String> resultMap = new HashMap<>();
        for (HashMap hashMap : hashMapList) {
            String algorithm_name = hashMap.get("algorithm_name").toString();
            Integer id = Integer.valueOf(hashMap.get("id").toString());
            resultMap.put(id, algorithm_name);
        }
        return resultMap;
    }

    @Override
    public Algorithm selectByPrimaryKey(Integer id) {
        return algorithmMapper.selectById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteById(int id) {
        return algorithmMapper.deleteByAlgorithmId(id);
    }

    @Override
    public int updateByPrimaryKey(Algorithm record) {
        return algorithmMapper.updateById(record);
    }

}
