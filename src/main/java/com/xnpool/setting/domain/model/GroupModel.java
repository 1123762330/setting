package com.xnpool.setting.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author zly
 * @version 1.0
 * @date 2020/3/12 17:34
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupModel implements Serializable {
    private static final long serialVersionUID = 2369920988398877538L;
    private String groupName;
    private String workerIp;
    private String ipQuJian;
    private String brandName;
    private Integer state;
    private Integer total;
    private Integer offLineSize;
}
