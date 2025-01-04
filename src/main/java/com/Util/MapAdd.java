package com.Util;


import com.Common.PathCommon;
import com.esri.arcgis.beans.map.MapBean;
import com.esri.arcgis.carto.*;
import com.esri.arcgis.geodatabase.*;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class MapAdd {

    public MapBean getMap(String mxdPath) {
        mxdPath = PathCommon.caoTuFileName+"/"+mxdPath;
        log.info("地图路径:{}",mxdPath);
        MapBean map = new MapBean(); //地图容器
        initMaps(map,mxdPath);
        return map;
    }

    public void initMaps(MapBean map,String mxdPath){
        try{
            Workspace ws = null;

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




}
