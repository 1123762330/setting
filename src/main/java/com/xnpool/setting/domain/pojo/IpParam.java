package com.xnpool.setting.domain.pojo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author zly
 * @version 1.0
 * @date 2020/4/27 13:58
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class IpParam {
    /**
     * 矿场id
     */
    private Integer mineId;

    //厂房数量
    private Integer factorySize;

    //厂房编号
    private Integer factoryNum;

    //机架数量
    private Integer frameSize;

    //起始ip
    private Integer ipbegin;

    //终止ip
    private Integer ipend;
}
