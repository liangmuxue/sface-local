package com.ss.sdk.pojo.terminal.model;

/**
 * 闸机信息
 *
 * @author 李爽超 chao
 * @create 2020/03/19
 * @email lishuangchao@ss-cas.com
 **/
public class GateInfo {

    private long ID;
    private long Timestamp;
    private long CapSrc;
    private long InPersonCnt;
    private long OutPersonCnt;

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

    public long getInPersonCnt() {
        return InPersonCnt;
    }

    public void setInPersonCnt(long inPersonCnt) {
        InPersonCnt = inPersonCnt;
    }

    public long getOutPersonCnt() {
        return OutPersonCnt;
    }

    public void setOutPersonCnt(long outPersonCnt) {
        OutPersonCnt = outPersonCnt;
    }
}
