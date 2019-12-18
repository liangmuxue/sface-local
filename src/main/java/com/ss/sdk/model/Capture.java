package com.ss.sdk.model;

/**
 * 抓拍信息类
 * @author 李爽超 chao
 * @create 2019/12/16
 * @email lishuangchao@ss-cas.com
 **/
public class Capture {

    private Integer id;
    private Integer peopleId;
    private String deviceId;
    private Integer opendoorMode;
    private Integer resultCode;
    private String captureUrl;
    private String compareDate;
    private String productCode;
    private String spotImgPath;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPeopleId() {
        return peopleId;
    }

    public void setPeopleId(Integer peopleId) {
        this.peopleId = peopleId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public Integer getOpendoorMode() {
        return opendoorMode;
    }

    public void setOpendoorMode(Integer opendoorMode) {
        this.opendoorMode = opendoorMode;
    }

    public Integer getResultCode() {
        return resultCode;
    }

    public void setResultCode(Integer resultCode) {
        this.resultCode = resultCode;
    }

    public String getCaptureUrl() {
        return captureUrl;
    }

    public void setCaptureUrl(String captureUrl) {
        this.captureUrl = captureUrl;
    }

    public String getCompareDate() {
        return compareDate;
    }

    public void setCompareDate(String compareDate) {
        this.compareDate = compareDate;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getSpotImgPath() {
        return spotImgPath;
    }

    public void setSpotImgPath(String spotImgPath) {
        this.spotImgPath = spotImgPath;
    }
}
