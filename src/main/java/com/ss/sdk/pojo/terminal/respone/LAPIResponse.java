package com.ss.sdk.pojo.terminal.respone;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * @author wW4799
 * @date 2018/10/18.
 */
public class LAPIResponse {
    /**
     * 响应URL
     */
    @JSONField(name = "ResponseURL", ordinal = 1)
    private String responseUrl;

    /**
     * 创建Id
     */
    @JSONField(name = "CreatedID", ordinal = 2)
    private int createdId;

    /**
     * 状态编码
     */
    @JSONField(name = "StatusCode", ordinal = 3)
    private int statusCode;

    /**
     * 状态描述
     */
    @JSONField(name = "StatusString", ordinal = 4)
    private String statusString;

    /**
     * 返回数据
     */
    @JSONField(name = "Data", ordinal = 5)
    private String data;


    public String getResponseUrl() {
        return responseUrl;
    }

    public void setResponseUrl(String responseUrl) {
        this.responseUrl = responseUrl;
    }


    public int getCreatedId() {
        return createdId;
    }

    public void setCreatedId(int createdId) {
        this.createdId = createdId;
    }


    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }


    public String getStatusString() {
        return statusString;
    }

    public void setStatusString(String statusString) {
        this.statusString = statusString;
    }


    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "LAPIResponse{" +
                "responseUrl='" + responseUrl + '\'' +
                ", createdId=" + createdId +
                ", statusCode=" + statusCode +
                ", statusString='" + statusString + '\'' +
                ", data='" + data + '\'' +
                '}';
    }
}
