package com.Util;


import com.esri.arcgis.beans.map.MapBean;
import com.esri.arcgis.carto.*;
import com.esri.arcgis.geodatabase.*;



public class MapAdd {

    public MapBean getMap() {
        MapBean map = new MapBean(); //地图容器
        initMaps(map);
        return map;
    }

    public void initMaps(MapBean map){
        try{
            Workspace ws = null;

            // 指定.mxd文件的路径
            String mxdPath = "D:\\arc\\arcgisdemo\\demo.mxd";

            // 打开地图文档
            IMapDocument mapDocument = new MapDocument();
            mapDocument.open(mxdPath, "");

            // 获取地图文档中的地图
            IMap mapDemo = mapDocument.getMap(0); // 这里的0表示第一个地图

//            // 要读取的 Shapefile 文件路径
//            String shapefilePath = "D:\\arc\\nanshi";
//            IWorkspace workspace  =  WorkspaceService.getWorkspace(shapefilePath);//初始化WorkspaceService
//
//            List<IFeatureClass> allFeatureClass = LeiYaoSuUtil.getAllFeatureClass(workspace, mapDemo);//获取所有的元素
//
//            for(int i = 0;i<allFeatureClass.size();i++){
//                LeiYaoSuUtil.displayAllAttributes(allFeatureClass.get(i));
//                System.out.println("第"+ (i+1) +"个图遍历完成***************");
//            }
            // 将地图添加到地图控件中
            map.setMapByRef(mapDemo);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }




}
