package com.Util;

import com.esri.arcgis.beans.map.MapBean;
import com.esri.arcgis.carto.IEnumLayer;
import com.esri.arcgis.carto.ILayer;
import com.esri.arcgis.carto.IMap;
import com.esri.arcgis.carto.IMapLayers;

public class MapDelete {
    public static void deleteLayers(MapBean map,String layerName) throws Exception{
        // 获取地图图层集合
        IMap mapObject = map.getActiveView().getFocusMap();
        IMapLayers mapLayers = (IMapLayers) mapObject;

        // 遍历图层并删除特定名称的图层
        IEnumLayer enumLayer = mapLayers.getLayers(null, true);//获取所有图层
        ILayer layer = enumLayer.next();
        while (layer != null) {
            if (layer.getName().equalsIgnoreCase(layerName)) {
                // 删除图层
                mapLayers.deleteLayer(layer);
                break;
            }
            layer = enumLayer.next();
        }
    }

}
