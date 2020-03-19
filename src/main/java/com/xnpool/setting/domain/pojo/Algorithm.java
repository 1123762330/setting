package com.xnpool.setting.domain.pojo;

import java.io.Serializable;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zly
 * @version 1.0
 * @date 2020/3/19 16:21
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Algorithm implements Serializable {
    private static final long serialVersionUID = -369931744112087271L;
    /**
     * 编号
     */
    private Integer id;

    /**
     * 算法名
     */
    private String algorithmName;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 是否删除,0:否,1:是
     */
    private Integer isDelete;

}