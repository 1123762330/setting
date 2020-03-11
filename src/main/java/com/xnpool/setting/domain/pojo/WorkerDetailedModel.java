package com.xnpool.setting.domain.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 用户网站的矿机详情
 * @author zly
 * @version 1.0
 * @date 2020/3/10 16:48
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkerDetailedModel implements Serializable {
    private static final long serialVersionUID = 3577630959789824608L;
    //分组名
    private String groupName;
    //ip
    private String ip;
    //矿机架名
    private String frameName;
    //位置层数
    private String frameNumber;
    //机位详情
    private String frameDetails;
    //机器名
    private String workerName;
    //矿机型号
    private String mineType;
    //mac地址
    private String mac;
    //算法
    private String algorithm;
    //状态
    private Integer state;
    //错误码
    private String errorCode;
    //开机时间
    private String onTime;
    //挖矿时间
    private String runTime;
    //平均算力
    private String avgHashrate;
    //功耗比
    private String powerDissipation;
    //功耗
    private String powerWaste;
}
