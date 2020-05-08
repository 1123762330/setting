package com.xnpool.setting.domain.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author zly
 * @since 2020-05-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("z_meter_review")
public class MeterReviewExample implements Serializable {

    private static final long serialVersionUID = -8581276902265533401L;
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 电表id
     */
    private Integer electricityId;

    /**
     * 电表名称
     */
    private String electricityMeterName;

    /**
     * 起码
     */
    private Double start;

    /**
     * 止码
     */
    private Double end;

    /**
     * 用电量
     */
    private Double usePower;

    /**
     * 拍照图片路径
     */
    private String path;

    /**
     * 抄表时间
     */
    private LocalDateTime createTime;

    /**
     * 厂房名称
     */
    private String factoryName;


    /**
     * 倍率
     */
    private Integer multiple;


}
