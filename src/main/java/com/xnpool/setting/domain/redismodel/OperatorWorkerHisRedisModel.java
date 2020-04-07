package com.xnpool.setting.domain.redismodel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author zly
 * @version 1.0
 * @date 2020/3/5 13:32
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OperatorWorkerHisRedisModel implements Serializable {
    private static final long serialVersionUID = 2967646280373685039L;
    /**
     * 编号
     */
    private Integer id;

    /**
     * 矿场Id
     */
    private Integer mine_id;

    /**
     * 矿机表id
     */
    private Integer worker_id;

    /**
     * 出库时间
     */
    private String move_out_time;

    /**
     * 入库时间
     */
    private String come_in_time;

    /**
     * 出库原因
     */
    private String reason;

    /**
     * 出库员
     */
    private Integer operator_id;

}