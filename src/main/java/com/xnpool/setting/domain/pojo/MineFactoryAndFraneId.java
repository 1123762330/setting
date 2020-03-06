package com.xnpool.setting.domain.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author zly
 * @version 1.0
 * @date 2020/3/6 16:59
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MineFactoryAndFraneId implements Serializable {

    private static final long serialVersionUID = -5349428779634465856L;

    private Integer userId;
    private Integer mineId;
    private Integer factoryId;
    private Integer frameId;
}
