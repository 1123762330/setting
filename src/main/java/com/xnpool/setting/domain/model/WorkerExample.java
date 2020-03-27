package com.xnpool.setting.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author  zly
 * @date  2020/2/18 12:02
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkerExample implements Serializable {
    private static final long serialVersionUID = 8596067628901141589L;
    /**
    * 矿机id
    */
    private Integer id;

    /**
    * IP
    */
    private String ip;



    /**
    * 所属IP段
    */
    private String ipQuJian;

    /**
    * 是否在线
    */
    private Integer state;

    /**
    * 是否入库
    */
    private Integer isComeIn;
}