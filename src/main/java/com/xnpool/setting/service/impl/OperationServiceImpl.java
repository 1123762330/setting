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
        operationMapper.insert(operation);
    }
}
