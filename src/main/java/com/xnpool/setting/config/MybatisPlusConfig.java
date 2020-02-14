package com.xnpool.setting.config;
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
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * mybatis-plus设置多租户
 * @author zly
 * @version 1.0
 * @date 2020/2/14 14:46
 */
@Configuration
@MapperScan("com.xnpool.setting.domain.mapper")
public class MybatisPlusConfig {
    //对应的数据表中的租户ID列
    private static final String SYSTEM_TENANT_ID = "tenant_id";
    //不需要实现多租户的数据表名集合
    private static final List<String> IGNORE_TENANT_TABLES  = new ArrayList<>();;

    @Autowired
    private ApiContext apiContext;

    //配置分页插件
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        //第一步创建sql解析器集合
        ArrayList<ISqlParser> sqlParsersList = new ArrayList<>();

        // 第二部SQL解析处理拦截：增加租户处理回调。(租户sql解析器)
        TenantSqlParser tenantSqlParser = new TenantSqlParser();
        tenantSqlParser.setTenantHandler(new TenantHandler() {
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

                    //多租户字段名
                    @Override
                    public String getTenantIdColumn() {
                        return SYSTEM_TENANT_ID;
                    }

                    //对不需要加企业id的表过滤
                    @Override
                    public boolean doTableFilter(String tableName) {
                        // 过滤掉一些表：如租户表（provider）本身不需要执行这样的处理。
                        // if ("role".equals(tableName)){
                        //   //过滤的表
                        //   return true;
                        // }
                        // return false;
                        return IGNORE_TENANT_TABLES.stream().anyMatch((e) -> e.equalsIgnoreCase(tableName));
                    }
                });
        //第三步
        //将多租户解析语句添加到集合里面去
        sqlParsersList.add(tenantSqlParser);
        //将这个上去了解析语句重新set进分页插件里面去
        paginationInterceptor.setSqlParserList(sqlParsersList);

        //过滤某些不需要多租户的方法
        paginationInterceptor.setSqlParserFilter(new ISqlParserFilter() {
            @Override
            public boolean doFilter(MetaObject metaObject) {
                //获取当前执行的方法,如果相等,那就不增加租户信息
                // MappedStatement mappedStatement = SqlParserHelper.getMappedStatement(metaObject);
                //if("com.xnpool.setting.domain.mapper.IpSettingMapper.selectByOther".equals(mappedStatement.getId())){
                //return true;
                //  }
                return false;
            }
        });
        return paginationInterceptor;
    }

    @Bean(name = "performanceInterceptor")
    public PerformanceInterceptor performanceInterceptor() {
        return new PerformanceInterceptor();
    }
}

