package com.zhouyuan.space.demo;

import com.alibaba.druid.filter.Filter;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.casic.log.OracleDruidFilter;
import com.zhouyuan.space.demo.filter.TestFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication(scanBasePackages = "com.zhouyuan.space.demo")
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Bean
    public DataSource dataSourceOne(Environment env){
        DruidDataSource dataSource = DruidDataSourceBuilder
                .create()
                .build(env, "spring.datasource");
        List<Filter> list = new ArrayList<>(1);
        list.add(new OracleDruidFilter());
        dataSource.setProxyFilters(list);
        return dataSource;
    }
}
