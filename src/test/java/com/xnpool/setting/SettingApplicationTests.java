package com.xnpool.setting;
import java.util.Date;

import com.xnpool.setting.controller.MineSettingController;
import com.xnpool.setting.domain.pojo.MineSetting;
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

    @Test
    public void addMineSetting() {
        MineSetting minesetting = new MineSetting();
        minesetting.setMinename("小鸟一号");
        minesetting.setDescription("新疆");
        minesetting.setIsdelete(0);
        minesetting.setUpdatetime(new Date());
        minesetting.setCreatetime(new Date());
        minesettingController.addMineSetting(minesetting);
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
