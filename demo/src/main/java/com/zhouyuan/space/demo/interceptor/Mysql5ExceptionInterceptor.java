//package com.zhouyuan.space.demo.interceptor;
//
//import com.mysql.jdbc.Connection;
//import com.mysql.jdbc.ConnectionImpl;
//import com.mysql.jdbc.ExceptionInterceptor;
//import com.mysql.jdbc.profiler.ProfilerEventHandler;
//
//import java.sql.SQLException;
//import java.sql.Statement;
//import java.util.Properties;
//
//public class Mysql5ExceptionInterceptor implements ExceptionInterceptor {
//
//    private Connection connection;
//    private Properties properties;
//
//    @Override
//    public SQLException interceptException(SQLException sqlEx, Connection conn) {
//        if (conn instanceof com.mysql.jdbc.ConnectionImpl) {
//            ConnectionImpl connectionImpl = (ConnectionImpl)conn;
//            Properties properties = connectionImpl.getProperties();
//            try {
//                Statement metadataSafeStatement = connectionImpl.getMetadataSafeStatement();
//                ProfilerEventHandler profilerEventHandlerInstance = connectionImpl.getProfilerEventHandlerInstance();
//                System.out.println("test");
//            } catch (SQLException throwables) {
//                throwables.printStackTrace();
//            }
//        }
//        return sqlEx;
//    }
//
//    @Override
//    public void init(Connection conn, Properties props) throws SQLException {
//        if (connection instanceof com.mysql.jdbc.ConnectionImpl) {
//            //System.out.println("Enter here ==========================================");
//
//            this.connection = connection;
//            this.properties = properties;
//        }
//    }
//
//    @Override
//    public void destroy() {
//        this.connection = null;
//        this.properties = null;
//
//    }
//}
