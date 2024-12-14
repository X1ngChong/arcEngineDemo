package com.redis;

import org.springframework.beans.factory.annotation.Autowired;  
import org.springframework.data.redis.core.RedisTemplate;  
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service  
public class RedisService {  

    @Resource
    private RedisTemplate<String, Object> redisTemplate;  

    public void saveValue(String key, Object value) {  
        redisTemplate.opsForValue().set(key, value);  
    }  

    public Object getValue(String key) {  
        return redisTemplate.opsForValue().get(key);  
    }  
}