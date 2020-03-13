package com.ss.sdk.utils;

/**
 * com.example.demo
 *
 * @author 李爽超 chao
 * @create 2020/02/14
 * @email lishuangchao@ss-cas.com
 **/
public class MyUtils {

    public static byte[] concat(byte[] firstArray, byte[] secondArray) {
        if (firstArray == null || secondArray == null) {
            if (firstArray != null)
                return firstArray;
            if (secondArray != null)
                return secondArray;
            return null;
        }
        byte[] bytes = new byte[firstArray.length + secondArray.length];
        System.arraycopy(firstArray, 0, bytes, 0, firstArray.length);
        System.arraycopy(secondArray, 0, bytes, firstArray.length, secondArray.length);
        return bytes;
    }

}
