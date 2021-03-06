package com.xnpool.setting.domain.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
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
public class ElectricityMeterSetting implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 电表名称
     */
    private String electricityMeterName;

    /**
     * 所属矿场
     */
    private Integer mineId;

    /**
     * 所属厂房
     */
    private Integer factoryId;

    /**
     * 初始码数
     */
    private String electricityStart;

    /**
     * 当前止码
     */
    private String nowCode;

    /**
     * 上次起码
     */
    private String lastCode;

    /**
     * 抄表时间
     */
    private LocalDateTime readingTime;

    /**
     * 线损
     */
    private Double lineLoss;

    /**
     * 倍率
     */
    private Integer multiple;

    /**
     * 是否删除,0:否,1:是
     */
    private Integer isDelete;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;

    /**
     * 企业id
     */
    private Long tenantId;


}
