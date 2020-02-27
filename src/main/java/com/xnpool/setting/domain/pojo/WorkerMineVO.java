package com.xnpool.setting.domain.pojo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author zly
 * @version 1.0
 * @date 2020/2/27 13:18
 */
@Data
public class WorkerMineVO implements Serializable {
    private static final long serialVersionUID = 3019280623626654125L;
    private Integer workerId;
    private Integer mineId;
}
