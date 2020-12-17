package com.ss.sdk.pojo.terminal.model;

import java.util.List;

/**
 * 人脸信息
 *
 * @author 李爽超 chao
 * @create 2020/03/19
 * @email lishuangchao@ss-cas.com
 **/
public class FaceInfo {

    private long ID;
    private long Timestamp;
    private long CapSrc;
    private long FeatureNum;
    private List<Feature> FeatureList;
    private float Temperature;
    private long MaskFlag;
    private Image PanoImage;
    private Image FaceImage;
    private FaceArea FaceArea;

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public long getTimestamp() {
        return Timestamp;
    }

    public void setTimestamp(long timestamp) {
        Timestamp = timestamp;
    }

    public long getCapSrc() {
        return CapSrc;
    }

    public void setCapSrc(long capSrc) {
        CapSrc = capSrc;
    }

    public long getFeatureNum() {
        return FeatureNum;
    }

    public void setFeatureNum(long featureNum) {
        FeatureNum = featureNum;
    }

    public List<Feature> getFeatureList() {
        return FeatureList;
    }

    public void setFeatureList(List<Feature> featureList) {
        FeatureList = featureList;
    }

    public float getTemperature() {
        return Temperature;
    }

    public void setTemperature(float temperature) {
        Temperature = temperature;
    }

    public long getMaskFlag() {
        return MaskFlag;
    }

    public void setMaskFlag(long maskFlag) {
        MaskFlag = maskFlag;
    }

    public Image getPanoImage() {
        return PanoImage;
    }

    public void setPanoImage(Image panoImage) {
        PanoImage = panoImage;
    }

    public Image getFaceImage() {
        return FaceImage;
    }

    public void setFaceImage(Image faceImage) {
        FaceImage = faceImage;
    }

    public FaceArea getFaceArea() {
        return FaceArea;
    }

    public void setFaceArea(FaceArea faceArea) {
        FaceArea = faceArea;
    }
}
