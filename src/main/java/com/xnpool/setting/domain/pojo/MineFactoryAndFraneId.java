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

    private Integer user_id;
    private Integer mine_id;
    private Integer factory_id;
    private Integer frame_id;
}
