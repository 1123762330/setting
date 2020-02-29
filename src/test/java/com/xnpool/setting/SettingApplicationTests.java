package com.xnpool.setting;

import java.util.Date;
import java.util.List;

import com.xnpool.setting.config.ApiContext;
import com.xnpool.setting.controller.MineSettingController;
import com.xnpool.setting.domain.mapper.IpSettingMapper;
import com.xnpool.setting.domain.pojo.IpSetting;
import com.xnpool.setting.domain.pojo.MineSetting;
import com.xnpool.setting.utils.JedisUtil;
import com.xnpool.setting.utils.PrimaryKeyUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SettingApplicationTests {

    @Autowired
    private ApiContext apiContext;

    @Before
    public void before() {
        // 在上下文中设置当前服务商的ID
        apiContext.setTenantId(112233L);
    }

    @Test
    public void addMineSetting() {
    }

}
