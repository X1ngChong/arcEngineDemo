package com.Util;

import com.esri.arcgis.beans.map.MapBean;
import com.esri.arcgis.carto.*;
import com.esri.arcgis.geodatabase.*;

import java.io.IOException;

public class MapAdd {
   private MapBean map = new MapBean(); //地图容器
    public void initMaps(){
        try{
            Workspace ws = null;

            // 指定.mxd文件的路径
            String mxdPath = "D:\\arc\\arcgisdemo\\nanshi.mxd";

            // 打开地图文档
            IMapDocument mapDocument = new MapDocument();
            mapDocument.open(mxdPath, "");

            // 获取地图文档中的地图
            IMap mapDemo = mapDocument.getMap(0); // 这里的0表示第一个地图



            // 将地图添加到地图控件中
            map.setMapByRef(mapDemo);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public MapBean getMap() {
        initMaps();
        return map;
    }

    private static IWorkspaceEdit getWorkspaceEdit(IMap map) throws IOException {
        // 获取工作空间
        IWorkspace workspace = (IWorkspace) ((IDataset) map).getWorkspace();

        // 如果工作空间支持编辑，返回工作空间的编辑对象
        if (workspace instanceof IWorkspaceEdit) {
            return (IWorkspaceEdit) workspace;
        }

        return null;
    }

    private static IFeatureLayer getFeatureLayerByName(IMap map, String layerName) throws IOException {
        // 遍历地图中的图层，查找指定名称的要素图层
        for (int i = 0; i < map.getLayerCount(); i++) {
            ILayer layer = map.getLayer(i);

            if (layer instanceof IFeatureLayer && layer.getName().equalsIgnoreCase(layerName)) {
                return (IFeatureLayer) layer;
            }
        }

        return null;
    }



}
