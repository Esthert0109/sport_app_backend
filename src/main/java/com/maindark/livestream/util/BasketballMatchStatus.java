package com.maindark.livestream.util;

public class BasketballMatchStatus {

    public static String convertStatusIdToStr(Integer statusId){
        String str;
        if(1 == statusId){
            str = "未";
        } else if(2 == statusId){
            str = "第一节";
        } else if(3 == statusId){
            str = "第一节完";
        } else if(4 == statusId){
            str = "第二节";
        } else if(5 == statusId){
            str = "第二节完";
        } else if(6 == statusId){
        str = "第三节";
        } else if(7 == statusId){
            str = "第三节完";
        } else if(8 == statusId){
            str = "第四节";
        } else if(9 == statusId){
            str = "加时";
        } else if(10 == statusId){
            str = "完场";
        } else if (11 == statusId){
            str = "中断";
        } else if (12 == statusId){
            str = "取消";
        } else if (13 == statusId){
            str = "延期";
        }else if (14 == statusId){
            str = "腰斩";
        }else if (15 == statusId){
            str = "待定";
        } else {
            str = "未知";
        }
        return str;
    }
}
