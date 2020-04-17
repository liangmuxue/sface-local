package com.ss.sdk.utils;

import com.ss.sdk.socket.MyWebSocketClientLL;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

/**
* AES工具类
* @author chao
* @create 2020/1/15
* @email lishuangchao@ss-cas.com
**/
@Component
public class AESUtil {

    /**
     * 获取登陆请求key
     * @param userName
     * @param password
     * @return
     * @throws Exception
     */
    public static String getLoginKey(String userName, String password) throws Exception {
        String pwd = DigestUtils.md5DigestAsHex(password.getBytes());
        String key = DigestUtils.md5DigestAsHex((userName + "Authorization" + pwd).getBytes());
        return key;
    }

    /**
     * 获取其他请求key
     * @param userName
     * @return
     */
    public static String getOtherKey(String userName){
        String key = DigestUtils.md5DigestAsHex((userName + "Business" + MyWebSocketClientLL.timestamp + MyWebSocketClientLL.sign).getBytes());
        return key;
    }

    /**
     * aes加密
     * @param sSrc 加密内容
     * @param sKey key
     * @param cKey iv
     * @return
     * @throws Exception
     */
    public static String encrypt(String sSrc, String sKey, String cKey) throws Exception {
        if (sKey == null) {
            //System.out.print("Key为空null");
            return null;
        }
        // 判断Key是否为16位
        if (sKey.length() != 32) {
            //System.out.print("Key长度不是16位");
            return null;
        }
        byte[] raw = sKey.getBytes("utf-8");
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");//"算法/模式/补码方式"
        IvParameterSpec iv = new IvParameterSpec(cKey.getBytes());//使用CBC模式，需要一个向量iv，可增加加密算法的强度
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
        byte[] encrypted = cipher.doFinal(sSrc.getBytes());
        return Base64.getEncoder().encodeToString(encrypted);
    }

    /**
     * aes解密
     * @param sSrc 解密内容
     * @param sKey key
     * @param cKey iv
     * @return
     * @throws Exception
     */
    public static String decrypt(String sSrc, String sKey, String cKey) throws Exception {
        try {
            // 判断Key是否正确
            if (sKey == null) {
                //System.out.print("Key为空null");
                return null;
            }
            // 判断Key是否为16位
            if (sKey.length() != 32) {
                //System.out.print("Key长度不是16位");
                return null;
            }
            byte[] raw = sKey.getBytes("utf-8");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec iv = new IvParameterSpec(cKey.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            byte[] encrypted1 = Base64.getDecoder().decode(sSrc);//先用base64解密
            try {
                byte[] original = cipher.doFinal(encrypted1);
                String originalString = new String(original);
                return originalString;
            } catch (Exception e) {
                //System.out.println(e.toString());
                return null;
            }
        } catch (Exception ex) {
            //System.out.println(ex.toString());
            return null;
        }
    }
}

