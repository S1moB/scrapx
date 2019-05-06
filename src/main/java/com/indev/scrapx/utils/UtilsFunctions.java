package com.indev.scrapx.utils;

import static org.apache.commons.lang3.ClassUtils.getAllInterfaces;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class UtilsFunctions {
    public static boolean checkIfString(Class checkedClass)
    {
        return checkClass(checkedClass,String.class);
    }
    public static boolean checkIfList(Class checkedClass)
    {
        return checkClass(checkedClass,List.class);
    }
    public static boolean checkIfMap(Class checkedClass)
    {
        return checkClass(checkedClass,Map.class);
    }
    private  static boolean checkClass(Class firstClass, Class secondClass)
    {
        return getAllInterfaces(firstClass).contains(secondClass) || firstClass == secondClass;
    }
    public static Map<String, String> fillAttributes(List<String> keys, List<String> values) {
        Map<String,String> attributes = new LinkedHashMap<>();
        for(int iterator = 0; iterator < keys.size(); iterator++)
        {
            if(iterator < values.size())
                attributes.put(keys.get(iterator),values.get(iterator));
            else
                attributes.put(keys.get(iterator),"");
        }
        return attributes;
    }
}
