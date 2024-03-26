package com.Util;


import com.Common.InfoCommon;
import com.esri.arcgis.system.AoInitialize;
import com.esri.arcgis.system.EngineInitializer;
import com.esri.arcgis.system.esriLicenseProductCode;
import com.esri.arcgis.system.esriLicenseStatus;
import com.esri.arcgis.version.VersionManager;

import java.io.IOException;


public class AoInitUtil {
    /**
     * 初始化ae
     * 初始化ae或许可失败，会报错或者返回null
     * @param aoInit
     * @return
     */
    public AoInitialize initializeEngine(AoInitialize aoInit) throws IOException {
        //初始化ae
        EngineInitializer.initializeVisualBeans();

        // 设置使用的arcgis产品和版本，使ao能运行在不同的arcgis环境下
        VersionManager versionManager = new VersionManager();

        // 第一个参数是arcgis产品编号：1=desktop，2=engine，5=server
        // 此参数可以通过枚举查看esriProductCode
        boolean s = versionManager.loadVersion(1, InfoCommon.arcgisVersion);

        aoInit = new AoInitialize();

        //判断并使用哪个级别的许可，PS：目前应该只能用desktop相关的许可，也就是Advanced，Standard，Basic等
        if (aoInit.isProductCodeAvailable(esriLicenseProductCode.esriLicenseProductCodeAdvanced) == esriLicenseStatus.esriLicenseAvailable) {
            aoInit.initialize(esriLicenseProductCode.esriLicenseProductCodeAdvanced);
        } else if (aoInit.isProductCodeAvailable(esriLicenseProductCode.esriLicenseProductCodeStandard) == esriLicenseStatus.esriLicenseAvailable) {
            aoInit.initialize(esriLicenseProductCode.esriLicenseProductCodeStandard);
        } else if (aoInit.isProductCodeAvailable(esriLicenseProductCode.esriLicenseProductCodeBasic) == esriLicenseStatus.esriLicenseAvailable) {
            aoInit.initialize(esriLicenseProductCode.esriLicenseProductCodeBasic);
        } else if (aoInit.isProductCodeAvailable(esriLicenseProductCode.esriLicenseProductCodeEngineGeoDB) == esriLicenseStatus.esriLicenseAvailable) {
            aoInit.initialize(esriLicenseProductCode.esriLicenseProductCodeEngineGeoDB);
        } else if (aoInit.isProductCodeAvailable(esriLicenseProductCode.esriLicenseProductCodeArcServer) == esriLicenseStatus.esriLicenseAvailable) {
            aoInit.initialize(esriLicenseProductCode.esriLicenseProductCodeArcServer);
        } else {
            aoInit = null;
        }

        return aoInit;
    }
}

