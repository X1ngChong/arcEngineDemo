package com.Util;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class GetDataFromFile {
//    public static void main(String[] args) {
//        System.out.println(getLocation()[0]);
//    }
    public static  String[] getPlace() {
        String fileName = "data/place.txt"; // 替换为您的文件路径
        ArrayList<String> searches = new ArrayList<>();
        try {
            Resource resource = new ClassPathResource(fileName);

            BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()));

            String line;

            while ((line = reader.readLine()) != null) {
             searches.add(line);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 将ArrayList转换为数组

//        // 打印结果以验证读取的数据是否正确
//        System.out.println(searchesArray[0]);
        return searches.toArray(new String[0]);
    }

    public static  String[] getLocation() {
        String fileName = "data/location.txt"; // 替换为您的文件路径
        ArrayList<String> searches = new ArrayList<>();
        try {
            Resource resource = new ClassPathResource(fileName);

            BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()));

            String line;

            while ((line = reader.readLine()) != null) {
                searches.add(line);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 将ArrayList转换为数组

//        // 打印结果以验证读取的数据是否正确
//        System.out.println(searchesArray[0]);

        return searches.toArray(new String[0]);
    }
}