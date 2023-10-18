package com.maindark.livestream.result;

import org.aspectj.apache.bcel.classfile.Code;

public class CodeMsg {
    private int code;
    private String msg;

    //common error
    public static CodeMsg SUCCESS = new CodeMsg(0,"success");
    public static CodeMsg SERVER_ERROR = new CodeMsg(500100,"server error");

    public static CodeMsg BIND_ERROR = new CodeMsg(500101,"para is not correct: %s");

    //login module 5002XX
    public static CodeMsg LOGIN_IN = new CodeMsg(500210,"please login in");
    public static CodeMsg PASSWORD_EMPTY = new CodeMsg(500211,"password can not be empty");
    public static CodeMsg MOBILE_EMPTY = new CodeMsg(500212,"mobile can not be empty");

    public static CodeMsg PASSWORD_ERROR = new CodeMsg(500213,"password is not correct, please try again!");
    public static CodeMsg MOBILE_NOT_EXIST = new CodeMsg(500214,"mobile is not exist");
    public static CodeMsg MOBILE_EXIST = new CodeMsg(500215,"mobile has already existed, please try another mobile");

    //sms
    public static CodeMsg SMS_CODE_NOT_EXIST = new CodeMsg(500310,"verify code is not exist, please try again");
    public static CodeMsg SMS_CODE_ERROR = new CodeMsg(500311,"verify code is incorrect, please try again");
    public static CodeMsg SMS_CODE_SEND_ERROR = new CodeMsg(500312,"message sends error, please try again");
    public static CodeMsg SMS_CODE_LIMIT = new CodeMsg(500313,"you cannot send message again, please try tomorrow");

    //img
    public static CodeMsg IMG_UPLOAD_ERROR = new CodeMsg(500410,"image uploads error, please try again");


    //nami football
    public static CodeMsg FOOT_BALL_ERROR = new CodeMsg(500510,"football service is wrong, please connect to nami platform!");
    public static CodeMsg FOOT_TEAM_IS_NOT_EXISTED = new CodeMsg(500511,"football team is not existed, please choose another team name");

    public static CodeMsg FOOTBALL_MATCH_IS_NOT_EXISTED = new CodeMsg(500512,"football match is not existed, please try again later.");
    public static CodeMsg FOOTBALL_LIVE_ADDRESS_IS_NOT_EXISTED = new CodeMsg(500513,"football live address is not existed!");

    // nami basketball
    public static CodeMsg BASKET_BALL_ERROR = new CodeMsg(500610,"basketball service is wrong, please connect to our administrator!");

    // football match error
    public static CodeMsg FOOT_BALL_MATCH_PARAMS_ERROR = new CodeMsg(500710,"competition name or team name can be not empty!");


    private CodeMsg(int code,String msg){
        this.code = code;
        this.msg = msg;
    }
    public int getCode() {
        return code;
    }


    public String getMsg() {
        return msg;
    }

    public CodeMsg fillArgs(Object...args){
        int code = this.code;
        String message = String.format(this.msg,args);
        return new CodeMsg(code,message);
    }


}
