package com.xnpool.setting.domain.pojo;

import java.io.Serializable;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zly
 * @version 1.0
 * @date 2020/3/5 12:49
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AgreementSetting implements Serializable {
    private static final long serialVersionUID = 8972091544690215549L;
    /**
     * 编号
     */
    private Integer id;

    /**
     * 协议名称
     */
    private String agreementName;

    /**
     * 文件名
     */
    private String fileName;

    /**
     * 文档路径地址
     */
    private String path;

    /**
     * 是否删除,0:否,1:是
     */
    private Integer isDelete;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

}