package com.ss.sdk.pojo.terminal.model;

/**
 * com.ss.model
 *
 * @author 李爽超 chao
 * @create 2020/03/19
 * @email lishuangchao@ss-cas.com
 **/
public class LibMatInfo {

    private long ID;
    private long LibID;
    private long LibType;
    private long MatchStatus;
    private long MatchPersonID;
    private long MatchFaceID;
    private MatchPersonInfo MatchPersonInfo;
    private String PersonName;
    private long Gender;
    private String CardID;
    private String IdentityNo;

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public long getLibID() {
        return LibID;
    }

    public void setLibID(long libID) {
        LibID = libID;
    }

    public long getLibType() {
        return LibType;
    }

    public void setLibType(long libType) {
        LibType = libType;
    }

    public long getMatchStatus() {
        return MatchStatus;
    }

    public void setMatchStatus(long matchStatus) {
        MatchStatus = matchStatus;
    }

    public long getMatchPersonID() {
        return MatchPersonID;
    }

    public void setMatchPersonID(long matchPersonID) {
        MatchPersonID = matchPersonID;
    }

    public long getMatchFaceID() {
        return MatchFaceID;
    }

    public void setMatchFaceID(long matchFaceID) {
        MatchFaceID = matchFaceID;
    }

    public MatchPersonInfo getMatchPersonInfo() {
        return MatchPersonInfo;
    }

    public void setMatchPersonInfo(MatchPersonInfo matchPersonInfo) {
        MatchPersonInfo = matchPersonInfo;
    }

    public String getPersonName() {
        return PersonName;
    }

    public void setPersonName(String personName) {
        PersonName = personName;
    }

    public long getGender() {
        return Gender;
    }

    public void setGender(long gender) {
        Gender = gender;
    }

    public String getCardID() {
        return CardID;
    }

    public void setCardID(String cardID) {
        CardID = cardID;
    }

    public String getIdentityNo() {
        return IdentityNo;
    }

    public void setIdentityNo(String identityNo) {
        IdentityNo = identityNo;
    }
}
