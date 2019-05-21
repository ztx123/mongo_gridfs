package com.txzhang.mongo.log;

import com.alibaba.fastjson.JSON;
import com.txzhang.mongo.entity.LogEntity;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 功能 : 日志记录
 * date : 2019-05-16 09:42
 *
 * @version : 0.0.4-snapshot
 * @Author : txzhang@wisdombud.com
 * @since : JDK 1.8
 */
@Component
@Slf4j
@Aspect
public class LogAspect {

//    @Autowired
//    private MongoTemplate mongoTemplate;

    /**
     * controller 包下的所有类的所有方法
     */
    private final String pointCut = "execution(* com.txzhang.mongo.controller..*(..))";

    @Pointcut(value = pointCut)
    public void log() {
        log.info("哈哈哈哈！");
    }

    @Around(value = "log()")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        final StringBuffer requestURL = request.getRequestURL();
        LogEntity logEntity = new LogEntity();
        logEntity.setUrl(requestURL.toString());
        final String name = proceedingJoinPoint.getClass().getName();
        logEntity.setClassName(name);
        log.info("类名:" + name);
        final MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();
        final String methodName = methodSignature.getMethod().getName();
        logEntity.setMethodName(methodName);
        log.info("方法：:" + methodName);
        final Object[] args = proceedingJoinPoint.getArgs();
        StringBuilder argsStr = new StringBuilder();
        for (Object obj : args){
            argsStr.append(obj).append("***华丽的分割线***");
            log.info("参数： " + obj);
        }
        logEntity.setArgs(argsStr.toString());
        final long startTime = System.currentTimeMillis();
        final Object proceed = proceedingJoinPoint.proceed();
        final long endTime = System.currentTimeMillis();
        log.info("返回结果：" + JSON.toJSON(proceed));
        logEntity.setResult(proceed);
        final long consuming = endTime - startTime;
        logEntity.setConsuming(consuming);
        log.info("耗时：" + consuming);
//        saveLog(logEntity);
        return proceed;
    }

//    private void saveLog(LogEntity logEntity) {
//        if (null == logEntity) {
//            return;
//        }
//        mongoTemplate.insert(logEntity);
//    }
}
