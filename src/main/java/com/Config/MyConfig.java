package com.Config;

import com.esri.arcgis.beans.toolbar.ToolbarBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;


public class MyConfig {


    public ToolbarBean toolbar() throws IOException {
        return  new ToolbarBean();
    }


}
