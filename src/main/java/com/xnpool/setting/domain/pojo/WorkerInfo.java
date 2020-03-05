package com.xnpool.setting.domain.pojo;

import java.io.Serializable;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author  zly
 * @date  2020/3/5 14:28
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkerInfo implements Serializable {
    private Long id;

    private String ip;

    /**
    * 状态
    */
    private String state;

    /**
    * 矿机类型
    */
    private String mineType;

    /**
    * 工作模式
    */
    private String workingMode;

    /**
    * 实时算例
    */
    private String curHashrate;

    /**
    * 平均算例
    */
    private String avgHashrate;

    /**
    * 温度
    */
    private String temperature;

    /**
    * 风扇转速
    */
    private String fanSpeed;

    /**
    * 启动时间
    */
    private String startupTime;

    /**
    * 矿池1
    */
    private String pool1;

    /**
    * 矿机名1
    */
    private String worker1;

    private String pool2;

    private String worker2;

    private String pool3;

    private String worker3;

    /**
    * 固件版本
    */
    private String firmwareVersion;

    /**
    * 软件版本
    */
    private String softwareVersion;

    /**
    * 硬件版本
    */
    private String hardwareVersion;

    /**
    * 网络类型
    */
    private String networkType;

    /**
    * mac地址
    */
    private String mac;

    /**
    * 企业ID
    */
    private Integer tenantId;

    /**
    * 矿厂ID
    */
    private Long mineId;

    /**
    * 时间戳
    */
    private Long ts;

    private Date createTime;

    private Date updateTime;

    private static final long serialVersionUID = 1L;
}