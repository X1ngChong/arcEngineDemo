package com.demo.before;

import com.Util.AoInitUtil;
import com.esri.arcgis.datasourcesfile.ShapefileWorkspaceFactory;
import com.esri.arcgis.geodatabase.IWorkspace;
import com.esri.arcgis.geodatabase.IWorkspaceFactory;
import com.esri.arcgis.system.AoInitialize;

import javax.swing.*;

public class Test {
    public static void main(String[] args) {

        String shapefilePath = "D:\\arc\\xianlin0910";
        try {

            //ae许可初始化
            AoInitialize aoInit = null;
            AoInitUtil aoInitUtil = new AoInitUtil();
            //初始化ae
            //初始化ae或许可失败，会报错或者返回null
            aoInit = aoInitUtil.initializeEngine(aoInit);
            if (aoInit == null) {
                JOptionPane.showMessageDialog(null, "ae初始化失败");
                return;
            }



            IWorkspaceFactory wsFactory = new ShapefileWorkspaceFactory();
            IWorkspace workspace = wsFactory.openFromFile(shapefilePath, 0);


            System.out.println("Shapefile工作空间打开成功！");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
