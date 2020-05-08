package com.xnpool.setting.domain.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

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
public class MeterReview implements Serializable {


    private static final long serialVersionUID = -3319744091256678449L;
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 电表id
     */
    private Integer electricityId;

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
     * 修改时间
     */
    private LocalDateTime updateTime;

    /**
     * 是否删除,0:否,1:是
     */
    private Integer isDelete;

    /**
     * 企业id
     */
    private Long tenantId;


}
