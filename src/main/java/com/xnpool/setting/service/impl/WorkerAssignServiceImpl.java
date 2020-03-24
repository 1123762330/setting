package com.xnpool.setting.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xnpool.logaop.service.exception.CheckException;
import com.xnpool.setting.common.BaseController;
import com.xnpool.setting.domain.pojo.MineFactoryAndFraneId;
import com.xnpool.setting.domain.pojo.UserRoleVO;
import com.xnpool.setting.service.FactoryHouseService;
import com.xnpool.setting.service.FrameSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import com.xnpool.setting.domain.mapper.WorkerAssignMapper;
import com.xnpool.setting.domain.pojo.WorkerAssign;
import com.xnpool.setting.service.WorkerAssignService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * @author  zly
 * @date  2020/3/6 14:57
 * @version 1.0
 */
@Service
public class WorkerAssignServiceImpl extends BaseController implements WorkerAssignService {

    @Resource
    private WorkerAssignMapper workerAssignMapper;

    @Autowired
    private FrameSettingService frameSettingService;

    @Autowired
    private FactoryHouseService factoryHouseService;

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
        List<UserRoleVO> userRoleVOS = workerAssignMapper.selectByOther();
        PageInfo<UserRoleVO> pageInfo = new PageInfo<>(userRoleVOS);
        return pageInfo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addAssignWorker(String ids,String token) {
        HashMap<Integer, List<MineFactoryAndFraneId>> resultMap = new HashMap<>();
        ArrayList<MineFactoryAndFraneId> list = new ArrayList<>();
        //从token中取出userid
        HashMap<String, Object> tokenData = getTokenData(token);
        Integer userId=null;
        if (tokenData!=null){
             userId = Integer.valueOf(tokenData.get("userId").toString());
        }else {
            throw new CheckException("校验token失败!");
        }
        if (ids.contains(",")){
            //多个矿机架
            String[] split = ids.split(",");
            for (int i = 0; i < split.length; i++) {
                String id = split[i];
                String[] splitId = id.split("-");
                //做三层判断,第一是否是矿场Id,第二,是否是厂房id,第三,是否是直接的机架id
                if (!id.contains("-")){
                    //直接是矿场ID
                    Integer mineId = Integer.valueOf(id);
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
                            MineFactoryAndFraneId mineFactoryAndFraneId = new MineFactoryAndFraneId(userId,mineId,factoryId,frameId);
                            list.add(mineFactoryAndFraneId);
                        }
                    }
                    resultMap.put(Integer.valueOf(ids),list);
                }else if(splitId.length==2){
                    //第二种,矿场ID-厂房ID
                    Integer mineId = Integer.valueOf(splitId[0]);
                    Integer factoryId = Integer.valueOf(splitId[1]);
                    HashMap<Integer, String> frameNameMap = frameSettingService.selectFrameNameByFactoryId(factoryId);
                    Set<Integer> keySet = frameNameMap.keySet();
                    for (Integer frameId : keySet) {
                        MineFactoryAndFraneId mineFactoryAndFraneId = new MineFactoryAndFraneId(userId,mineId,factoryId,frameId);
                        list.add(mineFactoryAndFraneId);
                    }
                    resultMap.put(mineId,list);
                }else {
                    //第三种,矿场ID-厂房ID-机架ID
                    Integer mineId = Integer.valueOf(splitId[0]);
                    Integer factoryId = Integer.valueOf(splitId[1]);
                    Integer frameId = Integer.valueOf(splitId[2]);
                    MineFactoryAndFraneId mineFactoryAndFraneId = new MineFactoryAndFraneId(userId,mineId,factoryId,frameId);
                    list.add(mineFactoryAndFraneId);
                    resultMap.put(mineId,list);
                }
            }
        }else {
            //单个矿机架
            //做三层判断,第一是否是矿场Id,第二,是否是厂房id,第三,是否是直接的机架id
            String[] splitId = ids.split("-");
            if (!ids.contains("-")){
                //直接是矿场ID
                Integer mineId = Integer.valueOf(ids);
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
                        MineFactoryAndFraneId mineFactoryAndFraneId = new MineFactoryAndFraneId(userId,mineId,factoryId,frameId);
                        list.add(mineFactoryAndFraneId);
                    }
                }
                resultMap.put(Integer.valueOf(ids),list);
            }else if(splitId.length==2){
                //第二种,矿场ID-厂房ID
                Integer mineId = Integer.valueOf(splitId[0]);
                Integer factoryId = Integer.valueOf(splitId[1]);
                HashMap<Integer, String> frameNameMap = frameSettingService.selectFrameNameByFactoryId(factoryId);
                Set<Integer> keySet = frameNameMap.keySet();
                for (Integer frameId : keySet) {
                    MineFactoryAndFraneId mineFactoryAndFraneId = new MineFactoryAndFraneId(userId,mineId,factoryId,frameId);
                    list.add(mineFactoryAndFraneId);
                }
                resultMap.put(mineId,list);
            }else {
                //第三种,矿场ID-厂房ID-机架ID
                Integer mineId = Integer.valueOf(splitId[0]);
                Integer factoryId = Integer.valueOf(splitId[1]);
                Integer frameId = Integer.valueOf(splitId[2]);
                MineFactoryAndFraneId mineFactoryAndFraneId = new MineFactoryAndFraneId(userId,mineId,factoryId,frameId);
                list.add(mineFactoryAndFraneId);
                resultMap.put(mineId,list);
            }

        }
        //执行保存功能
        int rows = workerAssignMapper.batchInsert(list);
        //同步入缓存
        for (Map.Entry<Integer, List<MineFactoryAndFraneId>> entry : resultMap.entrySet()) {
            redisToBatchInsert(rows, "worker_assign", list, entry.getKey());
        }
    }

    /**
     * @Description 判断添加的矿机架是否已经分配
     * @Author zly
     * @Date 21:04 2020/3/24
     * @Param
     * @return
     */
    @Override
    public List<Integer> selectWorkerAssign() {
        return workerAssignMapper.selectWorkerAssign();
    }

}
