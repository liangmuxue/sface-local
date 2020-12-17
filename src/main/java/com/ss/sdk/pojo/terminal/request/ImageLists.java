package com.ss.sdk.pojo.terminal.request;

import com.alibaba.fastjson.annotation.JSONField;

public class ImageLists {

    @JSONField(name = "FaceID")
    private Integer FaceID;
    @JSONField(name = "Name")
    private String Name;
    @JSONField(name = "Size")
    private String Size;
    @JSONField(name = "Data")
    private String Data;

    public Integer getFaceID() {
        return FaceID;
    }

    public void setFaceID(Integer faceID) {
        FaceID = faceID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getSize() {
        return Size;
    }

    public void setSize(String size) {
        Size = size;
    }

    public String getData() {
        return Data;
    }

    public void setData(String data) {
        Data = data;
    }
}
