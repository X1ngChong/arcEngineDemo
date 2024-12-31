package com.redis;

import com.Bean.PathResult;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service  
public class RedisService {  

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private final Gson gson = new Gson();

    public void saveValue(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public Object getValue(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public void savePathResults(String key, List<PathResult> pathResults) {
        String json = gson.toJson(pathResults);
        redisTemplate.opsForValue().set(key, json);
    }

    public List<PathResult> getPathResults(String key) {
        String json = (String) redisTemplate.opsForValue().get(key);
        return gson.fromJson(json, new com.google.gson.reflect.TypeToken<List<PathResult>>(){}.getType());
    }

    public void saveIntegerArrays(String key, List<Integer[]> integerArrays) {
        String json = gson.toJson(integerArrays);
        redisTemplate.opsForValue().set(key, json);
    }

    public List<Integer[]> getIntegerArrays(String key) {
        String json = (String) redisTemplate.opsForValue().get(key);
        return gson.fromJson(json, new com.google.gson.reflect.TypeToken<List<Integer[]>>(){}.getType());
    }
    public void saveDoubleArrays(String key, List<Double> doubleArrays) {
        String json = gson.toJson(doubleArrays);
        redisTemplate.opsForValue().set(key, json);
    }

    public List<Double> getDoubleArrays(String key) {
        String json = (String) redisTemplate.opsForValue().get(key);
        return gson.fromJson(json, new com.google.gson.reflect.TypeToken<List<Double>>(){}.getType());
    }
}