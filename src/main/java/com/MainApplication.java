package com;

import com.Bean.EngineVisualBeans;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * @author JXS
 */
@SpringBootApplication
public class MainApplication  {
    public static void main(String[] args) throws Exception {
        //初始化AE，初始化界面
        EngineVisualBeans engineVisualBeans = new EngineVisualBeans();
        engineVisualBeans.initialVisual("chenhui");

        SpringApplication.run(MainApplication.class,args);
    }
}
