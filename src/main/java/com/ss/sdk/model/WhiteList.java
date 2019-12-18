package com.ss.sdk.model;

/**
 * 下发设备类
 * @author 李爽超 chao
 * @create 2019/12/11
 * @email lishuangchao@ss-cas.com
 **/
public class WhiteList {

    private Integer id;
    private Integer peopleId;
    private String productCode;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPeopleId() {
        return peopleId;
    }

    public void setPeopleId(Integer peopleId) {
        this.peopleId = peopleId;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

}
