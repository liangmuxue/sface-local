/*
 * Copyright (c) 2018, ZheJiang Uniview Technologies Co., Ltd. All rights reserved.
 * <http://www.uniview.com/>
 *------------------------------------------------------------------------------
 * Product     : 速通门
 * Module Name : com.unv.fastgate.server.service
 * Date Created: 2019/5/13
 * Creator     : dW5565 dongchenghao
 * Description :
 *
 *------------------------------------------------------------------------------
 * Modification History
 * DATE        NAME             DESCRIPTION
 *------------------------------------------------------------------------------
 *------------------------------------------------------------------------------
 */
package com.ss.sdk.pojo.terminal.person;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

/**
 * description
 *
 * @author dW5565
 */
public class PersonInfo {

    @JSONField(name = "PersonID", ordinal = 1)
    private Long personID;
    @JSONField(name = "LastChange", ordinal = 2)
    private Long lastChange;
    @JSONField(name = "PersonName", ordinal = 3)
    private String personName;
    @JSONField(name = "Gender", ordinal = 4)
    private Integer gender;
    @JSONField(name = "TimeTemplate", ordinal = 5)
    private PersonTimeTemplateInfo timeTemplate;
    @JSONField(name = "IdentificationNum", ordinal = 6)
    private Integer identificationNum;
    @JSONField(name = "IdentificationList", ordinal = 7)
    private List<IdentificationInfo> identificationList;
    @JSONField(name = "ImageNum", ordinal = 8)
    private Integer imageNum;
    @JSONField(name = "ImageList", ordinal = 9)
    private List<PersonImageInfo> imageList;

    public Long getPersonID() {
        return personID;
    }

    public void setPersonID(Long personID) {
        this.personID = personID;
    }

    public Long getLastChange() {
        return lastChange;
    }

    public void setLastChange(Long lastChange) {
        this.lastChange = lastChange;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public PersonTimeTemplateInfo getTimeTemplate() {
        return timeTemplate;
    }

    public void setTimeTemplate(PersonTimeTemplateInfo timeTemplate) {
        this.timeTemplate = timeTemplate;
    }

    public Integer getIdentificationNum() {
        return identificationNum;
    }

    public void setIdentificationNum(Integer identificationNum) {
        this.identificationNum = identificationNum;
    }

    public List<IdentificationInfo> getIdentificationList() {
        return identificationList;
    }

    public void setIdentificationList(List<IdentificationInfo> identificationList) {
        this.identificationList = identificationList;
    }

    public Integer getImageNum() {
        return imageNum;
    }

    public void setImageNum(Integer imageNum) {
        this.imageNum = imageNum;
    }

    public List<PersonImageInfo> getImageList() {
        return imageList;
    }

    public void setImageList(List<PersonImageInfo> imageList) {
        this.imageList = imageList;
    }
}
