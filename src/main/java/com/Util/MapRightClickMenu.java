package com.Util;

import com.esri.arcgis.beans.TOC.TOCBean;
import com.esri.arcgis.framework.IApplication;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MapRightClickMenu {

    public JPopupMenu createRightClickMenu() {
        // Create a custom right-click menu
        JPopupMenu rightClickMenu = new JPopupMenu();
        JMenuItem deleteMenuItem = new JMenuItem("Delete");
        deleteMenuItem.addActionListener(e -> {
            // Handle delete action
            System.out.println("执行deleteMenuItem");
            deleteSelectedItem();
        });
        rightClickMenu.add(deleteMenuItem);

        return rightClickMenu;
    }

    private void deleteSelectedItem() {

//          String layerNameToDelete = "LayerToDelete";
//
//                //删除逻辑
//                int maxLayer = map.getLayerCount();
//                for (int i = 0;i<maxLayer;i++) {
//                    // 遍历地图中的所有图层，然后删除指定名称的图层
//                    ILayer layerTemp = map.getLayer(i);
//                    if (layerTemp.getName().equals(layerNameToDelete)) {
//                        // 找到要删除的图层
//                        map.deleteLayer(i);
//                        break;
//                    }
//                }

    }
}
