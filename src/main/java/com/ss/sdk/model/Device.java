package com.ss.sdk.model;

/**
 * 设备类
 * @author 李爽超 chao
 * @create 2019/12/11
 * @email lishuangchao@ss-cas.com
 **/
public class Device {

    private Integer id;
    private String deviceId;
    private String cplatDeviceId;
    private String ip;
    private Integer port;
    private String userName;
    private String password;
    private Integer state;
    private Integer isDelete;
    private String productCode;
    private Integer cameraState;
    private Integer deviceType;
    private Integer deviceTypeDetail;
    private Integer deviceFunction;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getCplatDeviceId() {
        return cplatDeviceId;
    }

    public void setCplatDeviceId(String cplatDeviceId) {
        this.cplatDeviceId = cplatDeviceId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
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

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public Integer getCameraState() {
        return cameraState;
    }

    public void setCameraState(Integer cameraState) {
        this.cameraState = cameraState;
    }

    public Integer getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(Integer deviceType) {
        this.deviceType = deviceType;
    }

    public Integer getDeviceTypeDetail() {
        return deviceTypeDetail;
    }

    public void setDeviceTypeDetail(Integer deviceTypeDetail) {
        this.deviceTypeDetail = deviceTypeDetail;
    }

    public Integer getDeviceFunction() {
        return deviceFunction;
    }

    public void setDeviceFunction(Integer deviceFunction) {
        this.deviceFunction = deviceFunction;
    }
}
