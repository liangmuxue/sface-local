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
package com.ss.sdk.pojo.accept;

/**
 * description
 *
 * @author dW5565
 */
public class AddPerson {
    private Integer libId;
    private String ip;

    public Integer getLibId() {
        return libId;
    }

    public void setLibId(Integer libId) {
        this.libId = libId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
