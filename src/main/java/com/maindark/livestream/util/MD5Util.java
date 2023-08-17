package com.maindark.livestream.util;

import org.apache.commons.codec.digest.DigestUtils;

public class MD5Util {
    public static String md5(String src){
        return DigestUtils.md5Hex(src);
    }

    private static final String salt = "1a2b3c4d";
    public static String inputPassToFormPass(String inputPass) {
        String str = "" + salt.charAt(0) + salt.charAt(2) + inputPass + salt.charAt(5) + salt.charAt(4);
        return md5(str);
    }

    public static String formPassToDBPass(String formPass,String salt) {
        String str = "" + salt.charAt(0) + salt.charAt(2) + formPass + salt.charAt(5) + salt.charAt(4);
        return md5(str);
    }

    public static String inputPassToDbPass(String input,String saltDb){
        String formPass = inputPassToFormPass(input);
        String dbPass = formPassToDBPass(formPass,saltDb);
        return dbPass;
    }

    public static void main(String[] args) {
        String formPass = inputPassToFormPass("123456");
        System.out.println(formPass);
        String dbPass = formPassToDBPass(formPass,"1a2b3c");
        System.out.printf(dbPass);
    }
}
