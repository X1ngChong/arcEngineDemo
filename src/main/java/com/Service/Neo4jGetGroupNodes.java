package com.Service;

import java.util.ArrayList;
import java.util.List;

public interface Neo4jGetGroupNodes {
    ArrayList<String[]> getNodeListByIds(List<Integer[]> resultIdList);
    ArrayList<Integer[]> getObjectIdByIds(List<Integer[]> resultIdList);
}
