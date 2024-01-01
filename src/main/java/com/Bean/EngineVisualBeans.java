package com.Bean;

import com.Service.impl.Neo4jServiceImpl;
import com.Util.AoInitUtil;
import com.Util.JPanelUtil;
import com.Util.MapAdd;
import com.Util.Toolbar;
import com.esri.arcgis.beans.TOC.TOCBean;
import com.esri.arcgis.beans.map.MapBean;
import com.esri.arcgis.beans.toolbar.ToolbarBean;
import com.esri.arcgis.carto.IActiveView;
import com.esri.arcgis.carto.IFeatureLayer;
import com.esri.arcgis.carto.IFeatureSelection;
import com.esri.arcgis.controls.MapControl;
import com.esri.arcgis.geodatabase.*;
import com.esri.arcgis.geometry.Envelope;
import com.esri.arcgis.geometry.IEnvelope;
import com.esri.arcgis.system.AoInitialize;
import com.neo4j.SearchDemo6;
import com.neo4j.SearchDemo7;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Component
public class EngineVisualBeans {

    Neo4jServiceImpl neo4jService = new Neo4jServiceImpl();

    //从neo4j获取的数据
    private static ArrayList<String[]> list = null;
    private static  MapBean map;
    private static Integer index = 0;//当前数据的下标

    private static boolean extentSet = false;

    private static Set<Object> set = new HashSet<>();


    static{
      list= SearchDemo7.searchDemo();
    }
    public  void initialVisual() throws Exception {
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

         map = initView();//完成可视化界面
        showData(index);
        System.out.println("总共有:"+list.size()+"条数据");

        //獲取圖層下所有地圖的類要素
    }

    public MapBean initView() throws IOException {
        /**
         * 可視化處理
         */
        //创建一个地图可视化组件并加载一个.mxd地图文档。
        MapAdd mapTemp = new MapAdd();
        MapBean map = mapTemp.getMap(); //地图容器

        //创建工具栏可视化组件并添加标准ESRI工具和命令。
        Toolbar tool = new Toolbar(); //工具栏
        ToolbarBean toolbar = tool.getToolbar();//获取ToolbarBean

        //为地图创建一个目录（TOCBean）可视化组件。
        TOCBean toc = new TOCBean(); //内容列表

        //将地图组件与工具栏和TOC组件组合在一起。
        toolbar.setBuddyControl(map);
        toc.setBuddyControl(map);

        // 创建内容
        JPanel panel = new JPanel();
        panel.setBackground( new Color(99,153,255));

        //初始化列表
        for (int i = 0; list.size() >= 10 ? i < 10 :  i< list.size(); i++) {
            JLabel label;
            if(i== 0){
//                System.out.println(list.get(0)[0]);
                label = new JLabel("当前展示id为:"+list.get(index + i)[0]+"名称为:"+neo4jService.getNameByOsmId(list.get(index + i)));//获取当前数组下标之后的元素(包括当前元素)
            }else{
               label = new JLabel(Arrays.toString(list.get(index + i)));//获取当前数组下标之后的元素(包括当前元素)
            }
            // 将标签添加到面板中
            panel.add(label);
        }

        //创建4个查看按钮
        JButton showDataButton = new JButton("查看下一处");
        JButton showDataButton2 = new JButton("查看上一处");
        JButton showDataButton3 = new JButton("查看下二十处");
        JButton showDataButton4 = new JButton("查看上二十处");
        // 添加按钮点击事件监听器
        showDataButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 在按钮点击时执行的方法
                try {
                    index++;
                    showData(index);
                    JPanelUtil.updatePanel(panel,list,index,neo4jService);
                } catch (Exception ex) {
                    //如果到头了
                    ex.printStackTrace();
                    index = list.toArray().length;
                    System.out.println("数组到头了");
                }
            }
        });

        showDataButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 在按钮点击时执行的方法
                try {
                    index--;
                    showData(index);
                    JPanelUtil.updatePanel(panel,list,index,neo4jService);
                } catch (Exception ex) {
                    //如果到底了
                    index = 0;
                    ex.printStackTrace();
                    System.out.println("数组到头了");
                }
            }
        });

        showDataButton3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 在按钮点击时执行的方法
                try {
                   index +=20;
                   showData(index);
                    JPanelUtil.updatePanel(panel,list,index,neo4jService);
                } catch (Exception ex) {
                    //如果到头了
                    index = list.toArray().length;
                    ex.printStackTrace();
                    System.out.println("数组到头了");
                }
            }
        });

        showDataButton4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 在按钮点击时执行的方法
                try {
                   index-=20;
                   showData(index);
                    JPanelUtil.updatePanel(panel,list,index,neo4jService);
                } catch (Exception ex) {
                    //如果到底了
                    index = 0;
                    ex.printStackTrace();
                    System.out.println("数组到头了");
                }
            }
        });

        //构建 frame.
        JFrame frame = new JFrame("Arcgis Engine");

        showDataButton.setBounds(150, 150, 120, 30);
        frame.add(showDataButton);
        showDataButton2.setBounds(150, 100, 120, 30);
        frame.add(showDataButton2);
        showDataButton3.setBounds(150, 250, 120, 30);
        frame.add(showDataButton3);
        showDataButton4.setBounds(150, 200, 120, 30);
        frame.add(showDataButton4);


        panel.setBounds(150, 300, 300, 250);
        frame.add(panel);


        frame.add(map, BorderLayout.CENTER);
        frame.add(toolbar, BorderLayout.NORTH);
        frame.add(toc, BorderLayout.WEST);



        frame.setSize(1000,1000);
        frame.setLocation(400,200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        return map;
    }

public void showData(Integer number) throws IOException {
    MapControl mapControl = map.getMapControl();//获取map
        /**
        * 遍历所有图层查询数据
        */
    for (int i = 0; i < map.getLayerCount(); i++) {
        // 获取目标图层
        IFeatureLayer targetLayer = (IFeatureLayer)map.getLayer(i); // 这里假设你的目标图层是地图的第一个图层
        IFeatureSelection featureSelection = (IFeatureSelection) targetLayer;//获取选中合集

        IActiveView activeView = mapControl.getActiveView();//获取显示界面

        // 获取目标图层的要素类
        IFeatureClass featureClass = targetLayer.getFeatureClass();

        // 创建查询过滤器
        IQueryFilter queryFilter = new QueryFilter();

        //判断不为空并且不包含重复元素
        if(list.get(number) != null && !hasDuplicates(list)) {
            featureSelection.clear();//清除选中元素 每次执行下一次查询开始
            // 构建 IN 子句的一部分，将数组转换为逗号分隔的字符串
            String osmIdList = String.join(", ", list.get(number));
            // 构建完整的 WHERE 子句
            queryFilter.setWhereClause("osm_id IN (" + osmIdList + ")");
            // 执行查询
            IFeatureCursor featureCursor = featureClass.search(queryFilter, true);

            // 遍历查询结果
            IFeature feature = featureCursor.nextFeature();
            while (feature != null) {

                if(!extentSet){
                    IEnvelope extent = feature.getExtent();
                    // 设置地图的显示范围为稍微扩大的范围,默认为第一个大小的15倍
                    activeView.setExtent(setExtent(extent));
                    extentSet = true; // 设置标志变量为 true，表示已经设置过一次

                }

                // 获取 "name" 属性的值
                Object nameValue = feature.getValue(featureClass.findField("name"));

                // 处理查找结果
//                if (nameValue != null) {
                //name的值可能为空但是地物是存在的

                /**
                 * 输出地物处
                 */
//                System.out.println("值为: " + nameValue.toString());

                featureSelection.add(feature);
                // 如果几何范围有效，则定位到该范围
                activeView.refresh();
//                }
                feature = featureCursor.nextFeature();
            }
            extentSet =false;
        }
    }
}
public IEnvelope setExtent(IEnvelope extent) throws IOException {
    // 放大范围的因子（以百分比表示）
    double expansionFactor = 10; // Adjust as needed
    // 计算扩大后的范围
    double xmin = extent.getXMin();
    double ymin = extent.getYMin();
    double xmax = extent.getXMax();
    double ymax = extent.getYMax();
    double width = xmax - xmin;
    double height = ymax - ymin;
    double expandedWidth = width * expansionFactor;
    double expandedHeight = height * expansionFactor;
    double expandedXmin = xmin - (expandedWidth - width) / 2.0;
    double expandedYmin = ymin - (expandedHeight - height) / 2.0;
    double expandedXmax = expandedXmin + expandedWidth;
    double expandedYmax = expandedYmin + expandedHeight;
    // 创建扩大后的范围
    IEnvelope expandedExtent = new Envelope();
    expandedExtent.setXMin(expandedXmin);
    expandedExtent.setYMin(expandedYmin);
    expandedExtent.setXMax(expandedXmax);
    expandedExtent.setYMax(expandedYmax);

   return expandedExtent;
}

    // 检查列表是否包含重复值
    private static boolean hasDuplicates(ArrayList<String[]> list) {
        for (Object element : list) {
            if (!set.add(element)) {
                set.clear();//清除集合
                return true; // 重复元素
            }
        }
        set.clear();//清除集合
        return false; // 没有重复元素
    }

}

