package com.xnpool.setting.service.impl;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xnpool.logaop.service.exception.CheckException;
import com.xnpool.logaop.util.JwtUtil;
import com.xnpool.setting.common.BaseController;
import com.xnpool.setting.domain.mapper.*;
import com.xnpool.setting.domain.model.IpSettingExample;
import com.xnpool.setting.domain.pojo.*;
import com.xnpool.setting.domain.redismodel.SysUserRedisModel;
import com.xnpool.setting.service.FactoryHouseService;
import com.xnpool.setting.service.FrameSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.xnpool.setting.service.WorkerAssignService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author zly
 * @version 1.0
 * @date 2020/3/6 14:57
 */
@Service
public class WorkerAssignServiceImpl extends BaseController implements WorkerAssignService {

    @Resource
    private WorkerAssignMapper workerAssignMapper;

    @Autowired
    private IpAssignMapper ipAssignMapper;

    @Autowired
    private FrameSettingService frameSettingService;

    @Autowired
    private FrameSettingMapper frameSettingMapper;

    @Autowired
    private FactoryHouseService factoryHouseService;

    @Autowired
    private FactoryHouseMapper factoryHouseMapper;

    @Autowired
    private MineSettingMapper mineSettingMapper;

    @Autowired
    private IpSettingMapper ipSettingMapper;

    @Autowired
    private SysUserMapper sysUserMapper;


    @Override
    public int deleteByPrimaryKey(Integer id) {
        return workerAssignMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(WorkerAssign record) {
        return workerAssignMapper.insert(record);
    }

    @Override
    public int insertSelective(WorkerAssign record) {
        return workerAssignMapper.insertSelective(record);
    }

    @Override
    public WorkerAssign selectByPrimaryKey(Integer id) {
        return workerAssignMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(WorkerAssign record) {
        return workerAssignMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(WorkerAssign record) {
        return workerAssignMapper.updateByPrimaryKey(record);
    }

    @Override
    public PageInfo<UserRoleVO> selectByOther(String keyWord, int pageNum, int pageSize) {
        if (!StringUtils.isEmpty(keyWord)) {
            keyWord = "%" + keyWord + "%";
        }
        PageHelper.startPage(pageNum, pageSize);
        HashMap<Integer, Integer> conutMap = new HashMap<>();
        List<HashMap> list = workerAssignMapper.selectCountGroupByUserId();
        list.forEach(hashMap -> {
            Integer user_id = Integer.valueOf(hashMap.get("user_id").toString());
            Integer count = Integer.valueOf(hashMap.get("count").toString());
            conutMap.put(user_id, count);
        });
        List<UserRoleVO> userRoleVOS = workerAssignMapper.selectByOther();
        for (UserRoleVO userRoleVO : userRoleVOS) {
            Integer userId = userRoleVO.getUserId();
            if (userId == null) {
                userRoleVO.setCount(0);
            } else {
                Integer count = conutMap.get(userId);
                userRoleVO.setCount(count);
            }
        }
        PageInfo<UserRoleVO> pageInfo = new PageInfo<>(userRoleVOS);
        return pageInfo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addAssignWorker(String ids, String deleteIds, String deleteIps, String ipId, String token) {
        Integer userId = getUserId(token);
        List<WorkerAssign> workerAssigns = workerAssignMapper.selectWorkerAssignList(userId);
        HashSet<String> frameSet_db = new HashSet<>();
        HashSet<String> factorySet_db = new HashSet<>();
        HashSet<Integer> mineSet_db = new HashSet<>();
        for (WorkerAssign workerAssign : workerAssigns) {
            Integer mineId = workerAssign.getMineId();
            Integer factoryId = workerAssign.getFactoryId();
            Integer frameId = workerAssign.getFrameId();
            frameSet_db.add(mineId + "-" + factoryId + "-" + frameId);
            factorySet_db.add(mineId + "-" + factoryId);
            mineSet_db.add(mineId);
        }

        //先执行删除操作
        HashMap<Integer, List<MineFactoryAndFraneId>> deleteResultMap = new HashMap<>();
        ArrayList<MineFactoryAndFraneId> deleteList = new ArrayList<>();

        //矿机架删除
        if (!StringUtils.isEmpty(deleteIds)) {
            if (deleteIds.contains(",")) {
                //多个矿机架
                String[] split = deleteIds.split(",");
                for (int i = 0; i < split.length; i++) {
                    String id = split[i];
                    String[] splitId = id.split("-");
                    //做三层判断,第一是否是矿场Id,第二,是否是厂房id,第三,是否是直接的机架id
                    if (!id.contains("-")) {
                        //直接是矿场ID
                        if (mineSet_db.contains(id)) {
                            Integer mineId = Integer.valueOf(id);
                            HashMap<Integer, String> factoryMap = factoryHouseService.selectFactoryNameByMineId(mineId);
                            Set<Integer> keySet = factoryMap.keySet();
                            HashSet<Integer> factorySet = new HashSet<>();
                            for (Integer factoryId : keySet) {
                                factorySet.add(factoryId);
                            }
                            for (Integer factoryId : factorySet) {
                                HashMap<Integer, String> frameNameMap = frameSettingService.selectFrameNameByFactoryId(factoryId);
                                Set<Integer> frameIdSet = frameNameMap.keySet();
                                for (Integer frameId : frameIdSet) {
                                    MineFactoryAndFraneId mineFactoryAndFraneId = new MineFactoryAndFraneId(userId, mineId, factoryId, frameId);
                                    deleteList.add(mineFactoryAndFraneId);
                                }
                            }
                            deleteResultMap.put(Integer.valueOf(ids), deleteList);
                        }

                    } else if (splitId.length == 2) {
                        //第二种,矿场ID-厂房ID
                        if (factorySet_db.contains(id)) {
                            Integer mineId = Integer.valueOf(splitId[0]);
                            Integer factoryId = Integer.valueOf(splitId[1]);
                            HashMap<Integer, String> frameNameMap = frameSettingService.selectFrameNameByFactoryId(factoryId);
                            Set<Integer> keySet = frameNameMap.keySet();
                            for (Integer frameId : keySet) {
                                MineFactoryAndFraneId mineFactoryAndFraneId = new MineFactoryAndFraneId(userId, mineId, factoryId, frameId);
                                deleteList.add(mineFactoryAndFraneId);
                            }
                            deleteResultMap.put(mineId, deleteList);
                        }
                    } else {
                        //第三种,矿场ID-厂房ID-机架ID
                        if (frameSet_db.contains(id)) {
                            Integer mineId = Integer.valueOf(splitId[0]);
                            Integer factoryId = Integer.valueOf(splitId[1]);
                            Integer frameId = Integer.valueOf(splitId[2]);
                            MineFactoryAndFraneId mineFactoryAndFraneId = new MineFactoryAndFraneId(userId, mineId, factoryId, frameId);
                            deleteList.add(mineFactoryAndFraneId);
                            deleteResultMap.put(mineId, deleteList);
                        }
                    }
                }
            } else {
                //单个矿机架
                //做三层判断,第一是否是矿场Id,第二,是否是厂房id,第三,是否是直接的机架id
                String[] splitId = deleteIds.split("-");
                if (!ids.contains("-")) {
                    //直接是矿场ID
                    Integer mineId = Integer.valueOf(ids);
                    if (mineSet_db.contains(mineId)) {
                        HashMap<Integer, String> factoryMap = factoryHouseService.selectFactoryNameByMineId(mineId);
                        Set<Integer> keySet = factoryMap.keySet();
                        List<Integer> factoryIdList = new ArrayList<>();
                        for (Integer factoryId : keySet) {
                            factoryIdList.add(factoryId);
                        }
                        for (Integer factoryId : factoryIdList) {
                            HashMap<Integer, String> frameNameMap = frameSettingService.selectFrameNameByFactoryId(factoryId);
                            Set<Integer> frameIdSet = frameNameMap.keySet();
                            for (Integer frameId : frameIdSet) {
                                MineFactoryAndFraneId mineFactoryAndFraneId = new MineFactoryAndFraneId(userId, mineId, factoryId, frameId);
                                deleteList.add(mineFactoryAndFraneId);
                            }
                        }
                        deleteResultMap.put(Integer.valueOf(ids), deleteList);
                    }
                } else if (splitId.length == 2) {
                    //第二种,矿场ID-厂房ID
                    if (factorySet_db.contains(ids)) {
                        Integer mineId = Integer.valueOf(splitId[0]);
                        Integer factoryId = Integer.valueOf(splitId[1]);
                        HashMap<Integer, String> frameNameMap = frameSettingService.selectFrameNameByFactoryId(factoryId);
                        Set<Integer> keySet = frameNameMap.keySet();
                        for (Integer frameId : keySet) {
                            MineFactoryAndFraneId mineFactoryAndFraneId = new MineFactoryAndFraneId(userId, mineId, factoryId, frameId);
                            deleteList.add(mineFactoryAndFraneId);
                        }
                        deleteResultMap.put(mineId, deleteList);
                    }

                } else {
                    //第三种,矿场ID-厂房ID-机架ID
                    if (frameSet_db.contains(ids)) {
                        Integer mineId = Integer.valueOf(splitId[0]);
                        Integer factoryId = Integer.valueOf(splitId[1]);
                        Integer frameId = Integer.valueOf(splitId[2]);
                        MineFactoryAndFraneId mineFactoryAndFraneId = new MineFactoryAndFraneId(userId, mineId, factoryId, frameId);
                        deleteList.add(mineFactoryAndFraneId);
                        deleteResultMap.put(mineId, deleteList);
                    }
                }
            }
            //执行批量软删除
            int rows3 = workerAssignMapper.batchToDelete(deleteList);
            //矿机架权限分配同步入缓存
            for (Map.Entry<Integer, List<MineFactoryAndFraneId>> entry : deleteResultMap.entrySet()) {
                redisToBatchDelete(rows3, "worker_assign", entry.getValue(), entry.getKey());
            }
        }
        //Ip区间删除
        ArrayList<MineIdAndIP> delete_ips = new ArrayList<>();
        if (!StringUtils.isEmpty(deleteIps)) {
            if (deleteIds.contains(",")) {
                String[] split = ipId.split(",");
                for (String ip_id : split) {
                    String[] split2 = ip_id.split("-");
                    Integer mineId = Integer.valueOf(split2[0]);
                    Integer ip_id2 = Integer.valueOf(split2[1]);
                    MineIdAndIP mineIdAndIP = new MineIdAndIP(userId, mineId, ip_id2);
                    delete_ips.add(mineIdAndIP);
                }
            } else {
                String[] split = deleteIds.split("-");
                Integer mineId = Integer.valueOf(split[0]);
                Integer ip_id = Integer.valueOf(split[1]);
                MineIdAndIP mineIdAndIP = new MineIdAndIP(userId, mineId, ip_id);
                delete_ips.add(mineIdAndIP);
            }
            //删除ip
            Integer rows4 = ipAssignMapper.deleteByBatch(delete_ips);
            //记录缓存
            Map<Integer, List<MineIdAndIP>> groupByMineId = delete_ips.stream().collect(Collectors.groupingBy(MineIdAndIP::getMine_id));
            //ip区间权限删除同步入缓存
            for (Map.Entry<Integer, List<MineIdAndIP>> entry : groupByMineId.entrySet()) {
                redisToBatchDelete(rows4, "ip_assign", entry.getValue(), entry.getKey());
            }
        }

        //执行添加操作
        HashMap<Integer, List<MineFactoryAndFraneId>> resultMap = new HashMap<>();
        ArrayList<MineFactoryAndFraneId> list = new ArrayList<>();
        if (ids.contains(",")) {
            //多个矿机架
            String[] split = ids.split(",");
            for (int i = 0; i < split.length; i++) {
                String id = split[i];
                String[] splitId = id.split("-");
                //做三层判断,第一是否是矿场Id,第二,是否是厂房id,第三,是否是直接的机架id
                if (!id.contains("-")) {
                    //直接是矿场ID
                    if (!mineSet_db.contains(id)) {
                        Integer mineId = Integer.valueOf(id);
                        HashMap<Integer, String> factoryMap = factoryHouseService.selectFactoryNameByMineId(mineId);
                        Set<Integer> keySet = factoryMap.keySet();
                        HashSet<Integer> factorySet = new HashSet<>();
                        for (Integer factoryId : keySet) {
                            factorySet.add(factoryId);
                        }
                        for (Integer factoryId : factorySet) {
                            HashMap<Integer, String> frameNameMap = frameSettingService.selectFrameNameByFactoryId(factoryId);
                            Set<Integer> frameIdSet = frameNameMap.keySet();
                            for (Integer frameId : frameIdSet) {
                                MineFactoryAndFraneId mineFactoryAndFraneId = new MineFactoryAndFraneId(userId, mineId, factoryId, frameId);
                                list.add(mineFactoryAndFraneId);
                            }
                        }
                        resultMap.put(Integer.valueOf(ids), list);
                    }

                } else if (splitId.length == 2) {
                    //第二种,矿场ID-厂房ID
                    if (!factorySet_db.contains(id)) {
                        Integer mineId = Integer.valueOf(splitId[0]);
                        Integer factoryId = Integer.valueOf(splitId[1]);
                        HashMap<Integer, String> frameNameMap = frameSettingService.selectFrameNameByFactoryId(factoryId);
                        Set<Integer> keySet = frameNameMap.keySet();
                        for (Integer frameId : keySet) {
                            MineFactoryAndFraneId mineFactoryAndFraneId = new MineFactoryAndFraneId(userId, mineId, factoryId, frameId);
                            list.add(mineFactoryAndFraneId);
                        }
                        resultMap.put(mineId, list);
                    }
                } else {
                    //第三种,矿场ID-厂房ID-机架ID
                    if (!frameSet_db.contains(id)) {
                        Integer mineId = Integer.valueOf(splitId[0]);
                        Integer factoryId = Integer.valueOf(splitId[1]);
                        Integer frameId = Integer.valueOf(splitId[2]);
                        MineFactoryAndFraneId mineFactoryAndFraneId = new MineFactoryAndFraneId(userId, mineId, factoryId, frameId);
                        list.add(mineFactoryAndFraneId);
                        resultMap.put(mineId, list);
                    }
                }
            }
        } else {
            //单个矿机架
            //做三层判断,第一是否是矿场Id,第二,是否是厂房id,第三,是否是直接的机架id
            String[] splitId = ids.split("-");
            if (!ids.contains("-")) {
                //直接是矿场ID
                Integer mineId = Integer.valueOf(ids);
                if (!mineSet_db.contains(mineId)) {
                    HashMap<Integer, String> factoryMap = factoryHouseService.selectFactoryNameByMineId(mineId);
                    Set<Integer> keySet = factoryMap.keySet();
                    List<Integer> factoryIdList = new ArrayList<>();
                    for (Integer factoryId : keySet) {
                        factoryIdList.add(factoryId);
                    }
                    for (Integer factoryId : factoryIdList) {
                        HashMap<Integer, String> frameNameMap = frameSettingService.selectFrameNameByFactoryId(factoryId);
                        Set<Integer> frameIdSet = frameNameMap.keySet();
                        for (Integer frameId : frameIdSet) {
                            MineFactoryAndFraneId mineFactoryAndFraneId = new MineFactoryAndFraneId(userId, mineId, factoryId, frameId);
                            list.add(mineFactoryAndFraneId);
                        }
                    }
                    resultMap.put(Integer.valueOf(ids), list);
                }
            } else if (splitId.length == 2) {
                //第二种,矿场ID-厂房ID
                if (!factorySet_db.contains(ids)) {
                    Integer mineId = Integer.valueOf(splitId[0]);
                    Integer factoryId = Integer.valueOf(splitId[1]);
                    HashMap<Integer, String> frameNameMap = frameSettingService.selectFrameNameByFactoryId(factoryId);
                    Set<Integer> keySet = frameNameMap.keySet();
                    for (Integer frameId : keySet) {
                        MineFactoryAndFraneId mineFactoryAndFraneId = new MineFactoryAndFraneId(userId, mineId, factoryId, frameId);
                        list.add(mineFactoryAndFraneId);
                    }
                    resultMap.put(mineId, list);
                }

            } else {
                //第三种,矿场ID-厂房ID-机架ID
                if (!frameSet_db.contains(ids)) {
                    Integer mineId = Integer.valueOf(splitId[0]);
                    Integer factoryId = Integer.valueOf(splitId[1]);
                    Integer frameId = Integer.valueOf(splitId[2]);
                    MineFactoryAndFraneId mineFactoryAndFraneId = new MineFactoryAndFraneId(userId, mineId, factoryId, frameId);
                    list.add(mineFactoryAndFraneId);
                    resultMap.put(mineId, list);
                }
            }

        }
        //执行保存功能
        int rows = workerAssignMapper.batchInsert(list);
        //添加机架成功后用户信息缓存到redis里面去
        SysUser sysUser = sysUserMapper.selectById(userId);
        SysUserRedisModel sysUserRedisModel = getSysUserRedisModel(sysUser);
        List<Integer> mineIdList = getMineId(token);
        for (Integer mineId : mineIdList) {
            redisToInsert(rows, "sys_user", sysUserRedisModel,mineId );
        }
        //执行ip区间分配
        ArrayList<MineIdAndIP> ip_list = new ArrayList<>();
        if (ipId.contains(",")) {
            String[] split = ipId.split(",");
            for (String ip_id : split) {
                String[] split2 = ip_id.split("-");
                Integer mineId = Integer.valueOf(split2[0]);
                Integer ip_id2 = Integer.valueOf(split2[1]);
                MineIdAndIP mineIdAndIP = new MineIdAndIP(userId, mineId, ip_id2);
                ip_list.add(mineIdAndIP);
            }
        } else {
            String[] split = ipId.split("-");
            Integer mineId = Integer.valueOf(split[0]);
            Integer ip_id = Integer.valueOf(split[1]);
            MineIdAndIP mineIdAndIP = new MineIdAndIP(userId, mineId, ip_id);
            ip_list.add(mineIdAndIP);
        }
        //执行入IP权限表
        Integer rows2 = ipAssignMapper.batchInsert(ip_list);
        Map<Integer, List<MineIdAndIP>> groupByMineId = ip_list.stream().collect(Collectors.groupingBy(MineIdAndIP::getMine_id));
        //矿机架权限分配同步入缓存
        for (Map.Entry<Integer, List<MineFactoryAndFraneId>> entry : resultMap.entrySet()) {
            redisToBatchInsert(rows, "worker_assign", entry.getValue(), entry.getKey());
        }
        //ip区间权限分配同步入缓存
        for (Map.Entry<Integer, List<MineIdAndIP>> entry : groupByMineId.entrySet()) {
            redisToBatchInsert(rows2, "ip_assign", entry.getValue(), entry.getKey());
        }
    }

    @Override
    public HashMap<Integer, HashMap<String, Object>> selectAssignMineMap(String token) {
        Integer userId = getUserId(token);
        List<MineSetting> mineSettingList = mineSettingMapper.selectByOther(null);
        List<WorkerAssign> workerAssigns = workerAssignMapper.selectWorkerAssignList(userId);
        HashSet<Integer> set = new HashSet<>();
        for (WorkerAssign workerAssign : workerAssigns) {
            set.add(workerAssign.getMineId());
        }
        HashMap<Integer, HashMap<String, Object>> resultMap = new HashMap<>();
        mineSettingList.forEach(mineSetting -> {
            Integer id = mineSetting.getId();
            if (set.contains(id)) {
                HashMap<String, Object> map = new HashMap<>();
                String mineName = mineSetting.getMineName();
                map.put("name", mineName);
                map.put("checked", true);
                resultMap.put(id, map);
            } else {
                HashMap<String, Object> map = new HashMap<>();
                String mineName = mineSetting.getMineName();
                map.put("name", mineName);
                map.put("checked", false);
                resultMap.put(id, map);
            }
        });
        return resultMap;
    }

    @Override
    public HashMap<Integer, HashMap<String, Object>> selectAssignFactoryMap(String token, Integer mineId) {
        Integer userId = getUserId(token);
        List<WorkerAssign> workerAssigns = workerAssignMapper.selectWorkerAssignList(userId);
        HashSet<Integer> set = new HashSet<>();
        for (WorkerAssign workerAssign : workerAssigns) {
            set.add(workerAssign.getFactoryId());
        }
        HashMap<Integer, HashMap<String, Object>> resultMap = new HashMap<>();
        List<HashMap> hashMaps = factoryHouseMapper.selectFactoryNameByMineId(mineId);
        if (!hashMaps.isEmpty()) {
            hashMaps.forEach(hashMap -> {
                Integer id = Integer.valueOf(hashMap.get("id").toString());
                if (set.contains(id)) {
                    String factoryName = hashMap.get("factory_name").toString();
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("name", factoryName);
                    map.put("checked", true);
                    resultMap.put(id, map);
                } else {
                    String factoryName = hashMap.get("factory_name").toString();
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("name", factoryName);
                    map.put("checked", false);
                    resultMap.put(id, map);
                }
            });
        }

        return resultMap;
    }

    @Override
    public HashMap<Integer, HashMap<String, Object>> selectAssignFrameMap(String token, Integer factoryId) {
        Integer userId = getUserId(token);
        List<WorkerAssign> workerAssigns = workerAssignMapper.selectWorkerAssignList(userId);
        HashSet<Integer> set = new HashSet<>();
        for (WorkerAssign workerAssign : workerAssigns) {
            set.add(workerAssign.getFrameId());
        }
        HashMap<Integer, HashMap<String, Object>> resultMap = new HashMap<>();
        List<HashMap> hashMaps = frameSettingMapper.selectFrameNameByFactoryId(factoryId);
        if (!hashMaps.isEmpty()) {
            hashMaps.forEach(hashMap -> {
                Integer id = Integer.valueOf(hashMap.get("id").toString());
                if (set.contains(id)) {
                    String frameName = hashMap.get("frame_name").toString();
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("name", frameName);
                    map.put("checked", true);
                    resultMap.put(id, map);
                } else {
                    String frameName = hashMap.get("frame_name").toString();
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("name", frameName);
                    map.put("checked", false);
                    resultMap.put(id, map);
                }
            });
        }
        return resultMap;
    }

    @Override
    public HashMap<Integer, HashMap<String, Object>> selectAssignIPMap(String token, String mineName, Integer mineId) {
        Integer userId = getUserId(token);
        QueryWrapper<IpAssign> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_del", 0);
        queryWrapper.eq("user_id", userId);
        List<IpAssign> ipAssigns = ipAssignMapper.selectList(queryWrapper);
        HashSet<Integer> set = new HashSet<>();
        for (IpAssign ipAssign : ipAssigns) {
            set.add(ipAssign.getIpId());
        }

        HashMap<Integer, HashMap<String, Object>> resultMap = new HashMap<>();
        List<IpSettingExample> ipSettings = ipSettingMapper.selectByOther(null, mineId);
        if (ipSettings != null) {
            ipSettings.forEach(ipSetting -> {
                Integer id = ipSetting.getId();
                if (set.contains(id)) {
                    String startIp = ipSetting.getStartIp();
                    String endIp = ipSetting.getEndIp();
                    String ipName = mineName + " " + startIp + "-" + endIp;
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("name", ipName);
                    map.put("checked", true);
                    resultMap.put(id, map);
                } else {
                    String startIp = ipSetting.getStartIp();
                    String endIp = ipSetting.getEndIp();
                    String ipName = mineName + " " + startIp + "-" + endIp;
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("name", ipName);
                    map.put("checked", false);
                    resultMap.put(id, map);
                }
            });
        }
        return resultMap;
    }

}

