package com.xnpool.setting.common;

import com.xnpool.logaop.util.WriteLogUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 日志切面注解
 * @author zly
 * @version 1.0
 * @date 2020/3/13 16:41
 */
@Aspect
@Component
public class SysLogAspect {
    @Autowired
    private WriteLogUtil writeLogUtil;
    //定义切点 @Pointcut
    //在注解的位置切入代码
    @Pointcut("@annotation(com.xnpool.logaop.annotation.SystemLog)")
    public void logPoinCut() {
    }


    @Around("logPoinCut()")
    public Object doAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable{
        return writeLogUtil.writeLog(proceedingJoinPoint);
    }

}
