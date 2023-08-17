package com.maindark.livestream.redis;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Service
public class RedisService {

    @Resource
    JedisPool jedisPool;


    /**
     * get an Object
     * **/
    public <T> T get(KeyPrefix keyPrefix,String key,Class<T> clazz){

        Jedis jedis = null;
        try{
            // real key
            String realKey = keyPrefix.getPreFix() + key;
            jedis = jedisPool.getResource();
            String str = jedis.get(realKey);
            T t = stringToBean(str,clazz);
            return t;
        }finally {
            returnToPool(jedis);
        }
    }

    /**
     * set an Object
     * **/
    public <T> Boolean set(KeyPrefix keyPrefix,String key,T value) {
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            String str = beanToString(value);
            if(StringUtils.isEmpty(str)){
                return false;
            }
            // real key
            String realKey = keyPrefix.getPreFix() + key;
            int expireSeconds = keyPrefix.expireSeconds();
            if(expireSeconds <= 0){
                jedis.set(realKey,str);
            } else {
                jedis.setex(realKey,expireSeconds,str);
            }
            return true;
        }finally {
            returnToPool(jedis);
        }
    }

    /**
     *    key is existed
     * **/
    public <T> Boolean exists(KeyPrefix keyPrefix,String key,T value) {
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            // real key
            String realKey = keyPrefix.getPreFix() + key;
            return jedis.exists(realKey);
        }finally {
            returnToPool(jedis);
        }
    }


    /**
     * delete
     * */
    public boolean delete(KeyPrefix prefix, String key) {
        Jedis jedis = null;
        try {
            jedis =  jedisPool.getResource();
            //生成真正的key
            String realKey  = prefix.getPreFix() + key;
            long ret =  jedis.del(realKey);
            return ret > 0;
        }finally {
            returnToPool(jedis);
        }
    }
    /**
     *    key is increased
     * **/
    public <T> Long incr(KeyPrefix keyPrefix,String key,T value) {
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            // real key
            String realKey = keyPrefix.getPreFix() + key;
            return jedis.incr(realKey);
        }finally {
            returnToPool(jedis);
        }
    }

    /**
     *    key is decreased
     * **/
    public <T> Long decr(KeyPrefix keyPrefix,String key,T value) {
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            // real key
            String realKey = keyPrefix.getPreFix() + key;
            return jedis.decr(realKey);
        }finally {
            returnToPool(jedis);
        }
    }

    private <T> String beanToString(T value) {
        if(value == null){
            return  null;
        }
        Class<?> clazz = value.getClass();
        if(clazz == int.class || clazz == Integer.class){
            return "" + value;
        } else if(clazz == long.class || clazz == Long.class){
            return "" + value;
        } else if(clazz == String.class) {
            return (String) value;
        } else {
            return JSON.toJSONString(value);
        }

    }

    private <T> T stringToBean(String str,Class<T> clazz) {
        if(StringUtils.isEmpty(str)) {
            return null;
        }
        if(clazz == int.class || clazz == Integer.class){
            return (T)Integer.valueOf(str);
        } else if(clazz == long.class || clazz == Long.class){
            return (T)Long.valueOf(str);
        } else if(clazz == String.class) {
            return (T) str;
        } else {
            return JSON.parseObject(str,clazz);
//            return JSON.toJavaObject(JSON.parseObject(str), clazz);
        }
    }

    private void returnToPool(Jedis jedis) {
        if (jedis != null) {
            jedis.close();
        }
    }



}
