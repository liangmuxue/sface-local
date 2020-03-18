package com.ss.sdk.service;

import com.ss.sdk.model.GuanpinRequest;

public interface IGuanpinService {

    int dataUpload(GuanpinRequest guanpinRequest) throws Exception;

    int getPersonPermission(GuanpinRequest guanpinRequest) throws Exception;
}
