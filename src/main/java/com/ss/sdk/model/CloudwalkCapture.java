package com.ss.sdk.model;

/**
 * com.ss.sdk.model
 *
 * @author 李爽超 chao
 * @create 2020/04/09
 * @email lishuangchao@ss-cas.com
 **/
public class CloudwalkCapture {

    private String deviceNo;
    private String spotImgPath;
    private String panoramaPath;
    private String compareDate;
    private String compareDateStamp;

    public String getDeviceNo() {
        return deviceNo;
    }

    public void setDeviceNo(String deviceNo) {
        this.deviceNo = deviceNo;
    }

    public String getSpotImgPath() {
        return spotImgPath;
    }

    public void setSpotImgPath(String spotImgPath) {
        this.spotImgPath = spotImgPath;
    }

    public String getPanoramaPath() {
        return panoramaPath;
    }

    public void setPanoramaPath(String panoramaPath) {
        this.panoramaPath = panoramaPath;
    }

    public String getCompareDate() {
        return compareDate;
    }

    public void setCompareDate(String compareDate) {
        this.compareDate = compareDate;
    }

    public String getCompareDateStamp() {
        return compareDateStamp;
    }

    public void setCompareDateStamp(String compareDateStamp) {
        this.compareDateStamp = compareDateStamp;
    }
}
