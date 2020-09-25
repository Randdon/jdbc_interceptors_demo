//package com.zhouyuan.space.demo.exception;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.serializer.SerializerFeature;
//import com.casic.log.constant.CommonConstant;
//import com.casic.log.constant.LogLevelConstant;
//import com.casic.log.domain.HttpLogInfo;
//import com.casic.log.domain.LogInfo;
//import com.casic.log.utils.DateTimeUtils;
//import com.casic.log.utils.TraceIdUtils;
//import com.zhouyuan.space.demo.util.WebUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.core.env.Environment;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.method.HandlerMethod;
//
//import javax.servlet.http.HttpServletRequest;
//import java.util.Arrays;
//import java.util.Enumeration;
//import java.util.Map;
//import java.util.UUID;
//
//@ControllerAdvice
//public class GlobalExceptionHandler {
//
//    private static Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
//
//    @Autowired
//    Environment environment;
////    @Autowired
////    KafkaSender kafkaSender;
//
//    /**
//     * 拦截所有未捕获的异常报错
//     *
//     * @param request
//     * @param exception
//     * @return
//     * @throws Exception
//     */
//    @ExceptionHandler(value = Exception.class)
//    public void allExceptionHandler(HttpServletRequest request, HandlerMethod method,
//                                    Exception exception) {
//
//        Object bean = method.getBean();
//        String logInfo = generateLogInfo(request, exception);
//
//        log.error(logInfo);
//
//    }
//
//    /**
//     * 组装日志记录实体
//     * @param request
//     * @param exception
//     * @return
//     */
//    private String generateLogInfo(HttpServletRequest request, Exception exception) {
//        //  收集参数
//        String requestIp = WebUtils.getIpAddress(request);
//        StringBuffer requestUrL = request.getRequestURL();
//        String requestMethod = request.getMethod();
//        String contentType = request.getContentType();
//        String userAgent = request.getHeader("User-Agent");
//        String requestSource = WebUtils.judgeAgentType(userAgent);
//        String userId = request.getHeader(CommonConstant.USER_ID);
//        String traceId = TraceIdUtils.getTraceId();
//        String threadName = Thread.currentThread().getName();
//        Map<String, String[]> parameterMap = request.getParameterMap();
//
//        HttpLogInfo httpLogInfo = HttpLogInfo.builder()
//                .threadName(threadName).requestIp(requestIp)
//                .requestParams(JSON.toJSONString(parameterMap)).contentType(contentType).requestSource(requestSource)
//                .requestUrl(requestUrL.toString()).requestMethod(requestMethod)
//                .build();
//        httpLogInfo.setLevel(LogLevelConstant.ERROR);
//        LogInfo logInfo = LogInfo.builder().messageId(UUID.randomUUID().toString())
//                .systemId("132456").serverId("654321").userId(userId).traceId(traceId)
//                .httpLogInfo(httpLogInfo).build();
//        return JSON.toJSONString(logInfo, SerializerFeature.WriteMapNullValue);
//
//    }
//}
