package com.xnpool.setting.domain.pojo;

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
public class MeterReviewParam implements Serializable {

    /**
     * 电表id
     */
    private Integer electricityId;

    /**
     * 止码
     */
    private Double end;

    /**
     * 拍照图片路径
     */
    private String path;

    /**
     * 抄表时间
     */
    private LocalDateTime createTime;



}
