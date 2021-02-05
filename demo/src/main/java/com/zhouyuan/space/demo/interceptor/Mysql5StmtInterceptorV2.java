//package com.zhouyuan.space.demo.interceptor;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.serializer.SerializerFeature;
//import com.casic.htzy.log.common.constant.stater.DataSourceTypeEnum;
//import com.casic.htzy.log.common.constant.stater.LogLevelConstant;
//import com.casic.htzy.log.common.dto.starter.DataLogDetailInfo;
//import com.casic.htzy.log.common.dto.starter.LogInfo;
//import com.casic.htzy.log.common.dto.starter.SysTrailTypeDetailInfo;
//import com.casic.htzy.log.common.utils.DateTimeUtils;
//import com.casic.htzy.log.common.utils.MD5Util;
//import com.casic.htzy.log.config.LogCenterLocalProperties;
//import com.casic.htzy.log.constant.CommonConstant;
//import com.casic.htzy.log.constant.LogCodeEnum;
//import com.casic.htzy.log.constant.ServiceStatusEnum;
//import com.casic.htzy.log.service.KafkaSender;
//import com.casic.htzy.log.utils.*;
//import com.mysql.jdbc.*;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.web.context.request.RequestContextHolder;
//import org.springframework.web.context.request.ServletRequestAttributes;
//
//import javax.servlet.http.HttpServletRequest;
//import java.sql.SQLException;
//import java.time.LocalDateTime;
//import java.time.ZoneId;
//import java.time.ZonedDateTime;
//import java.util.Arrays;
//import java.util.Properties;
//import java.util.UUID;
//
///**
// * MySQL5.0 sql拦截器V2 可同时拦截sql语句和异常
// *
// * @author yuandong
// * @since 2020/9/24
// */
//public class Mysql5StmtInterceptorV2 implements StatementInterceptorV2 {
//    /**
//     * 数据库连接
//     */
//    private Connection connection;
//
//    /**
//     * 数据库连接属性
//     */
//    private Properties properties;
//
//    private static final Logger logger = LoggerFactory.getLogger(Mysql5StmtInterceptorV2.class);
//
//    /**
//     * sql执行开始时间
//     */
//    private long startTime = 0L;
//
//    /**
//     * 日志中心配置
//     */
//    private  LogCenterLocalProperties logCenterProperties;
//
//    /**
//     * kafka消息发送器
//     */
//    private KafkaSender kafkaSender;
//
//    private LogCenterLocalProperties getLogCenterProperties(){
//        if (null == logCenterProperties){
//
//            logCenterProperties = SpringContextUtil.getBean(LogCenterLocalProperties.class);
//            return logCenterProperties;
//        }
//        return logCenterProperties;
//    }
//
//    private KafkaSender getKafkaSender(){
//        if (null == kafkaSender){
//            kafkaSender = SpringContextUtil.getBean(KafkaSender.class);
//            return kafkaSender;
//        }
//        return kafkaSender;
//    }
//
//    /**
//     * 初始化
//     *
//     * @param connection
//     * @param properties
//     * @throws SQLException
//     */
//    @Override
//    public void init(Connection connection, Properties properties) throws SQLException {
//        // Connection
//        if (connection instanceof ConnectionImpl) {
//            this.connection = connection;
//            this.properties = properties;
//        }
//
//    }
//
//    /**
//     * 只在顶级执行
//     *
//     * @return
//     */
//    @Override
//    public boolean executeTopLevelOnly() {
//        return false;
//    }
//
//
//    /**
//     * 执行请求前
//     * <p>
//     * 可以在这里篡改执行的sql语句
//     *
//     * @param sql
//     * @param interceptedStatement
//     * @param connection
//     * @return 如果返回的不是为null，那么不会发起远程的请求。而且不会执行"Interceptor链条"后面的Interceptor
//     * @throws SQLException
//     */
//    @Override
//    public ResultSetInternalMethods preProcess(String sql, Statement interceptedStatement, Connection connection) throws SQLException {
//        startTime = System.currentTimeMillis();
//        return null;
//    }
//
//    /**
//     * 销毁
//     */
//    @Override
//    public void destroy() {
//        this.connection = null;
//        this.properties = null;
//        this.startTime = 0L;
//    }
//
//    /**
//     * sql执行拦截
//     * @param sql sql语句
//     * @param interceptedStatement
//     * @param originalResultSet
//     * @param connection
//     * @param warningCount
//     * @param noIndexUsed
//     * @param noGoodIndexUsed
//     * @param statementException 异常对象
//     * @return
//     * @throws SQLException
//     */
//    @Override
//    public ResultSetInternalMethods postProcess(String sql, Statement interceptedStatement,
//                                                ResultSetInternalMethods originalResultSet, Connection connection,
//                                                int warningCount, boolean noIndexUsed, boolean noGoodIndexUsed,
//                                                SQLException statementException) throws SQLException {
//
//        if (interceptedStatement instanceof com.mysql.jdbc.PreparedStatement) {
//            sql = ((PreparedStatement) interceptedStatement).asSql(true);
//        }
//
//        //拦截过滤
//        if (StringUtils.isNullOrEmpty(sql)
//                || sql.contains("@@")
//                || StringUtils.startsWithIgnoreCase(sql, "SET")
//                || StringUtils.startsWithIgnoreCase(sql, "SHOW")
//                || StringUtils.startsWithIgnoreCase(sql, "COMMIT")
//                || StringUtils.startsWithIgnoreCase(sql, "USE")) {
//            return originalResultSet;
//        }
//
//        LogCenterLocalProperties logCenterProperties = getLogCenterProperties();
//        if (null == logCenterProperties){
//            return originalResultSet;
//        }
//
//        //获取用户配置的拦截sql类型
//        Boolean sqlType = logCenterProperties.getJustFilterErrorSql();
//
//        if (null != sqlType){
//
//            if (sqlType && null == statementException){
//                //如果用户配置只拦截异常sql，但进入此拦截器的sql未报错，则返回
//                return originalResultSet;
//            }
//
//            if (!sqlType && null != statementException){
//                //如果用户配置只拦截正常sql，但进入此拦截器的sql报错，则返回
//                return originalResultSet;
//            }
//        }
//
//        if (interceptedStatement instanceof PreparedStatement) {
//            sql = ((PreparedStatement) interceptedStatement).asSql(true);
//        }
//
//        //默认正常执行的情况
//        String code = LogCodeEnum.OK.getCode();
//        String message = LogCodeEnum.OK.getMessage();
//        String level = LogLevelConstant.INFO;
//        String errorMsg = CommonConstant.EMPTY_STR;
//        if (null != statementException) {
//            //sql执行出错的情况
//            level = LogLevelConstant.ERROR;
//            errorMsg = statementException.getMessage() + Arrays.toString(statementException.getStackTrace());
//            //获取供应商特定的异常编码
//            code = String.valueOf(statementException.getErrorCode());
//            //获取SQL标准中的异常编码
//            message = statementException.getSQLState();
//        }
//
//        if (connection instanceof ConnectionImpl) {
//            ConnectionImpl connectionImpl = (ConnectionImpl) connection;
//
//            //获取当前时间
//            LocalDateTime now = LocalDateTime.now();
//
//            //获取请求信息
//            HttpServletRequest request = ((ServletRequestAttributes)
//                    RequestContextHolder.getRequestAttributes()).getRequest();
//
//            //组装数据库日志实体
//            String dataSource = connectionImpl.getHostPortPair() + "/" + connectionImpl.getCatalog();
//            DataLogDetailInfo dataLogDetailInfo = new DataLogDetailInfo(level,code,
//                    message, dataSource, DataSourceTypeEnum.MYSQL5.getType(),
//                    sql,DateTimeUtils.LocalDateTime2Long(now) - startTime, Thread.currentThread().getName());
//            dataLogDetailInfo.setRawLog(errorMsg);
//
//            //系统名称，根据系统名称进行MD5加密得到系统id
//            String systemName = logCenterProperties.getSystemName();
//            String systemId = MD5Util.getSaltMD5(CommonConstant.SALT, systemName);
//            //服务名称，根据服务名称进行MD5加密得到服务id
//            String serviceName = logCenterProperties.getServiceName();
//            String serviceId = MD5Util.getSaltMD5(CommonConstant.SALT, serviceName);
//
//            //组装系统活动过程实体
//            //组装系统活动过程实体
//            SysTrailTypeDetailInfo sysTrailTypeDetailInfo = new SysTrailTypeDetailInfo(ServiceStatusEnum.RUNNING.getType(),
//                    logCenterProperties.getServicePostStatusEnum().getType());
//
//            //组装日志实体类
//            LogInfo logInfo = new LogInfo(UUID.randomUUID().toString(),systemName,systemId,serviceName,serviceId,
//                    TraceIdUtils.getTraceId(), ZonedDateTime.now(ZoneId.of("Asia/Shanghai")),
//                    SystemUtils.getPid());
//            logInfo.setUserId(request.getHeader(CommonConstant.USER_CODE));
//            logInfo.setUserName(request.getHeader(CommonConstant.USER_NAME));
//            logInfo.setSysTrailTypeDetailInfo(sysTrailTypeDetailInfo);
//            logInfo.setDataLogDetailInfo(dataLogDetailInfo);
//
//            String result;
//            try {
//                result = JSON.toJSONString(logInfo, SerializerFeature.WriteMapNullValue);
//            } catch (Exception e) {
//                logger.error(CommonConstant.LOG_LOCAL_PREFIX + "JSON转换出错，实体类为：{}", logInfo.toString(), e);
//                return originalResultSet;
//            }
//
//            if (null == statementException){
//                //sql正常执行的情况
//                logger.info(CommonConstant.LOG_CENTER_PREFIX + result);
//            } else {
//                //sql执行出错的情况
//                logger.error(CommonConstant.LOG_CENTER_PREFIX + result);
//            }
//
//            try {
//                getKafkaSender().send(result);
//            } catch (Exception e) {
//                logger.error(CommonConstant.LOG_LOCAL_PREFIX + "Kafka发送sql日志失败，logInfo：{}，Kafka`s topic：{}", result, systemName, e);
//            }
//        }
//
//        return originalResultSet;
//    }
//
//}
