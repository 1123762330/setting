package com.xnpool.setting.domain.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author zly
 * @version 1.0
 * @date 2020/4/2 19:09
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MineIdAndIP implements Serializable {
    private static final long serialVersionUID = 126263305631309203L;
    private Integer user_id;
    private Integer mine_id;
    private Integer ip_id;
}
