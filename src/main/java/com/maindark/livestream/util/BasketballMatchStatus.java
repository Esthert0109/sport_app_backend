package com.maindark.livestream.util;

public class BasketballMatchStatus {

    public static String convertStatusIdToStr(String status){
        String str;
        switch(status){
            case "0":
                str = "未";
                break;
            case "1":
                str = "一节";
                break;
            case "2":
                str = "二节";
                break;
            case "3":
                str = "三节";
                break;
            case "4":
                str = "四节";
                break;
            case "5":
                str = "1'OT";
                break;
            case "6":
                str = "2'OT";
                break;
            case "7":
                str = "3'OT";
                break;
            case "50":
                str = "中场";
                break;
            case "-1":
                str = "完场";
                break;
            case "-2":
                str = "待定";
                break;
            case "-3":
                str = "中断";
                break;
            case "-4":
                str = "取消";
                break;
            case "-5":
                str = "推迟";
                break;
            default:
                str = "未知";
        }
        return str;
    }
}
