package com.ss.sdk.pojo.terminal.model;

/**
 *
 *
 * @author zhangao
 * @create 2020/12/14
 * @email zhangao@ss-cas.com
 **/
public class SfaceCapture {

    private String peopleId;
    private String deviceId;
    private Integer opendoorMode;
    private Float recogScore;
    private Integer resultCode;
    private String captureUrl;
    private String captureFullUrl;
    private Long compareDate;
    private float temp;
    private Integer tempState;
    private Long createTime;

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

    public Float getRecogScore() {
        return recogScore;
    }

    public void setRecogScore(Float recogScore) {
        this.recogScore = recogScore;
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

    public Long getCompareDate() {
        return compareDate;
    }

    public void setCompareDate(Long compareDate) {
        this.compareDate = compareDate;
    }

    public float getTemp() {
        return temp;
    }

    public void setTemp(float temp) {
        this.temp = temp;
    }

    public Integer getTempState() {
        return tempState;
    }

    public void setTempState(Integer tempState) {
        this.tempState = tempState;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }
}
