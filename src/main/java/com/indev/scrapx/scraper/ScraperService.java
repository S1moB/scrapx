package com.indev.scrapx.scraper;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.Proxy;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.indev.scrapx.data.model.LoginInformationClass;

public class ScraperService {
    public static final String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 " +
        "Safari/537.36";
    private Map<String, Document> documents = new HashMap<>();
    private Optional<Proxy> proxy;
    private Optional<LoginInformationClass> loginInformations;

    public ScraperService() {
        this.proxy = Optional.empty();
        this.loginInformations = Optional.empty();
    }

    public ScraperService(Proxy proxy) {
        this.proxy = Optional.ofNullable(proxy);
    }

    public ScraperService(LoginInformationClass loginInfomrations) {
        this();
        this.loginInformations = Optional.of(loginInfomrations);
    }

    public void refreshPage(String url) {
        if (documents.containsKey(url)) {
            documents.remove(url);
        }
    }

    public Elements getElements(String url, String query) throws IOException {
        if (documents.containsKey(url)) {
            return documents.get(url).select(query);
        }
        Map<String, String> cookie = new HashMap<>();
        if (loginInformations.isPresent()) {
            cookie = getCookieAfterLogin(loginInformations.get());
        }
        Connection connection = Jsoup
            .connect(url)
            .cookies(cookie)
            .timeout(0)
            .userAgent(USER_AGENT);

        if (proxy.isPresent()) {
            connection.proxy("216.198.170.70", 8080);
        }
        Document document = connection.get();

        documents.put(query, document);

        return document.select(query);
    }

    private Map<String, String> getCookieAfterLogin(LoginInformationClass informationClass) throws IOException {
        Map<String, String> mapParams = new HashMap<>();
        mapParams.put(informationClass.getUserNameInput(), informationClass.getUserName());
        mapParams.put(informationClass.getPasswordInput(), informationClass.getPassword());
        Connection.Response res = Jsoup.connect(informationClass.getLoginPage())
            .data(mapParams)
            .method(informationClass.getLoginMethod())
            .execute();
        return res.cookies();
    }
}
