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

import java.util.List;

/**
 * add description of types here
 *
 * @author dW5565
 */
public class AllLibInfo {

    @JSONField(name = "Num", ordinal = 1)
    private Integer num;
    @JSONField(name = "LibList", ordinal = 2)
    private List<SingleLibInfo> libList;


    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public List<SingleLibInfo> getLibList() {
        return libList;
    }

    public void setLibList(List<SingleLibInfo> libList) {
        this.libList = libList;
    }

    @Override
    public String toString() {
        return "AllLibInfo{" +
                "num=" + num +
                ", libList=" + libList +
                '}';
    }
}
