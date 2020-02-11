package com.xnpool.setting.domain.pojo;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * @author  zly
 * @date  2020/2/7 9:42
 * @version 1.0
 */
@Data
public class AgreementSetting implements Serializable {
    private static final long serialVersionUID = -5014975223213669615L;
    /**
    * 编号
    */
    private Integer id;

    /**
    * 协议名称
    */
    private String agreementname;

    /**
    * 文档路径地址
    */
    private String path;

    /**
    * 是否删除,0:否,1:是
    */
    private Integer isdelete;

    /**
    * 创建时间
    */
    private Date createtime;

    /**
    * 修改时间
    */
    private Date updatetime;
}