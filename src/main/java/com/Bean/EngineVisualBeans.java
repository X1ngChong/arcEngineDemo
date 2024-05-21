package com.Bean;

import com.Common.PathCommon;
import com.Service.impl.Neo4jServiceImpl;
import com.Util.*;
import com.Util.list.ListUtils2;
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

import com.neo4j.real.SearchDemo9;
import com.neo4j.real.SearchFromReal;
import lombok.extern.slf4j.Slf4j;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * @author JXS
 */
@Slf4j
public class EngineVisualBeans {


    public Neo4jServiceImpl neo4jService = new Neo4jServiceImpl();

    //从neo4j获取的数据,主要的数据储存地
    public  ArrayList<String[]> list = null;
     private JFrame frame;//frame主窗口

    private JDialog popupDialog;//剩余列表弹窗
    private static  MapBean map;
    private  static Integer index = 0;//当前数据的下标

    private static boolean extentSet = false;
    private static JDialog caoTuLog = new JDialog();



    public  void initialVisual(String labelName) throws Exception {

        //ae许可初始化
        AoInitialize aoInit = null;
        AoInitUtil aoInitUtil = new AoInitUtil();
        aoInit = aoInitUtil.initializeEngine(aoInit);
        if (aoInit == null) {
            JOptionPane.showMessageDialog(null, "ae初始化失败");
            return;
        }

        log.info("初始化中");

        //SearchDemo9 searchList = new SearchDemo9();
        SearchFromReal searchList = new SearchFromReal();

        /*
         草图数据
         */

        // list = searchDemo.searchDemo("zhangpengtao");

        list = searchList.searchDemo(labelName);

       // ListUtils sorter = new ListUtils();//统计每个第一个元素出现的次数，然后执行排序和过滤操作(区域的展示)





        log.info("初始化完成");

         map = initView(labelName);//完成可视化界面
        showData(index);
        log.info("总共有:{}条数据",list.size());

    }

    public MapBean initView(String labelName) throws IOException {
        /**
         * 可視化處理
         */
        //创建一张地图可视化组件并加载一个.mxd地图文档。

        MapAdd mapTemp = new MapAdd();
        MapBean map = mapTemp.getMap(PathCommon.caoTuName); //地图容器

        //TODO 根据输入的草图名称去选择草图视图。
        MapBean caoTuMap  = mapTemp.getMap(labelName+".mxd"); //草图


        //创建工具栏可视化组件并添加标准ESRI工具和命令。
        Toolbar tool = new Toolbar(); //工具栏
        ToolbarBean toolbar = tool.getToolbar();//获取ToolbarBean、
        ToolbarBean toolbar2 = tool.getToolbar();//获取ToolbarBean


        //为地图创建一个目录（TOCBean）可视化组件。
        TOCBean toc = new TOCBean(); //内容列表

        //将地图组件与工具栏和TOC组件组合在一起。
        toolbar.setBuddyControl(map);
        toc.setBuddyControl(map);
        toolbar2.setBuddyControl(caoTuMap);

        // 创建内容

        JPanel panel = new JPanel();
        panel.setBackground(new Color(99,153,255));


        // 创建剩余列表弹窗窗口
         popupDialog = new JDialog(); // 创建一个新的弹窗窗口实例

        //初始化列表
        for (int i = 0; list.size() >= 10 ? i < 10 :  i< list.size(); i++) {
            JButton label;
            if(i== 0){
                label = new JButton(index+i+":"+list.get(index + i)[0]+"名称为:"+neo4jService.getNameByOsmId(list.get(index + i)));//获取当前数组下标之后的元素(包括当前元素)
            }else{
               label = new JButton(index+i+":"+Arrays.toString(list.get(index + i)));//获取当前数组下标之后的元素(包括当前元素)
            }
            /**
             * 为每个按钮添加一个点击事件动态根据index
             */
            label.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    // 在按钮点击时执行的方法
                    try {
                        int tempIndex = Integer.parseInt(label.getText().split(":")[0]);//不变的tempIndex
                        showData(tempIndex);
                        JPanelUtil.updatePanel(panel,list,tempIndex,neo4jService);
//                        System.out.println(tempIndex);
                        index = tempIndex;
                    } catch (Exception ex) {
                        System.out.println("查看失败");
                    }
                }
            });
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

        // Create a button to trigger the name selection dialog
        JButton selectNameButton = new JButton("请选择草图");
        selectNameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 显示新的弹窗
                String name = showNameSelectionDialog(); // 获取选中的草图名称
                caoTuLog = JPanelUtil.updateCaoTuLogMap(caoTuMap, mapTemp, caoTuLog, name + ".mxd", toolbar2);

                try {
                    frame.dispose();
                    popupDialog.dispose();
                    index = 0; //初始化下标
                    initialVisual(name);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }

            }
        });
        selectNameButton.setBounds(150, 300, 120, 30);

        //构建 frame.
        frame = new JFrame("Arcgis Engine");

        showDataButton.setBounds(150, 150, 120, 30);
        frame.add(showDataButton);
        showDataButton2.setBounds(150, 100, 120, 30);
        frame.add(showDataButton2);
        showDataButton3.setBounds(150, 250, 120, 30);
        frame.add(showDataButton3);
        showDataButton4.setBounds(150, 200, 120, 30);
        frame.add(showDataButton4);
        frame.add(selectNameButton);

        panel.setBounds(150, 300, 300, 250);
        frame.add(panel);


        frame.add(map, BorderLayout.CENTER);
        frame.add(toolbar, BorderLayout.NORTH);
        frame.add(toc,BorderLayout.WEST);


        frame.setSize(1000,1000);
        frame.setLocation(800,200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);


        popupDialog.setTitle("剩余列表"); // 设置弹窗标题
        popupDialog.setSize(400, 400); // 设置弹窗大小，根据需要调整大小和形状等属性
        popupDialog.setLocation(200,600);
        popupDialog.getContentPane().add(panel); // 将标签添加到弹窗中
        popupDialog.setVisible(true); // 显示弹窗窗口

        caoTuLog.add(caoTuMap,BorderLayout.CENTER);
        caoTuLog.add(toolbar2,BorderLayout.NORTH);
        caoTuLog.setTitle("草图视图"); // 设置弹窗标题
        caoTuLog.setSize(500, 500); // 设置弹窗大小，根据需要调整大小和形状等属性
        caoTuLog.setLocation(200,100);
        caoTuLog.setVisible(true);// 显示弹窗窗口


        return map;
    }

    /**
     * TODO 更改地图后查询不到匹配的数据
     * @param number
     * @throws IOException
     */
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
        if(list.get(number) != null) {
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



    private String showNameSelectionDialog()  {
        // Specify the directory path
        String directoryPath = PathCommon.caoTuFileName;

        // Get file names from the specified directory
        String[] possibleNames;
        try {
            possibleNames = CommonUtil.getFileNames(directoryPath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        // Create a combo box with the possible names
        JComboBox<String> nameComboBox = new JComboBox<>(possibleNames);

        // Create the option pane with the combo box and OK button
        JOptionPane optionPane = new JOptionPane(nameComboBox, JOptionPane.QUESTION_MESSAGE, JOptionPane.OK_CANCEL_OPTION);

        // Show the option pane and get the user's choice
        JDialog dialog = optionPane.createDialog("图层列表");
        dialog.setVisible(true);

        // Check if the user selected "OK"
        String name = null;
        if (optionPane.getValue() != null && (int) optionPane.getValue() == JOptionPane.OK_OPTION) {
            // Get the selected name from the combo box
            name =  (String) nameComboBox.getSelectedItem();
            // Do something with the selected name (e.g., assign it to a variable)
            log.info("选择的图层:{}",name);
            System.out.println("选择的图层: " + name);
        }
        return name;
    }

}

