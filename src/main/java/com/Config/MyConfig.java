package com.Config;

import com.Util.Toolbar;
import com.esri.arcgis.beans.toolbar.ToolbarBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



@Configuration
public class MyConfig {


    public ToolbarBean toolbar(){
        Toolbar toolbar =  new Toolbar();
        return toolbar.getToolbar();
    }


}
