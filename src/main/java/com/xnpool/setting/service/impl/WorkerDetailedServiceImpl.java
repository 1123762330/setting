package com.xnpool.setting.service.impl;

import java.util.Date;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xnpool.logaop.service.exception.DeleteException;
import com.xnpool.logaop.service.exception.InsertException;
import com.xnpool.logaop.service.exception.UpdateException;
import com.xnpool.setting.common.BaseController;
import com.xnpool.setting.config.ApiContext;
import com.xnpool.setting.domain.mapper.OperatorWorkerHistoryMapper;
import com.xnpool.setting.domain.mapper.WorkerInfoMapper;
import com.xnpool.setting.domain.model.GroupModel;
import com.xnpool.setting.domain.model.WorkerDetailedExample;
import com.xnpool.setting.domain.model.WorkerDetailedModel;
import com.xnpool.setting.domain.model.WorkerExample;
import com.xnpool.setting.domain.pojo.*;
import com.xnpool.setting.domain.redismodel.WorkerDetailedRedisModel;
import com.xnpool.setting.service.*;
import com.xnpool.setting.utils.PageUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import com.xnpool.setting.domain.mapper.WorkerDetailedMapper;
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

    @Autowired
    private WorkerbrandSettingService workerbrandSettingService;

    @Autowired
    private FactoryHouseService factoryHouseService;

    @Autowired
    private FrameSettingService frameSettingService;

    @Autowired
    private OperatorWorkerHistoryMapper operatorWorkerHistoryMapper;

    @Autowired
    private MineSettingService mineSettingService;

    @Autowired
    private GroupSettingService groupSettingService;

    @Autowired
    private CustomerSettingService customerSettingService;

    @Autowired
    private ApiContext apiContext;

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
    public PageInfo<WorkerDetailedExample> selectMoveOutList(String keyWord, int pageNum, int pageSize, String token) {
        if (!StringUtils.isEmpty(keyWord)) {
            keyWord = "%" + keyWord + "%";
        }
        Long tenantId = getTenantId(token);
        PageHelper.startPage(pageNum, pageSize);

        List<WorkerDetailedExample> WorkerDetailedExampleList = workerDetailedMapper.selectMoveOutList(keyWord, tenantId);
        if (!WorkerDetailedExampleList.isEmpty()) {
            HashMap<Integer, String> groupMap = groupSettingService.selectGroupMap();
            HashMap<Integer, String> userListMap = customerSettingService.selectUserList();
            //System.out.println("查询的矿机出库列表是:" + WorkerDetailedExampleList);
            WorkerDetailedExampleList.forEach(workerDetailedExample -> {
                String workerName = workerDetailedExample.getWorkerName();
                Integer groupId = workerDetailedExample.getGroupId();
                if (groupId != null) {
                    String groupName = groupMap.get(groupId);
                    workerDetailedExample.setGroupName(groupName);
                }

                Integer userId = workerDetailedExample.getUserId();
                if (userId != null) {
                    String userName = userListMap.get(userId);
                    workerDetailedExample.setUsername(userName);
                }
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
        }
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
    public void addWorkerToLibrary(WorkerDetailedParam workerDetailedParam, String token) {
        //后期从token中获取用户Id
        Integer operatorId = getUserId(token);
        Integer id = workerDetailedParam.getId();
        Integer mineId = workerDetailedParam.getMineId();
        Integer userId = workerDetailedParam.getUserId();
        Integer workerbrandId = workerDetailedParam.getWorkerbrandId();
        Integer groupId = workerDetailedParam.getGroupId();
        String workerIp = workerDetailedParam.getWorkerIp();
        String remarks = workerDetailedParam.getRemarks();
        Integer workerIds = workerInfoMapper.selectWorkerIdByIp(workerIp, mineId);
        List<Integer> workerIdList = workerDetailedMapper.selectWorkerIdlist(1);

        if (workerIdList.contains(workerIds)) {
            throw new InsertException("该矿机已经添加过!");
        } else {
            WorkerDetailed workerDetailed = new WorkerDetailed();
            workerDetailed.setId(id);
            workerDetailed.setWorkerId(workerIds);
            workerDetailed.setWorkerIp(workerIp);
            workerDetailed.setUserId(userId);
            workerDetailed.setGroupId(groupId);
            workerDetailed.setUpdateTime(new Date());
            workerDetailed.setWorkerIp(workerIp);
            workerDetailed.setWorkerbrandId(workerbrandId);
            workerDetailed.setRemarks(remarks);


            OperatorWorkerHistory operatorWorkerHistory = new OperatorWorkerHistory();
            operatorWorkerHistory.setMineId(mineId);
            operatorWorkerHistory.setWorkerId(workerIds);
            operatorWorkerHistory.setMoveOutTime(new Date());
            operatorWorkerHistory.setComeInTime(new Date());
            operatorWorkerHistory.setReason("");
            operatorWorkerHistory.setOperatorId(operatorId);

            //批量入管理仓库
            int rows = workerDetailedMapper.update(workerDetailed);
            WorkerDetailed workerDetailed_db = workerDetailedMapper.selectByPrimaryKey(id);
            WorkerDetailedRedisModel workerDetailedRedisModel = getWorkerDetailedRedisModel(workerDetailed_db);
            redisToUpdate(rows, "worker_detailed", workerDetailedRedisModel, mineId);

            //记录到操作历史表中
            int rows2 = operatorWorkerHistoryMapper.insertSelective(operatorWorkerHistory);
        }
    }

    //入库列表
    @Override
    public HashMap<String, Object> selectComeInWorkerList(String workerType, Integer state, String ip, int pageNum, int pageSize,String token) {
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
        List<WorkerInfo> workers = workerInfoMapper.selectByOther(workerType, state, ip);
        //已经入库的矿机Id
        List<Integer> comeInlist = workerDetailedMapper.selectWorkerIdlist(1);
        //List<Integer>转List<Long>
        ArrayList<Long> comeInlist2 = new ArrayList<>();
        if (!comeInlist.isEmpty()) {
            for (Integer integer : comeInlist) {
                Long workerId = Long.valueOf(integer.toString());
                comeInlist2.add(workerId);
            }
        }
        HashMap<Integer, String> mineNameMap = mineSettingService.selectMineNameByOther(token);
        //遍历list集合,setIP所属区间进去
        for (WorkerInfo worker : workers) {
            int lastIndexOf = worker.getIp().lastIndexOf(".");
            String substring = worker.getIp().substring(0, lastIndexOf);
            String ip_quJian = ipMap.get(substring);
            String mineType = worker.getMineType();
            String worker1 = worker.getWorker1();
            String avgHashrate = worker.getAvgHashrate();
            String curHashrate = worker.getCurHashrate();
            Integer mineId = Integer.valueOf(worker.getMineId().toString());
            //这里需要做个判断,判断这个矿机有没有入库,如果入库列表里有那就是1,如果没有就是0
            //过滤已经上架的机器
            if (!comeInlist2.contains(worker.getId())) {
                WorkerExample workerExample = new WorkerExample();
                workerExample.setId(Integer.valueOf(worker.getId().toString()));
                workerExample.setIp(worker.getIp());
                workerExample.setIpQuJian(ip_quJian);
                workerExample.setState(Integer.valueOf(worker.getState()));
                workerExample.setWorker1(worker1);
                workerExample.setMineType(mineType);
                workerExample.setCurHashrate(curHashrate);
                workerExample.setAvgHashrate(avgHashrate);
                workerExample.setIsComeIn(0);
                String mineName = mineNameMap.get(mineId);
                workerExample.setMineName(mineName);
                result.add(workerExample);
            }
        }
        //过滤已经上架的机器
        //List<WorkerExample> filterList = result.stream().filter(a -> a.getIsComeIn().equals("0")).collect(Collectors.toList());
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
        //同时需要记录到历史表中
        List<Integer> workerIdList = new ArrayList<>();
        int rows = workerDetailedMapper.updateMoveOutByid(list);
        List<WorkerDetailedRedisModel> redisModels = new ArrayList<>();
        List<WorkerDetailed> workerDetaileds = workerDetailedMapper.selectWorkerDetailedList(list);
        for (WorkerDetailed workerDetailed : workerDetaileds) {
            WorkerDetailedRedisModel workerDetailedRedisModel = getWorkerDetailedRedisModel(workerDetailed);
            redisToUpdate(rows, "worker_detailed", workerDetailedRedisModel, workerDetailed.getMineId());
            workerIdList.add(workerDetailed.getWorkerId());
        }

        List<OperatorWorkerHistory> operatorWorkerHistories = operatorWorkerHistoryService.selectOperatorWorkerId(workerIdList);
        int rows2 = operatorWorkerHistoryService.updateMoveOutTimeById(workerIdList);
    }

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
        int rows = workerDetailedMapper.updateById(list);
        if (rows == 0) {
            throw new DeleteException("矿机删除失败");
        }else {
            //批量入库数据同步到缓存里
            List<WorkerDetailed> workerDetaileds = workerDetailedMapper.selectWorkerDetailedList(list);
            for (WorkerDetailed workerDetailed : workerDetaileds) {
                WorkerDetailedRedisModel workerDetailedRedisModel = getWorkerDetailedRedisModel(workerDetailed);
                redisToUpdate(rows, "worker_detailed", workerDetailedRedisModel, workerDetailed.getMineId());
            }

        }
    }

    /**
     * @return
     * @Description 用户网站的矿机详情列表
     * @Author zly
     * @Date 18:00 2020/3/10
     * @Param
     */
    public PageInfo<WorkerDetailedModel> selectAllWorkerDetailed(String onLine, String offLine, String workerName, String startIp,
                                                                 String endIp, Integer pageNum,
                                                                 Integer pageSize, String token, Long tenantId) {
        Long startIpToLong = null;
        Long endIpToLong = null;
        if (!StringUtils.isEmpty(startIp)) {
            startIpToLong = getStringIpToLong(startIp);
        }
        if (!StringUtils.isEmpty(endIp)) {
            endIpToLong = getStringIpToLong(endIp);
        }
        Integer userId = getUserId(token);
        PageHelper.startPage(pageNum, pageSize);
        List<WorkerDetailedModel> workerDetailedModels = workerDetailedMapper.selectAllWorkerDetailed(startIpToLong, endIpToLong, userId, tenantId);
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
                Long runTime = Long.valueOf(workerDetailedModel.getRunTime());
                long runTotal = new Date().getTime() / 1000 - runTime;
                String onTimeStr = calculTime(runTotal);
                workerDetailedModel.setRunTime(onTimeStr);
            }
        }
        List<WorkerDetailedModel> filterList = workerDetailedModels;

        //在在线列表里面过滤三个条件
        if ("1".equals(onLine) && "0".equals(offLine)) {
            filterList = filterList.stream().filter(a -> a.getState() == 1).collect(Collectors.toList());
            //过滤矿机名
            filterList = getFilterList(workerName, startIp, endIp, filterList);
        } else if ("0".equals(onLine) && "1".equals(offLine)) {
            //在离线列表里面过滤三个条件
            filterList = workerDetailedModels.stream().filter(a -> a.getState() != 1).collect(Collectors.toList());
            //过滤矿机名
            filterList = getFilterList(workerName, startIp, endIp, filterList);
        } else {
            //在全部列表过滤三个条件
            filterList = getFilterList(workerName, startIp, endIp, filterList);
        }
        PageInfo<WorkerDetailedModel> pageInfo = new PageInfo<>(filterList);
        return pageInfo;
    }

    //用户网站矿机详情条件过滤
    private List<WorkerDetailedModel> getFilterList(String workerName, String startIp, String endIp, List<WorkerDetailedModel> filterList) {
        if (!StringUtils.isEmpty(workerName)) {
            filterList = filterList.stream().filter(a -> a.getWorkerName().contains(workerName)).collect(Collectors.toList());
            //if (!StringUtils.isEmpty(startIp)) {
            //    filterList = filterList.stream().filter(a -> a.getIp().contains(startIp)).collect(Collectors.toList());
            //}
            //if (!StringUtils.isEmpty(endIp)) {
            //    filterList = filterList.stream().filter(a -> a.getIp().contains(endIp)).collect(Collectors.toList());
            //}
        }
        //if (!StringUtils.isEmpty(startIp)) {
        //    filterList = filterList.stream().filter(a -> a.getIp().contains(startIp)).collect(Collectors.toList());
        //    if (!StringUtils.isEmpty(endIp)) {
        //        filterList = filterList.stream().filter(a -> a.getIp().contains(endIp)).collect(Collectors.toList());
        //    }
        //}
        //if (!StringUtils.isEmpty(endIp)) {
        //    filterList = filterList.stream().filter(a -> a.getIp().contains(endIp)).collect(Collectors.toList());
        //}
        return filterList;
    }

    /**
     * @return
     * @Description 用户网站的分组列表
     * @Author zly
     * @Date 18:54 2020/3/12
     * @Param
     */
    public HashMap<String, Object> selectGroupModel(String groupName, String startIp, String endIp, String token, Integer pageNum, Integer pageSize, Long tenantId) {
        //后面从token中获取
        Integer userId = getUserId(token);
        List<GroupModel> groupModels = workerDetailedMapper.selectGroupModel(userId, tenantId);
        HashMap<String, String> ipQuJianMap = ipSettingService.selectIpQuJian();
        ArrayList<GroupModel> resultList = new ArrayList<>();
        //按分组名进行分组
        Map<String, List<GroupModel>> groupMap = groupModels.stream().collect(Collectors.groupingBy(GroupModel::getGroupName));
        //遍历每个分组下面的list集合
        for (Map.Entry<String, List<GroupModel>> entry : groupMap.entrySet()) {
            List<GroupModel> groupModelList = entry.getValue();
            GroupModel groupModel_tmp = new GroupModel();
            HashSet<String> ipQuJianset = new HashSet<>();
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
                if (groupModel.getState() == 0 || groupModel.getState() == 2) {
                    offLinesize++;
                }
                //对这个分组下的所有的矿机ip进行区间分组,取前三组进行字符串匹配
                String workerIp = groupModel.getWorkerIp();
                String startIpStr = workerIp.substring(0, workerIp.lastIndexOf("."));
                String ipQuJianStr = ipQuJianMap.get(startIpStr);
                if (!StringUtils.isEmpty(ipQuJianStr)) {
                    ipQuJianset.add(ipQuJianStr);
                }
                brandList.add(brandName);
            }
            //将set集合转成字符串
            String ipQuJiansetStr = "";
            if (!ipQuJianset.isEmpty()) {
                ipQuJiansetStr = ipQuJianset.toString().substring(1, ipQuJianset.toString().length() - 1);
            }
            groupModel_tmp.setIpQuJian(ipQuJiansetStr);
            groupModel_tmp.setGroupName(entry.getKey());
            groupModel_tmp.setTotal(groupModelList.size());
            groupModel_tmp.setOffLineSize(offLinesize);
            resultList.add(groupModel_tmp);
        }
        List<GroupModel> filterList = resultList;
        //过滤矿机名
        if (!StringUtils.isEmpty(groupName)) {
            filterList = filterList.stream().filter(a -> a.getGroupName().contains(groupName)).collect(Collectors.toList());
            if (!StringUtils.isEmpty(startIp)) {
                filterList = filterList.stream().filter(a -> a.getIpQuJian().contains(startIp)).collect(Collectors.toList());
                if (!StringUtils.isEmpty(endIp)) {
                    filterList = filterList.stream().filter(a -> a.getIpQuJian().contains(endIp)).collect(Collectors.toList());
                }
            }
        }
        if (!StringUtils.isEmpty(startIp)) {
            filterList = filterList.stream().filter(a -> a.getIpQuJian().contains(startIp)).collect(Collectors.toList());
            if (!StringUtils.isEmpty(endIp)) {
                filterList = filterList.stream().filter(a -> a.getIpQuJian().contains(endIp)).collect(Collectors.toList());
            }
        }
        if (!StringUtils.isEmpty(endIp)) {
            filterList = filterList.stream().filter(a -> a.getIpQuJian().contains(endIp)).collect(Collectors.toList());
        }
        //进行分页
        HashMap<String, Object> startPage = PageUtil.startPage(filterList, pageNum, pageSize);
        return startPage;
    }

    @Override
    public HashMap<Integer, String> selectNullFrame(Integer frameId) {
        List<HashMap> mapList = workerDetailedMapper.selectNullFrame(frameId);
        HashMap<Integer, String> resultMap = new HashMap<>();
        mapList.forEach(hashMap -> {
            Integer id = Integer.valueOf(hashMap.get("id").toString());
            Integer frameNumber = Integer.valueOf(hashMap.get("frame_number").toString());
            resultMap.put(id, frameNumber + "层");
        });
        return resultMap;
    }

    /**
     * @return
     * @Description 全部上架
     * 1.读取worker_Info表,拿到所有的矿机ip,然后根据前端提交过来的批量矿机ip,分配给的用户id,还有分组以及矿机品牌
     * 2.按照规则切割ip,IP规则:171.3(厂房号).5(货架号).23(二层第三台)
     * 3.首先去判断有没有这个厂房名,有的话直接取厂房id,没有的话直接新建厂房名,然后返回厂房id
     * 4.再去判断有没有这个货架号,因为同名货架有多个,所以把第几台拼接到货架号上,就相当于去判断有没有53这个名字的机架
     * 5.如果有,则直接取机架ID,如果没有,则创建名字叫53的这个机架,同时在worker_detailed创建位置信息,然后取机架ID
     * 6.最后取层数,23取2,最终将上面这零散的信息拼成一个完整的对象,封装进集合,执行批量入库
     * @Author zly
     * @Date 16:08 2020/4/4
     * @Param
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchIntoFrame(String token) {
        Integer operatorId = getUserId(token);
        List<Integer> workerIdList = workerDetailedMapper.selectWorkerIdlist(1);
        List<WorkerInfo> workers = workerInfoMapper.selectByOther(null, null, null);
        System.out.println("workers===" + workers.size());
        //批量入数据库的集合
        ArrayList<WorkerDetailed> list = new ArrayList<>();
        //历史记录表操作
        ArrayList<OperatorWorkerHistory> operatorWorkerList = new ArrayList<>();
        //矿机品牌集合
        HashMap<String, Integer> workerTypeMap = workerbrandSettingService.selectMapByWorkerType();
        long start = System.currentTimeMillis();
        System.out.println("开始时间:" + start);
        System.out.println("此时的企业id是:"+apiContext.getTenantId());
        for (WorkerInfo worker : workers) {
            Integer mineId = Integer.valueOf(worker.getMineId().toString());
            String ip = worker.getIp();
            Integer workerId = Integer.valueOf(worker.getId().toString());
            if (!workerIdList.contains(workerId)) {
                if (!StringUtils.isEmpty(ip)) {
                    WorkerDetailed workerDetailed = new WorkerDetailed();
                    //如果ip不为空
                    String[] split = ip.split("\\.");
                    String factoryStr = split[1];
                    String frameStr = split[2];
                    String placeStr = split[3];
                    if (Integer.valueOf(placeStr) > 10 && Integer.valueOf(placeStr) < 111) {
                        Integer num = Integer.valueOf(placeStr) % 10;
                        String paiNumber = "";
                        String Number = "";
                        if (num == 0) {
                            //这是第10排的
                            Number = String.valueOf((Integer.valueOf(placeStr) / 10) - 1);
                            paiNumber = "10";
                        } else {
                            Number = placeStr.substring(0, placeStr.length() - 1);
                            paiNumber = placeStr.substring(placeStr.length() - 1);
                        }
                        long fstart = System.currentTimeMillis();
                        frameStr = frameStr + "#" + paiNumber;
                        Integer factoryId = factoryHouseService.equalsFactoryName(factoryStr, mineId);
                        if (factoryId == null) {
                            FactoryHouse factoryHouse = new FactoryHouse();
                            factoryHouse.setFactoryName(factoryStr);
                            factoryHouse.setMineId(mineId);
                            factoryId = factoryHouseService.insertSelectiveToBatch(factoryHouse);
                        }
                        long fend = System.currentTimeMillis();
                        System.out.println("厂房新增查询耗时:" + (fend - fstart));
                        Integer frameId = frameSettingService.equalsFrameName(frameStr, factoryId, mineId);
                        if (frameId == null) {
                            FrameSetting frameSetting = new FrameSetting();
                            frameSetting.setFrameName(frameStr);
                            frameSetting.setFactoryId(factoryId);
                            frameSetting.setMineId(mineId);
                            frameSetting.setNumber(10);
                            frameId = frameSettingService.insertSelectiveToBatch(frameSetting);
                            System.out.println("新增的矿机架ID" + frameId);
                            System.out.println("此时的企业id是:"+apiContext.getTenantId());
                            workerDetailed.setWorkerId(workerId);
                            workerDetailed.setFactoryId(factoryId);
                            //获取品牌id
                            String mineType = worker.getMineType();
                            Integer workerbrandId = workerTypeMap.get(mineType);
                            workerDetailed.setWorkerbrandId(workerbrandId);
                            workerDetailed.setFrameId(frameId);
                            workerDetailed.setFrameNumber(Integer.valueOf(Number));
                            workerDetailed.setMineId(mineId);
                            workerDetailed.setIsComeIn(1);
                            workerDetailed.setIsDelete(0);
                            workerDetailed.setCreateTime(new Date());
                            workerDetailed.setUpdateTime(new Date());
                            workerDetailed.setWorkerIp(ip);
                            list.add(workerDetailed);
                        } else {
                            workerDetailed.setWorkerId(workerId);
                            workerDetailed.setFactoryId(factoryId);
                            //获取品牌id
                            String mineType = worker.getMineType();
                            Integer workerbrandId = workerTypeMap.get(mineType);
                            workerDetailed.setWorkerbrandId(workerbrandId);
                            workerDetailed.setFrameId(frameId);
                            workerDetailed.setFrameNumber(Integer.valueOf(Number));
                            workerDetailed.setMineId(mineId);
                            workerDetailed.setIsComeIn(1);
                            workerDetailed.setIsDelete(0);
                            workerDetailed.setCreateTime(new Date());
                            workerDetailed.setUpdateTime(new Date());
                            workerDetailed.setWorkerIp(ip);
                            list.add(workerDetailed);
                        }
                        long frend = System.currentTimeMillis();
                        System.out.println("机架新增耗时:" + (frend - fend));
                        OperatorWorkerHistory operatorWorkerHistory = new OperatorWorkerHistory(null, mineId, workerId, null, new Date(), null, operatorId);
                        operatorWorkerList.add(operatorWorkerHistory);
                    }

                }
            }
        }
        System.out.println("此时的企业id是:"+apiContext.getTenantId());
        long wstart = System.currentTimeMillis();
        if (!list.isEmpty()&&list.size()!=0) {
            //批量上架
            log.info("需要批量上架的条数:"+list.size());
            Integer rows = workerDetailedMapper.updateBatch(list);
            log.info("批量上架成功条数:"+rows);
            //查询入缓存的数据
            List<WorkerDetailed> workerDetailedRedisModels = workerDetailedMapper.selectModelToRedis(list);
            for (WorkerDetailed workerDetaile : workerDetailedRedisModels) {
                WorkerDetailedRedisModel workerDetailedRedisModel = getWorkerDetailedRedisModel(workerDetaile);
                redisToUpdate(rows, "worker_detailed", workerDetailedRedisModel, workerDetaile.getMineId());
            }
        }
        long wend = System.currentTimeMillis();
        System.out.println("入详情表耗时:" + (wend - wstart));
        if (!operatorWorkerList.isEmpty()&&operatorWorkerList.size()!=0) {
            int rows = operatorWorkerHistoryMapper.insertTobatch(operatorWorkerList);
            log.info("批量记录上架条数:"+rows);
        }
        long oend = System.currentTimeMillis();
        System.out.println("历史操作耗时:" + (oend - wend));
        System.out.println("总耗时:" + (System.currentTimeMillis() - start));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchUpdateToUser(String ids, Integer userId, Integer groupId) {
        ArrayList<Integer> list = new ArrayList<>();
        if (ids.contains(",")) {
            //多个
            String[] split = ids.split(",");
            for (int i = 0; i < split.length; i++) {
                list.add(Integer.valueOf(split[i]));
            }
        } else {
            //单条
            list.add(Integer.valueOf(ids));
        }
        int rows = workerDetailedMapper.batchUpdateToUser(list, userId, groupId);
        for (Integer id : list) {
            WorkerDetailed workerDetailed = workerDetailedMapper.selectByPrimaryKey(id);
            WorkerDetailedRedisModel workerDetailedRedisModel = getWorkerDetailedRedisModel(workerDetailed);
            redisToUpdate(rows, "worker_detailed", workerDetailedRedisModel, workerDetailed.getMineId());
        }
    }

}






