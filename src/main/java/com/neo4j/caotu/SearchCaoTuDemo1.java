package com.neo4j.caotu;

import com.Common.DriverCommon;
import com.Util.CalculateLocation;
import org.neo4j.driver.*;

import java.util.*;

/**
 * 将草图转化为标志性地物
 */
public class SearchCaoTuDemo1 {
    public static void main(String[] args) {
        SearchCaoTuDemo1 s = new SearchCaoTuDemo1("huanjing ");
    }
    private final Integer number = 3;//设置初始的筛选地物数量



    private  ArrayList<String> searches = new ArrayList<>();//地物类型

    private  ArrayList<List> bbox = new ArrayList<>();//searches的地物坐标

    private  ArrayList<String> positions = new ArrayList<>();//方位关系

    private List<String> landmarkTypes = new ArrayList<>(); // 标志性地物类型
    private Map<String, Integer> landmarkTypeCount = new HashMap<>(); // 标志性地物类型及其数量

    public SearchCaoTuDemo1(String labelName) {
        searchDemo(labelName);

    }

    public void searchDemo(String labelName) {
        try (DriverCommon driverCommon = new DriverCommon()) {
            Driver driver = driverCommon.getGraphDatabase();
            try (Session session = driver.session()) {
                try (Transaction tx = session.beginTransaction()) {
                    // 查询三个最常见的标志性地物类型
                    String cypherQuery = "MATCH (n:" + labelName + ") WHERE n.Type <> 'road' RETURN n.Type AS type, COUNT(n) AS count ORDER BY count ASC LIMIT 3";
                    Result result = tx.run(cypherQuery);
                    List<Record> landmarkRecords = result.list();
                    // 将标志性地物类型及其数量存储到地物类型关系的Map中
                    for (Record record : landmarkRecords) {
                        String type = record.get("type").asString();
                        int count = record.get("count").asInt();
                        landmarkTypes.add(type);
                        landmarkTypeCount.put(type, count);
                    }
                    // 将标志性地物类型按数量倒序排列
                    landmarkTypes.sort((t1, t2) -> landmarkTypeCount.get(t2) - landmarkTypeCount.get(t1));
                    // 构造地物类型关系
                    for (int i = 0; i < landmarkTypes.size() - 1; i++) {
                        String type1 = landmarkTypes.get(i);
                        String type2 = landmarkTypes.get(i + 1);
                    }

                    //不为空代表里面有数据
                    if(landmarkTypeCount != null) {
                        searches = convertMapToList(landmarkTypeCount);
                  //      System.out.println(searches);
                    }  //完成searches数组的查找[pitch, pitch, building]

                    //System.out.println(landmarkTypes.toString());
                   // System.out.println(landmarkTypeCount.toString());


                    //TODO: 完成方位关系的查找   会重复查找后续优化可以修改
                    Set<List<Object>> uniqueBbox = new HashSet<>();//判断是否有相同的数据
                    for (String search : searches) {
                        String cypherQuery2 = "MATCH (n:" + labelName + ") WHERE n.Type = '" + search + "' RETURN n.bbox as box ";
                        Result result2 = tx.run(cypherQuery2);
                        while (result2.hasNext()  && bbox.size() <number) {
                            Record record = result2.next();
                            List<Object> box1 = record.get("box").asList();
                            if (!uniqueBbox.contains(box1)) { // 检查是否已经存在相同的 bbox
                                bbox.add(box1);
                                uniqueBbox.add(box1); // 将 bbox 添加到 Set 中
                            }
                        }
                    }

                   // System.out.println(bbox.toString());//获取到了bbox数据


                    for(int i = 0 ; i< searches.size() ; i++){
                        if (i+1 < searches.size() ){
                            positions.add(CalculateLocation.getDirection2(bbox.get(i),bbox.get(i+1)));//正常比较
                        }else{
                            positions.add(CalculateLocation.getDirection2(bbox.get(0),bbox.get(searches.size()-1)));//第一个和最后一个比较
                        }
                    }
               //     System.out.println(positions.toString());

                    // 提交事务
                    tx.commit();
                }
                driver.close();
            }
        }
    }

    /**
     * 将{pitch=2, building=11} 变成 [pitch, pitch, building]
     * @param landmarkTypeCount
     * @return
     */
    private  ArrayList<String> convertMapToList(Map<String, Integer> landmarkTypeCount) {
        ArrayList<String> repeatedLandmarks = new ArrayList<>();
        // 遍历映射，将每种地物类型按照数量添加到列表中
        int temp = number;
        for (Map.Entry<String, Integer> entry : landmarkTypeCount.entrySet()) {
            String landmark = entry.getKey();
            int count = entry.getValue();
                for (int i = 0; i < count; i++) {
                    if(temp > 0){
                    repeatedLandmarks.add(landmark);
                        temp--;
                  }
             }
        }
        return repeatedLandmarks;
    }

    public ArrayList<String> getSearches() {
        return searches;
    }

    public void setSearches(ArrayList<String> searches) {
        this.searches = searches;
    }

    public ArrayList<String> getPositions() {
        return positions;
    }

    public void setPositions(ArrayList<String> positions) {
        this.positions = positions;
    }

}
