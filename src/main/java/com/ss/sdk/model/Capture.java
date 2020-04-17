package com.ss.sdk.model;

/**
 * 抓拍信息类
 * @author 李爽超 chao
 * @create 2019/12/16
 * @email lishuangchao@ss-cas.com
 **/
public class Capture {

    private Integer id;
    private String peopleId;
    private String deviceId;
    private Integer opendoorMode;
    private Integer resultCode;
    private String captureUrl;
    private String captureFullUrl;
    private String compareDate;
    private String productCode;
    private String spotImgPath;
    private String panoramaPath;
    private Float recogScore;
    private Double temp;
    private Integer tempState;
    private String createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPeopleId() {
        return peopleId;
    }

    public void setPeopleId(String peopleId) {
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

    public String getCaptureFullUrl() {
        return captureFullUrl;
    }

    public void setCaptureFullUrl(String captureFullUrl) {
        this.captureFullUrl = captureFullUrl;
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

    public String getPanoramaPath() {
        return panoramaPath;
    }

    public void setPanoramaPath(String panoramaPath) {
        this.panoramaPath = panoramaPath;
    }

    public Float getRecogScore() {
        return recogScore;
    }

    public void setRecogScore(Float recogScore) {
        this.recogScore = recogScore;
    }

    public Double getTemp() {
        return temp;
    }

    public void setTemp(Double temp) {
        this.temp = temp;
    }

    public Integer getTempState() {
        return tempState;
    }

    public void setTempState(Integer tempState) {
        this.tempState = tempState;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
