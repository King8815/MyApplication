package com.example.user.myapplication.Utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by user on 2016/6/8.
 */
/*
* MD5是一种不可逆的加密算法，
* */
public class MD5Utils {
    public static String encode(String string) {
        try {
            MessageDigest digest = MessageDigest.getInstance("md5");
            byte[] result = digest.digest(string.getBytes());
            StringBuilder builder = new StringBuilder();
            for (byte b : result) {
                int num = b & 0xff;
                String hex = Integer.toHexString(num);
                if (hex.length() == 1) {
                    builder.append("0" + hex);
                } else {
                    builder.append(hex);
                }
            }
            return builder.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }
}

