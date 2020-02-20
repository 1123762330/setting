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
    private MineSettingController minesettingController;

    @Autowired
    private PrimaryKeyUtils primaryKeyUtils;

    @Autowired
    private ApiContext apiContext;

    @Autowired
    private IpSettingMapper ipSettingMapper;

    @Before
    public void before() {
        // 在上下文中设置当前服务商的ID
        apiContext.setTenantId(112233L);
    }

    @Test
    public void addMineSetting() {
        long startMillis = System.currentTimeMillis();
        String orderIdPrefix = primaryKeyUtils.getOrderIdPrefix(new Date());
        System.out.println("orderIdPrefix=="+orderIdPrefix);
        Long aLong = primaryKeyUtils.orderId(orderIdPrefix);
        System.out.println(aLong);
        long endMillis = System.currentTimeMillis();
        System.out.println("生成速度:" + (endMillis - startMillis) + ",单位毫秒");
    }

    @Test
    public void updateMineSettingList() {
        MineSetting minesetting = new MineSetting();
        minesetting.setId(8);
        minesetting.setMinename("风铃花山号5号");
        minesetting.setDescription("阿富汗");
        minesetting.setIsdelete(0);
        minesetting.setUpdatetime(new Date());
        minesettingController.updateMineSetting(minesetting);
    }

    @Test
    public void deleteMineSetting() {
        minesettingController.deleteMineSetting(8);
    }

}
