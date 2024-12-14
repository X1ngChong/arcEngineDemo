package com.Service;


import com.Bean.GroupMap;

import java.util.HashMap;
import java.util.List;

/**
 * @author JXS
 */
public interface Neo4jService {
     String getNameByOsmId(String[] list);

     List<Integer> getGroupIdByTags(String tags);

      HashMap<Integer,List<GroupMap>> getGroupIdMap();

    Double getPartLocationSimByType(Integer groupId1,Integer groupId2,String type);

    Double getPartLocationSimNoType(Integer groupId1,Integer groupId2);

}
