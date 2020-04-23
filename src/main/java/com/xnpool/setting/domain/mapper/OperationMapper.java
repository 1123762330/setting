package com.xnpool.setting.domain.mapper;

import com.baomidou.mybatisplus.annotation.SqlParser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xnpool.logaop.entity.Operation;

public interface OperationMapper extends BaseMapper<Operation> {
    @SqlParser(filter = true)
    void insertWinLog(Operation operation);
}
