package com.ss.sdk.job;

import com.ss.sdk.utils.ApplicationContextProvider;
import com.ss.sdk.utils.FolderUtil;
import com.ss.sdk.utils.PropertiesUtil;
import com.ss.sdk.utils.UUIDUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Component
public class PushPictureJobServiceImpl implements ApplicationRunner {

    @Resource
    private PropertiesUtil propertiesUtil;

    int num = 0;
    int zipNum = 1;
    FTPClient ftpClient = null;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        /*zipNum = 1;
        ftpClient = init();
        while (true) {
            compress(null);
            Thread.sleep(1000);
        }*/
    }

    public FTPClient init(){
        //创建ftp客户端
        FTPClient ftpClient = new FTPClient();
        ftpClient.setControlEncoding("utf-8");
        try {
            //主动模式
            ftpClient.enterLocalActiveMode();
            //链接ftp服务器
            ftpClient.connect(propertiesUtil.getFtpIp(),propertiesUtil.getFtpPort());
            //登录ftp
            boolean login = ftpClient.login(propertiesUtil.getFtpUserName(), propertiesUtil.getFtppassWord());
            //设置二进制
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ftpClient;
    }

    public int compress(Boolean keepDirStructure) throws IOException {
        num = 0;
        //筛选文件名
        List<String> filePaths = new ArrayList<>();
        //查询全部文件名
        List<String> fileList = FolderUtil.getFileList(propertiesUtil.getPicUrl());
        if(CollectionUtils.isEmpty(fileList)){
            return 0;
        }
        int a = 0;
        int b = 0;
        for (String file : fileList) {
            a = (propertiesUtil.getPicUrl()).length() + 1;
            b = file.indexOf(".");
            String n = file.substring(a, b);
            if (Integer.valueOf(n) > num) {
                filePaths.add(file);
            }
        }
        Collections.sort(filePaths);
        //记录打包文件名
        num = Integer.valueOf(filePaths.get(filePaths.size() - 1).substring(a, b));
        String name = UUIDUtils.getUUID() + "_" + zipNum + ".zip";
        byte[] buf = new byte[1024];
        //需要压缩到哪个zip文件（无需创建这样一个zip，只需要指定一个全路径）
        File zipFile = new File(propertiesUtil.getPicUrl() + "\\" + name);
        //zip文件不存在，则创建文件，用于压缩
        if (!zipFile.exists()) {
            zipFile.createNewFile();
        }
        //记录压缩了几个文件
        int fileCount = 0;
        try {
            ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFile));
            for (int i = 0; i < filePaths.size(); i++) {
                String relativePath = filePaths.get(i);
                if (StringUtils.isEmpty(relativePath)) {
                    continue;
                }
                //绝对路径找到file
                File sourceFile = new File(relativePath);
                if (sourceFile == null || !sourceFile.exists()) {
                    continue;
                }
                FileInputStream fis = new FileInputStream(sourceFile);
                if (keepDirStructure != null && keepDirStructure) {
                    //保持目录结构
                    zos.putNextEntry(new ZipEntry(relativePath));
                } else {
                    //直接放到压缩包的根目录
                    zos.putNextEntry(new ZipEntry(sourceFile.getName()));
                }
                //System.out.println("压缩当前文件："+sourceFile.getName());
                int len;
                while ((len = fis.read(buf)) > 0) {
                    zos.write(buf, 0, len);
                }
                zos.closeEntry();
                fis.close();
                fileCount++;
            }
            zos.close();
            //ftp传送
            InputStream input = new FileInputStream(propertiesUtil.getPicUrl() + "\\" + name);
            ftpClient.storeFile(name, input);
            input.close();
            //删除压缩包
            zipFile.delete();
            //删除已压缩图片
            for (String filePath : fileList) {
                File file = new File(filePath);
                file.delete();
            }
            //计数自增
            zipNum++;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileCount;
    }


}
