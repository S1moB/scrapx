package com.indev.scrapx;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Connection;

import com.indev.scrapx.data.annotations.Authentication;
import com.indev.scrapx.data.annotations.Credentials;
import com.indev.scrapx.data.annotations.ScrapedElement;
import com.indev.scrapx.data.annotations.ScrapedList;
import com.indev.scrapx.data.annotations.ScrapedMap;
import com.indev.scrapx.data.annotations.ScrapedText;
import com.indev.scrapx.data.annotations.ScrapingUrl;

@ScrapingUrl("")
@Authentication(loginURL = "",method = Connection.Method.POST)
@Credentials(userName = "",userNameInput = "",password = "", passwordInput = "")
@ScrapedElement("table tbody tr")
public class Information {

    public String type;
    @ScrapedText("td")
    private String name;
    @ScrapedMap(headerElement = "table thead tr th", valueElement = "td")
    private Map attributes = new LinkedHashMap();

    public List getListTest() {
        return listTest;
    }

    public void setListTest(List listTest) {
        this.listTest = listTest;
    }

    @ScrapedList("td")
    private List listTest = new ArrayList();



    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }

    public Information() {
    }
}
