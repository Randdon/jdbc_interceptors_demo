package com.zhouyuan.space.demo.entity;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 日志中心配置类
 *
 * @author yuanhao
 * @since 2020/9/24
 */
@Component
@ConfigurationProperties(prefix = "log.center.local")
public class LogCenter {

    /**
     * 系统名称
     */
    private String systemName;

    /**
     * 服务名称
     */
    private String serviceName;

    /**
     * 服务版本
     */
    private String version;

    /**
     * 服务访问地址
     */
    private String url;

    /**
     * 服务访问端口
     */
    private String port;

    /**
     * 服务健康检查接口访问地址 在服务的访问路径后面跟上health即可，
     * 确保服务启动后get请求调用该接口能正常返回200
     */
    private String checkHealthUrl;

    /**
     * 0:全部拦截
     * 1：只拦截出错sql
     * 2：只拦截执行成功的sql
     */
    private Integer sqlType;

    private Boolean aBoolean;

    private Data data;

    public LogCenter(Data data1) {
        this.data = data1;
    }

    public LogCenter() {
    }

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getCheckHealthUrl() {
        return checkHealthUrl;
    }

    public void setCheckHealthUrl(String checkHealthUrl) {
        this.checkHealthUrl = checkHealthUrl;
    }

    public Integer getSqlType() {
        return sqlType;
    }

    public void setSqlType(Integer sqlType) {
        this.sqlType = sqlType;
    }

    public Boolean getaBoolean() {
        return aBoolean;
    }

    public void setaBoolean(Boolean aBoolean) {
        this.aBoolean = aBoolean;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }
}
