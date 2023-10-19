package com;

import com.Bean.EngineVisualBeans;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class Application {
    public static void main(String[] args) throws IOException {
        //初始化AE，初始化界面
        EngineVisualBeans.initialVisual();

        //Spring 初始化
        SpringApplication.run(Application.class, args);
    }


}
