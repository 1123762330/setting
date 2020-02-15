package com.xnpool.setting.config;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 获取上下文的企业ID
 * @author zly
 * @version 1.0
 * @date 2020/2/14 14:45
 */
@Component
public class ApiContext {
    private static final String KEY_CURRENT_TENANT_ID = "KEY_CURRENT_TENANT_ID";
    private static final Map<String, Object> mContext = new HashMap<>();

    public void setTenantId(Long tenantId) {
        mContext.put(KEY_CURRENT_TENANT_ID, tenantId);
    }

    public Long getTenantId() {
        return (Long) mContext.get(KEY_CURRENT_TENANT_ID);
    }
}

