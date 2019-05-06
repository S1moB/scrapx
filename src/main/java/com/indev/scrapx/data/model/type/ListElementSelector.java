package com.indev.scrapx.data.model.type;

import java.util.List;

import com.indev.scrapx.data.model.ElementSelector;

public class ListElementSelector extends ElementSelector {

    public ListElementSelector(String selector) {
        super(selector,List.class);
    }
}
