package com.maindark.livestream.result;

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

    //sms
    public static CodeMsg SMS_CODE_NOT_EXIST = new CodeMsg(500310,"verify code is not exist, please try again");
    public static CodeMsg SMS_CODE_ERROR = new CodeMsg(500311,"verify code is incorrect, please try again");
    public static CodeMsg SMS_CODE_SEND_ERROR = new CodeMsg(500312,"message sends error, please try again");


    //
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
