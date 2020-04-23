package com.xnpool.setting.service.impl;

import com.xnpool.logaop.entity.Operation;
import com.xnpool.logaop.service.IOperationService;
import com.xnpool.setting.domain.mapper.OperationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OperationServiceImpl implements IOperationService {
    @Autowired
    OperationMapper operationMapper;
    @Override
    public void saveLog(Operation operation) {
        Long tenantId = operation.getTenantId();
        if(tenantId==null){//mybatis多租户自动注入
            operationMapper.insert(operation);
        }else {
            //用户端请求头tenantId注入
            operationMapper.insertWinLog(operation);
        }
    }
}
