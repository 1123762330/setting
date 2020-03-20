package com.xnpool.setting;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.xnpool.logaop.util.JwtUtil;
import com.xnpool.setting.config.ApiContext;
import com.xnpool.setting.controller.MineSettingController;
import com.xnpool.setting.domain.mapper.AlgorithmMapper;
import com.xnpool.setting.domain.mapper.IpSettingMapper;
import com.xnpool.setting.domain.pojo.Algorithm;
import com.xnpool.setting.domain.pojo.IpSetting;
import com.xnpool.setting.domain.pojo.MineSetting;
import com.xnpool.setting.utils.JedisUtil;
import com.xnpool.setting.utils.PrimaryKeyUtils;
import com.xnpool.setting.utils.TokenUtil;
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
    private AlgorithmMapper algorithmMapper;

    @Before
    public void before() {
        // 在上下文中设置当前服务商的ID
        //apiContext.setTenantId(112233L);
    }

    @Test
    public void addMineSetting() {
        //Map<String, Object> verify = TokenUtil.verify("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX25hbWUiOiJhZG1pbiIsInNjb3BlIjpbImFsbCJdLCJpZCI6MSwiZW50ZXJwcmlzZUlkIjoxLCJleHAiOjE1ODQ2MTc1MDIsImF1dGhvcml0aWVzIjpbIkFETUlOIl0sImp0aSI6ImE1ZDU2M2Y3LThiM2ItNGE5Mi05ZDZkLWRjMmE0NDJhNWNlMiIsImNsaWVudF9pZCI6InpsdCJ9.OFJ9k4yCPpZDHnuaX-hn-r86uPfJf8x6NbbLJ6HndwY");
        //System.out.println("verify==="+verify);
        JSONObject jsonObject = TokenUtil.verify("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX25hbWUiOiJhZG1pbiIsInNjb3BlIjpbImFsbCJdLCJpZCI6MSwiZW50ZXJwcmlzZUlkIjoxLCJleHAiOjE1ODQ2Njc5NDksImF1dGhvcml0aWVzIjpbIkFETUlOIl0sImp0aSI6IjFjMmY2NWVlLTNiNzEtNDU0Yy05Y2I1LWRjYmRlYzk4ZjZhZSIsImNsaWVudF9pZCI6InpsdCJ9.9cEqM7zE_s1ZDb7lmzSSCdMMHwtJp8Ihps5BprO5cto");
        System.out.println("jsonObject=="+jsonObject);
        Integer success = jsonObject.getInteger("success");
        if (success==200){
            JSONObject data = jsonObject.getJSONObject("data");
            Integer userId = data.getInteger("id");
            Integer enterpriseId = data.getInteger("enterpriseId");
            System.out.println("userId"+userId);
            System.out.println("enterpriseId"+enterpriseId);
        }
    }

}
