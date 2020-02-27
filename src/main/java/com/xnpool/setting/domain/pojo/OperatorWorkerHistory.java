package com.xnpool.setting.domain.pojo;

import java.io.Serializable;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zly
 * @version 1.0
 * @date 2020/2/27 14:16
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OperatorWorkerHistory implements Serializable {
    /**
     * 编号
     */
    private Integer id;

    /**
     * 矿场Id
     */
    private Integer mineid;

    /**
     * 矿机表id
     */
    private Integer workerid;

    /**
     * 出库时间
     */
    private Date moveouttime;

    /**
     * 入库时间
     */
    private Date comeintime;

    /**
     * 出库原因
     */
    private String reason;

    /**
     * 出库员
     */
    private Integer operatorid;

    private static final long serialVersionUID = 1L;
}