package com.indev.scrapx;

import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.indev.scrapx.data.definition.ScrapingDefinition;
import com.indev.scrapx.data.definition.ScrapingDefinitionFactory;

public class ScrapperTester {
    private ScrapingDefinition<Information> definition;

    @Before
    public void init() {
        try {
             definition = ScrapingDefinitionFactory.of(Information.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void populateObject() {
        Information result = definition.scrap();
        Assert.assertNotNull(result);
    }
    @Test
    public void populateList() {
        Information result = definition.scrap();
        Assert.assertNotNull(result.getListTest());
    }
    @Test
    public void populateMap() {
        Information result = definition.scrap();
        Assert.assertNotNull(result.getAttributes());
    }

    @Test
    public void populateListOfObject() {
        List<Information> results = definition.scrapAll();
        Assert.assertNotEquals(Collections.EMPTY_LIST, results);
    }
}
