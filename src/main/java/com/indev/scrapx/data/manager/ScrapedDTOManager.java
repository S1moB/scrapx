package com.indev.scrapx.data.manager;

import static com.indev.scrapx.utils.UtilsFunctions.checkIfList;
import static com.indev.scrapx.utils.UtilsFunctions.checkIfMap;
import static com.indev.scrapx.utils.UtilsFunctions.checkIfString;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import com.indev.scrapx.scraper.ScrapingException;

public class ScrapedDTOManager {
    public <T> T createNew(Class<T> resultObjectClass, Map<String, Object> fieldValues) {
        final T newObject;
        try {
            newObject = resultObjectClass.newInstance();
        } catch (InstantiationException | IllegalAccessException ex) {
            throw new ScrapingException("Cannot instantiate " + resultObjectClass, ex);
        }
        for (String fieldName : fieldValues.keySet()) {
            Class setterType = Object.class;
            if(checkIfString(fieldValues.get(fieldName).getClass()))
                setterType = String.class;
            if(checkIfList(fieldValues.get(fieldName).getClass()))
                setterType = List.class;
            if(checkIfMap(fieldValues.get(fieldName).getClass()))
                setterType = Map.class;
            try {
                Method declaredMethod = resultObjectClass.getDeclaredMethod("set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1), setterType);
                declaredMethod.invoke(newObject, fieldValues.get(fieldName));
            } catch (NoSuchMethodException e) {
                throw new ScrapingException("Setter not found for field " + fieldName + " in " + resultObjectClass);
            } catch (InvocationTargetException | IllegalAccessException e) {
                throw new ScrapingException("Error calling setter for field " + fieldName + " in " + resultObjectClass);
            }
        }
        return newObject;
    }
}
