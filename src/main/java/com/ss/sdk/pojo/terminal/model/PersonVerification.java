package com.ss.sdk.pojo.terminal.model;

import java.util.List;

/**
 * 记录推送
 *
 * @author 李爽超 chao
 * @create 2020/03/19
 * @email lishuangchao@ss-cas.com
 **/
public class PersonVerification {

    private Integer ID;
    private String DeviceCode;
    private String Reference;
    private long Seq;
    private long Timestamp;
    private long NotificationType;
    private long FaceInfoNum;
    private List<FaceInfo> FaceInfoList;
    private long CardInfoNum;
    private List<CardInfo> CardInfoList;
    private long GateInfoNum;
    private List<GateInfo> GateInfoList;
    private long LibMatInfoNum;
    private List<LibMatInfo> LibMatInfoList;

    public String getReference() {
        return Reference;
    }

    public void setReference(String reference) {
        Reference = reference;
    }

    public long getSeq() {
        return Seq;
    }

    public void setSeq(long seq) {
        Seq = seq;
    }

    public long getTimestamp() {
        return Timestamp;
    }

    public void setTimestamp(long timestamp) {
        Timestamp = timestamp;
    }

    public long getNotificationType() {
        return NotificationType;
    }

    public void setNotificationType(long notificationType) {
        NotificationType = notificationType;
    }

    public long getFaceInfoNum() {
        return FaceInfoNum;
    }

    public void setFaceInfoNum(long faceInfoNum) {
        FaceInfoNum = faceInfoNum;
    }

    public List<FaceInfo> getFaceInfoList() {
        return FaceInfoList;
    }

    public void setFaceInfoList(List<FaceInfo> faceInfoList) {
        FaceInfoList = faceInfoList;
    }

    public long getCardInfoNum() {
        return CardInfoNum;
    }

    public void setCardInfoNum(long cardInfoNum) {
        CardInfoNum = cardInfoNum;
    }

    public List<CardInfo> getCardInfoList() {
        return CardInfoList;
    }

    public void setCardInfoList(List<CardInfo> cardInfoList) {
        CardInfoList = cardInfoList;
    }

    public long getGateInfoNum() {
        return GateInfoNum;
    }

    public void setGateInfoNum(long gateInfoNum) {
        GateInfoNum = gateInfoNum;
    }

    public List<GateInfo> getGateInfoList() {
        return GateInfoList;
    }

    public void setGateInfoList(List<GateInfo> gateInfoList) {
        GateInfoList = gateInfoList;
    }

    public long getLibMatInfoNum() {
        return LibMatInfoNum;
    }

    public void setLibMatInfoNum(long libMatInfoNum) {
        LibMatInfoNum = libMatInfoNum;
    }

    public List<LibMatInfo> getLibMatInfoList() {
        return LibMatInfoList;
    }

    public void setLibMatInfoList(List<LibMatInfo> libMatInfoList) {
        LibMatInfoList = libMatInfoList;
    }

    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public String getDeviceCode() {
        return DeviceCode;
    }

    public void setDeviceCode(String deviceCode) {
        DeviceCode = deviceCode;
    }
}
