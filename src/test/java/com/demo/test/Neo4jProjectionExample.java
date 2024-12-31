package com.demo.test;

import org.locationtech.jts.linearref.LengthIndexedLine;
import org.neo4j.driver.*;
import org.locationtech.jts.geom.*;
import org.locationtech.jts.io.WKTReader;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.neo4j.driver.Values.parameters;

/**
 * 投影最终结果:
 * ID: 75, 距离起始点距离: 0.023719119576314708, 距离道路直线距离: 0.0015599354579195139
 * 投影最终结果:
 * ID: 44, 距离起始点距离: 0.010257962237222248, 距离道路直线距离: 0.006524578482171938
 * ID: 54, 距离起始点距离: 0.011257437831368896, 距离道路直线距离: 0.006094943905723661
 * ID: 36, 距离起始点距离: 0.01152465527244335, 距离道路直线距离: 0.002106681528071408
 * ID: 73, 距离起始点距离: 0.012405453174931767, 距离道路直线距离: 0.007371985359443595
 * ID: 52, 距离起始点距离: 0.017974504190172623, 距离道路直线距离: 0.00290219170173227
 * ID: 53, 距离起始点距离: 0.02722299061744822, 距离道路直线距离: 0.006976075863703541
 * 投影最终结果:
 * ID: 46, 距离起始点距离: 0.0014150061187040983, 距离道路直线距离: 0.0026202923538686737
 * ID: 74, 距离起始点距离: 0.003579946748782673, 距离道路直线距离: 0.0011647916123704448
 * ID: 47, 距离起始点距离: 0.0036844009370722098, 距离道路直线距离: 0.004312565983246404
 * 投影最终结果:
 * ID: 52, 距离起始点距离: 0.03082491882245255, 距离道路直线距离: 0.0019349785119440086
 * ID: 36, 距离起始点距离: 0.03146010512723579, 距离道路直线距离: 0.007048567627243832
 * ID: 53, 距离起始点距离: 0.03523159432917598, 距离道路直线距离: 0.0024131151453071257
 * ID: 54, 距离起始点距离: 0.03545611552169228, 距离道路直线距离: 0.00673261695235772
 * ID: 44, 距离起始点距离: 0.035972659978403335, 距离道路直线距离: 0.008783755916626753
 * ID: 73, 距离起始点距离: 0.03637352862601642, 距离道路直线距离: 0.004755959275965525
 * 投影最终结果:
 * ID: 40, 距离起始点距离: 0.028788951680084326, 距离道路直线距离: 0.0047155453617120525
 * ID: 41, 距离起始点距离: 0.02885105522350975, 距离道路直线距离: 0.001499852996733836
 * ID: 43, 距离起始点距离: 0.03855000331200023, 距离道路直线距离: 0.002401958949475343
 * ID: 42, 距离起始点距离: 0.043615008990689756, 距离道路直线距离: 0.0017342214014376958
 * 投影最终结果:
 * ID: 109, 距离起始点距离: 0.008506392448075913, 距离道路直线距离: 0.004319452403834111
 * ID: 55, 距离起始点距离: 0.012548719569678686, 距离道路直线距离: 0.0014174923295726851
 * ID: 57, 距离起始点距离: 0.012954711921713918, 距离道路直线距离: 0.004445510917628392
 * ID: 56, 距离起始点距离: 0.014108887831387276, 距离道路直线距离: 0.0013603919792045092
 * ID: 38, 距离起始点距离: 0.014559382983129527, 距离道路直线距离: 0.006028207692712065
 * 投影最终结果:
 * ID: 35, 距离起始点距离: 0.03130794937761659, 距离道路直线距离: 0.0015674754958784993
 * ID: 50, 距离起始点距离: 0.0344889521099797, 距离道路直线距离: 0.0010328146748648276
 * ID: 33, 距离起始点距离: 0.0347408646176935, 距离道路直线距离: 0.0032602342832828003
 * ID: 49, 距离起始点距离: 0.035973584024070644, 距离道路直线距离: 0.0011590926195029821
 * ID: 48, 距离起始点距离: 0.036150392927033925, 距离道路直线距离: 0.002066384137566615
 * 投影最终结果:
 * ID: 31, 距离起始点距离: 0.007021946721676105, 距离道路直线距离: 0.006463470570622999
 * ID: 106, 距离起始点距离: 0.015250469128309234, 距离道路直线距离: 0.01608778061744205
 * ID: 51, 距离起始点距离: 0.017065442808314846, 距离道路直线距离: 0.0018840185044293096
 * 投影最终结果:
 * ID: 46, 距离起始点距离: 0.011105969642692089, 距离道路直线距离: 0.002959248018677046
 * ID: 74, 距离起始点距离: 0.011334639118696675, 距离道路直线距离: 0.004916837505655087
 * ID: 47, 距离起始点距离: 0.012479081514142055, 距离道路直线距离: 0.0023205450080411865
 * 投影最终结果:
 * ID: 108, 距离起始点距离: 0.016339892370495677, 距离道路直线距离: 0.004813891446016994
 * ID: 45, 距离起始点距离: 0.020869334684738994, 距离道路直线距离: 0.0035051036753591413
 * ID: 78, 距离起始点距离: 0.02090225741357713, 距离道路直线距离: 0.009476315295622607
 * ID: 77, 距离起始点距离: 0.024243009652487796, 距离道路直线距离: 0.013983871663318138
 * ID: 107, 距离起始点距离: 0.02538670252213931, 距离道路直线距离: 0.00606869228714511
 * 投影最终结果:
 * ID: 66, 距离起始点距离: 0.03828422814560949, 距离道路直线距离: 0.00221152998387518
 * ID: 67, 距离起始点距离: 0.041402877561502065, 距离道路直线距离: 0.0019755661438856523
 * ID: 39, 距离起始点距离: 0.0420212637728636, 距离道路直线距离: 0.003122758564543208
 * ID: 70, 距离起始点距离: 0.04207508256378984, 距离道路直线距离: 0.0041120720603671805
 * ID: 58, 距离起始点距离: 0.042812054389029214, 距离道路直线距离: 0.0047863885807412155
 * ID: 71, 距离起始点距离: 0.04485275916408563, 距离道路直线距离: 0.004436088169992859
 * ID: 69, 距离起始点距离: 0.046876483008084495, 距离道路直线距离: 0.005436222023324466
 * 投影最终结果:
 * ID: 109, 距离起始点距离: 0.01910017629258179, 距离道路直线距离: 0.004817021785429405
 * ID: 55, 距离起始点距离: 0.023252812064608774, 距离道路直线距离: 0.007665131988197513
 * ID: 57, 距离起始点距离: 0.023504904149593563, 距离道路直线距离: 0.004620435625622531
 * ID: 56, 距离起始点距离: 0.02481386985612694, 距离道路直线距离: 0.007643131388323596
 * ID: 38, 距离起始点距离: 0.02502734649840514, 距离道路直线距离: 0.0029584886975440685
 * 投影最终结果:
 * ID: 74, 距离起始点距离: 0.03830142946233435, 距离道路直线距离: 0.0013830646779345159
 * ID: 46, 距离起始点距离: 0.0397528087659294, 距离道路直线距离: 0.0035507704830134343
 * ID: 47, 距离起始点距离: 0.041449396860795615, 距离道路直线距离: 0.001284599325606038
 * 投影最终结果:
 * ID: 40, 距离起始点距离: 0.028788951680084326, 距离道路直线距离: 0.0047155453617120525
 * ID: 41, 距离起始点距离: 0.02885105522350975, 距离道路直线距离: 0.001499852996733836
 * ID: 43, 距离起始点距离: 0.03855000331200023, 距离道路直线距离: 0.002401958949475343
 * ID: 42, 距离起始点距离: 0.043615008990689756, 距离道路直线距离: 0.0017342214014376958
 * 投影最终结果:
 * ID: 76, 距离起始点距离: 0.016433046667168365, 距离道路直线距离: 0.00413034301595071
 * ID: 79, 距离起始点距离: 0.01754926619430587, 距离道路直线距离: 0.010152792881001663
 * ID: 68, 距离起始点距离: 0.01859727232757675, 距离道路直线距离: 0.009926095399032185
 * ID: 72, 距离起始点距离: 0.02231036423435969, 距离道路直线距离: 0.011111077168245944
 * ID: 65, 距离起始点距离: 0.02438925934554642, 距离道路直线距离: 0.0037214402064386512
 * ID: 37, 距离起始点距离: 0.02510377968349287, 距离道路直线距离: 0.01064242038015026
 * 投影最终结果:
 * ID: 52, 距离起始点距离: 0.00652958125047213, 距离道路直线距离: 0.006416512502481006
 * ID: 53, 距离起始点距离: 0.00726355049347558, 距离道路直线距离: 0.002045163418722185
 * ID: 73, 距离起始点距离: 0.009668924023916548, 距离道路直线距离: 0.001041614347723108
 * ID: 36, 距离起始点距离: 0.011328292210117876, 距离道路直线距离: 0.006109483603178925
 * ID: 54, 距离起始点距离: 0.011471220519609917, 距离道路直线距离: 0.0021035510775460923
 * ID: 44, 距离起始点距离: 0.013567962400009612, 距离道路直线距离: 0.0018248707706278928
 * 投影最终结果:
 * ID: 63, 距离起始点距离: 0.004573412742184224, 距离道路直线距离: 0.0049024371504438405
 * ID: 64, 距离起始点距离: 0.005025288395234538, 距离道路直线距离: 0.0029371638340714022
 * ID: 62, 距离起始点距离: 0.005619221345573146, 距离道路直线距离: 0.006086653605814375
 * ID: 59, 距离起始点距离: 0.00564475908413709, 距离道路直线距离: 0.00903570714541535
 * ID: 61, 距离起始点距离: 0.007278496872227637, 距离道路直线距离: 0.004649507722331761
 * ID: 60, 距离起始点距离: 0.013328579756705813, 距离道路直线距离: 0.008186465150151664
 * 投影最终结果:
 * ID: 76, 距离起始点距离: 0.04257495289416823, 距离道路直线距离: 0.003412378682835686
 * ID: 65, 距离起始点距离: 0.043942047123963456, 距离道路直线距离: 0.013101734550372512
 * ID: 79, 距离起始点距离: 0.049386068254237914, 距离道路直线距离: 0.003450706975083983
 * ID: 68, 距离起始点距离: 0.04968997559219959, 距离道路直线距离: 0.006499740308173842
 * ID: 37, 距离起始点距离: 0.05089459116577068, 距离道路直线距离: 0.013371247490945105
 * ID: 72, 距离起始点距离: 0.05118326547557907, 距离道路直线距离: 0.01055353987671022
 * 投影最终结果:
 * ID: 75, 距离起始点距离: 0.005235317481594944, 距离道路直线距离: 0.0024324940154629494
 * 投影最终结果:
 * ID: 55, 距离起始点距离: 0.03761851528004975, 距离道路直线距离: 0.0031474441898377025
 * ID: 56, 距离起始点距离: 0.03763198384698567, 距离道路直线距离: 0.0015862894721513435
 * ID: 109, 距离起始点距离: 0.039784982644265256, 距离道路直线距离: 0.007109952211615193
 * ID: 57, 距离起始点距离: 0.04024549302832957, 距离道路直线距离: 0.0029738395941651757
 * ID: 38, 距离起始点距离: 0.04209922216300797, 距离道路直线距离: 0.0016918151823611764
 * 投影最终结果:
 * ID: 52, 距离起始点距离: 0.03017665635773713, 距离道路直线距离: 0.00932514323671244
 * ID: 36, 距离起始点距离: 0.030226636841050657, 距离道路直线距离: 0.004172497582953028
 * ID: 54, 距离起始点距离: 0.03405237288674478, 距离道路直线距离: 0.004064711494607034
 * ID: 53, 距离起始点距离: 0.034127738209226814, 距离道路直线距离: 0.008389387863440782
 * ID: 44, 距离起始点距离: 0.03442552903811351, 距离道路直线距离: 0.0019827067604162836
 * ID: 73, 距离起始点距离: 0.035104565104437956, 距离道路直线距离: 0.005973039011218209
 * 投影最终结果:
 * ID: 64, 距离起始点距离: 0.052125212138262184, 距离道路直线距离: 0.004873401124386455
 * ID: 63, 距离起始点距离: 0.05399936414477936, 距离道路直线距离: 0.005617743162929483
 * ID: 61, 距离起始点距离: 0.05418725062050036, 距离道路直线距离: 0.0032541375042097623
 * ID: 62, 距离起始点距离: 0.05532832567908884, 距离道路直线距离: 0.004763374689487906
 * ID: 59, 距离起始点距离: 0.058247220758423236, 距离道路直线距离: 0.005184825127148068
 * ID: 60, 距离起始点距离: 0.058678484403893075, 距离道路直线距离: 0.00291867935505762
 * 投影最终结果:
 * ID: 76, 距离起始点距离: 0.009636915490656608, 距离道路直线距离: 0.008227081873402408
 * ID: 79, 距离起始点距离: 0.011488861538373191, 距离道路直线距离: 0.0029254895080799586
 * ID: 68, 距离起始点距离: 0.01549189528188258, 距离道路直线距离: 0.00477958423354009
 * ID: 72, 距离起始点距离: 0.02176058069384261, 距离道路直线距离: 0.0035262557963695496
 * ID: 65, 距离起始点距离: 0.024415999575026347, 距离道路直线距离: 0.010728844905580176
 * ID: 37, 距离起始点距离: 0.024582263498646287, 距离道路直线距离: 0.0037730658560392624
 * 投影最终结果:
 * ID: 55, 距离起始点距离: 0.038792270259018956, 距离道路直线距离: 0.007474180532007001
 * ID: 56, 距离起始点距离: 0.038854188915700555, 距离道路直线距离: 0.009034164998211964
 * ID: 109, 距离起始点距离: 0.04152862423707522, 距离道路直线距离: 0.0034586779301529675
 * ID: 57, 距离起始点距离: 0.04184241810929095, 距离道路直线距离: 0.007648319703369342
 * ID: 38, 距离起始点距离: 0.043028492561660074, 距离道路直线距离: 0.00916075649461122
 * 投影最终结果:
 * ID: 76, 距离起始点距离: 0.016433046667168365, 距离道路直线距离: 0.00413034301595071
 * ID: 79, 距离起始点距离: 0.01754926619430587, 距离道路直线距离: 0.010152792881001663
 * ID: 68, 距离起始点距离: 0.01859727232757675, 距离道路直线距离: 0.009926095399032185
 * ID: 72, 距离起始点距离: 0.02231036423435969, 距离道路直线距离: 0.011111077168245944
 * ID: 65, 距离起始点距离: 0.02438925934554642, 距离道路直线距离: 0.0037214402064386512
 * ID: 37, 距离起始点距离: 0.02510377968349287, 距离道路直线距离: 0.01064242038015026
 * 投影最终结果:
 * ID: 33, 距离起始点距离: 0.0016116115825779273, 距离道路直线距离: 0.0022654888963003377
 * ID: 48, 距离起始点距离: 0.0028611659146136194, 距离道路直线距离: 9.050984389148427E-4
 * ID: 35, 距离起始点距离: 0.0031649829159728824, 距离道路直线距离: 0.005763685125569343
 * ID: 49, 距离起始点距离: 0.0037606153383465666, 距离道路直线距离: 0.0011182416327132827
 * ID: 50, 距离起始点距离: 0.0038271022273162745, 距离道路直线距离: 0.0026067501255404896
 * 投影最终结果:
 * ID: 45, 距离起始点距离: 0.05822708168274543, 距离道路直线距离: 0.00976678152170588
 * ID: 108, 距离起始点距离: 0.05904343096576868, 距离道路直线距离: 0.004055478231130529
 * ID: 107, 距离起始点距离: 0.060723321715218645, 距离道路直线距离: 0.014321711208221642
 * ID: 78, 距离起始点距离: 0.06419714642504505, 距离道路直线距离: 0.009888350814005237
 * ID: 77, 距离起始点距离: 0.0686546083124401, 距离道路直线距离: 0.013295655240100561
 * 投影最终结果:
 * ID: 69, 距离起始点距离: 0.003673935742372211, 距离道路直线距离: 0.002165754042387757
 * ID: 58, 距离起始点距离: 0.003693516618544092, 距离道路直线距离: 0.006407031447585525
 * ID: 70, 距离起始点距离: 0.004269940021896628, 距离道路直线距离: 0.007222853916578082
 * ID: 71, 距离起始点距离: 0.0043559961767888535, 距离道路直线距离: 0.00431761823074231
 * ID: 39, 距离起始点距离: 0.005244661750977287, 距离道路直线距离: 0.007400491767641184
 * ID: 66, 距离起始点距离: 0.005679351704273294, 距离道路直线距离: 0.011222378402556039
 * ID: 67, 距离起始点距离: 0.005997091481984205, 距离道路直线距离: 0.008163867458495847
 * 投影最终结果:
 * ID: 40, 距离起始点距离: 0.0024581605025145135, 距离道路直线距离: 0.0011525633349794278
 * ID: 43, 距离起始点距离: 0.0025250521467407687, 距离道路直线距离: 0.011004819046880025
 * ID: 42, 距离起始点距离: 0.0025250521467407687, 距离道路直线距离: 0.015717168949702407
 * ID: 41, 距离起始点距离: 0.00627759986534003, 距离道路直线距离: 0.0018351161016452585
 * 投影最终结果:
 * ID: 106, 距离起始点距离: 0.009890329550321642, 距离道路直线距离: 0.009248336238022879
 * ID: 31, 距离起始点距离: 0.022048377213742726, 距离道路直线距离: 0.003128823719321977
 * ID: 51, 距离起始点距离: 0.027187206867199866, 距离道路直线距离: 0.010042016462362155
 */
public class Neo4jProjectionExample {
    public static void main(String[] args) throws Exception {
        // 创建 Neo4j 驱动
        Driver driver = GraphDatabase.driver("bolt://localhost:7687", AuthTokens.basic("neo4j", "198234bh"));

        String getRelationList = "MATCH p=(s:xianLinGroup)-[r:NEXT_TO]->(e:xianLinGroup) RETURN id(s) as s,id(e) as e,id(r) as r,r.roadId as roadId";
        List<List<Integer>> ids = new ArrayList<>();//填充需要的id列表
        try (Session session = driver.session()) {
            Result result = session.run(getRelationList);
            while (result.hasNext()) {
                List<Integer> temp = new ArrayList<>();
                Record record = result.next();
                Integer sId = record.get("s").asInt();
                Integer rId = record.get("r").asInt();
                Integer roadId = record.get("roadId").asInt();

                temp.add(sId);
                temp.add(rId);
                temp.add(roadId);

                ids.add(temp);//暂存进最终结果
            }
        }
        System.out.println("-----------数组填充完毕---------------------");
        for (List<Integer> finalList:ids
             ) {
        // 查询地物数据
            String featureQuery = "MATCH (g:xianLinGroup)-[r:Have]->(f:xianLinBuidling)  where id(g) = "+finalList.get(0) +
                    " RETURN id(g) AS groupId, id(f) AS featureId, f.geometry AS geometry, type(r) AS relationship";
            //查询道路数据
            String roadQuery = "MATCH (r:xianLinRoad) WHERE id(r) = "+finalList.get(2)+" RETURN r.geometry AS geometry";

        // 使用 JTS 解析 WKT
        GeometryFactory geometryFactory = new GeometryFactory();
        WKTReader wktReader = new WKTReader(geometryFactory);

        // 查询道路
        MultiLineString road = null;
        try (Session session = driver.session()) {
            road = session.readTransaction(tx -> {
                Result result = tx.run(roadQuery);
                String wkt = result.single().get("geometry").asString();
                try {
                    return (MultiLineString) wktReader.read(wkt); // 修改为 MultiLineString
                } catch (Exception e) {
                    throw new RuntimeException("Failed to parse road geometry: " + wkt, e);
                }
            });
        }

        // 查询地物
        List<FeatureProjection> featureProjections = new ArrayList<>();
        try (Session session = driver.session()) {
            MultiLineString finalRoad = road;
            session.readTransaction(tx -> {
                Result result = tx.run(featureQuery);
                while (result.hasNext()) {
                    Record record = result.next();
                    Integer id = record.get("featureId").asInt();
                    String wkt = record.get("geometry").asString();
                    try {
                        Geometry geometry = wktReader.read(wkt);

                        // 检查几何类型
                        Point featurePoint;
                        if (geometry instanceof Point) {
                            featurePoint = (Point) geometry;
                        } else if (geometry instanceof MultiPolygon || geometry instanceof Polygon) {
                            featurePoint = geometry.getCentroid(); // 提取中心点
                        } else {
                            throw new RuntimeException("Unsupported geometry type: " + geometry.getGeometryType());
                        }

                        // 计算投影点
                        Coordinate closestProjection = null;
                        double minDistance = Double.MAX_VALUE;
                        double projectionIndex = -1;

                        for (int i = 0; i < finalRoad.getNumGeometries(); i++) {
                            LineString lineString = (LineString) finalRoad.getGeometryN(i);

                            // 使用 LengthIndexedLine 计算投影点
                            LengthIndexedLine indexedLine = new LengthIndexedLine(lineString);
                            double index = indexedLine.project(featurePoint.getCoordinate());
                            Coordinate projectedCoordinate = indexedLine.extractPoint(index);

                            // 计算投影点到地物的距离
                            double distance = featurePoint.getCoordinate().distance(projectedCoordinate);
                            if (distance < minDistance) {
                                minDistance = distance;
                                closestProjection = projectedCoordinate;
                                projectionIndex = index;
                            }
                        }

                        // 保存地物的投影信息
                        featureProjections.add(new FeatureProjection(id, closestProjection, projectionIndex, minDistance));
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to parse feature geometry: " + wkt, e);
                    }
                }
                return null;
            });
        }

        // 按投影点的索引排序
        featureProjections.sort(Comparator.comparingDouble(FeatureProjection::getProjectionIndex));

        // 输出排序结果
            List<Integer> orderList = new ArrayList<>();
        System.out.println("投影最终结果:");
        for (FeatureProjection projection : featureProjections) {
            orderList.add(projection.getId());//添加到最终结果集中
//            System.out.println("ID: " + projection.getId() +
//                    ", 距离起始点距离: " + projection.getProjectionIndex() +
//                    ", 距离道路直线距离: " + projection.getDistance());
        }

            changeOrderList(driver.session(),finalList.get(1),orderList);//将排序好的投影存储到关系上


        }
        driver.close();
    }

    public static void changeOrderList(Session session,Integer idr,List<Integer> orderList){
        /**
         * 修改当前的NEXT_TO关系,给当前的NEXT_TO关系上添加roadID
         */
        session.run("MATCH p=()-[r:NEXT_TO]->() where id(r) =  $idr  set r.orderList = $orderList", parameters("idr", idr,"orderList", orderList));
        System.out.println("修改成功");
    }

    // 定义一个类来保存地物的投影信息
    static class FeatureProjection {
        private final Integer id;
        private final Coordinate projection;
        private final double projectionIndex;
        private final double distance;

        public FeatureProjection(Integer id, Coordinate projection, double projectionIndex, double distance) {
            this.id = id;
            this.projection = projection;
            this.projectionIndex = projectionIndex;
            this.distance = distance;
        }

        public Integer getId() {
            return id;
        }

        public Coordinate getProjection() {
            return projection;
        }

        public double getProjectionIndex() {
            return projectionIndex;
        }

        public double getDistance() {
            return distance;
        }
    }
}