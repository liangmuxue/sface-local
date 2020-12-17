/*
 * Copyright (c) 2018, ZheJiang Uniview Technologies Co., Ltd. All rights reserved.
 * <http://www.uniview.com/>
 *------------------------------------------------------------------------------
 * Product     : 速通门
 * Module Name : com.unv.fastgate.service.black.httpclient.model
 * Date Created: 2018/8/1
 * Creator     : dW5565 dongchenghao
 * Description :
 *
 *------------------------------------------------------------------------------
 * Modification History
 * DATE        NAME             DESCRIPTION
 *------------------------------------------------------------------------------
 *------------------------------------------------------------------------------
 */
package com.ss.sdk.pojo.terminal.libinfo;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * add description of types here
 *
 * @author dW5565
 */
public class SingleLibInfo {
    @JSONField(name = "ID", ordinal = 1)
    private Integer iD;
    @JSONField(name = "Name", ordinal = 2)
    private String name;
    @JSONField(name = "Type", ordinal = 3)
    private Integer type;
    @JSONField(name = "PersonNum", ordinal = 4)
    private Integer personNum;
    @JSONField(name = "FaceNum", ordinal = 5)
    private Integer faceNum;
    @JSONField(name = "MemberNum", ordinal = 6)
    private Integer memberNum;
    @JSONField(name = "LastChange", ordinal = 7)
    private Long lastChange;

    public Integer getiD() {
        return iD;
    }

    public void setiD(Integer iD) {
        this.iD = iD;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getPersonNum() {
        return personNum;
    }

    public void setPersonNum(Integer personNum) {
        this.personNum = personNum;
    }

    public Integer getFaceNum() {
        return faceNum;
    }

    public void setFaceNum(Integer faceNum) {
        this.faceNum = faceNum;
    }

    public Integer getMemberNum() {
        return memberNum;
    }

    public void setMemberNum(Integer memberNum) {
        this.memberNum = memberNum;
    }

    public Long getLastChange() {
        return lastChange;
    }

    public void setLastChange(Long lastChange) {
        this.lastChange = lastChange;
    }
}
