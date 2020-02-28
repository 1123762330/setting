package com.xnpool.setting.domain.mapper;

import com.baomidou.mybatisplus.annotation.SqlParser;
import com.github.pagehelper.PageInfo;import com.xnpool.setting.domain.pojo.Worker;import com.xnpool.setting.domain.pojo.WorkerExample;import org.apache.ibatis.annotations.Param;import java.util.List;

/**
 * @author zly
 * @version 1.0
 * @date 2020/2/27 10:07
 */
public interface WorkerMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Worker record);

    int insertSelective(Worker record);

    Worker selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Worker record);

    int updateByPrimaryKey(Worker record);

    //开启这个注解,就相当于这个方法不增加多租户信息
    //@SqlParser(filter = true)
    List<Worker> selectByOther(@Param("keyWord") String keyWord);


    int updateById(@Param("list") List<Integer> list);
}