package com.ss.sdk.pojo.terminal.request;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

public class PersonInfoLists {

    @JSONField(name = "PersonID")
    private Integer PersonID;
    @JSONField(name = "LastChange")
    private Long LastChange;
    @JSONField(name = "PersonCode")
    private String PersonCode;
    @JSONField(name = "PersonName")
    private String PersonName;
    @JSONField(name = "Remarks")
    private String Remarks;
    @JSONField(name = "TimeTemplateNum")
    private Integer TimeTemplateNum;
    @JSONField(name = "IdentificationNum")
    private Integer IdentificationNum;
    @JSONField(name = "IdentificationList")
    private List<IdentificationLists> IdentificationList;
    @JSONField(name = "ImageNum")
    private Integer ImageNum;
    @JSONField(name = "ImageList")
    private List<ImageLists> ImageList;

    public Integer getPersonID() {
        return PersonID;
    }

    public void setPersonID(Integer personID) {
        PersonID = personID;
    }

    public Long getLastChange() {
        return LastChange;
    }

    public void setLastChange(Long lastChange) {
        LastChange = lastChange;
    }

    public String getPersonCode() {
        return PersonCode;
    }

    public void setPersonCode(String personCode) {
        PersonCode = personCode;
    }

    public String getPersonName() {
        return PersonName;
    }

    public void setPersonName(String personName) {
        PersonName = personName;
    }

    public String getRemarks() {
        return Remarks;
    }

    public void setRemarks(String remarks) {
        Remarks = remarks;
    }

    public Integer getTimeTemplateNum() {
        return TimeTemplateNum;
    }

    public void setTimeTemplateNum(Integer timeTemplateNum) {
        TimeTemplateNum = timeTemplateNum;
    }

    public Integer getIdentificationNum() {
        return IdentificationNum;
    }

    public void setIdentificationNum(Integer identificationNum) {
        IdentificationNum = identificationNum;
    }

    public List<IdentificationLists> getIdentificationList() {
        return IdentificationList;
    }

    public void setIdentificationList(List<IdentificationLists> identificationList) {
        IdentificationList = identificationList;
    }

    public Integer getImageNum() {
        return ImageNum;
    }

    public void setImageNum(Integer imageNum) {
        ImageNum = imageNum;
    }

    public List<ImageLists> getImageList() {
        return ImageList;
    }

    public void setImageList(List<ImageLists> imageList) {
        ImageList = imageList;
    }
}
