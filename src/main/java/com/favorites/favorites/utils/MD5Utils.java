package com.favorites.favorites.utils;

import org.springframework.util.DigestUtils;

import java.util.UUID;

public class MD5Utils {
    public static String getSalt(){
        String salt = UUID.randomUUID().toString().toUpperCase();
        return salt;
    }

    /**
     *
     * @param password 密码字符串
     * @param salt 盐值字符串
     * @return 加密后的字符串
     */
    public static String getMd5Pwd(String password , String salt){
        /**
         * 加密2次 salt+password+salt
         */
        for (int i = 0; i < 2; i++) {
            password = DigestUtils.md5DigestAsHex((salt+password+salt).getBytes()).toUpperCase();
        }
        return password;
    }

}
