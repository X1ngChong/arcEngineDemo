package com;

import com.Bean.EngineVisualBeans;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * @author JXS
 */
@SpringBootApplication
public class Application {
    public static void main(String[] args) throws Exception {
        //初始化AE，初始化界面
      EngineVisualBeans engineVisualBeans = new EngineVisualBeans();
      engineVisualBeans.initialVisual();

        //Spring 初始化
        SpringApplication.run(Application.class, args);
    }


}
