package com.Service;

import com.esri.arcgis.datasourcesfile.ShapefileWorkspaceFactory;
import com.esri.arcgis.geodatabase.IWorkspace;
import com.esri.arcgis.geodatabase.IWorkspaceFactory;

import java.io.IOException;

/**
 * 带参数用来初始化
 * 不带参数用来获取当前的workspace
 */
public class WorkspaceService {

    private static IWorkspace workspace;


    public static IWorkspace getWorkspace(String shapefilePath) throws IOException {
        IWorkspaceFactory workspaceFactory = new ShapefileWorkspaceFactory();
        workspace = workspaceFactory.openFromFile(shapefilePath, 0);
        return workspace;
    }

    public static IWorkspace getWorkspace(){
        return  workspace;
    }

}
