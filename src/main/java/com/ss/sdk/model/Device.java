package com.ss.sdk.model;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 设备类
 * @author 李爽超 chao
 * @create 2019/12/11
 * @email lishuangchao@ss-cas.com
 **/
@Table(name = "sface_device")
public class Device {

    @Id
    private Integer id;
    @Column(name = "device_id")
    private String deviceId;
    @Column(name = "product_code")
    private String productCode;
    private String ip;
    private Integer port;
    @Column(name = "user_name")
    private String userName;
    private String password;
    private Integer state;
    @Column(name = "is_delete")
    private Integer isDelete;
    @Column(name = "device_type")
    private Integer deviceType;
    @Column(name = "device_type_detail")
    private Integer deviceTypeDetail;
    @Transient
    private Integer cameraState;

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

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
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

    public Integer getCameraState() {
        return cameraState;
    }

    public void setCameraState(Integer cameraState) {
        this.cameraState = cameraState;
    }
}
