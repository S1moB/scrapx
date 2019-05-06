package com.indev.scrapx.data.model.type;

import java.util.Map;

import com.indev.scrapx.data.model.ElementSelector;

import javafx.util.Pair;

public class MapElementSelector extends ElementSelector {
    public MapElementSelector(Pair<String,String> selector) {
        super(selector,Map.class);
    }
}
