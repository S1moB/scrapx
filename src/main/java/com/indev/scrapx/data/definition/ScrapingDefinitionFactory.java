package com.indev.scrapx.data.definition;

import com.indev.scrapx.data.annotations.Authentication;
import com.indev.scrapx.data.annotations.Credentials;
import com.indev.scrapx.data.annotations.ScrapedList;
import com.indev.scrapx.data.annotations.ScrapedMap;
import com.indev.scrapx.data.annotations.ScrapedElement;
import com.indev.scrapx.data.annotations.ScrapedText;
import com.indev.scrapx.data.annotations.ScrapingUrl;
import com.indev.scrapx.data.manager.ScrapedDTOManager;
import com.indev.scrapx.data.model.ElementSelector;
import com.indev.scrapx.data.model.LoginInformationClass;
import com.indev.scrapx.data.model.type.ListElementSelector;
import com.indev.scrapx.data.model.type.MapElementSelector;
import com.indev.scrapx.data.model.type.StringElementSelector;
import com.indev.scrapx.scraper.ScraperApi;
import com.indev.scrapx.scraper.ScraperService;
import com.indev.scrapx.scraper.ScrapingException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javafx.util.Pair;

public class ScrapingDefinitionFactory {
    private static ScrapedDTOManager scrapedDTOManager = new ScrapedDTOManager();
    private static ScraperService scrapingService = new ScraperService();

    public static <T> ScrapingDefinition of(Class<T> searchListScraperClass) throws Exception {
        Annotation[] declaredAnnotations = searchListScraperClass.getAnnotations();
        Optional<String> scrapingUrl = Optional.empty();
        Optional<String> elementQuery = Optional.empty();
        Optional<LoginInformationClass> loginInformations = Optional.empty();
        for (Annotation annotation : declaredAnnotations) {
            if (annotation instanceof ScrapedElement) {
                elementQuery = Optional.ofNullable(((ScrapedElement) annotation).value());
            }
            if (annotation instanceof Authentication) {
                if (!loginInformations.isPresent()) {
                    loginInformations = Optional.ofNullable(new LoginInformationClass());
                }
                loginInformations.get().setLoginPage(((Authentication) annotation).loginURL());
                loginInformations.get().setLoginMethod(((Authentication) annotation).method());
            }
            if (annotation instanceof Credentials) {
                if (!loginInformations.isPresent()) {
                    throw new ScrapingException(searchListScraperClass + " should be annotated also with @Authentication");
                }
                loginInformations.get().setUserName(((Credentials) annotation).userName());
                loginInformations.get().setUserNameInput(((Credentials) annotation).userNameInput());
                loginInformations.get().setPassword(((Credentials) annotation).password());
                loginInformations.get().setPasswordInput(((Credentials) annotation).passwordInput());
            }
            if (annotation instanceof ScrapingUrl) {
                scrapingUrl = Optional.ofNullable(((ScrapingUrl) annotation).value());
            }
        }
        if (!scrapingUrl.isPresent()) {
            throw new ScrapingException(searchListScraperClass + " should be annotated with @ScrapingUrl");
        }

        if (!elementQuery.isPresent()) {
            elementQuery = Optional.of("body");
        }
        if (loginInformations.isPresent()) {
            if (loginInformations.get().getUserName() == null) {
                throw new ScrapingException(searchListScraperClass + " should be annotated also with @Credentials if you want to be authenticated");
            }
            scrapingService = new ScraperService(loginInformations.get());
        }

        ScrapingDefinition scrapingDefinition = new ScrapingDefinition(new ScraperApi(scrapingUrl.get(), scrapingService), searchListScraperClass,
            elementQuery.get());

        Map<String, ElementSelector> fields = new HashMap<>();
        for (Field field : searchListScraperClass.getDeclaredFields()) {
            for (Annotation fieldAnnotation : field.getAnnotations()) {
                if (fieldAnnotation instanceof ScrapedText) {
                    String fieldQuery = ((ScrapedText) fieldAnnotation).value();
                    ElementSelector stringSelector = new StringElementSelector(fieldQuery);
                    fields.put(field.getName(), stringSelector);
                } else {
                    if (fieldAnnotation instanceof ScrapedMap) {
                        String header = ((ScrapedMap) fieldAnnotation).headerElement();
                        String value = ((ScrapedMap) fieldAnnotation).valueElement();
                        Pair<String, String> headerAndValue = new Pair<>(header, value);
                        ElementSelector mapSelector = new MapElementSelector(headerAndValue);
                        fields.put(field.getName(), mapSelector);
                    } else {
                        if (fieldAnnotation instanceof ScrapedList) {
                            String fieldQuery = ((ScrapedList) fieldAnnotation).value();
                            ElementSelector listSelector = new ListElementSelector(fieldQuery);
                            fields.put(field.getName(),listSelector);
                        }
                    }
                }
            }
        }

        scrapingDefinition.setFields(fields);
        scrapingDefinition.setScrapedDTOManager(scrapedDTOManager);
        return scrapingDefinition;
    }
}
