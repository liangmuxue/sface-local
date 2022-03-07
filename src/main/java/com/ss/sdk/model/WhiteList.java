package com.ss.sdk.model;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 下发设备类
 * @author 李爽超 chao
 * @create 2019/12/11
 * @email lishuangchao@ss-cas.com
 **/
@Table(name = "sface_white_list")
public class WhiteList {

    @Id
    private Integer id;
    @Column(name = "people_id")
    private String peopleId;
    @Column(name = "product_code")
    private String productCode;
    @Column(name = "device_people_id")
    private String devicePeopleId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPeopleId() {
        return peopleId;
    }

    public void setPeopleId(String peopleId) {
        this.peopleId = peopleId;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getDevicePeopleId() {
        return devicePeopleId;
    }

    public void setDevicePeopleId(String devicePeopleId) {
        this.devicePeopleId = devicePeopleId;
    }
}
