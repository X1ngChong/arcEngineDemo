package com.controller;

import com.Service.impl.Neo4jGetGroupNodesImpl;
import com.demo.impl.matrix.GetFinalResultByMatrix;
import com.response.ResponseData;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author JXS
 */
@RestController
@RequestMapping("/data")
public class DataController {
    @GetMapping("/S10AndXianLin2")
    public ResponseData<ArrayList<Integer[]>> getS10AndXianLin2() {
        GetFinalResultByMatrix getFinalResultByMatrix = new GetFinalResultByMatrix();
        List<Integer[]> realResultList = getFinalResultByMatrix.getFinalResultByMatrix();
        Neo4jGetGroupNodesImpl test = new Neo4jGetGroupNodesImpl();
        //获取OBJECTID作为主键去查找
        ArrayList<Integer[]> list = test.getObjectIdByIds(realResultList);
        return ResponseData.succeed(list);
    }

}
