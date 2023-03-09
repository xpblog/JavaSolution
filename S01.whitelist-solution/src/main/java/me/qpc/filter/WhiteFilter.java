package me.qpc.filter;

import me.qpc.config.WhiteListConfig;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Aspect
@Component
public class WhiteFilter {

  @Resource
  private WhiteListConfig whiteListConfig;

  /**
   * 定义切入点
   */
  @Pointcut("execution( * me.qpc.controller..*(..))")
  public void white() {

  }

  /**
   * 拦截
   */
  @Around("white()")
  public Object whiteList(ProceedingJoinPoint joinPoint) throws Throwable {
    RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
    ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) requestAttributes;
    String ipAddr = getIpAddr(servletRequestAttributes.getRequest());
    if (!whiteListConfig.getIps().contains(ipAddr)) {
      throw new RuntimeException("ip不在白名单里");
    }
    return joinPoint.proceed();
  }


  /**
   * 获取IP地址
   */
  public String getIpAddr(HttpServletRequest request) {
    String ip = request.getHeader("x-forwarded-for");
    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getHeader("Proxy-Client-IP");
    }
    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getHeader("WL-Proxy-Client-IP");
    }
    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getRemoteAddr();
    }
    return "0:0:0:0:0:0:0:1".equals(ip) ? "127.0.0.1" : ip;
  }

}
