package com.ss.sdk.pojo.terminal.model;

/**
 * com.ss.model
 *
 * @author 李爽超 chao
 * @create 2020/03/19
 * @email lishuangchao@ss-cas.com
 **/
public class MatchPersonInfo {

    private String PersonName;
    private long Gender;
    private String CardID;
    private String IdentityNo;

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
