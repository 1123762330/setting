//package com.xnpool.setting;
//
//import java.util.*;
//import java.util.regex.Pattern;
//
//import com.alibaba.fastjson.JSONObject;
//import com.auth0.jwt.JWT;
//import com.auth0.jwt.JWTVerifier;
//import com.auth0.jwt.interfaces.Claim;
//import com.auth0.jwt.interfaces.DecodedJWT;
//import com.xnpool.logaop.util.JwtUtil;
//import com.xnpool.setting.config.ApiContext;
//import com.xnpool.setting.controller.MineSettingController;
//import com.xnpool.setting.domain.mapper.*;
//import com.xnpool.setting.domain.model.WorkerDetailedExample;
//import com.xnpool.setting.domain.pojo.*;
//import com.xnpool.setting.service.CustomerSettingService;
//import com.xnpool.setting.service.impl.UserWebService;
//import com.xnpool.setting.utils.JedisUtil;
//import com.xnpool.setting.utils.PrimaryKeyUtils;
//import com.xnpool.setting.utils.TokenUtil;
//import net.sf.json.JSON;
//import net.sf.json.JSONArray;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//public class SettingApplicationTests {
//
//    @Autowired
//    private WorkerAssignMapper workerAssignMapper;
//
//    @Autowired
//    private ApiContext apiContext;
//
//    @Before
//    public void before() {
//        // 在上下文中设置当前服务商的ID
//        //apiContext.setTenantId(112233L);
//    }
//
//    @Test
//    public void test() {
//        ArrayList<MineFactoryAndFraneId> list = new ArrayList<>();
//        MineFactoryAndFraneId mineFactoryAndFraneId = new MineFactoryAndFraneId(49, 142, 34, 1078);
//        MineFactoryAndFraneId mineFactoryAndFraneId2 = new MineFactoryAndFraneId(49, 142, 34, 1077);
//        list.add(mineFactoryAndFraneId);
//        list.add(mineFactoryAndFraneId2);
//        apiContext.setTenantId(7L);
//        int rows3 = workerAssignMapper.batchToDelete(list);
//        //long start1 = System.currentTimeMillis();
//        //jedisUtil.zadd("syncing:"+7+":"+142, 2020040812001987L, "jsonString");
//        //System.out.println("终极一步耗时:"+(System.currentTimeMillis()-start1));
//        //HashSet<String> hashSet = new HashSet<>();
//        //hashSet.add("127.0.0.1");
//        //hashSet.add("127.0.0.2");
//        //hashSet.add("127.0.0.3");
//        //hashSet.add("127.0.0.4");
//        //jedisUtil.hset("test1111","241",hashSet.toString());
//        //String test1111 = jedisUtil.hget("test1111", "241");
//        //JSONArray jsonArray = JSONArray.fromObject(test1111);
//        //System.out.println("====="+jsonArray);
//        //jedisUtil.scan("");
//        //HashMap<String, Integer> workerTotal = userWebService.getWorkerTotal("");
//        //System.out.println(workerTotal);
//        //HashMap<Long, String> longStringHashMap = customerSettingService.selectTenantList("");
//        //System.out.println(longStringHashMap);
//        //List<WorkerDetailedExample> WorkerDetailedExampleList = workerDetailedMapper.selectMoveOutList(null);
//        //System.out.println(WorkerDetailedExampleList);
//        //WorkerbrandSetting workerbrandSetting = new WorkerbrandSetting();
//        //workerbrandSetting.setBrandName("");
//        //workerbrandSetting.setWorkerType("");
//        //workerbrandSetting.setBusiness("");
//        //workerbrandSetting.setWorkerSize("");
//        //workerbrandSetting.setPowerWaste("");
//        //workerbrandSetting.setDifficulty("");
//        //workerbrandSetting.setIsDelete(0);
//        //workerbrandSetting.setCreateTime(new Date());
//        //workerbrandSetting.setUpdateTime(new Date());
//        //workerbrandSetting.setAlgorithmId(0);
//        //
//        //int rows = workerbrandSettingMapper.insert(workerbrandSetting);
//        //System.out.println(rows);
//        //Map<String, Object> verify = TokenUtil.verify("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX25hbWUiOiJhZG1pbiIsInNjb3BlIjpbImFsbCJdLCJpZCI6MSwiZW50ZXJwcmlzZUlkIjoxLCJleHAiOjE1ODQ2MTc1MDIsImF1dGhvcml0aWVzIjpbIkFETUlOIl0sImp0aSI6ImE1ZDU2M2Y3LThiM2ItNGE5Mi05ZDZkLWRjMmE0NDJhNWNlMiIsImNsaWVudF9pZCI6InpsdCJ9.OFJ9k4yCPpZDHnuaX-hn-r86uPfJf8x6NbbLJ6HndwY");
//        //System.out.println("verify==="+verify);
//        //JSONObject jsonObject = TokenUtil.verify("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX25hbWUiOiJhZG1pbiIsInNjb3BlIjpbImFsbCJdLCJpZCI6MSwiZW50ZXJwcmlzZUlkIjoxLCJleHAiOjE1ODQ2Njc5NDksImF1dGhvcml0aWVzIjpbIkFETUlOIl0sImp0aSI6IjFjMmY2NWVlLTNiNzEtNDU0Yy05Y2I1LWRjYmRlYzk4ZjZhZSIsImNsaWVudF9pZCI6InpsdCJ9.9cEqM7zE_s1ZDb7lmzSSCdMMHwtJp8Ihps5BprO5cto");
//        //System.out.println("jsonObject=="+jsonObject);
//        //Integer success = jsonObject.getInteger("success");
//        //if (success==200){
//        //    JSONObject data = jsonObject.getJSONObject("data");
//        //    Integer userId = data.getInteger("id");
//        //    Integer enterpriseId = data.getInteger("enterpriseId");
//        //    System.out.println("userId"+userId);
//        //    System.out.println("enterpriseId"+enterpriseId);
//        //}
//    }
//
//}
