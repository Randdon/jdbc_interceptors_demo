//package com.zhouyuan.space.demo.aop;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONObject;
//import com.alibaba.fastjson.serializer.SerializerFeature;
//import com.casic.htzy.log.annotation.Identification;
//import com.casic.htzy.log.annotation.NetworkLog;
//import com.casic.htzy.log.config.LogCenterLocalProperties;
//import com.casic.htzy.log.constant.CommonConstant;
//import com.casic.htzy.log.constant.LogCodeEnum;
//import com.casic.htzy.log.constant.LogLevelConstant;
//import com.casic.htzy.log.constant.ServiceStatusEnum;
//import com.casic.htzy.log.domain.LogInfo;
//import com.casic.htzy.log.domain.NetworkLogDetailInfo;
//import com.casic.htzy.log.domain.SysTrailTypeDetailInfo;
//import com.casic.htzy.log.service.KafkaSender;
//import com.casic.htzy.log.utils.*;
//import org.apache.catalina.connector.CoyoteOutputStream;
//import org.apache.commons.lang3.StringUtils;
//import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.annotation.*;
//import org.aspectj.lang.reflect.MethodSignature;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.context.request.RequestContextHolder;
//import org.springframework.web.context.request.ServletRequestAttributes;
//import org.springframework.web.multipart.MultipartFile;
//
//import javax.servlet.ServletOutputStream;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.DataOutputStream;
//import java.lang.annotation.Annotation;
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.UUID;
//import java.util.function.Function;
//
///**
// * 网络请求日志切面
// *
// * @author yuanhao
// * @since 2020/9/16
// */
//@Aspect
//@Component
//public class NetworkLogAspectSelf {
//
//    private static final Logger log = LoggerFactory.getLogger(NetworkLogAspectSelf.class);
//
//    @Autowired
//    private KafkaSender kafkaSender;
//
//    @Autowired
//    private LogCenterLocalProperties logCenterProperties;
//
//    /**
//     * 方法执行开始时间
//     */
//    private long startTime;
//
//    /**
//     * 配置织入点
//     *
//     * @author yuanhao
//     * @since 2020/9/16
//     */
//    @Pointcut("@annotation(com.casic.htzy.log.annotation.NetworkLog)")
//    public void logPointCut() {
//    }
//
//    /**
//     * 处理请求前执行
//     *
//     * @author yuanhao
//     * @since 2020/9/29
//     */
//    @Before("logPointCut()")
//    public void doBefore() {
//        this.startTime = System.currentTimeMillis();
//    }
//
//    /**
//     * 处理完请求后执行
//     *
//     * @param joinPoint  joinPoint
//     * @param jsonResult jsonResult
//     * @author yuanhao
//     * @since 2020/9/24
//     */
//    @AfterReturning(pointcut = "logPointCut()", returning = "jsonResult")
//    public void doAfterReturning(JoinPoint joinPoint, Object jsonResult) {
//        handleLog(joinPoint, null, jsonResult);
//    }
//
//    /**
//     * 抛出异常后执行
//     *
//     * @param joinPoint joinPoint
//     * @param e         e
//     * @author yuanhao
//     * @since 2020/9/24
//     */
//    @AfterThrowing(value = "logPointCut()", throwing = "e")
//    public void doAfterThrowing(JoinPoint joinPoint, Exception e) {
//        handleLog(joinPoint, e, null);
//    }
//
//    /**
//     * 网络请求日志处理
//     *
//     * @param joinPoint 切点
//     * @param exception 异常
//     * @param response  方法返回值
//     * @author yuanhao
//     * @since 2020/9/16
//     */
//    private void handleLog(JoinPoint joinPoint, Exception exception, Object response) {
//        try {
//            // 获取请求
//            ServletRequestAttributes requestAttributes = (ServletRequestAttributes)
//                    RequestContextHolder.getRequestAttributes();
//            if (requestAttributes == null) {
//                return;
//            }
//            HttpServletRequest request = requestAttributes.getRequest();
//            // 获得注解
//            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
//            NetworkLog networkLog = signature.getMethod().getAnnotation(NetworkLog.class);
//            Identification identification = signature.getMethod().getAnnotation(Identification.class);
//            System.out.println(extractRequestPath(joinPoint));
//            // =========================================================
//            String header = request.getHeader("Content-Type");
//            System.out.println(header);
//            System.out.println(request.getContentType());
//            String kind = joinPoint.getKind();
//            Object[] args = joinPoint.getArgs();
//            for (Object arg : args) {
//                if (arg instanceof MultipartFile){
//                    MultipartFile file = (MultipartFile) arg;
//                    String fileName = file.getName();
//                    String originalFilename = file.getOriginalFilename();
//                    String contentType = file.getContentType();
//                    long size = file.getSize();
//                    JSONObject object = new JSONObject();
//                    object.put("fileName",fileName);
//                    object.put("originalFilename",originalFilename);
//                    object.put("contenType",contentType);
//                    object.put("size",size);
//                    System.out.println(object.toJSONString());
//                }else if (arg instanceof MultipartFile[]){
//                    MultipartFile[] files = (MultipartFile[]) arg;
//                    for (MultipartFile file : files) {
//                        System.out.println(file.getOriginalFilename());
//                    }
//                }
//            }
//
//            HttpServletResponse servletResponse = requestAttributes.getResponse();
//            if (response instanceof byte[]){
//                byte[] bytes = (byte[]) response;
//            }else if (null != servletResponse){
//                String header1 = servletResponse.getHeader("Content-Disposition");
//                if (StringUtils.isNotBlank(header1) && header1.contains("filename")){
//                    String[] split = header1.split("=");
//                    String filename = null != split && split.length > 1 ? split[1] : header1;
//                    System.out.println(filename);
//                }
//                ServletOutputStream outputStream = servletResponse.getOutputStream();
//                if (outputStream instanceof CoyoteOutputStream){
//                    CoyoteOutputStream coyoteOutputStream = (CoyoteOutputStream) outputStream;
//                }
//                DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
//                int size = dataOutputStream.size();
//
//                System.out.println(header1);
//            }
//            //===================================================================
//            // 方法入参
//            HashMap<String, Object> params = AopParamsExtractUtils.extractParams(signature.getParameterNames(), args);
//            long endTime = System.currentTimeMillis();
//
//            // 组装网络请求日志消息
//            NetworkLogDetailInfo networkLogDetailInfo = NetworkLogDetailInfo.builder()
//                    .action(networkLog.action())
//                    .description(networkLog.description())
//                    .level(LogLevelConstant.INFO)
//                    .code(LogCodeEnum.OK.getCode())
//                    .message(LogCodeEnum.OK.getMessage())
//                    .threadName(Thread.currentThread().getName())
//                    .originIp(WebUtils.getIpAddress(request))
//                    .originPort(String.valueOf(request.getServerPort()))
//                    .protocol(request.getScheme())
//                    .requestUrl(request.getRequestURL().toString())
//                    .requestSource(WebUtils.judgeAgentType(request.getHeader(CommonConstant.USER_AGENT)))
//                    .contentType(request.getContentType())
//                    .requestMethod(request.getMethod())
//                    .requestTime(DateTimeUtils.formatTime(startTime))
//                    .requestParams(params)
//                    .responseTime(DateTimeUtils.formatTime(endTime))
//                    .expendTime(endTime - startTime)
//                    .responseBody(JSON.toJSONString(response, SerializerFeature.WriteMapNullValue))
//                    .build();
//
//            // 方式抛出异常
//            if (exception != null) {
//                networkLogDetailInfo.setRawLog(exception.getMessage() + Arrays.toString(exception.getStackTrace()));
//                networkLogDetailInfo.setLevel(LogLevelConstant.ERROR);
//                networkLogDetailInfo.setCode(LogCodeEnum.B0001.getCode());
//                networkLogDetailInfo.setMessage(LogCodeEnum.B0001.getMessage());
//            }
//
//            // 组装系统活动参数
//            SysTrailTypeDetailInfo sysTrailTypeDetailInfo = SysTrailTypeDetailInfo.builder()
//                    .serviceStatusEnum(ServiceStatusEnum.RUNNING.getType())
//                    .servicePostStatusEnum(logCenterProperties.getServicePostStatusEnum().getType())
//                    .build();
//            // Identification注解用来记录系统操作类型
//            // 需要和NetworkLog组合使用，只有当目标方法上有NetworkLog注解时，Identification注解才生效。
//            // 记录的内容为NetworkLog注解的action和description属性值
//            if (null != identification) {
//                Map<String, String> iden = new HashMap<>(1);
//                iden.put(networkLog.action().getType(), networkLog.description());
//                sysTrailTypeDetailInfo.setIdentification(iden);
//            }
//
//            String systemName = logCenterProperties.getSystemName();
//            String systemId = MD5Util.getSaltMD5(CommonConstant.SALT, systemName);
//            String serviceName = logCenterProperties.getServiceName();
//            String serviceId = MD5Util.getSaltMD5(CommonConstant.SALT, serviceName);
//
//            LogInfo logInfo = LogInfo.builder()
//                    .messageId(UUID.randomUUID().toString())
//                    .systemId(systemId)
//                    .systemName(systemName)
//                    .serviceId(serviceId)
//                    .serviceName(serviceName)
//                    .traceId(TraceIdUtils.getTraceId())
//                    .timestamp(DateTimeUtils.getFormatCurrentTime())
//                    .userId(request.getHeader(CommonConstant.USER_ID))
//                    .userName(request.getHeader(CommonConstant.USER_NAME))
//                    .networkLogDetailInfo(networkLogDetailInfo)
//                    .sysTrailTypeDetailInfo(sysTrailTypeDetailInfo)
//                    .build();
//            String logJsonString = JSON.toJSONString(logInfo, SerializerFeature.WriteMapNullValue);
//
//            if (exception != null) {
//                log.error(CommonConstant.LOG_CENTER_PREFIX + logJsonString);
//            } else {
//                log.info(CommonConstant.LOG_CENTER_PREFIX + logJsonString);
//            }
//
//            kafkaSender.send(logJsonString);
//        } catch (Exception exp) {
//            // 记录本地异常日志
//            log.error(CommonConstant.LOG_LOCAL_PREFIX + "网络请求日志收集异常信息:", exp);
//        }
//    }
//
//    public String extractRequestPath(JoinPoint joinPoint){
//        Annotation[] classAnnotations = joinPoint.getTarget().getClass().getAnnotations();
//        String[] classPath = null;
//        for (Annotation classAnnotation : classAnnotations) {
//            if (classAnnotation.annotationType().equals(RequestMapping.class)){
//                RequestMapping requestMapping = (RequestMapping) classAnnotation;
//                String[] path = requestMapping.path();
//                classPath = requestMapping.value();
//            }
//        }
//
//        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
//        Annotation[] methodAnnotations = signature.getMethod().getAnnotations();
//        String[] methodPath = null;
//        for (Annotation methodAnnotation : methodAnnotations) {
//            if (methodAnnotation.annotationType().equals(RequestMapping.class)){
//                RequestMapping requestMapping = (RequestMapping) methodAnnotation;
//                methodPath = requestMapping.value();
//                String[] path = requestMapping.path();
//            }else if (methodAnnotation.annotationType().equals(GetMapping.class)){
//                GetMapping getMapping = (GetMapping) methodAnnotation;
//                methodPath = getMapping.value();
//                String[] path = getMapping.path();
//                System.out.println(path.length);
//            }else if (methodAnnotation.annotationType().equals(PostMapping.class)){
//                PostMapping postMapping = (PostMapping) methodAnnotation;
//                String[] path = postMapping.path();
//                methodPath = postMapping.value();
//
//            }else if (methodAnnotation.annotationType().equals(DeleteMapping.class)){
//                DeleteMapping deleteMapping = (DeleteMapping) methodAnnotation;
//                methodPath = deleteMapping.value();
//                String[] path = deleteMapping.path();
//
//            }else if (methodAnnotation.annotationType().equals(PutMapping.class)){
//                PutMapping putMapping = (PutMapping) methodAnnotation;
//                methodPath = putMapping.value();
//                String[] path = putMapping.path();
//
//            }else if (methodAnnotation.annotationType().equals(PatchMapping.class)){
//                PatchMapping patchMapping = (PatchMapping) methodAnnotation;
//                methodPath = patchMapping.value();
//                String[] path = patchMapping.path();
//
//            }
//        }
//
//        StringBuilder path = new StringBuilder();
//        if (null != classPath){
//            for (String s : classPath) {
//                if (StringUtils.isNotBlank(s) && !s.equals("/")){
//                    path.append(s);
//                }
//            }
//        }
//        if (null != methodPath){
//            for (String s : methodPath) {
//                if (StringUtils.isNotBlank(s) && !s.equals("/")){
//                    path.append(s);
//                }
//            }
//        }
//        return path.toString();
//    }
//
//    private void test(Function<Object,String[]> function){}
//
//}
