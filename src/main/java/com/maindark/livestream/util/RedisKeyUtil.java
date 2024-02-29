package com.maindark.livestream.util;



public class RedisKeyUtil {
    private static final String PREFIX_FOLLOWEE = "followee";
    private static final String PREFIX_FOLLOWER = "follower";

    // 用户关注的实体(实体包括(post, user))
    public static String getFolloweeKey(Integer userId,int entityType) {
        return PREFIX_FOLLOWEE + ":" + userId + ":" + entityType;
    }

    //某个实体拥有的粉丝(实体包括帖子和用户)
    public static String getFollowerKey(int entityType,Integer entityId){
        return PREFIX_FOLLOWER + ":" + entityType + ":" + entityId;
    }

}
