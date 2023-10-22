package com.Util;

import com.esri.arcgis.beans.toolbar.ToolbarBean;
import com.esri.arcgis.controls.*;
import com.esri.arcgis.systemUI.esriCommandStyles;

import java.io.IOException;


public class Toolbar {

    private ToolbarBean toolbar = null; //工具栏
    private IToolbarPalette toolbarPalette =null;
    public Toolbar() throws IOException {
    }// 右键工具菜单

    public void initBar(){//初始化工具栏
        try{
            // 对toolbarPalette菜单添加按钮
            toolbarPalette = new ToolbarPalette();
            toolbarPalette.addItem(new ControlsSelectTool(), 0, -1);// 选择
            toolbarPalette.addItem(new ControlsNewCircleTool(), 0, -1);// 画⚪
            toolbarPalette.addItem(new ControlsNewCurveTool(), 0, -1);// 曲线
            toolbarPalette.addItem(new ControlsNewEllipseTool(), 0, -1);// 椭圆
            toolbarPalette.addItem(new ControlsNewLineTool(), 0, -1);// 线
            toolbarPalette.addItem(new ControlsNewPolygonTool(), 0, -1);// 多边形
            toolbarPalette.addItem(new ControlsNewRectangleTool(), 0, -1);// 矩形
            toolbarPalette.addItem(new ControlsNewFreeHandTool(), 0, -1);// 任意线
            toolbarPalette.addItem(new ControlsEditingEditTool(), 0, -1);// 编辑


            toolbar = new ToolbarBean();
            toolbar.addItem(ControlsOpenDocCommand.getClsid(),0,-1,true,0,
                    esriCommandStyles.esriCommandStyleIconOnly);// 添加打开文件按钮
            toolbar.addItem(ControlsMapZoomInTool.getClsid(), 0, 0, false, 0,
                    esriCommandStyles.esriCommandStyleIconOnly); //缩小
            toolbar.addItem(ControlsMapZoomOutTool.getClsid(), 0, 0, false, 0,
                    esriCommandStyles.esriCommandStyleIconOnly); //放大
            toolbar.addItem(ControlsMapFullExtentCommand.getClsid(), 0,  - 1, false, 0,
                    esriCommandStyles.esriCommandStyleIconOnly); //全图
            toolbar.addItem(ControlsMapPanTool.getClsid(), 0, 0, false, 0,
                    esriCommandStyles.esriCommandStyleIconOnly); //移动
            toolbar.addItem(ControlsSaveAsDocCommand.getClsid(), 0, -1, false, 0,
                    esriCommandStyles.esriCommandStyleIconOnly); // 另存为按钮
            toolbar.addItem(ControlsEditingSaveCommand.getClsid(), 0, -1, false, 0,
                    esriCommandStyles.esriCommandStyleIconOnly); // 保存按钮
            toolbar.addItem(ControlsMapMeasureTool.getClsid(),0,0,false,0,
                    esriCommandStyles.esriCommandStyleIconOnly );//测量工具
            toolbar.addItem(ControlsMapSwipeTool.getClsid(),0,0,false,0,
                    esriCommandStyles.esriCommandStyleIconOnly );//标记工具
            toolbar.addItem(ControlsMapFindCommand.getClsid(),0,0,false,0,
                    esriCommandStyles.esriCommandStyleIconOnly );//查找工具
            toolbar.addItem(ControlsAddDataCommand.getClsid(), 0, -1, true, 0,
                    esriCommandStyles.esriCommandStyleIconOnly);//

            toolbar.addItem(ControlsLayerListToolControl.getClsid(),0,0,false,0,
                    esriCommandStyles.esriCommandStyleIconOnly );//图层列表

            toolbar.addItem(ControlsMapViewMenu.getClsid(),0,0,false,0,
                    esriCommandStyles.esriCommandStyleIconOnly );

//            toolbar.addItem(Controls.getClsid(),0,0,false,0,
//                    esriCommandStyles.esriCommandStyleIconOnly );


            toolbar.addItem(toolbarPalette,0,-1,true,0,1); // 编辑菜单

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public ToolbarBean getToolbar() {
        initBar();
        return toolbar;
    }

}
