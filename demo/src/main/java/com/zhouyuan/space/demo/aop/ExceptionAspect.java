//package com.zhouyuan.space.demo.aop;
//
//import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.annotation.AfterThrowing;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Pointcut;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Component;
//
///**
// * 业务日志收集切面
// *
// * @author yuanhao
// * @since 2020/9/16
// */
//
//@Aspect
//@Component
//public class ExceptionAspect {
//
//    private static final Logger log = LoggerFactory.getLogger(ExceptionAspect.class);
//
//
//    /**
//     * 配置织入点
//     *
//     * @author yuanhao
//     * @since 2020/9/16
//     */
//    @Pointcut("@within(org.springframework.web.bind.annotation.RestController) && (" +
//            "@annotation(org.springframework.web.bind.annotation.GetMapping) || " +
//            "@annotation(org.springframework.web.bind.annotation.PostMapping) ||" +
//            "@annotation(org.springframework.web.bind.annotation.PutMapping) ||" +
//            "@annotation(org.springframework.web.bind.annotation.DeleteMapping) ||" +
//            "@annotation(org.springframework.web.bind.annotation.PatchMapping) ||" +
//            "@annotation(org.springframework.web.bind.annotation.RequestMapping))")
//    public void logPointCut() {
//    }
//
//    /**
//     * 处理完请求后执行
//     *
//     * @param joinPoint 切点
//     */
///*    @AfterReturning(pointcut = "logPointCut()", returning = "jsonResult")
//    public void doAfterReturning(JoinPoint joinPoint, Object jsonResult) {
//        handleLog(joinPoint, null, jsonResult);
//    }*/
//
//    /**
//     * 拦截异常操作
//     *
//     * @param joinPoint 切点
//     * @param e         异常
//     */
//    @AfterThrowing(value = "logPointCut()", throwing = "e")
//    public void doAfterThrowing(JoinPoint joinPoint, Exception e) {
//        handleLog(joinPoint, e, null);
//    }
//
//    /**
//     * 业务日志处理
//     *
//     * @param joinPoint  切点
//     * @param e          异常
//     * @param jsonResult 方法返回值
//     * @author yuanhao
//     * @since 2020/9/16
//     */
//    private void handleLog(final JoinPoint joinPoint, final Exception e, Object jsonResult) {
//        Object target = joinPoint.getTarget();
//        String signature = joinPoint.getSignature().toString();
//        //String fileName = joinPoint.getSourceLocation().getFileName();
//        log.info( "==" + signature + "==" );
//        log.error(e.getMessage());
//    }
//
//
//}
