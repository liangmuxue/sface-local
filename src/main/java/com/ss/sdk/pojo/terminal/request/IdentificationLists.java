package com.ss.sdk.pojo.terminal.request;

import com.alibaba.fastjson.annotation.JSONField;

public class IdentificationLists {

    @JSONField(name = "Type")
    private Integer Type;
    @JSONField(name = "Number")
    private String Number;

    public Integer getType() {
        return Type;
    }

    public void setType(Integer type) {
        Type = type;
    }

    public String getNumber() {
        return Number;
    }

    public void setNumber(String number) {
        Number = number;
    }
}
