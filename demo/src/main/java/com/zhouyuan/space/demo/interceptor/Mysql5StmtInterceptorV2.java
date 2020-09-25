//package com.zhouyuan.space.demo.interceptor;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.serializer.SerializerFeature;
//import com.casic.log.domain.DataBaseLogInfo;
//import com.casic.log.domain.LogInfo;
//import com.casic.log.utils.DateTimeUtils;
//import com.casic.log.utils.SpringContextUtil;
//import com.casic.log.utils.TraceIdUtils;
//import com.mysql.jdbc.*;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.core.env.Environment;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.web.context.request.RequestContextHolder;
//import org.springframework.web.context.request.ServletRequestAttributes;
//
//import javax.servlet.http.HttpServletRequest;
//import java.sql.SQLException;
//import java.time.LocalDateTime;
//import java.util.Properties;
//import java.util.UUID;
//
//public class Mysql5StmtInterceptorV2 implements StatementInterceptorV2 {
//    private Connection connection;
//    private Properties properties;
//
//    private static Logger logger = LoggerFactory.getLogger(Mysql5StmtInterceptorV2.class);
//
//    private long startTime = 0L;
//
//    private static final KafkaTemplate template = SpringContextUtil.getBean(KafkaTemplate.class);
//    private static final Environment environment = SpringContextUtil.getBean(Environment.class);
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
//        logger.info("Enter into {}`s init() method.", Mysql5StmtInterceptorV2.class);
//        // Connection
//        if (connection instanceof com.mysql.jdbc.ConnectionImpl) {
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
//    @Override
//    public ResultSetInternalMethods postProcess(String sql, Statement interceptedStatement,
//                                                ResultSetInternalMethods originalResultSet, Connection connection,
//                                                int warningCount, boolean noIndexUsed, boolean noGoodIndexUsed,
//                                                SQLException statementException) throws SQLException {
//        if (interceptedStatement instanceof com.mysql.jdbc.PreparedStatement) {
//            sql = ((PreparedStatement) interceptedStatement).asSql(true);
//        } /*else if (interceptedStatement instanceof com.mysql.jdbc.StatementImpl) {
//
//        }*/
//
//        String code = "0";
//        String message = "succeed";
//        String level = "info";
//        if (null == originalResultSet){
//            code = "1";
//            message = "failed";
//            level = "error";
//        }
//
//        if (StringUtils.isNullOrEmpty(sql)
//                || sql.contains("@@")
//                || StringUtils.startsWithIgnoreCase(sql, "SET")
//                || StringUtils.startsWithIgnoreCase(sql, "SHOW")
//                || StringUtils.startsWithIgnoreCase(sql, "COMMIT")
//                || StringUtils.startsWithIgnoreCase(sql, "USE")) {
//            return originalResultSet;
//        }
//        System.out.println("JDBC ThreadName: " + Thread.currentThread().getName());
//
//        if (connection instanceof com.mysql.jdbc.ConnectionImpl) {
//            ConnectionImpl connectionImpl = (ConnectionImpl)connection;
///*            if (connection instanceof com.mysql.jdbc.MySQLConnection) {
//
//            }*/
//            LocalDateTime now = LocalDateTime.now();
//            HttpServletRequest request = ((ServletRequestAttributes)
//                    RequestContextHolder.getRequestAttributes()).getRequest();
//
//            DataBaseLogInfo dataBaseLogInfo = DataBaseLogInfo.builder().level("info")
//                    .code(code)
//                    .message(message)
//                    .level(level)
//                    .dataSource(connectionImpl.getHostPortPair() + "/" + connectionImpl.getCatalog())
//                    //.returnValue(ResultSet2BeanUtil.dealWithResultSet(resultSetInternalMethods))
//                    .threadName(Thread.currentThread().getName())
//                    .sql(sql)
//                    .sqlTime(DateTimeUtils.LocalDateTime2Long(now)- startTime)
//                    .build();
//            LogInfo logInfo = LogInfo.builder()
//                    .messageId(UUID.randomUUID().toString())
//                    .serverId("")
//                    .systemId(environment.getProperty("spring.application.name"))
//                    .timestamp(DateTimeUtils.format(now,DateTimeUtils.DATE_TIME_MILLIS))
//                    .userId(request.getHeader("userId"))
//                    .traceId(TraceIdUtils.getTraceId())
//                    .dataBaseLogInfo(dataBaseLogInfo)
//                    .build();
//
//            String result = null;
//            try {
//                result = JSON.toJSONString(logInfo, SerializerFeature.WriteMapNullValue);
//            } catch (Exception e) {
//                logger.error("JSON转换出错，实体类为：{}",logInfo.toString(),e);
//            }
//            logger.info(result);
//
//            String topic = "topic-user";
//            try {
//                template.send(topic,result);
//            } catch (Exception e) {
//                logger.error("Kafka发送sql日志失败，logInfo：{}，Kafka`s topic：{}",result,topic,e);
//            }
//        }
//
//        return originalResultSet;
//    }
//
//}
