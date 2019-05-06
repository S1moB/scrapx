package com.indev.scrapx.data.definition;

import static com.indev.scrapx.utils.UtilsFunctions.checkIfList;
import static com.indev.scrapx.utils.UtilsFunctions.checkIfMap;
import static com.indev.scrapx.utils.UtilsFunctions.checkIfString;
import static com.indev.scrapx.utils.UtilsFunctions.fillAttributes;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.nodes.Element;

import com.indev.scrapx.data.manager.ScrapedDTOManager;
import com.indev.scrapx.data.model.ElementSelector;
import com.indev.scrapx.scraper.ScraperApi;

import javafx.util.Pair;

public class ScrapingDefinition<T> {
    private Class<T> resultObjectClass;
    private String elementQuery;
    private Map<String, ElementSelector> fields = new HashMap<>();
    private ScrapedDTOManager scrapedDTOManager;
    private ScraperApi scraperApi;
    private Map<String, List> elementsOutsideOfTheLoop = new LinkedHashMap<>();

    public ScrapingDefinition(ScraperApi scraperApi, Class<T> resultObjectClass, String elementQuery) {
        this.scraperApi = scraperApi;
        this.resultObjectClass = resultObjectClass;
        this.elementQuery = elementQuery;
    }

    public T scrap() {
        return scraperApi.forFirst(elementQuery, elements -> convert(elements.get(0)));
    }

    public T scrap(int number) {
        return scraperApi.forFirst(elementQuery, elements -> convert(elements.get(number)));
    }

    public List<T> scrapAll() {
        return scraperApi.forEach(elementQuery, elements -> convert(elements));
    }

    private T convert(Element element) throws Exception {
        Map<String, Object> fieldValues = new HashMap<>();
        for (String fieldName : fields.keySet()) {
            if (checkIfString(fields.get(fieldName).getFieldType())) {
                String value = element.select((String) fields.get(fieldName).getSelector()).first().text();
                fieldValues.put(fieldName, value);
            }
            if(checkIfList(fields.get(fieldName).getFieldType()))
            {
                List values = element.select((String) fields.get(fieldName).getSelector()).eachText();
                fieldValues.put(fieldName,values);
            }
            if (checkIfMap(fields.get(fieldName).getFieldType())) {
                Map<String, String> mapConverted = getMapFromSelector(element, fieldName);
                fieldValues.put(fieldName, mapConverted);
            }
        }
        return scrapedDTOManager.createNew(resultObjectClass, fieldValues);
    }

    private Map<String, String> getMapFromSelector(Element element, String fieldName) throws Exception {
        Pair<String, String> headerAndValue = (Pair) fields.get(fieldName).getSelector();
        List<String> value = element.select(headerAndValue.getValue()).eachText();
        List<String> header;
        if (elementsOutsideOfTheLoop.containsKey(headerAndValue.getValue())) {
            header = elementsOutsideOfTheLoop.get(headerAndValue.getValue());
        } else {
            header = element.select(headerAndValue.getKey()).eachText();
            if (header.size() < value.size()) {
                header = scraperApi.getElements(headerAndValue.getKey()).eachText();
                if (header.size() < value.size()) {
                    throw new Exception("Cannot convert To map since The values are more than headers");
                }
            }
            elementsOutsideOfTheLoop.put(headerAndValue.getValue(), header);
        }
        return fillAttributes(header, value);
    }

    public Map<String, ElementSelector> getFields() {
        return fields;
    }

    public void setFields(Map<String, ElementSelector> fields) {
        this.fields = fields;
    }

    public ScrapedDTOManager getScrapedDTOManager() {
        return scrapedDTOManager;
    }

    public void setScrapedDTOManager(ScrapedDTOManager scrapedDTOManager) {
        this.scrapedDTOManager = scrapedDTOManager;
    }
}
