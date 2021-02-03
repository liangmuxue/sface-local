//package com.ss.sdk.job;
//
//import com.dangdang.ddframe.job.api.ShardingContext;
//import com.dangdang.ddframe.job.api.simple.SimpleJob;
//import com.ss.sdk.utils.ApplicationContextProvider;
//import com.ss.sdk.utils.PropertiesUtil;
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
//
//import java.io.File;
//import java.util.Date;
//import java.util.LinkedList;
//
///**
// * 照片删除定时任务
// * @author 李爽超 chao
// * @create 2020/04/22
// * @email lishuangchao@ss-cas.com
// **/
//public class DeletePicturesJob implements SimpleJob {
//
//    private Log logger = LogFactory.getLog(DeletePicturesJob.class);
//    private PropertiesUtil propertiesUtil = ApplicationContextProvider.getBean(PropertiesUtil.class);
//
//    @Override
//    public void execute(ShardingContext shardingContext) {
//        this.logger.info("定时任务DeletePicturesJob已经启动" + new Date().toString());
//        String url = this.propertiesUtil.getCaptureUrl();
//        Date date = new Date(System.currentTimeMillis() - 1000 * 60 * 2);
//        File folder = new File(url);
//        if (folder.exists()) {
//            //存放文件夹
//            LinkedList<File> directories = new LinkedList<>();
//            if (folder.isDirectory()) {
//                directories.add(folder);
//            } else {
//                folder.delete();
//            }
//            while (directories.size() > 0) {
//                File[] files = directories.removeFirst().listFiles();
//                if (files != null) {
//                    for (File f : files) {
//                        if (f.isDirectory()) {
//                            directories.add(f);
//                        } else if (new Date(f.lastModified()).before(date)){
//                            f.delete();
//                        }
//                    }
//                }
//            }
//        }
//    }
//}
//
//
