//package com.zhouyuan.space.demo.interceptor;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.serializer.SerializerFeature;
//import com.casic.htzy.log.config.LogCenterProperties;
//import com.casic.htzy.log.constant.CommonConstant;
//import com.casic.htzy.log.constant.LogCodeEnum;
//import com.casic.htzy.log.constant.LogLevelConstant;
//import com.casic.htzy.log.domain.DataLogDetailInfo;
//import com.casic.htzy.log.domain.LogInfo;
//import com.casic.htzy.log.service.KafkaSender;
//import com.casic.htzy.log.utils.DateTimeUtils;
//import com.casic.htzy.log.utils.MD5Util;
//import com.casic.htzy.log.utils.SpringContextUtil;
//import com.casic.htzy.log.utils.TraceIdUtils;
//import com.mysql.cj.MysqlConnection;
//import com.mysql.cj.Query;
//import com.mysql.cj.conf.HostInfo;
//import com.mysql.cj.exceptions.ExceptionInterceptor;
//import com.mysql.cj.interceptors.QueryInterceptor;
//import com.mysql.cj.log.Log;
//import com.mysql.cj.protocol.Resultset;
//import com.mysql.cj.protocol.ServerSession;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.util.StringUtils;
//import org.springframework.web.context.request.RequestContextHolder;
//import org.springframework.web.context.request.ServletRequestAttributes;
//
//import javax.servlet.http.HttpServletRequest;
//import java.sql.SQLException;
//import java.time.LocalDateTime;
//import java.util.Arrays;
//import java.util.Properties;
//import java.util.UUID;
//import java.util.function.BiConsumer;
//import java.util.function.Supplier;
//
//public class Mysql8QueryInterceptor implements QueryInterceptor, ExceptionInterceptor {
//    private MysqlConnection connection;
//    private Properties properties;
//
//    private static Logger logger = LoggerFactory.getLogger(Mysql8QueryInterceptor.class);
//
//    private long startTime = 0L;
//
//    private static final LogCenterProperties logCenterProperties = SpringContextUtil.getBean(LogCenterProperties.class);
//
//    private static final KafkaSender kafkaSender = SpringContextUtil.getBean(KafkaSender.class);
//
//    private static ThreadLocal<LogInfo> threadLocal = new ThreadLocal<>();
//
//    /**
//     * 初始化
//     *
//     * @param connection
//     * @param properties
//     * @throws SQLException
//     */
//    @Override
//    public QueryInterceptor init(MysqlConnection connection, Properties properties, Log log)  {
//        logger.info("Enter into {}`s init() method.", Mysql8QueryInterceptor.class);
//        // Connection
//        if (connection instanceof MysqlConnection) {
//            //System.out.println("Enter here ==========================================");
//
//            this.connection = connection;
//            this.properties = properties;
//        }
//
//        // Properties
///*
//        {
//            System.out.println("properties.get(\"user\") = " + properties.get("user"));
//            System.out.println("properties.get(\"password\") = " + properties.get("password"));
//        }
//*/
//        return this;
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
//     * @param interceptedQuery
//     * @return 如果返回的不是为null，那么不会发起远程的请求。而且不会执行"Interceptor链条"后面的Interceptor
//     */
//    @Override
//    public <T extends Resultset> T preProcess(Supplier<String> sql, Query interceptedQuery){
///*        if (interceptedStatement instanceof com.mysql.jdbc.PreparedStatement) {
//            String preparedSql = ((PreparedStatement) interceptedStatement).getPreparedSql();
//            System.out.println("preparedSql = " + preparedSql);
//
//            String asSql = ((PreparedStatement) interceptedStatement).asSql(true);
//            System.out.println("asSql = " + asSql);
//        } else if (interceptedStatement instanceof com.mysql.jdbc.StatementImpl) {
//            if (interceptedStatement instanceof com.mysql.jdbc.Statement) {
//
//            }
//            System.out.println("sql = " + sql);
//        }
//
//        if (connection instanceof com.mysql.jdbc.ConnectionImpl) {
//            if (connection instanceof com.mysql.jdbc.MySQLConnection) {
//
//            }
//        }*/
//        startTime = System.currentTimeMillis();
//
//        return null;
//    }
//
//
//    /**
//     * 执行请求后
//     * <p>
//     * 可以在这里篡改返回的结果集合
//     *
//     * @param sql
//     * @param interceptedQuery
//     * @param originalResultSet
//     * @param serverSession
//     * @return 返回的值会作为"后面的Interceptor"的入参
//     */
//    @Override
//    public <T extends Resultset> T postProcess(Supplier<String> sql,
//                                               Query interceptedQuery,
//                                               T originalResultSet,
//                                               ServerSession serverSession){
//
//        if (StringUtils.isEmpty(sql.get())
//                || StringUtils.startsWithIgnoreCase(sql.get(), "SET")
//                || StringUtils.startsWithIgnoreCase(sql.get(), "SHOW")
//                || StringUtils.startsWithIgnoreCase(sql.get(), "USE")) {
//            return originalResultSet;
//        }
//
//        String code = LogCodeEnum.OK.getCode();
//        String message = LogCodeEnum.OK.getMessage();
//        String level = LogLevelConstant.INFO;
//        String errorMsg = CommonConstant.EMPTY_STR;
//
//        if (null == originalResultSet) {
//
//            //sql执行异常
//            code = LogCodeEnum.C0300.getCode();
//            message = LogCodeEnum.C0300.getMessage();
//            level = LogLevelConstant.ERROR;
//        }
//
//        LocalDateTime now = LocalDateTime.now();
//        HttpServletRequest request = ((ServletRequestAttributes)
//                RequestContextHolder.getRequestAttributes()).getRequest();
//
//        HostInfo hostInfo = connection.getSession().getHostInfo();
//
//        long executeTime = null == interceptedQuery ? DateTimeUtils.LocalDateTime2Long(now) - startTime :
//                interceptedQuery.getExecuteTime();
//        DataLogDetailInfo dataBaseLogInfo = DataLogDetailInfo.builder()
//                .code(code)
//                .message(message)
//                .level(level)
//                .dataSource(hostInfo.getHost() + ":" + hostInfo.getPort() + "/" + hostInfo.getDatabase())
//                //.returnValue(ResultSet2BeanUtil.dealWithResultSet(resultSetInternalMethods))
//                .threadName(Thread.currentThread().getName())
//                .sql(sql.get())
//                .sqlTime(executeTime)
//                .rawLog(errorMsg)
//                .build();
//
//        String systemName = logCenterProperties.getSystemName();
//        String systemId = MD5Util.getSaltMD5(CommonConstant.SALT, systemName);
//        String serverName = logCenterProperties.getServiceName();
//        String serverId = MD5Util.getSaltMD5(CommonConstant.SALT, serverName);
//
//        LogInfo logInfo = LogInfo.builder()
//                .messageId(UUID.randomUUID().toString())
//                .userId(request.getHeader(CommonConstant.USER_ID))
//                .systemName(systemName)
//                .systemId(systemId)
//                .serviceName(serverName)
//                .serviceId(serverId)
//                .traceId(TraceIdUtils.getTraceId())
//                .timestamp(DateTimeUtils.format(now, DateTimeUtils.DATE_TIME_MILLIS))
//                .dataLogDetailInfo(dataBaseLogInfo)
//                .build();
//
//        if (null != originalResultSet){
//            //sql正常执行，打印日志并发送Kafka
//            logJsonAndSend(logInfo,Logger::info);
//        }else {
//            //sql执行异常，先保存LogInfo信息，等待拦截异常后补充异常信息再打印并发送
//            threadLocal.set(logInfo);
//        }
//        return originalResultSet;
//    }
//
//
//    /**
//     * 打印日志并发送Kafka
//     * @param logInfo 需要打印的日志实体
//     * @param logAction 具体级别的打印动作
//     * @author yuandong
//     * @date 2020-09-25
//     */
//    private void logJsonAndSend(LogInfo logInfo, BiConsumer<Logger,String> logAction){
//        String result = null;
//        try {
//            result = JSON.toJSONString(logInfo, SerializerFeature.WriteMapNullValue);
//        } catch (Exception e) {
//            logger.error("JSON转换出错，实体类为：{}", logInfo.toString(), e);
//        }
//
//        String topic = logCenterProperties.getSystemName();
//
//        if (!StringUtils.isEmpty(result)){
//
//            //打印日志
//            logAction.accept(logger,result);
//
//            try {
//                kafkaSender.send(result);
//            } catch (Exception e) {
//                logger.error("Kafka发送sql日志失败，logInfo：{}，Kafka`s topic：{}", result, topic, e);
//            }
//        }
//    }
//
//
//    @Override
//    public ExceptionInterceptor init(Properties props, Log log) {
//        this.properties = props;
//        return this;
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
//     * 异常拦截
//     * @param sqlEx
//     * @return
//     */
//    @Override
//    public Exception interceptException(Exception sqlEx) {
//        LogInfo logInfo = threadLocal.get();
//        if (null != logInfo){
//            logInfo.getDataLogDetailInfo().setRawLog(sqlEx.getMessage() + Arrays.toString(sqlEx.getStackTrace()));
//            if (sqlEx instanceof SQLException){
//                SQLException sqlException = (SQLException) sqlEx;
//                logInfo.getDataLogDetailInfo().setCode(String.valueOf(sqlException.getErrorCode()));
//                logInfo.getDataLogDetailInfo().setRawLog(sqlException.getSQLState());
//            }
//            logJsonAndSend(logInfo,Logger::error);
//            threadLocal.remove();
//        }
//        return null;
//    }
//
//}
