package com.ss.sdk.model;

/**
 * com.ss.sdk.model
 *
 * @author 李爽超 chao
 * @create 2020/03/16
 * @email lishuangchao@ss-cas.com
 **/
public class GuanpinResponse {

    private Integer result;
    private Boolean success;
    private String status;
    private String msg;

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
