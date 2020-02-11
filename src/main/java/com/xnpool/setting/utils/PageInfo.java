package com.xnpool.setting.utils;

import lombok.Data;

import java.io.Serializable;

@Data
public class PageInfo implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 总记录数
     */
    private Long total;
    /**
     * 总页数
     */
    private Long totalPages;
    /**
     * 当前页
     */
    private Integer pageNo;
    /**
     * 页记录数
     */
    private Integer pageSize;

    public PageInfo(Long total) {
        this.total = total;
    }

    public PageInfo(Long total, Integer pageNo,
                    Integer pageSize) {
        this.total = total;
        this.totalPages = (total - 1) / pageSize + 1;
        this.pageNo = pageNo;
        this.pageSize = pageSize;
    }

    public static PageInfo page(Long total) {
        return new PageInfo(total);
    }

    public static PageInfo page(Long total, Integer pageNo,
                                Integer pageSize) {
        return new PageInfo(total, pageNo, pageSize);
    }


}

