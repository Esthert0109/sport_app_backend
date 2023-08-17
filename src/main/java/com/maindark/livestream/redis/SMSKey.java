package com.maindark.livestream.redis;

public class SMSKey extends BasePrefix{
    public SMSKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }


    public static final int expiredSeconds = 360;

    public static SMSKey smsKey = new SMSKey(expiredSeconds,"sms");
}
