package com.ss.sdk.pojo.terminal.model;

/**
 * 人脸全景图人脸区域坐标
 *
 * @author 李爽超 chao
 * @create 2020/03/19
 * @email lishuangchao@ss-cas.com
 **/
public class FaceArea {

    private long LeftTopX;
    private long LeftTopY;
    private long RightBottomX;
    private long RightBottomY;

    public long getLeftTopX() {
        return LeftTopX;
    }

    public void setLeftTopX(long leftTopX) {
        LeftTopX = leftTopX;
    }

    public long getLeftTopY() {
        return LeftTopY;
    }

    public void setLeftTopY(long leftTopY) {
        LeftTopY = leftTopY;
    }

    public long getRightBottomX() {
        return RightBottomX;
    }

    public void setRightBottomX(long rightBottomX) {
        RightBottomX = rightBottomX;
    }

    public long getRightBottomY() {
        return RightBottomY;
    }

    public void setRightBottomY(long rightBottomY) {
        RightBottomY = rightBottomY;
    }
}
