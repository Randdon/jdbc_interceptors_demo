package com.zhouyuan.space.demo.filter;

import com.alibaba.druid.filter.FilterEventAdapter;
import com.alibaba.druid.proxy.jdbc.JdbcParameter;
import com.alibaba.druid.proxy.jdbc.StatementProxy;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.casic.log.config.LogCenterProperties;
import com.casic.log.constant.CommonConstant;
import com.casic.log.constant.LogCodeEnum;
import com.casic.log.constant.LogLevelConstant;
import com.casic.log.domain.DataLogDetailInfo;
import com.casic.log.domain.LogInfo;
import com.casic.log.service.KafkaSender;
import com.casic.log.utils.DateTimeUtils;
import com.casic.log.utils.MD5Util;
import com.casic.log.utils.SpringContextUtil;
import com.casic.log.utils.TraceIdUtils;
import oracle.jdbc.driver.OracleConnection;
import org.apache.commons.collections4.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

public class TestFilter extends FilterEventAdapter {

    private static LogCenterProperties logCenterProperties;

    private static KafkaSender kafkaSender;

    private static final Logger logger = LoggerFactory.getLogger(TestFilter.class);

/*    @Override
    public PreparedStatementProxy connection_prepareStatement(FilterChain chain, ConnectionProxy connection, String sql) throws SQLException {
        System.out.println(sql);
        if (connection instanceof ConnectionProxyImpl){
            DruidDataSource dataSource = (DruidDataSource) connection.getDirectDataSource();
            Throwable lastError = dataSource.getLastError();
            System.out.println(null == lastError);
        }
        return super.connection_prepareStatement(chain, connection, sql);
    }

    @Override
    protected void statementExecuteAfter(StatementProxy statement, String sql, boolean result) {
        System.out.println("this is Druid ==========================================haha");
        String lastExecuteSql = statement.getLastExecuteSql();
        System.out.println(lastExecuteSql);
        long lastExecuteTimeNano = statement.getLastExecuteTimeNano();
        System.out.println(lastExecuteTimeNano);
        super.statementExecuteAfter(statement, sql, result);
    }

    @Override
    protected void statementPrepareAfter(PreparedStatementProxy statement) {
        String lastExecuteSql = statement.getLastExecuteSql();
        System.out.println(lastExecuteSql);
        Map<Integer, JdbcParameter> parameters = statement.getParameters();
        super.statementPrepareAfter(statement);
        System.out.println(statement.getSql());
    }*/

    @Override
    protected void statementExecuteAfter(StatementProxy statement, String sql, boolean result) {
        try {
            Connection connection = statement.getConnection();
            if (connection instanceof OracleConnection){
                OracleConnection oracleConnection = (OracleConnection) connection;

                Properties properties = oracleConnection.getProperties();

                //String currentSchema = oracleConnection.getCurrentSchema();

                long lastExecuteTimeNano = statement.getLastExecuteTimeNano();
                Map<Integer, JdbcParameter> parameters = statement.getParameters();

                logAndSend(null, properties, sql, lastExecuteTimeNano, parameters);
            }

        } catch (SQLException throwables) {
            logger.error("记录异常sql出错，sql：{}",sql,throwables);
        }
    }

    @Override
    protected void statement_executeErrorAfter(StatementProxy statement, String sql, Throwable error) {
        try {
            Connection connection = statement.getConnection();
            if (connection instanceof OracleConnection){
                OracleConnection oracleConnection = (OracleConnection) connection;

                Properties properties = oracleConnection.getProperties();

                //String currentSchema = oracleConnection.getCurrentSchema();

                long lastExecuteTimeNano = statement.getLastExecuteTimeNano();
                Map<Integer, JdbcParameter> parameters = statement.getParameters();

                logAndSend(error, properties, sql, lastExecuteTimeNano, parameters);
            }

        } catch (SQLException throwables) {
            logger.error("记录异常sql出错，sql：{}",sql,throwables);
        }
    }


    private void logAndSend(Throwable e, Properties properties, String sql, long lastExecuteTimeNano,
                            Map<Integer, JdbcParameter> parameters){

        String dataBase = null != properties ?
                properties.getProperty("database",CommonConstant.EMPTY_STR) : CommonConstant.EMPTY_STR;

        String code = LogCodeEnum.OK.getCode();
        String message = LogCodeEnum.OK.getMessage();
        String level = LogLevelConstant.INFO;
        String errorMsg = CommonConstant.EMPTY_STR;

        if (null != e) {
            errorMsg = e.getMessage() + Arrays.toString(e.getStackTrace());
            level = LogLevelConstant.ERROR;
            if (e instanceof SQLException){
                SQLException sqlException = (SQLException) e;
                code = String.valueOf(sqlException.getErrorCode());
                message = sqlException.getSQLState();
            } else{
                code = LogCodeEnum.C0300.getCode();
                message = LogCodeEnum.C0300.getMessage();
            }
        }

        if (MapUtils.isNotEmpty(parameters)){
            sql += "\n parameters: " + JSON.toJSONString(parameters);
        }


        DataLogDetailInfo dataBaseLogInfo = DataLogDetailInfo.builder()
                .code(code)
                .message(message)
                .level(level)
                .dataSource(dataBase)
                .threadName(Thread.currentThread().getName())
                .sql(sql)
                .sqlTime(lastExecuteTimeNano / 1000000L)
                .errorMsg(errorMsg)
                .build();

        if (null == logCenterProperties){
            logCenterProperties = SpringContextUtil.getBean(LogCenterProperties.class);
        }

        String systemName = logCenterProperties.getSystemName();
        String systemId = MD5Util.getSaltMD5(CommonConstant.SALT, systemName);
        String serverName = logCenterProperties.getServiceName();
        String serverId = MD5Util.getSaltMD5(CommonConstant.SALT, serverName);
        LocalDateTime now = LocalDateTime.now();
        HttpServletRequest request = ((ServletRequestAttributes)
                RequestContextHolder.getRequestAttributes()).getRequest();

        LogInfo logInfo = LogInfo.builder()
                .messageId(UUID.randomUUID().toString())
                .userId(request.getHeader(CommonConstant.USER_ID))
                .systemName(systemName)
                .systemId(systemId)
                .serviceName(serverName)
                .serviceId(serverId)
                .traceId(TraceIdUtils.getTraceId())
                .timestamp(DateTimeUtils.format(now, DateTimeUtils.DATE_TIME_MILLIS))
                .dataLogDetailInfo(dataBaseLogInfo)
                .build();

        String result;
        try {
            result = JSON.toJSONString(logInfo, SerializerFeature.WriteMapNullValue);
        } catch (Exception exception) {
            logger.error("JSON转换出错，实体类为：{}", logInfo.toString(), exception);
            return;
        }

        logger.error(result);

        try {
            if (null == kafkaSender){
                kafkaSender  = SpringContextUtil.getBean(KafkaSender.class);
            }
            kafkaSender.send(result);
        } catch (Exception exception) {
            logger.error("Kafka发送sql日志失败，logInfo：{}，Kafka`s topic：{}", result, systemName, exception);
        }

    }
}
