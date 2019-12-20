package com.ss.sdk.utils;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * 配置类
 * @author 李爽超 chao
 * @create 2019/12/12
 * @email lishuangchao@ss-cas.com
 **/
@Component
@ConfigurationProperties(prefix = "system.pram")
@PropertySource(value = "classpath:application.properties")
public class PropertiesUtil {

    private String ip;
    private String port;
    private String tenantId;
    private String cplatHttp;
    private String userName;
    private String password;
    private String captureUrl;
    private String serverIp;
    private String socketPort;
    private String webSocketUrl;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getCplatHttp() {
        return cplatHttp;
    }

    public void setCplatHttp(String cplatHttp) {
        this.cplatHttp = cplatHttp;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCaptureUrl() {
        return captureUrl;
    }

    public void setCaptureUrl(String captureUrl) {
        this.captureUrl = captureUrl;
    }

    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    public String getSocketPort() {
        return socketPort;
    }

    public void setSocketPort(String socketPort) {
        this.socketPort = socketPort;
    }

    public String getWebSocketUrl() {
        return webSocketUrl;
    }

    public void setWebSocketUrl(String webSocketUrl) {
        this.webSocketUrl = webSocketUrl;
    }
}
