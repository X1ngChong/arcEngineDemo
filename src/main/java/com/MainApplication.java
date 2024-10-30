package com;

import com.Bean.EngineVisualBeans;
import com.Common.PathCommon;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.swing.*;
import java.awt.*;


/**
 * @author JXS
 */
@SpringBootApplication
public class MainApplication  {
    public static void main(String[] args) throws Exception {
        try {
            //加载图形化服务要优先于spring
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            GraphicsDevice[] devices = ge.getScreenDevices();
            if (devices.length ==0) {
                System.err.println("No graphics environment available.");
            } else {
                System.out.println("Graphics environment is available.");
            }
        } catch (HeadlessException e) {
            System.err.println("Headless exception: " + e.getMessage());
        }

        SpringApplication.run(MainApplication.class,args);

        try {
            //初始化AE，初始化界面
            EngineVisualBeans engineVisualBeans = new EngineVisualBeans();
            engineVisualBeans.initialVisual(PathCommon.initMXD,null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    }

