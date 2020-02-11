package com.xnpool.setting.utils;

/**
 * 返回json数据
 */

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

@Data
public class ResponseResult<T> implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -1626793180717240861L;
    private Integer status;
    private String message;
    private T data;
    private Map<String,Object> ext;
    private T accesstoken;
    public ResponseResult() {
        super();
    }

    public ResponseResult(Integer status) {
        super();
        setStatus(status);
    }



    public ResponseResult(Integer status, String message) {
        this(status);
        setMessage(message);
    }

    public ResponseResult(Integer status, String message,T data) {
        this(status,message);
        setData(data);
    }
    public ResponseResult(Integer status, Exception e) {
        this(status, e.getMessage());
    }



    public ResponseResult(Integer status, T data) {
        this(status);
        setData(data);
    }

    public ResponseResult(Integer status, T data, Map<String, Object> ext) {
        this(status,data);
        setExt(ext);
    }
    public ResponseResult(Integer status, T data, T accesstoken) {
        this(status,data);
        setAccesstoken(accesstoken);
    }

    public ResponseResult(Integer status, String message, T data, T accesstoken) {
        this(status,message,data);
        setAccesstoken(accesstoken);
    }
}
