package com.xnpool.setting.service.impl;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xnpool.logaop.service.exception.DataExistException;
import com.xnpool.setting.common.BaseController;
import com.xnpool.setting.domain.mapper.MineSettingMapper;
import com.xnpool.setting.domain.model.IpSettingExample;
import com.xnpool.setting.domain.pojo.IpParam;
import com.xnpool.setting.domain.pojo.IpSetting;
import com.xnpool.setting.domain.mapper.IpSettingMapper;
import com.xnpool.setting.domain.pojo.MineSetting;
import com.xnpool.setting.domain.redismodel.IpSettingRedisModel;
import com.xnpool.setting.service.IpSettingService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zly
 * @since 2020-04-27
 */
@Service
public class IpSettingServiceImpl extends ServiceImpl<IpSettingMapper, IpSetting> implements IpSettingService {

    @Autowired
    private IpSettingMapper ipSettingMapper;

    @Autowired
    private BaseController baseController;

    @Autowired
    private MineSettingMapper mineSettingMapper;

    @Autowired
    private IpSettingService ipSettingService;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer insertSelective(IpSetting record) {
        List<String> list = ipSettingMapper.selectNameList(record.getId());
        if (list.contains(record.getStartIp())) {
            throw new DataExistException("数据已存在,请勿重复添加!");
        }
        int rows = ipSettingMapper.insert(record);
        IpSettingRedisModel ipSettingRedisModel = baseController.getIpSettingRedisModel(record);
        baseController.redisToInsert(rows, "ip_setting", ipSettingRedisModel, record.getMineId());
        return rows;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateByPrimaryKeySelective(IpSetting record) {
        List<String> list = ipSettingMapper.selectNameList(record.getId());
        if (list.contains(record.getStartIp())) {
            throw new DataExistException("数据已存在,请勿重复添加!");
        }
        int rows = ipSettingMapper.updateById(record);
        IpSetting ipSetting = ipSettingMapper.selectById(record.getId());
        IpSettingRedisModel ipSettingRedisModel = baseController.getIpSettingRedisModel(ipSetting);
        baseController.redisToUpdate(rows, "ip_setting", ipSettingRedisModel, ipSetting.getMineId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer deleteById(int id) {
        Integer rows = ipSettingMapper.deleteByKeyId(id);
        IpSetting ipSetting = ipSettingMapper.selectById(id);
        IpSettingRedisModel ipSettingRedisModel = baseController.getIpSettingRedisModel(ipSetting);
        baseController.redisToDelete(rows, "ip_setting", ipSettingRedisModel, ipSetting.getMineId());
        return rows;
    }

    @Override
    public Page<IpSettingExample> selectByOther(String keyWord, int pageNum, int pageSize) {
        if (!StringUtils.isEmpty(keyWord)) {
            keyWord = "%" + keyWord + "%";
        }
        Page<IpSettingExample> page = new Page<>(pageNum, pageSize);
        List<IpSettingExample> ipSettings = ipSettingMapper.selectByOther(keyWord,null,page);
        page.setRecords(ipSettings);
        return page;
    }

    @Override
    public HashMap<Integer, String> selectByIPStart() {
        List<IpSettingExample> ipSettings = ipSettingMapper.selectByOther(null,null,null);
        if (ipSettings != null) {
            HashMap<Integer, String> resultMap = new HashMap<>();
            ipSettings.forEach(ipSetting -> {
                Integer id = ipSetting.getId();
                String startIp = ipSetting.getStartIp();
                String endIp = ipSetting.getEndIp();
                String ipName = startIp + "-" + endIp;
                resultMap.put(id, ipName);
            });
            return resultMap;
        } else {
            return null;
        }
    }

    @Override
    public HashMap<Integer, String> selectByIpStartByMineId(String mineName, Integer mineId) {
        List<IpSettingExample> ipSettings = ipSettingMapper.selectByOther(null,mineId,null);
        if (ipSettings != null) {
            HashMap<Integer, String> resultMap = new HashMap<>();
            ipSettings.forEach(ipSetting -> {
                Integer id = ipSetting.getId();
                String startIp = ipSetting.getStartIp();
                String endIp = ipSetting.getEndIp();
                String ipName = startIp + "-" + endIp;
                resultMap.put(id, ipName);
            });
            return resultMap;
        } else {
            return null;
        }
    }

    @Override
    public void batchSaveIp(IpParam ipParam) {
        Integer factoryNum = ipParam.getFactoryNum();
        Integer mineId = ipParam.getMineId();
        Integer factorySize = ipParam.getFactorySize();
        Integer frameSize = ipParam.getFrameSize();
        Integer ipbegin = ipParam.getIpbegin();
        Integer ipend = ipParam.getIpend();
        if (StringUtils.isEmpty(ipbegin)){
            ipbegin=1;
        }
        if (StringUtils.isEmpty(ipend)){
            ipend=110;
        }

        //拼接ip,封装成一个个完成的对象,然后
        //第一步,读取矿场所附带的ip前缀,+厂房递增+机架递增+ipbegin+ipend
        ArrayList<IpSetting> list = new ArrayList<>();
        MineSetting mineSetting = mineSettingMapper.selectById(mineId);
        //查看已经添加了的ip有哪些
        List<String> ipList = ipSettingMapper.selectIpByMineId(mineId);
        //从矿场表里面直接获取ip前缀
        String ipPrefix ="0";
        if (!StringUtils.isEmpty( mineSetting.getIpPrefix())){
            ipPrefix=String.valueOf(mineSetting.getIpPrefix());
        }

        int num=factoryNum-1;
        for (Integer i = 1; i <= factorySize; i++) {
            num = num + 1;
            for (Integer j = 1; j <= frameSize; j++) {
                IpSetting ipSetting = new IpSetting();

                StringBuffer startIp =new StringBuffer();
                StringBuffer endIp =new StringBuffer();
                if (factoryNum==0){
                    //如果厂房编号为0,那就代表从1开始
                    startIp = startIp.append(ipPrefix).append(".").append(i).append(".").append(j).append(".").append(ipbegin);
                    endIp = endIp.append(ipPrefix).append(".").append(i).append(".").append(j).append(".").append(ipend);
                }else {
                    //如果厂房编号为选中的数字,那就代表从选中的数字开始递增
                    startIp = startIp.append(ipPrefix).append(".").append(num).append(".").append(j).append(".").append(ipbegin);
                    endIp = endIp.append(ipPrefix).append(".").append(num).append(".").append(j).append(".").append(ipend);
                }
                ipSetting.setStartIp(startIp.toString());
                ipSetting.setEndIp(endIp.toString());
                ipSetting.setMineId(mineId);
                ipSetting.setCreateTime(LocalDateTime.now());
                ipSetting.setUpdateTime(LocalDateTime.now());
                ipSetting.setIsDelete(0);
                ipSetting.setIsToInt(false);
                ipSetting.setFactoryNum(factoryNum);
                if (!ipList.contains(startIp.toString())){
                    list.add(ipSetting);
                }
            }
        }

        //执行批量入库
        boolean batch = ipSettingService.saveBatch(list);
        //执行同步入缓存
        if (batch){
            baseController.batchPipelinedToRedis(1,"ip_setting",list,mineId);
        }
    }

    @Override
    public HashMap<String, String> selectIpQuJian() {
        List<IpSettingExample> ipSettings = ipSettingMapper.selectByOther(null,null,null);
        if (ipSettings != null) {
            HashMap<String, String> resultMap = new HashMap<>();
            ipSettings.forEach(ipSetting -> {
                String startIp = ipSetting.getStartIp();
                String endIp = ipSetting.getEndIp();
                String ipName = startIp + "-" + endIp;
                int lastIndexOf = startIp.lastIndexOf(".");
                String startIp_tmp = startIp.substring(0, lastIndexOf);
                resultMap.put(startIp_tmp, ipName);
            });
            return resultMap;
        } else {
            return null;
        }
    }
}
