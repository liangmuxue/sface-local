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

    private String tenantId;
    private String tenantName;
    private String serverHttp;
    private String userName;
    private String password;
    private String captureUrl;
    private String webSocketUrl;
    private String webSocketUrlLL;
    private String userNameLL;
    private String passwordLL;


    public PropertiesUtil() {
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getTenantName() {
        return tenantName;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

    public String getServerHttp() {
        return serverHttp;
    }

    public void setServerHttp(String serverHttp) {
        this.serverHttp = serverHttp;
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

    public String getWebSocketUrl() {
        return webSocketUrl;
    }

    public void setWebSocketUrl(String webSocketUrl) {
        this.webSocketUrl = webSocketUrl;
    }

    public String getWebSocketUrlLL() {
        return webSocketUrlLL;
    }

    public void setWebSocketUrlLL(String webSocketUrlLL) {
        this.webSocketUrlLL = webSocketUrlLL;
    }

    public String getUserNameLL() {
        return userNameLL;
    }

    public void setUserNameLL(String userNameLL) {
        this.userNameLL = userNameLL;
    }

    public String getPasswordLL() {
        return passwordLL;
    }

    public void setPasswordLL(String passwordLL) {
        this.passwordLL = passwordLL;
    }
}
