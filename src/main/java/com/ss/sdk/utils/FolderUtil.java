package com.ss.sdk.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FolderUtil {

    public static List getFileList(String path) {
        List list = new ArrayList();
        try {
            File file = new File(path);
            String[] filelist = file.list();
            for (int i = 0; i < filelist.length; i++) {
                list.add(path+"\\"+filelist[i]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
