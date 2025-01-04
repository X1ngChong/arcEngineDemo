package com.Util;

import com.esri.arcgis.carto.IMap;
import com.esri.arcgis.geodatabase.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 獲取類要素
 * 按照順序提取地圖中的所有要素類
 */
public class LeiYaoSuUtil {

    public static  List<IFeatureClass> getAllFeatureClass(IWorkspace workspace, IMap mapDemo) throws IOException {
        List<IFeatureClass> IFeatureClassList = new ArrayList();

        for (int i = 0 ;i<mapDemo.getLayerCount();i++){
            IFeatureClass iFeatureClassTemp = ((IFeatureWorkspace) workspace).openFeatureClass(mapDemo.getLayer(i).getName());
            IFeatureClassList.add(iFeatureClassTemp);
        }

        return IFeatureClassList;
    }


    public static IFeatureClass getFeatureClass(IWorkspace workspace, String featureClassName) throws Exception {
        // 从工作空间中打开要素类
        return  ((IFeatureWorkspace)workspace).openFeatureClass(featureClassName);
    }

    public static void displayAllAttributes(IFeatureClass featureClass) throws Exception {
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
