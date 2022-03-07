package com.ss.sdk.utils;

import com.sun.org.apache.xml.internal.security.exceptions.Base64DecodingException;
import com.sun.org.apache.xml.internal.security.utils.Base64;
import org.springframework.util.Base64Utils;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 获取远程文件并转为base64
 * @author 李爽超 chao
 * @create 2020/01/16
 * @email lishuangchao@ss-cas.com
 **/
public class Base64Util {

    // 图片路劲层级分隔符
    private static String separator = "/";

    /**
     * 远程地址图片转base64
     * @param imgUrl
     * @return
     */
    public static String imagebase64(String imgUrl) {
        URL url = null;
        InputStream is = null;
        ByteArrayOutputStream outStream = null;
        HttpURLConnection httpUrl = null;
        try {
            url = new URL(imgUrl);
            httpUrl = (HttpURLConnection) url.openConnection();
            httpUrl.connect();
            httpUrl.getInputStream();
            is = httpUrl.getInputStream();
            outStream = new ByteArrayOutputStream();
            //创建一个Buffer字符串
            byte[] buffer = new byte[1024];
            //每次读取的字符串长度，如果为-1，代表全部读取完毕
            int len = 0;
            //使用一个输入流从buffer里把数据读取出来
            while ((len = is.read(buffer)) != -1) {
                //用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度
                outStream.write(buffer, 0, len);
            }
            // 对字节数组Base64编码
            return new BASE64Encoder().encode(outStream.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outStream != null) {
                try {
                    outStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (httpUrl != null) {
                httpUrl.disconnect();
            }
        }
        return imgUrl;
    }

    /**
     * base64图片保存到本地
     * @param baseImg
     * @param path
     * @throws Base64DecodingException
     */
    public static void saveImg(String baseImg, String path) throws Base64DecodingException {
        //定义一个正则表达式的筛选规则，为了获取图片的类型
        String rgex = "data:image/(.*?);base64";
        //String type = getSubUtilSimple(baseImg, rgex);
        //去除base64图片的前缀
        baseImg = baseImg.replaceFirst("data:(.+?);base64,", "");
        byte[] b;
        byte[] bs;
        OutputStream os = null;
        //把图片转换成二进制
        b = Base64.decode(baseImg.replaceAll(" ", "+"));
        BASE64Decoder d = new BASE64Decoder();
        // 保存
        try {
            File dir = new File(path);
            if (!dir.getParentFile().exists()) {
                dir.getParentFile().mkdirs();
            }
            dir.createNewFile();
            bs = d.decodeBuffer(Base64.encode(b));
            os = new FileOutputStream(path);
            os.write(bs);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.getLocalizedMessage();
                }
            }
        }
    }


    public static String getSubUtilSimple(String soap,String rgex){
        Pattern pattern = Pattern.compile(rgex);
        Matcher m = pattern.matcher(soap);
        while(m.find()){
            return m.group(1);
        }
        return "";
    }

    /**
     * 本地图片转base64
     * @param url
     * @return
     */
    public static String localBase64(String url){
        InputStream in = null;
        byte[] data = null;
        String imgBase64 = null;
        try {
            File dir = new File(url);
            if (dir.exists()) {
                in = new FileInputStream(url);
                data = new byte[in.available()];
                in.read(data);
                in.close();
                imgBase64 = Base64Utils.encodeToString(data);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return imgBase64;
    }

    /**
     *通过图片base64流判断图片等于多少字节
     *image 图片流
     */
    public static Integer imageSize(String image){
        // 1.需要计算文件流大小，首先把头部的data:image/png;base64,（注意有逗号）去掉。
       /* String str=image.substring(22);
        //2.找到等号，把等号也去掉
        Integer equalIndex= str.indexOf("=");
        if(str.indexOf("=")>0) {
            str=str.substring(0, equalIndex);
        }*/
        //3.原来的字符流大小，单位为字节
        Integer strLength=image.length();
        //4.计算后得到的文件流大小，单位为字节
        Integer size=strLength-(strLength/8)*2;
        return size;
    }

    public static int getLength(String str) {
        if (str == null || str.length() == 0) {
            return 0;
        }
        try {
            return str.getBytes("UTF-8").length;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
