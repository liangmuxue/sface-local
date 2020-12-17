package com.ss.sdk.pojo.terminal.model;

import org.apache.tomcat.jni.FileInfo;

/**
 * 卡信息
 *
 * @author 李爽超 chao
 * @create 2020/03/19
 * @email lishuangchao@ss-cas.com
 **/
public class CardInfo {

    private long ID;
    private long Timestamp;
    private long CapSrc;
    private long CardType;
    private String CardID;
    private long CardStatus;
    private String Name;
    private long Gender;
    private long Ethnicity;
    private String Birthday;
    private String ResidentialAddress;
    private String IdentityNo;
    private String IssuingAuthority;
    private String IssuingDate;
    private String ValidDateStart;
    private String ValidDateEnd;
    private FileInfo IDImage;

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

    public long getCardType() {
        return CardType;
    }

    public void setCardType(long cardType) {
        CardType = cardType;
    }

    public String getCardID() {
        return CardID;
    }

    public void setCardID(String cardID) {
        CardID = cardID;
    }

    public long getCardStatus() {
        return CardStatus;
    }

    public void setCardStatus(long cardStatus) {
        CardStatus = cardStatus;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public long getGender() {
        return Gender;
    }

    public void setGender(long gender) {
        Gender = gender;
    }

    public long getEthnicity() {
        return Ethnicity;
    }

    public void setEthnicity(long ethnicity) {
        Ethnicity = ethnicity;
    }

    public String getBirthday() {
        return Birthday;
    }

    public void setBirthday(String birthday) {
        Birthday = birthday;
    }

    public String getResidentialAddress() {
        return ResidentialAddress;
    }

    public void setResidentialAddress(String residentialAddress) {
        ResidentialAddress = residentialAddress;
    }

    public String getIdentityNo() {
        return IdentityNo;
    }

    public void setIdentityNo(String identityNo) {
        IdentityNo = identityNo;
    }

    public String getIssuingAuthority() {
        return IssuingAuthority;
    }

    public void setIssuingAuthority(String issuingAuthority) {
        IssuingAuthority = issuingAuthority;
    }

    public String getIssuingDate() {
        return IssuingDate;
    }

    public void setIssuingDate(String issuingDate) {
        IssuingDate = issuingDate;
    }

    public String getValidDateStart() {
        return ValidDateStart;
    }

    public void setValidDateStart(String validDateStart) {
        ValidDateStart = validDateStart;
    }

    public String getValidDateEnd() {
        return ValidDateEnd;
    }

    public void setValidDateEnd(String validDateEnd) {
        ValidDateEnd = validDateEnd;
    }

    public FileInfo getIDImage() {
        return IDImage;
    }

    public void setIDImage(FileInfo IDImage) {
        this.IDImage = IDImage;
    }
}
