package com.ss.sdk.pojo.terminal.model;

/**
 * com.unv.demo.pojo.terminal.model
 *
 * @author 李爽超 chao
 * @create 2020/03/20
 * @email lishuangchao@ss-cas.com
 **/
public class PersonInfo {

    private int id;
    private String personName;
    private String startTime;
    private String endTime;
    private double startTemp;
    private double endTemp;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public double getStartTemp() {
        return startTemp;
    }

    public void setStartTemp(double startTemp) {
        this.startTemp = startTemp;
    }

    public double getEndTemp() {
        return endTemp;
    }

    public void setEndTemp(double endTemp) {
        this.endTemp = endTemp;
    }

}
