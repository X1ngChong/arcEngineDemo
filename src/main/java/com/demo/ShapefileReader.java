package com.demo;

import com.Util.AoInitUtil;
import com.esri.arcgis.datasourcesfile.ShapefileWorkspaceFactory;
import com.esri.arcgis.geodatabase.*;
import com.esri.arcgis.system.AoInitialize;


import javax.swing.*;

/**
 *
 * 使用IWorkspace打開工作空間獲取shp文件的數據
 *
 */
public class ShapefileReader {

    public static void main(String[] args) {
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


            // 要读取的 Shapefile 文件路径
            String shapefilePath = "D:\\arc\\nanshi";

            // 打开 Shapefile 工作空间
            IWorkspaceFactory workspaceFactory = new ShapefileWorkspaceFactory();//開打的是文件夾而不是一個文件
            IWorkspace workspace = workspaceFactory.openFromFile(shapefilePath, 0);

            // 获取要素类
            IFeatureClass featureClass = getFeatureClass(workspace,"bulingbin");

            // 输出所有属性
            displayAllAttributes(featureClass);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static IFeatureClass getFeatureClass(IWorkspace workspace,String featureClassName) throws Exception {
        // 从工作空间中打开要素类
        return  ((IFeatureWorkspace)workspace).openFeatureClass(featureClassName);
    }

    /**
     * 可以獲取一個圖層的所有信息
     * @param featureClass
     * @throws Exception
     */
    private static void displayAllAttributes(IFeatureClass featureClass) throws Exception {
        // 获取要素类的字段
        IFields fields = featureClass.getFields();

        // 使用要素游标遍历要素
        IFeatureCursor cursor = featureClass.search(null, true);
        IFeature feature = cursor.nextFeature();
        while (feature != null) {
            // 遍历每个字段
            for (int i = 0; i < fields.getFieldCount(); i++) {
                String fieldName = fields.getField(i).getName();
                Object value = feature.getValue(i);
                System.out.println(fieldName + ": " + value);
            }
            // 移动到下一要素
            feature = cursor.nextFeature();
        }

        // 释放要素游标
        cursor.flush();
    }

}
