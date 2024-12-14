package com.controller;

import com.redis.RedisService;
import org.springframework.beans.factory.annotation.Autowired;  
import org.springframework.web.bind.annotation.*;  

@RestController  
@RequestMapping("/redis")  
public class RedisController {  

    @Autowired  
    private RedisService redisService;  

    @PostMapping("/set")  
    public String setValue(@RequestParam String key, @RequestParam String value) {  
        redisService.saveValue(key, value);  
        return "Value set successfully!";  
    }  

    @GetMapping("/get")  
    public Object getValue(@RequestParam String key) {  
        return redisService.getValue(key);  
    }  
}