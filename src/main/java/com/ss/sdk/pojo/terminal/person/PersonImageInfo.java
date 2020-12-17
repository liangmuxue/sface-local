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

/**
 * description
 *
 * @author dW5565
 */
public class PersonImageInfo {
    @JSONField(name = "FaceID", ordinal = 1)
    private Long faceID;
    @JSONField(name = "Name", ordinal = 2)
    private String name;
    @JSONField(name = "Size", ordinal = 3)
    private Integer size;
    @JSONField(name = "Data", ordinal = 4)
    private String data;

    public Long getFaceID() {
        return faceID;
    }

    public void setFaceID(Long faceID) {
        this.faceID = faceID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
