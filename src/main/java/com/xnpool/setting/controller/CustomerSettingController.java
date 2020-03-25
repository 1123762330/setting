package com.xnpool.setting.controller;

import com.github.pagehelper.PageInfo;
import com.xnpool.logaop.annotation.SystemLog;
import com.xnpool.logaop.util.LogType;
import com.xnpool.logaop.util.ResponseResult;
import com.xnpool.setting.common.BaseController;
import com.xnpool.setting.domain.pojo.CustomerSetting;
import com.xnpool.setting.domain.model.CustomerSettingExample;
import com.xnpool.setting.service.CustomerSettingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.HashMap;

/**
 * 客户设置
 * @author zly
 * @version 1.0
 * @date 2020/2/10 10:36
 */
@RestController
@Slf4j
@RequestMapping("/api/v1/customer")
public class CustomerSettingController extends BaseController {
    @Autowired
    private CustomerSettingService customerSettingService;

    /**
     * @Description 添加客户设置
     * @Author zly
     * @Date 10:40 2020/2/10
     * @Param
     * @return
     */
    @SystemLog(value = "添加客户",type = LogType.SYSTEM)
    @PostMapping("/addCustomer")
    public ResponseResult addCustomer(CustomerSetting customerSetting) {
        customerSettingService.insertSelective(customerSetting,null);
        return new ResponseResult(SUCCESS);
    }

    /**
     * @Description 修改客户设置
     * @Author zly
     * @Date 10:41 2020/2/10
     * @Param
     * @return
     */
    @SystemLog(value = "修改客户设置",type = LogType.SYSTEM)
    @PutMapping("/updateCustomer")
    public ResponseResult updateCustomer(CustomerSetting customerSetting) {
        customerSettingService.updateByPrimaryKeySelective(customerSetting);
        return new ResponseResult(SUCCESS);
    }

    /**
     * @Description 删除客户设置
     * @Author zly
     * @Date 10:44 2020/2/10
     * @Param
     * @return
     */
    @SystemLog(value = "删除客户",type = LogType.SYSTEM)
    @DeleteMapping("/deleteCustomer")
    public ResponseResult deleteCustomer(int id) {
        customerSettingService.updateById(id);
        return new ResponseResult(SUCCESS);
    }

    /**
     * @Description 查询客户列表
     * 每个管理员只能查询自己权限内的客户
     * @Author zly
     * @Date 11:37 2020/2/10
     * @Param
     * @return
     */
    @SystemLog(value = "查询客户列表",type = LogType.SYSTEM)
    @GetMapping("/selectCustomerList")
    public ResponseResult selectCustomerList (String keyWord, @RequestParam(value = "pageNum", required = false, defaultValue = "1") int pageNum,
                                              @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize){
        PageInfo<CustomerSettingExample> pageInfo = customerSettingService.selectByOther(keyWord, pageNum, pageSize);
        return new ResponseResult(SUCCESS,pageInfo);
    }

    /**
     * 认证模式还需要用户表才能实现,需要企业ID,管理员登录,根据管理员的企业ID来查看申请列表.然后选择是否同意认证
     * @Description 认证用户
     * @Author zly
     * @Date 11:57 2020/2/10
     * @Param
     * @return
     */
    @SystemLog(value = "客户认证",type = LogType.SYSTEM)
    @PutMapping("/attestationCustomer")
    public ResponseResult attestationCustomer() {
       //首先根据根据管理者的企业ID去查询向企业请求认证的用户列表,如果authentication=2就是用户申请认证
        //这个时候管理者可以选择通过认证,也可以选择拒绝认证,通过认证就是1,拒绝认证就是-1.
        int userid=2;
        int isPass=1;
        customerSettingService.updateAttestationById(userid,isPass);
        return new ResponseResult(SUCCESS);
    }

    /**
     * @Description 查询客户名称,在矿机入库时绑定userid
     * @Author zly
     * @Date 13:50 2020/3/8
     * @Param
     * @return
     */
    @GetMapping("/selectUserList")
    public ResponseResult selectUserList (){
        HashMap<Integer, String> userNameList = customerSettingService.selectUserList();
        return new ResponseResult(SUCCESS,userNameList);
    }

    /**
     * @Description 查询客户姓名集合,添加电表时需要
     * @Author zly
     * @Date 9:51 2020/3/23
     * @Param
     * @return
     */
    @GetMapping("/selectCustomerMap")
    public ResponseResult selectCustomerMap (){
        HashMap<Integer, String> userNameList = customerSettingService.selectCustomerMap();
        return new ResponseResult(SUCCESS,userNameList);
    }

    /**
     * @Description 查询用户表所有的用户
     * @Author zly
     * @Date 10:21 2020/3/23
     * @Param
     * @return
     */
    @GetMapping("/selectAllUser")
    public ResponseResult selectAllUser (){
        HashMap<Integer, String> userNameList = customerSettingService.selectAllUser();
        return new ResponseResult(SUCCESS,userNameList);
    }

    @Configuration
    @Slf4j
    public static class RedisConfig {
        @Value("${spring.redis.host}")
        private String host;

        @Value("${spring.redis.port}")
        private int port;
        @Value("${spring.redis.password}")
        private String password;

        @Value("${spring.redis.database}")
        private int database;

        @Value("${spring.redis.block-when-exhausted}")
        private boolean  blockWhenExhausted;

        @Value("${spring.redis.timeout}")
        private String timeout;

        @Value("${spring.redis.jedis.pool.max-idle}")
        private int maxIdle;

        @Value("${spring.redis.jedis.pool.min-idle}")
        private int minIdle;

        @Value("${spring.redis.jedis.pool.max-active}")
        private int maxActive;

        @Value("${spring.redis.jedis.pool.max-wait}")
        private String maxWaitMillis;

        @Bean
        public JedisPool redisPoolFactory() {
            //String waitMillis = maxWaitMillis.substring(0, maxWaitMillis.length() - 2);
            String timeoutStr = timeout.substring(0, timeout.length() - 2);
            //System.err.println(waitMillis+"waitMillis"+"timeoutStr"+timeoutStr);
            log.info("JedisPool注入成功！！");
            log.info("redis地址：" + host + ":" + port);
            JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
            //最大空闲连接数, 默认8个
            jedisPoolConfig.setMaxIdle(maxIdle);
            //最小空闲连接数, 默认0
            jedisPoolConfig.setMinIdle(minIdle);
            //最大连接数, 默认8个
            jedisPoolConfig.setMaxTotal(maxActive);
            jedisPoolConfig.setMaxWaitMillis(Integer.valueOf(maxWaitMillis));
            // 连接耗尽时是否阻塞, false报异常,ture阻塞直到超时, 默认true
            jedisPoolConfig.setBlockWhenExhausted(blockWhenExhausted);
            //设置的逐出策略类名, 默认DefaultEvictionPolicy(当连接超过最大空闲时间,或连接数超过最大空闲连接数)
            jedisPoolConfig.setEvictionPolicyClassName("org.apache.commons.pool2.impl.DefaultEvictionPolicy");
            // 是否启用pool的jmx管理功能, 默认true
            jedisPoolConfig.setJmxEnabled(true);
            jedisPoolConfig.setJmxNamePrefix("pool");
            //逐出连接的最小空闲时间 默认1800000毫秒(30分钟)
            jedisPoolConfig.setMinEvictableIdleTimeMillis(1000*60*5);
            //每次逐出检查时 逐出的最大数目 如果为负数就是 : 1/abs(n), 默认3
            jedisPoolConfig.setNumTestsPerEvictionRun(3);
            //对象空闲多久后逐出, 当空闲时间>该值 且 空闲连接>最大空闲数 时直接逐出,不再根据MinEvictableIdleTimeMillis判断  (默认逐出策略)
            jedisPoolConfig.setSoftMinEvictableIdleTimeMillis(1000*60*5);
            //是否启用后进先出, 默认true
            jedisPoolConfig.setLifo(true);
            //在获取连接的时候检查有效性, 默认false
            jedisPoolConfig.setTestOnBorrow(true);
            // 在还回 jedis pool 时，是否提前进行 validate 操作
            jedisPoolConfig.setTestOnReturn(false);
            //在空闲时检查有效性, 默认false
            jedisPoolConfig.setTestWhileIdle(true);
            // 表示 idle object evitor 两次扫描之间要sleep的毫秒数
            jedisPoolConfig.setTimeBetweenEvictionRunsMillis(30000);
            JedisPool jedisPool;
            if("".equals(password)){
              password = null;
            }
            jedisPool  = new JedisPool(jedisPoolConfig, host, port, Integer.valueOf(timeoutStr), password,database);
            return jedisPool;
        }



    }
}
