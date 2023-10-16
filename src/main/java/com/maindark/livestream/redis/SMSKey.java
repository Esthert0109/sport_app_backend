package com.maindark.livestream.redis;

public class SMSKey extends BasePrefix{
    public SMSKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }


    public static final int expiredSeconds = 30;

    public static final int limitTime = 3600 * 24;

    /*register msgCode*/
    public static SMSKey smsKey = new SMSKey(expiredSeconds,"sms");

    public static SMSKey smsLimit = new SMSKey(limitTime,"smsLimit");

    public static SMSKey resetPass = new SMSKey(expiredSeconds,"resetPass");

    public static SMSKey forgotPass = new SMSKey(expiredSeconds,"forgotPass");
}
