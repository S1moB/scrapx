package com.indev.scrapx.scraper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.indev.scrapx.URLUtils;
import com.indev.scrapx.converter.ElementConverter;
import com.indev.scrapx.converter.ElementsConverter;

/**
 * Created by elmahdibenzekri on 02/08/2017.
 */
public class ScraperApi {

    private String url;

    private ScraperService scraperService;
    private Map<String, String> loginCookie = new HashMap<>();

    public ScraperApi(String url, ScraperService scraperService) {
        this.url = URLUtils.encodeURL(url);
        this.scraperService = scraperService;
        scraperService.refreshPage(url);
    }
    public void addAuthentification(Map<String,String> cookie)
    {
       this.loginCookie = cookie;
    }

    public <T> T forFirst(String query, ElementsConverter<T> converter) {
        try {
            Elements elements = scraperService.getElements(url, query);
            T convert = converter.convert(elements);
            return convert;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ScrapingException("Exception while getting " + query + " from " + url);
        }
    }
    public Elements getElements(String query) {
        try {
            Elements elements = scraperService.getElements(url, query);
            return elements;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ScrapingException("Exception while getting " + query + " from " + url);
        }
    }

    public <T> List<T> forEach(String query, ElementConverter<T> converter) {
        try {
            Elements elements = scraperService.getElements(url, query);
            List<Element> listOfElement = elements.stream().collect(Collectors.toList());
            return convertElements(converter, listOfElement);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ScrapingException("Exception while getting " + query + " from " + url);
        }
    }
    public <T> List<T> forFromTo(String query, ElementConverter<T> converter,int from, int to) {
        try {
            Elements elements = scraperService.getElements(url, query);
            return convertElements(converter, elements.subList(1,elements.size()));
        } catch (Exception e) {
            e.printStackTrace();
            throw new ScrapingException("Exception while getting " + query + " from " + url);
        }
    }

    private <T> List<T> convertElements(ElementConverter<T> converter, List<Element>  elements) throws Exception {
        List<T> result = new ArrayList<>();
        for (Element element : elements) {
            T convert = converter.convert(element);
            result.add(convert);
        }
        return result;
    }

}
