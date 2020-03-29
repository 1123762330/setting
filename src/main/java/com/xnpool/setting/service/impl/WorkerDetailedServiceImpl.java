package com.xnpool.setting.service.impl;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xnpool.logaop.service.exception.CheckException;
import com.xnpool.logaop.service.exception.DeleteException;
import com.xnpool.logaop.service.exception.InsertException;
import com.xnpool.logaop.util.JwtUtil;
import com.xnpool.setting.common.BaseController;
import com.xnpool.setting.domain.mapper.WorkerInfoMapper;
import com.xnpool.setting.domain.model.GroupModel;
import com.xnpool.setting.domain.model.WorkerDetailedExample;
import com.xnpool.setting.domain.model.WorkerDetailedModel;
import com.xnpool.setting.domain.model.WorkerExample;
import com.xnpool.setting.domain.pojo.*;
import com.xnpool.setting.domain.redismodel.WorkerDetailedRedisModel;
import com.xnpool.setting.service.IpSettingService;
import com.xnpool.setting.service.OperatorWorkerHistoryService;
import com.xnpool.setting.utils.PageUtil;
import com.xnpool.setting.utils.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import com.xnpool.setting.domain.mapper.WorkerDetailedMapper;
import com.xnpool.setting.service.WorkerDetailedService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * @author zly
 * @version 1.0
 * @date 2020/2/23 12:52
 */
@Service
@Slf4j
public class WorkerDetailedServiceImpl extends BaseController implements WorkerDetailedService {

    @Resource
    private WorkerDetailedMapper workerDetailedMapper;

    @Resource
    private WorkerInfoMapper workerInfoMapper;

    @Autowired
    private IpSettingService ipSettingService;

    @Autowired
    private OperatorWorkerHistoryService operatorWorkerHistoryService;

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return workerDetailedMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(WorkerDetailed record) {
        return workerDetailedMapper.insert(record);
    }

    @Override
    public int insertSelective(WorkerDetailed record) {
        return workerDetailedMapper.insertSelective(record);
    }

    @Override
    public WorkerDetailed selectByPrimaryKey(Integer id) {
        return workerDetailedMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(WorkerDetailed record) {
        return workerDetailedMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(WorkerDetailed record) {
        return workerDetailedMapper.updateByPrimaryKey(record);
    }

    @Override
    public PageInfo<WorkerDetailedExample> selectMoveOutList(String keyWord, int pageNum, int pageSize) {
        if (!StringUtils.isEmpty(keyWord)) {
            keyWord = "%" + keyWord + "%";
        }
        PageHelper.startPage(pageNum, pageSize);
        List<WorkerDetailedExample> WorkerDetailedExampleList = workerDetailedMapper.selectMoveOutList(keyWord);
        System.out.println("查询的矿机出库列表是:" + WorkerDetailedExampleList);
        WorkerDetailedExampleList.forEach(workerDetailedExample -> {
            String workerName = workerDetailedExample.getWorkerName();
            if (StringUtils.isEmpty(workerName)) {
                String frameName = workerDetailedExample.getFrameName();
                Integer frameNumber = workerDetailedExample.getFrameNumber();
                workerDetailedExample.setMiner("");
                workerDetailedExample.setWorkerName("");
                StringBuffer frameNameBuffer = new StringBuffer(frameName).append(" ").append(frameNumber).append("层");
                workerDetailedExample.setFrameName(frameNameBuffer.toString());
            } else {
                String frameName = workerDetailedExample.getFrameName();
                Integer frameNumber = workerDetailedExample.getFrameNumber();
                String minerName = "";
                String workerNameStr = "";
                if (!workerName.contains(".")) {
                    minerName = workerName;
                    workerNameStr = "";
                } else {
                    int lastIndexOf = workerName.lastIndexOf(".");
                    minerName = workerName.substring(0, lastIndexOf);
                    workerNameStr = workerName.substring(lastIndexOf + 1);
                }
                workerDetailedExample.setMiner(minerName);
                workerDetailedExample.setWorkerName(workerNameStr);
                StringBuffer frameNameBuffer = new StringBuffer(frameName).append(" ").append(frameNumber).append("层");
                workerDetailedExample.setFrameName(frameNameBuffer.toString());

            }
        });
        PageInfo<WorkerDetailedExample> pageInfo = new PageInfo<>(WorkerDetailedExampleList);
        return pageInfo;
    }

    /**
     * @return
     * @Description 添加矿机到出入库管理表中
     * @Author zly
     * @Date 13:38 2020/2/26
     * @Param
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addWorkerToLibrary(WorkerDetailedParam workerDetailedParam,String token) {
        //后期从token中获取用户Id
        Integer operatorId = getUserId(token);
        Integer id = workerDetailedParam.getId();
        Integer mineId = workerDetailedParam.getMineId();
        String workerIds = workerDetailedParam.getWorkerId();
        Integer userId = workerDetailedParam.getUserId();
        Integer workerbrandId = workerDetailedParam.getWorkerbrandId();
        Integer frameNumber = workerDetailedParam.getFrameNumber();
        Integer groupId = workerDetailedParam.getGroupId();
        String workerIp = workerDetailedParam.getWorkerIp();
        String remarks = workerDetailedParam.getRemarks();
        List<Integer> workerIdList = workerDetailedMapper.selectWorkerIdlist(null);
        log.info("库中已经存在的矿机ID:" + workerIdList);
        ArrayList<WorkerDetailed> list = new ArrayList<>();
        ArrayList<WorkerDetailedRedisModel> redisModelList = new ArrayList<>();
        if (workerIds.contains(",")) {
            //批量添加
            String[] split = workerIds.split(",");
            String[] split_ip = workerIp.split(",");
            for (int i = 0; i < split.length; i++) {
                if (workerIdList.contains(Integer.valueOf(split[i]))) {
                    throw new InsertException("ID为" + split[i] + "的矿机已经添加过!");
                } else {
                    String workerId = split[i];
                    WorkerDetailed workerDetailed = new WorkerDetailed();
                    workerDetailed.setWorkerId(Integer.valueOf(workerId));
                    workerDetailed.setWorkerIp(split_ip[i]);
                    workerDetailed.setUserId(userId);
                    workerDetailed.setWorkerbrandId(workerbrandId);
                    workerDetailed.setGroupId(groupId);
                    workerDetailed.setCreateTime(new Date());
                    workerDetailed.setRemarks(remarks);
                    list.add(workerDetailed);

                    WorkerDetailedRedisModel workerDetailedRedisModel = new WorkerDetailedRedisModel();
                    workerDetailedRedisModel.setWorker_id(Integer.valueOf(workerId));
                    workerDetailedRedisModel.setUser_id(userId);
                    workerDetailedRedisModel.setWorkerbrand_id(workerbrandId);
                    workerDetailedRedisModel.setFrame_number(frameNumber);
                    workerDetailedRedisModel.setGroup_id(groupId);
                    workerDetailedRedisModel.setRemarks(remarks);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    workerDetailedRedisModel.setCreate_time(sdf.format(new Date()));
                    workerDetailedRedisModel.setWorker_ip(split_ip[i]);
                    redisModelList.add(workerDetailedRedisModel);
                }
                //int rows = workerDetailedMapper.update(workerDetailed);
            }
        } else {
            if (workerIdList.contains(workerIds)) {
                throw new InsertException("该矿机已经添加过!");
            } else {
                WorkerDetailed workerDetailed = new WorkerDetailed();
                workerDetailed.setId(id);
                workerDetailed.setWorkerId(Integer.valueOf(workerIds));
                workerDetailed.setWorkerIp(workerIp);
                workerDetailed.setUserId(userId);
                workerDetailed.setGroupId(groupId);
                workerDetailed.setUpdateTime(new Date());
                workerDetailed.setWorkerIp(workerIp);
                workerDetailed.setWorkerbrandId(workerbrandId);
                workerDetailed.setRemarks(remarks);
                list.add(workerDetailed);

                WorkerDetailedRedisModel workerDetailedRedisModel = new WorkerDetailedRedisModel();
                workerDetailedRedisModel.setWorker_id(Integer.valueOf(workerIds));
                workerDetailedRedisModel.setUser_id(userId);
                workerDetailedRedisModel.setWorkerbrand_id(workerbrandId);
                workerDetailedRedisModel.setId(id);
                workerDetailedRedisModel.setGroup_id(groupId);
                workerDetailedRedisModel.setRemarks(remarks);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                workerDetailedRedisModel.setUpdate_time(sdf.format(new Date()));
                workerDetailedRedisModel.setWorker_ip(workerIp);

                redisModelList.add(workerDetailedRedisModel);
                //批量入管理仓库
                System.out.println(workerDetailed);
                int rows = workerDetailedMapper.update(workerDetailed);
                //记录到操作历史表中
                List<Integer> arrayList = new ArrayList<>();
                arrayList.add(Integer.valueOf(workerIds));
                String reason="";
                int rows2 = operatorWorkerHistoryService.insertTobatch(arrayList, reason, mineId, operatorId);
                //出库数据同步到缓存里
                HashMap<String, Object> operatorWorkerData = new HashMap<>();
                operatorWorkerData.put("workerIdList", workerIdList);
                operatorWorkerData.put("reason", reason);
                operatorWorkerData.put("operatorId", userId);
                redisToBatchInsert(rows2, "operator_worker_histkory", operatorWorkerData, mineId);
                //批量入缓存
                redisToBatchInsert(rows, "worker_detailed", redisModelList, mineId);
            }
        }
    }

    //入库列表
    @Override
    public HashMap<String, Object>  selectComeInWorkerList(String workerType, Integer state, String ip, int pageNum, int pageSize) {
        if (!StringUtils.isEmpty(ip)) {
            ip = "%" + ip + "%";
        }
        //取IP字段区间Map,便于后面取值
        HashMap<Integer, String> selectByIPStart = ipSettingService.selectByIPStart();
        List<WorkerExample> result = new ArrayList<>();
        HashMap<String, String> ipMap = new HashMap<>();
        for (Map.Entry<Integer, String> entry : selectByIPStart.entrySet()) {
            String value = entry.getValue();
            String[] split = value.split("-");
            String ipStart = split[0];
            int lastIndexOf = ipStart.lastIndexOf(".");
            String key = ipStart.substring(0, lastIndexOf);
            ipMap.put(key, value);
        }
        List<WorkerInfo> workers = workerInfoMapper.selectByOther(workerType,state,ip);
        //已经入库的矿机Id
        List<Integer> comeInlist = workerDetailedMapper.selectWorkerIdlist(1);
        //List<Integer>转List<Long>
        ArrayList<Long> comeInlistToLong = new ArrayList<>();
        if (!comeInlist.isEmpty()){
            for (Integer integer : comeInlist) {
                Long workerIdToLong = Long.valueOf(integer.toString());
                comeInlistToLong.add(workerIdToLong);
            }
        }

        //遍历list集合,setIP所属区间进去
        for (WorkerInfo worker : workers) {
            int lastIndexOf = worker.getIp().lastIndexOf(".");
            String substring = worker.getIp().substring(0, lastIndexOf);
            String ip_quJian = ipMap.get(substring);
            //这里需要做个判断,判断这个矿机有没有入库,如果入库列表里有那就是1,如果没有就是0
            if (comeInlistToLong.contains(worker.getId())) {
                WorkerExample workerExample = new WorkerExample(Integer.valueOf(worker.getId().toString()), worker.getIp(), ip_quJian, Integer.valueOf(worker.getState()), 1);
                result.add(workerExample);
            } else {
                WorkerExample workerExample = new WorkerExample(Integer.valueOf(worker.getId().toString()), worker.getIp(), ip_quJian, Integer.valueOf(worker.getState()), 0);
                result.add(workerExample);
            }
        }
        HashMap<String, Object> startPage = PageUtil.startPage(result, pageNum, pageSize);

        return startPage;
    }

    //下架操作
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateMoveOutByid(String ids, String reason, String token) {
        ArrayList<Integer> list = new ArrayList<>();
        Integer userId = getUserId(token);
        if (ids.contains(",")) {
            //全部出库
            String[] split = ids.split(",");
            for (int i = 0; i < split.length; i++) {
                list.add(Integer.valueOf(split[i]));
            }
        } else {
            //单个出库
            list.add(Integer.valueOf(ids));
        }
        List<Integer> idList = workerDetailedMapper.selectIdByWorkerId(list);
        List<WorkerMineVO> workerMineVOS = workerDetailedMapper.selectByWorkerId(list);
        workerDetailedMapper.updateMoveOutByid(idList);

        Map<Integer, List<WorkerMineVO>> groupByMineId = workerMineVOS.stream().collect(Collectors.groupingBy(WorkerMineVO::getMineId));
        for (Map.Entry<Integer, List<WorkerMineVO>> entry : groupByMineId.entrySet()) {
            //同时需要记录到历史表中
            List<Integer> workerIdList = new ArrayList<>();
            //将该矿场下的矿机id提取到集合中
            List<WorkerMineVO> WorkerMineVOList = entry.getValue();
            for (WorkerMineVO workerMineVO : WorkerMineVOList) {
                Integer workerId = workerMineVO.getWorkerId();
                workerIdList.add(workerId);
            }
            int rows = operatorWorkerHistoryService.updateMoveOutTimeById(workerIdList);
            //出库数据同步到缓存里
            HashMap<String, Object> operatorWorkerData = new HashMap<>();
            operatorWorkerData.put("workerIdList", workerIdList);
            operatorWorkerData.put("reason", reason);
            operatorWorkerData.put("operatorId", userId);
            redisToBatchInsert(rows, "operator_worker_histkory", operatorWorkerData, entry.getKey());
            batchMoveOut(rows, "worker_detailed", workerIdList, entry.getKey());
        }
    }

    //入库操作
    //@Override
    //@Transactional(rollbackFor = Exception.class)
    //public void updateComeInByid(String ids) {
    //    ArrayList<Integer> list = new ArrayList<>();
    //    if (ids.contains(",")) {
    //        //全部入库
    //        String[] split = ids.split(",");
    //        for (int i = 0; i < split.length; i++) {
    //            list.add(Integer.valueOf(split[i]));
    //        }
    //    } else {
    //        //单个入库
    //        list.add(Integer.valueOf(ids));
    //    }
    //    int rows = workerDetailedMapper.updateComeInByid(list);
    //    //修改记录表里的出库时间
    //    int rows2 = operatorWorkerHistoryService.updateMoveOutTimeById(list);
    //    if (rows == 0 || rows2 == 0) {
    //        throw new InsertException("矿机入库失败");
    //    }
    //    //根据批量更新的id去库里面查询矿场id
    //    List<WorkerMineVO> workerMineVOS = workerDetailedMapper.selectByWorkerId(list);
    //    Map<Integer, List<WorkerMineVO>> groupByMineId = workerMineVOS.stream().collect(Collectors.groupingBy(WorkerMineVO::getMineId));
    //    for (Map.Entry<Integer, List<WorkerMineVO>> entry : groupByMineId.entrySet()) {
    //        //同时需要记录到历史表中
    //        List<Integer> workerIdList = new ArrayList<>();
    //        //将该矿场下的矿机id提取到集合中
    //        List<WorkerMineVO> WorkerMineVOList = entry.getValue();
    //        for (WorkerMineVO workerMineVO : WorkerMineVOList) {
    //            Integer workerId = workerMineVO.getWorkerId();
    //            workerIdList.add(workerId);
    //        }
    //        //批量入库数据同步到缓存里
    //        redisToBatchUpdate(rows, "operator_worker_histkory", workerIdList, entry.getKey());
    //        batchComeIn(rows, "worker_detailed", workerIdList, entry.getKey());
    //    }
    //}

    //软删除
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateById(String ids) {
        ArrayList<Integer> list = new ArrayList<>();
        if (ids.contains(",")) {
            //全部出库
            String[] split = ids.split(",");
            for (int i = 0; i < split.length; i++) {
                list.add(Integer.valueOf(split[i]));
            }
        } else {
            //单个出库
            list.add(Integer.valueOf(ids));
        }

        //根据批量更新的id去库里面查询矿场id
        List<WorkerMineVO> workerMineVOS = workerDetailedMapper.selectByWorkerId(list);
        Map<Integer, List<WorkerMineVO>> groupByMineId = workerMineVOS.stream().collect(Collectors.groupingBy(WorkerMineVO::getMineId));
        log.info("矿机矿场ID列表:" + groupByMineId);

        int rows = workerDetailedMapper.updateById(list);
        if (rows == 0) {
            throw new DeleteException("矿机删除失败");
        }
        for (Map.Entry<Integer, List<WorkerMineVO>> entry : groupByMineId.entrySet()) {
            //同时需要记录到历史表中
            List<Integer> workerIdList = new ArrayList<>();
            //将该矿场下的矿机id提取到集合中
            List<WorkerMineVO> WorkerMineVOList = entry.getValue();
            for (WorkerMineVO workerMineVO : WorkerMineVOList) {
                Integer workerId = workerMineVO.getWorkerId();
                workerIdList.add(workerId);
            }
            //批量入库数据同步到缓存里
            redisToBatchDelete(rows, "worker_info", workerIdList, entry.getKey());
            redisToBatchDelete(rows, "worker_detailed", workerIdList, entry.getKey());
        }

    }

    /**
     * @return
     * @Description 用户网站的矿机详情列表
     * @Author zly
     * @Date 18:00 2020/3/10
     * @Param
     */
    public PageInfo<WorkerDetailedModel> selectAllWorkerDetailed(String workerName, String startIp,
                                                                 String endIp, Integer pageNum,
                                                                 Integer pageSize, String token) {
        Long startIpToLong = null;
        Long endIpToLong = null;
        if (!StringUtils.isEmpty(startIp)) {
            startIpToLong = getStringIpToLong(startIp);
        }
        if (!StringUtils.isEmpty(endIp)) {
            endIpToLong = getStringIpToLong(endIp);
        }
        Integer userId = null;
        HashMap<String, Object> tokenData = getTokenData(token);
        if (tokenData != null) {
            userId = Integer.valueOf(tokenData.get("userId").toString());
        } else {
            throw new CheckException("校验token失败!");
        }
        PageHelper.startPage(pageNum, pageSize);
        List<WorkerDetailedModel> workerDetailedModels = workerDetailedMapper.selectAllWorkerDetailed(workerName, startIpToLong, endIpToLong, userId);
        System.out.println("用户网站的矿机详情列表:" + workerDetailedModels);
        for (WorkerDetailedModel workerDetailedModel : workerDetailedModels) {
            String frameName = workerDetailedModel.getFrameName();
            String frameNumber = workerDetailedModel.getFrameNumber();
            StringBuffer frameDetailsBuffer = new StringBuffer(frameName).append(" ").append(frameNumber);
            workerDetailedModel.setFrameDetails(frameDetailsBuffer.toString());
            String workerNameStr = workerDetailedModel.getWorkerName();
            int lastIndexOf = workerNameStr.lastIndexOf(".");
            workerDetailedModel.setWorkerName(workerNameStr.substring(lastIndexOf + 1));
            if (workerDetailedModel.getOnTime() != null) {
                String onTimeStr = calculTime(Long.valueOf(workerDetailedModel.getOnTime()));
                workerDetailedModel.setOnTime(onTimeStr);
            }
            if (workerDetailedModel.getRunTime() != null) {
                String runTimeStr = calculTime(Long.valueOf(workerDetailedModel.getRunTime()));
                workerDetailedModel.setRunTime(runTimeStr);
            }
        }
        PageInfo<WorkerDetailedModel> pageInfo = new PageInfo<>(workerDetailedModels);
        return pageInfo;
    }

    /**
     * @return
     * @Description 用户网站的分组列表
     * @Author zly
     * @Date 18:54 2020/3/12
     * @Param
     */
    public HashMap<String, Object> selectGroupModel(String token, Integer pageNum, Integer pageSize) {
        //后面从token中获取
        Integer userId = null;
        JSONObject jsonObject = TokenUtil.verify(token);
        Integer success = jsonObject.getInteger("success");
        if (success == 200) {
            JSONObject data = jsonObject.getJSONObject("data");
            userId = data.getInteger("id");
        } else {
            throw new CheckException("token解析错误");
        }
        List<GroupModel> groupModels = workerDetailedMapper.selectGroupModel(userId);
        HashMap<String, String> ipQuJianMap = ipSettingService.selectIpQuJian();
        ArrayList<GroupModel> resultList = new ArrayList<>();
        //按分组名进行分组
        Map<String, List<GroupModel>> groupMap = groupModels.stream().collect(Collectors.groupingBy(GroupModel::getGroupName));
        //遍历每个分组下面的list集合
        for (Map.Entry<String, List<GroupModel>> entry : groupMap.entrySet()) {
            List<GroupModel> groupModelList = entry.getValue();
            GroupModel groupModel_tmp = new GroupModel();
            ArrayList<String> brandList = new ArrayList<>();
            int offLinesize = 0;
            //遍历这个分组下的所有的数据
            for (GroupModel groupModel : groupModelList) {
                String brandName = groupModel.getBrandName();
                //如果分组下面矿机品牌不为null,并且这个矿机品牌是新的,那就进行追加,否则就直接添加到该分组下
                if (groupModel_tmp.getBrandName() != null && !brandList.contains(brandName)) {
                    StringBuffer brandStringBuffer = new StringBuffer(groupModel_tmp.getBrandName()).append(",").append(brandName);
                    groupModel_tmp.setBrandName(brandStringBuffer.toString());
                } else {
                    groupModel_tmp.setBrandName(brandName);
                }
                //记录离线的数量
                if (groupModel.getState() == 0) {
                    offLinesize++;
                }
                //对这个分组下的所有的矿机ip进行区间分组,取前三组进行字符串匹配
                String workerIp = groupModel.getWorkerIp();
                String startIpStr = workerIp.substring(0, workerIp.lastIndexOf("."));
                String ipQuJianStr = ipQuJianMap.get(startIpStr);
                if (!StringUtils.isEmpty(groupModel_tmp.getIpQuJian())) {
                    StringBuffer ipQujianBuffer = new StringBuffer(groupModel_tmp.getIpQuJian()).append(",").append(ipQuJianStr);
                    groupModel_tmp.setIpQuJian(ipQujianBuffer.toString());
                } else {
                    groupModel_tmp.setIpQuJian(ipQuJianStr);
                }
                brandList.add(brandName);
            }
            groupModel_tmp.setGroupName(entry.getKey());
            groupModel_tmp.setTotal(groupModelList.size());
            groupModel_tmp.setOffLineSize(offLinesize);
            resultList.add(groupModel_tmp);
        }
        //进行分页
        HashMap<String, Object> startPage = PageUtil.startPage(resultList, pageNum, pageSize);
        return startPage;
    }

    @Override
    public HashMap<Integer, String> selectNullFrame(Integer frameId) {
        List<HashMap> mapList = workerDetailedMapper.selectNullFrame(frameId);
        HashMap<Integer, String> resultMap = new HashMap<>();
        mapList.forEach(hashMap -> {
            Integer id = Integer.valueOf(hashMap.get("id").toString());
            Integer frameNumber = Integer.valueOf(hashMap.get("frame_number").toString());
            resultMap.put(id,frameNumber+"层");
        });
        return resultMap;
    }

}






