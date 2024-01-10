package com.Util;




import com.Common.PathCommon;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class GetDataFromFile {


    public String[] getPlace() {
        ArrayList<String> searches = new ArrayList<>();
        try {
            String placeName = PathCommon.placeName;
            Resource resource = new ClassPathResource(placeName);

            BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()));

            String line;

            while ((line = reader.readLine()) != null) {
             searches.add(line);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return searches.toArray(new String[0]);
    }

    public   String[] getLocation() {
        ArrayList<String> searches = new ArrayList<>();
        try {
            String locationName = PathCommon.locationName;
            Resource resource = new ClassPathResource(locationName);

            BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()));

            String line;

            while ((line = reader.readLine()) != null) {
                searches.add(line);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return searches.toArray(new String[0]);
    }
}