package com.Util;

import com.Service.impl.Neo4jServiceImpl;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class JPanelUtil {
    public static void updatePanel(JPanel panel, ArrayList<String[]> list, Integer index, Neo4jServiceImpl neo4jService){

        Component[] components = panel.getComponents();

        // 遍历标签并更新它们的名称
        for (int i = 0; i < components.length; i++) {
            if (components[i] instanceof JLabel) {
                JLabel label = (JLabel) components[i];
               //TODO 如果超出了下标让下面的数组变为空
                if(index + i < list.size()){
                    if(i == 0){
                        label.setText("当前展示id为:"+list.get(index + i)[0]+"名称为:"+neo4jService.getNameByOsmId(list.get(index + i)));
                    }else {
                        label.setText(Arrays.toString(list.get(index + i)));
                    }

                }else{
                    label.setText("");
                }
            }
        }
    }
}
