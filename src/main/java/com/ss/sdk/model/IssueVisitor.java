package com.ss.sdk.model;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 下发设备类
 * @author 李爽超 chao
 * @create 2019/12/11
 * @email lishuangchao@ss-cas.com
 **/
@Table(name = "sface_visitor")
public class IssueVisitor {

    @Id
    private Integer id;
    @Column(name = "people_id")
    private String peopleId;
    @Column(name = "people_face_path")
    private String peopleFacePath;
    @Column(name = "issue_time")
    private Long issueTime;
    @Column(name = "return_result")
    private Integer returnResult;
    @Column(name = "issue_status")
    private Integer issueStatus;
    @Column(name = "fail_reason")
    private String failReason;
    @Transient
    private String productCode;
    @Transient
    private Integer taskType;
    @Transient
    private String peopleName;
    @Transient
    private String cardId;
    @Column(name = "device_id")
    private String deviceId;
    @Transient
    private String devicePeopleId;
    @Transient
    private String code;
    @Transient
    private Long visitorTime;
    @Transient
    private Long leaveTime;

    public Long getLeaveTime() {
        return leaveTime;
    }

    public void setLeaveTime(Long leaveTime) {
        this.leaveTime = leaveTime;
    }

    public Long getVisitorTime() {
        return visitorTime;
    }

    public void setVisitorTime(Long visitorTime) {
        this.visitorTime = visitorTime;
    }

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

    public String getPeopleFacePath() {
        return peopleFacePath;
    }

    public void setPeopleFacePath(String peopleFacePath) {
        this.peopleFacePath = peopleFacePath;
    }

    public Long getIssueTime() {
        return issueTime;
    }

    public void setIssueTime(Long issueTime) {
        this.issueTime = issueTime;
    }

    public Integer getReturnResult() {
        return returnResult;
    }

    public void setReturnResult(Integer returnResult) {
        this.returnResult = returnResult;
    }

    public Integer getIssueStatus() {
        return issueStatus;
    }

    public void setIssueStatus(Integer issueStatus) {
        this.issueStatus = issueStatus;
    }

    public String getFailReason() {
        return failReason;
    }

    public void setFailReason(String failReason) {
        this.failReason = failReason;
    }


    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public Integer getTaskType() {
        return taskType;
    }

    public void setTaskType(Integer taskType) {
        this.taskType = taskType;
    }

    public String getPeopleName() {
        return peopleName;
    }

    public void setPeopleName(String peopleName) {
        this.peopleName = peopleName;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDevicePeopleId() {
        return devicePeopleId;
    }

    public void setDevicePeopleId(String devicePeopleId) {
        this.devicePeopleId = devicePeopleId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
