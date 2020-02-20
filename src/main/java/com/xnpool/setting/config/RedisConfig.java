package com.xnpool.setting.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
@Slf4j
public class RedisConfig {
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
