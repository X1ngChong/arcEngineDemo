package com.Util;

import com.Service.impl.Neo4jServiceImpl;
import com.esri.arcgis.beans.map.MapBean;
import com.esri.arcgis.beans.toolbar.ToolbarBean;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class JPanelUtil {
    public static void updatePanel(JPanel panel, ArrayList<String[]> list, Integer index, Neo4jServiceImpl neo4jService){

        Component[] components = panel.getComponents();

        // 遍历标签并更新它们的名称
        for (int i = 0; i < components.length; i++) {
            if (components[i] instanceof JButton) {
                JButton label = (JButton) components[i];
               //TODO 如果超出了下标让下面的数组变为空
                if(index + i < list.size()){
                    if(i == 0){
                        label.setText(index+i+":"+"当前展示id为:"+list.get(index + i)[0]+"名称为:"+neo4jService.getNameByOsmId(list.get(index + i)));
                    }else {
                        label.setText(index+i+":"+Arrays.toString(list.get(index + i)));
                    }
                }else{
                    label.setText("");
                }
            }
        }
    }

    //TODO
    public static JDialog updateCaoTuLogMap(MapBean caoTuLogMap, MapAdd mapTemp, JDialog caoTuLog, String mapFileName, ToolbarBean toolbar2) {

        if (caoTuLog != null) {
            caoTuLog.dispose();
            caoTuLog = new JDialog();
            try {
                caoTuLogMap.clearLayers();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }


        caoTuLogMap = mapTemp.getMap(mapFileName); // 加载新的地图

//        JDialog caoTuLogTemp = new JDialog();

        try {

            toolbar2.setBuddyControl(caoTuLogMap);

        } catch (IOException e) {

            throw new RuntimeException(e);

        }

        caoTuLog.setTitle("更改后");

        caoTuLog.add(caoTuLogMap, BorderLayout.CENTER);

        caoTuLog.add(toolbar2, BorderLayout.NORTH);

        caoTuLog.setTitle("草图视图"); // 设置弹窗标题

        caoTuLog.setSize(500, 500); // 设置弹窗大小，根据需要调整大小和形状等属性

        caoTuLog.setLocation(200, 200);

        caoTuLog.setVisible(true); // 显示弹窗窗口

        return caoTuLog;

    }

}
