package com.xnpool.setting.config;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.parser.ISqlParser;
import com.baomidou.mybatisplus.core.parser.ISqlParserFilter;
import com.baomidou.mybatisplus.core.parser.SqlParserHelper;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PerformanceInterceptor;
import com.baomidou.mybatisplus.extension.plugins.tenant.TenantHandler;
import com.baomidou.mybatisplus.extension.plugins.tenant.TenantSqlParser;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.reflection.MetaObject;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * mybatis-plus设置多租户
 *
 * @author zly
 * @version 1.0
 * @date 2020/2/14 14:46
 */
@Configuration
public class MybatisPlusConfig {
    @Autowired
    private ApiContext apiContext;

    @Autowired
    private TenantProperties tenantProperties;

    //配置分页插件
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        if (tenantProperties.getEnable()) {
            //第一步new一个sql解析器集合
            // 第二部SQL解析处理拦截：增加租户处理回调。(租户sql解析器)
            //第三步将多租户解析语句添加到集合里面去,然后将这个上去了解析语句重新set进分页插件里面去
            TenantSqlParser tenantSqlParser = new TenantSqlParser().setTenantHandler(
                    new TenantHandler() {
                        //具体的多租户ID
                        @Override
                        public Expression getTenantId() {
                            // 从当前系统上下文中取出当前请求的服务商ID，通过解析器注入到SQL中。
                            Long tenantId = apiContext.getTenantId();
                            if (null == tenantId) {
                                throw new RuntimeException("获取企业id失败");
                            }
                            return new LongValue(tenantId);
                        }

                        //多租户字段名对应的数据表中的租户ID列
                        @Override
                        public String getTenantIdColumn() {
                            return "tenant_id";
                        }

                        //对不需要加企业id的表过滤
                        @Override
                        public boolean doTableFilter(String tableName) {
                            // 过滤掉一些表：如租户表（provider）本身不需要执行这样的处理。
                            //if ("worker".equals(tableName)){
                            //    //过滤的表
                            //    return true;
                            //}
                            //return false;
                            return tenantProperties.getIgnoreSqls().stream().anyMatch((e) -> e.equalsIgnoreCase(tableName));
                        }
                    });
            paginationInterceptor.setSqlParserList(CollUtil.toList(tenantSqlParser));

            //过滤某些不需要多租户的方法
            paginationInterceptor.setSqlParserFilter(new ISqlParserFilter() {
                @Override
                public boolean doFilter(MetaObject metaObject) {
                    //获取当前执行的方法,如果相等,那就不增加租户信息
                    //MappedStatement mappedStatement = SqlParserHelper.getMappedStatement(metaObject);
                    //if (mappedStatement.getId().contains("com.xnpool.setting.domain.mapper.WorkerMapper.selectByOther")) {
                    //    return true;
                    //}
                    return false;
                }
            });
        }
        return paginationInterceptor;
    }


    //性能分析插件,显示sql语句
    @ConditionalOnProperty(name = "performanceInterceptor", havingValue = "true")
    @Bean
    public PerformanceInterceptor performanceInterceptor() {
        return new PerformanceInterceptor();
    }
}


