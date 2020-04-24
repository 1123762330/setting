package com.xnpool.setting.domain.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author  zly
 * @date  2020/2/23 12:52
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MoveOutParam implements Serializable {

    private static final long serialVersionUID = -5361526784047783155L;
    /**
     * ip区间过滤
     */
    private String ipStr="";

    /**
     * 矿场过滤
     */
    private String mineName="";

    /**
     * 厂房过滤
     */
    private String factoryName="";

    /**
     * 机架过滤
     */
    private String frameName="";

    /**
     * 不存在用户
     */
    private String notExistUser="";

    /**
     * 不存在品牌
     */
    private String notExistBrand="";


}