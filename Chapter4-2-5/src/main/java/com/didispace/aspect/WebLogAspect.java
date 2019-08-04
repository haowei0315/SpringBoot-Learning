package com.didispace.aspect;

import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.mongodb.BasicDBObject;

/**
 * Web层日志切面,修改
 *
 * @author 程序猿DD
 * @version 1.0.0
 * @date 16/5/17 上午10:42.
 * @blog http://blog.didispace.com
 */
@Aspect
@Order(1)
@Component
public class WebLogAspect {

    private Logger logger = Logger.getLogger("mongodb");

    @Pointcut("execution(public * com.didispace.web..*.*(..))")
    public void webLog(){}

    /*@Before("webLog()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {
        // 获取HttpServletRequest
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        // 获取要记录的日志内容
        BasicDBObject logInfo = getBasicDBObject(request, joinPoint);
        logger.info(logInfo);
    }*/

    
    @Around("webLog()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable{
/*    	System.out.println("已经记录下操作日志@Around 方法执行前");
        Object proceed = pjp.proceed();
        System.err.println(proceed);
        System.out.println("已经记录下操作日志@Around 方法执行后");
        return proceed;*/
        
        // 获取HttpServletRequest
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        Object result = joinPoint.proceed();
        // 获取要记录的日志内容
        BasicDBObject logInfo = getBasicDBObject(request, joinPoint, result);
        logger.info(logInfo);
        return result;
        
    }


    private BasicDBObject getBasicDBObject(HttpServletRequest request, JoinPoint joinPoint, Object result) {
        // 基本信息
        BasicDBObject r = new BasicDBObject();
        r.append("requestURL", request.getRequestURL().toString());
        r.append("requestURI", request.getRequestURI());
        r.append("queryString", request.getQueryString());
        r.append("remoteAddr", request.getRemoteAddr());
        r.append("remoteHost", request.getRemoteHost());
        r.append("remotePort", request.getRemotePort());
        r.append("localAddr", request.getLocalAddr());
        r.append("localName", request.getLocalName());
        r.append("method", request.getMethod());
        r.append("headers", getHeadersInfo(request));
        r.append("parameters", request.getParameterMap());
        r.append("classMethod", joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
        r.append("args", Arrays.toString(joinPoint.getArgs()));
        r.append("returnResult", result);
        r.append("date", new Date());
        
        return r;
    }

    /**
     * 获取头信息
     *
     * @param request
     * @return
     */
    private Map<String, String> getHeadersInfo(HttpServletRequest request) {
        Map<String, String> map = new HashMap<>();
        Enumeration headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = (String) headerNames.nextElement();
            String value = request.getHeader(key);
            map.put(key, value);
        }
        return map;
    }

}

