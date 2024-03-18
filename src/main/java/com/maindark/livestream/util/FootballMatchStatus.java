package com.maindark.livestream.util;

public class FootballMatchStatus {

    public static String convertStatusIdToStr(Integer statusId){
        String str;
        if(0 == statusId){
            str = "未";
        } else if(1 == statusId){
            str = "上半场";
        } else if(2 == statusId){
            str = "中场";
        } else if(3 == statusId){
            str = "下半场";
        } else if(4 == statusId){
            str = "加时赛";
        } else if(5 == statusId){
            str = "点球";
        } else if (-1 == statusId){
            str = "完场";
        } else if (-10 == statusId){
            str = "取消";
        } else if (-11 == statusId){
            str = "待定";
        }else if (-12 == statusId){
            str = "腰斩";
        }else if (-13 == statusId){
            str = "中断";
        } else if(-14 == statusId) {
            str = "推迟";
        } else {
            str = "未知";
        }
        return str;
    }
}
