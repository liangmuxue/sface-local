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
public class PersonTimeTemplateInfo {


    @JSONField(name = "BeginTime", ordinal = 1)
    private Long beginTime;

    @JSONField(name = "EndTime", ordinal = 2)
    private Long endTime;

    @JSONField(name = "Index", ordinal = 3)
    private Integer index;

    public Long getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Long beginTime) {
        this.beginTime = beginTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }
}
