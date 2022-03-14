package com.ss.sdk.model;

import java.util.List;

public class Tenement {

    private String FrameNo;
    private String Name;
    private Integer Gender;
    private Integer CredentialType;
    private String CredentialID;
    private List<Faces> Faces;
    private List<Rights> Rights;

    public String getFrameNo() {
        return FrameNo;
    }

    public void setFrameNo(String frameNo) {
        FrameNo = frameNo;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public Integer getGender() {
        return Gender;
    }

    public void setGender(Integer gender) {
        Gender = gender;
    }

    public Integer getCredentialType() {
        return CredentialType;
    }

    public void setCredentialType(Integer credentialType) {
        CredentialType = credentialType;
    }

    public String getCredentialID() {
        return CredentialID;
    }

    public void setCredentialID(String credentialID) {
        CredentialID = credentialID;
    }

    public List<com.ss.sdk.model.Faces> getFaces() {
        return Faces;
    }

    public void setFaces(List<com.ss.sdk.model.Faces> faces) {
        Faces = faces;
    }

    public List<com.ss.sdk.model.Rights> getRights() {
        return Rights;
    }

    public void setRights(List<com.ss.sdk.model.Rights> rights) {
        Rights = rights;
    }
}
