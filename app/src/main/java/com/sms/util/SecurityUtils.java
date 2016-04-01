package com.sms.util;


/*
 * sms
 * Created by A.Kolchev  1.3.2016
 */

import android.text.TextUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SecurityUtils {

    //private static final String SALT = "S{M]S)";

    public static String hashPassword(String password) {
        if (TextUtils.isEmpty(password)) {
            throw new IllegalArgumentException("password should not be empty");
        }

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            //md.update(SALT.getBytes());
            byte[] bytes = md.digest(password.getBytes());

            //This bytes[] has bytes in decimal format;
            //Convert it to hexadecimal format
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }

            //Get complete hashed password in hex format
            return sb.toString();
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }

        return null;
    }
}
