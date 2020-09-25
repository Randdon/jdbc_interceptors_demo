//package com.zhouyuan.space.demo.filter;
//
//import com.alibaba.druid.filter.FilterEventAdapter;
//import com.alibaba.druid.proxy.jdbc.StatementProxy;
//import org.springframework.stereotype.Component;
//
//@Component
//public class TestFilter extends FilterEventAdapter {
//    @Override
//    protected void statementExecuteAfter(StatementProxy statement, String sql, boolean result) {
//        int i = 1 / 0;
//        System.out.println("this is Druid ==========================================haha");
//        super.statementExecuteAfter(statement, sql, result);
//    }
//
//}
