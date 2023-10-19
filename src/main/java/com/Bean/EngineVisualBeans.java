package com.Bean;

import com.Util.AoInitUtil;
import com.Util.MapAdd;
import com.Util.Toolbar;
import com.esri.arcgis.beans.TOC.TOCBean;
import com.esri.arcgis.beans.map.MapBean;
import com.esri.arcgis.beans.toolbar.ToolbarBean;
import com.esri.arcgis.datasourcesfile.ShapefileWorkspaceFactory;
import com.esri.arcgis.geodatabase.*;
import com.esri.arcgis.system.AoInitialize;


import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class EngineVisualBeans {

    public static void initialVisual() throws IOException {
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


        //创建一个地图可视化组件并加载一个.mxd地图文档。
        MapAdd mapTemp = new MapAdd();
        MapBean map = mapTemp.getMap(); //地图容器


        // 在地图控件上关联事件处理程序

//        // 获取地图图层集合
//        IMap mapObject = map.getActiveView().getFocusMap();
//        IMapLayers mapLayers = (IMapLayers) mapObject;
//
//        // 遍历图层并删除特定名称的图层
//        String targetLayerName = "TargetLayerName"; // 要删除的图层名称
//        IEnumLayer enumLayer = mapLayers.getLayers(null, true);
//        ILayer layer = enumLayer.next();
//        while (layer != null) {
//            if (layer.getName().equalsIgnoreCase(targetLayerName)) {
//                // 删除图层
//                mapLayers.deleteLayer(layer);
//                break;
//            }
//            layer = enumLayer.next();
//        }




        //创建工具栏可视化组件并添加标准ESRI工具和命令。
        Toolbar tool = new Toolbar(); //工具栏
        ToolbarBean toolbar = tool.getToolbar();//获取ToolbarBean



        //为地图创建一个目录（TOCBean）可视化组件。
        TOCBean toc = new TOCBean(); //内容列表


//
//        // 添加右键删除菜单
//        MapRightClickMenu rightMenu = new MapRightClickMenu();
//        JPopupMenu rightClickMenu = rightMenu.createRightClickMenu();//获得JPopupMenu
//        JPanel panel = new JPanel();
//        panel.setComponentPopupMenu(rightClickMenu);
//
//        // 添加鼠标右键监听器
//        panel.addMouseListener(new MouseAdapter() {
//            @Override
//            public void mouseReleased(MouseEvent e) {
//                if (e.isPopupTrigger()) {
//                    rightClickMenu.show(e.getComponent(), e.getX(), e.getY());
//                }
//            }
//        });



        //将地图组件与工具栏和TOC组件组合在一起。
        toolbar.setBuddyControl(map);
        toc.setBuddyControl(map);



        //构建 frame.
        JFrame frame = new JFrame("Arcgis Engine");

//        frame.getContentPane().add(panel);

        frame.add(map, BorderLayout.CENTER);
        frame.add(toolbar, BorderLayout.NORTH);
        frame.add(toc, BorderLayout.WEST);
        frame.setSize(1000,1000);
        frame.setLocation(400,200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);



        frame.setVisible(true);
    }



}

