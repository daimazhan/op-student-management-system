package com.example.studentmanagement.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5工具类
 */
public class Md5Util {
    
    /**
     * MD5加密
     * @param plainText 明文
     * @return MD5加密后的字符串（32位小写）
     */
    public static String md5(String plainText) {
        if (plainText == null || plainText.isEmpty()) {
            return null;
        }
        
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(plainText.getBytes());
            byte[] digest = md.digest();
            
            // 转换为16进制字符串
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                int i = b & 0xff;
                if (i < 16) {
                    sb.append('0');
                }
                sb.append(Integer.toHexString(i));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5加密失败", e);
        }
    }
    
    /**
     * 验证密码
     * @param plainText 明文密码
     * @param encrypted 加密后的密码
     * @return 是否匹配
     */
    public static boolean verify(String plainText, String encrypted) {
        if (plainText == null || encrypted == null) {
            return false;
        }
        return md5(plainText).equals(encrypted);
    }
}
