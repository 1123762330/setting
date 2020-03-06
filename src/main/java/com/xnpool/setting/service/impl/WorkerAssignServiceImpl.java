package com.xnpool.setting.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xnpool.setting.common.BaseController;
import com.xnpool.setting.domain.pojo.MineFactoryAndFraneId;
import com.xnpool.setting.domain.pojo.PowerSetting;
import com.xnpool.setting.domain.pojo.UserRoleVO;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import com.xnpool.setting.domain.mapper.WorkerAssignMapper;
import com.xnpool.setting.domain.pojo.WorkerAssign;
import com.xnpool.setting.service.WorkerAssignService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author  zly
 * @date  2020/3/6 14:57
 * @version 1.0
 */
@Service
public class WorkerAssignServiceImpl extends BaseController implements WorkerAssignService {

    @Resource
    private WorkerAssignMapper workerAssignMapper;

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
        List<UserRoleVO> userRoleVOS = workerAssignMapper.selectByOther(keyWord);
        PageInfo<UserRoleVO> pageInfo = new PageInfo<>(userRoleVOS);
        return pageInfo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addAssignWorker(String ids,String token) {
        HashMap<Integer, List<MineFactoryAndFraneId>> resultMap = new HashMap<>();
        ArrayList<MineFactoryAndFraneId> list = new ArrayList<>();
        //从token中取出userid
        int userId=12;
        if (ids.contains(",")){
            //多个矿机架
            String[] split = ids.split(",");
            for (int i = 0; i < split.length; i++) {
                String id = split[i];
                String[] splitId = id.split("-");
                Integer frameId = Integer.valueOf(splitId[0]);
                Integer factoryId = Integer.valueOf(splitId[1]);
                Integer mineId = Integer.valueOf(splitId[2]);
                MineFactoryAndFraneId mineFactoryAndFraneId = new MineFactoryAndFraneId(userId,mineId,factoryId,frameId);
                list.add(mineFactoryAndFraneId);
                resultMap.put(mineId,list);
            }
        }else {
            //单个矿机架
            String[] splitId = ids.split("-");
            Integer frameId = Integer.valueOf(splitId[0]);
            Integer factoryId = Integer.valueOf(splitId[1]);
            Integer mineId = Integer.valueOf(splitId[2]);
            MineFactoryAndFraneId mineFactoryAndFraneId = new MineFactoryAndFraneId(userId,mineId,factoryId,frameId);
            list.add(mineFactoryAndFraneId);
            resultMap.put(mineId,list);
        }
        //执行保存功能
        int rows = workerAssignMapper.batchInsert(list);
        //同步入缓存
        for (Map.Entry<Integer, List<MineFactoryAndFraneId>> entry : resultMap.entrySet()) {
            redisToBatchInsert(rows, "worker_assign", list, entry.getKey());
        }
    }

}
