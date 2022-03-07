package com.ss.sdk.model;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 抓拍信息类
 * @author 李爽超 chao
 * @create 2019/12/16
 * @email lishuangchao@ss-cas.com
 **/
@Table(name = "sface_capture")
public class Capture {

    @Id
    private Integer id;
    @Column(name = "people_id")
    private String peopleId;
    @Column(name = "product_code")
    private String productCode;
    @Column(name = "opendoor_mode")
    private Integer opendoorMode;
    @Column(name = "recog_score")
    private Float recogScore;
    @Column(name = "result_code")
    private Integer resultCode;
    @Column(name = "capture_url")
    private String captureUrl;
    @Column(name = "capture_full_url")
    private String captureFullUrl;
    @Column(name = "compare_date")
    private Long compareDate;
    private Double temp;
    @Column(name = "temp_state")
    private Integer tempState;
    @Column(name = "create_time")
    private Long createTime;
    private String code;
    @Transient
    private String captureBase64;
    @Transient
    private String fullBase64;

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

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
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

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCaptureBase64() {
        return captureBase64;
    }

    public void setCaptureBase64(String captureBase64) {
        this.captureBase64 = captureBase64;
    }

    public String getFullBase64() {
        return fullBase64;
    }

    public void setFullBase64(String fullBase64) {
        this.fullBase64 = fullBase64;
    }
}
