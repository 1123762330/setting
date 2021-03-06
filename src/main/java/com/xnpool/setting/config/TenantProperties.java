package com.xnpool.setting.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 多租户配置
 */
@Data
@Component
@ConfigurationProperties(prefix = "zlt.tenant")
@RefreshScope //配置文件自动刷新
public class TenantProperties {

    /**
     * 是否开启多租户
     */
    private Boolean enable = true;

    /**
     * 配置不进行多租户隔离的表名
     */
    private List<String> ignoreTables = new ArrayList<>();


    /**
     * 配置不进行多租户隔离的sql
     * 需要配置mapper的全路径如：com.central.user.mapper.SysUserMapper.findList
     */
    private List<String> ignoreSqls = new ArrayList<>();
}
