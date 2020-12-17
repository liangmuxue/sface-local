/*
 * Copyright (c) 2018, ZheJiang Uniview Technologies Co., Ltd. All rights reserved.
 * <http://www.uniview.com/>
 *------------------------------------------------------------------------------
 * Product     : 速通门
 * Module Name : com.unv.fastgate.server.service
 * Date Created: 2018/11/14
 * Creator     : dW5565 dongchenghao
 * Description :
 *
 *------------------------------------------------------------------------------
 * Modification History
 * DATE        NAME             DESCRIPTION
 *------------------------------------------------------------------------------
 *------------------------------------------------------------------------------
 */
package com.ss.sdk.service.terminal.impl;


import com.ss.sdk.pojo.terminal.libinfo.AllLibInfo;

/**
 * description
 *
 * @author dW5565
 */
public interface ILibInfoService {

    AllLibInfo getAllLibInfo(String ip);

    boolean add(String ip, Integer libId);

}
